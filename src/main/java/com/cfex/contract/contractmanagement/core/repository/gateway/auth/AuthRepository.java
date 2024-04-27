package com.cfex.contract.contractmanagement.core.repository.gateway.auth;

public interface AuthRepository {
    AuthorizedClientValue getAuthorizedClient(String clientRegistrationId, String sub);
    void saveAuthorizedClient(AuthorizedClientValue authorizedClient);
}
