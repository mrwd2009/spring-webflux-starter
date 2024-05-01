package com.cfex.contract.contractmanagement.infrastructure.web.security.authorizedclient;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.AuthorizedClientModel;
import com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway.AuthorizedClientDbRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@Component
public class CustomOAuth2AuthorizedClientService implements ReactiveOAuth2AuthorizedClientService {
    private final AuthorizedClientDbRepository authorizedClientDbRepository;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Override
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return Mono.zip(clientRegistrationRepository.findByRegistrationId(clientRegistrationId), authorizedClientDbRepository.findFirstByClientRegistrationIdAndSub(clientRegistrationId, principalName))
                .map(tuple -> {
                    var clientRegistration = tuple.getT1();
                    var authorizedClientModel = tuple.getT2();

                    if (authorizedClientModel == null || clientRegistration == null) {
                        return null;
                    }

                    var systemZoneId = ZoneId.systemDefault();

                    var accessToken = new OAuth2AccessToken(
                            OAuth2AccessToken.TokenType.BEARER,
                            authorizedClientModel.getAccessTokenValue(),
                            authorizedClientModel.getAccessTokenIssuedAt().atZone(systemZoneId).toInstant(),
                            authorizedClientModel.getAccessTokenExpiresAt().atZone(systemZoneId).toInstant(),
                            StringUtils.commaDelimitedListToSet(authorizedClientModel.getAccessTokenScopes())
                    );

                    OAuth2RefreshToken refreshToken = null;

                    if (!ObjectUtils.isEmpty(authorizedClientModel.getRefreshTokenValue())) {
                        refreshToken = new OAuth2RefreshToken(authorizedClientModel.getRefreshTokenValue(), authorizedClientModel.getRefreshTokenIssuedAt().atZone(systemZoneId).toInstant());
                    }

                    OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                            clientRegistration,
                            authorizedClientModel.getSub(),
                            accessToken,
                            refreshToken
                    );
                    return (T) authorizedClient;
                });
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        var registrationId = authorizedClient.getClientRegistration().getRegistrationId();
        return authorizedClientDbRepository.findFirstByClientRegistrationIdAndSub(registrationId, principal.getName())
                .flatMap(authorizedClientModel -> {
                    var systemZoneId = ZoneId.systemDefault();
                    authorizedClientModel.setAccessTokenType(authorizedClient.getAccessToken().getTokenType().getValue());
                    authorizedClientModel.setAccessTokenValue(authorizedClient.getAccessToken().getTokenValue());
                    authorizedClientModel.setAccessTokenIssuedAt(authorizedClient.getAccessToken().getIssuedAt() == null ? null : authorizedClient.getAccessToken().getIssuedAt().atZone(systemZoneId).toLocalDateTime());
                    authorizedClientModel.setAccessTokenExpiresAt(authorizedClient.getAccessToken().getExpiresAt() == null ? null : authorizedClient.getAccessToken().getExpiresAt().atZone(systemZoneId).toLocalDateTime());
                    authorizedClientModel.setAccessTokenScopes(String.join(",", authorizedClient.getAccessToken().getScopes()));

                    var refreshToken = authorizedClient.getRefreshToken();
                    authorizedClientModel.setRefreshTokenValue(refreshToken == null ? null : refreshToken.getTokenValue());
                    authorizedClientModel.setRefreshTokenIssuedAt(refreshToken == null ? null : refreshToken.getIssuedAt() == null ? null : refreshToken.getIssuedAt().atZone(systemZoneId).toLocalDateTime());

                    return authorizedClientDbRepository.save(authorizedClientModel);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    var sub = principal.getName();
                    var accessToken = authorizedClient.getAccessToken();
                    var systemZoneId = ZoneId.systemDefault();
                    var modelBuilder = AuthorizedClientModel.builder()
                            .sub(sub)
                            .clientRegistrationId(registrationId)
                            .accessTokenType(accessToken.getTokenType().getValue())
                            .accessTokenValue(accessToken.getTokenValue())
                            .accessTokenIssuedAt(accessToken.getIssuedAt() == null ? null : LocalDateTime.ofInstant(accessToken.getIssuedAt(), systemZoneId))
                            .accessTokenExpiresAt(accessToken.getExpiresAt() == null ? null : LocalDateTime.ofInstant(accessToken.getExpiresAt(), systemZoneId))
                            .accessTokenScopes(String.join(",", accessToken.getScopes()));

                    var refreshToken = authorizedClient.getRefreshToken();

                    if (refreshToken != null) {
                        modelBuilder.refreshTokenValue(refreshToken.getTokenValue())
                                .refreshTokenIssuedAt(refreshToken.getIssuedAt() == null ? null : LocalDateTime.ofInstant(refreshToken.getIssuedAt(), systemZoneId));
                    }

                    var authorizedClientModel = modelBuilder.build();
                    return authorizedClientDbRepository.save(authorizedClientModel);
                }))
                .then();
    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
        return authorizedClientDbRepository.deleteByClientRegistrationIdAndSub(clientRegistrationId, principalName);
    }
}
