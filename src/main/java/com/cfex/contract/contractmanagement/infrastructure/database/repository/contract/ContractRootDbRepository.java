package com.cfex.contract.contractmanagement.infrastructure.database.repository.contract;

import com.cfex.contract.contractmanagement.infrastructure.database.model.contract.ContractRootModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContractRootDbRepository extends ReactiveCrudRepository<ContractRootModel, Long> {
}
