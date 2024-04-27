package com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.AuthorizedClientModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AuthorizedClientDbRepository extends ReactiveCrudRepository<AuthorizedClientModel, Long> {
}
