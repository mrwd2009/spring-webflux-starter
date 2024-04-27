package com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.UserModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserDbRepository extends ReactiveCrudRepository<UserModel, Long> {
}
