package com.cfex.contract.contractmanagement.infrastructure.database.model.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contractroot")
public class ContractRootModel {
    @Id
    @Column("__pk_contractroot")
    private Long id;
    @Column("name")
    private String name;
}
