package com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.UserModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserDbRepository extends ReactiveCrudRepository<UserModel, Long> {
    @Query("SELECT * FROM user WHERE __pk_user = :id for update")
    Mono<UserModel> getLockedUser(Long id);
}
