package com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.AuthorizedClientModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthorizedClientDbRepository extends ReactiveCrudRepository<AuthorizedClientModel, Long> {
    Mono<AuthorizedClientModel> findFirstByClientRegistrationIdAndSub(String clientRegistrationId, String sub);
    Mono<Void> deleteByClientRegistrationIdAndSub(String clientRegistrationId, String sub);
}
