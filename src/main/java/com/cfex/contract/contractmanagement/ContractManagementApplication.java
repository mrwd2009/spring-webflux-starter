package com.cfex.contract.contractmanagement;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ContractManagementApplication {

	public static void main(String[] args) {
		log.info(StringUtils.abbreviate("Starting Contract Management Application", 10));
		SpringApplication.run(ContractManagementApplication.class, args);
		log.info("after initialization");
	}

}
