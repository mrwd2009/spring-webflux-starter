package com.cfex.contract.contractmanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;

//@SpringBootTest
class ContractManagementApplicationTests {

	enum TestEnum {
		Test1,
		Test2
	}

	static class Sample {
		public String name;
		public int age;
		@JsonFormat(shape = JsonFormat.Shape.STRING)
		public BigDecimal salary;
		public TestEnum testEnum = TestEnum.Test1;
		public OffsetDateTime utcDate;
	}

	@Test
	void contextLoads() throws JsonProcessingException {
		var mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.registerModule(new JavaTimeModule());
		var sample = new Sample();
		sample.name = "John";
		sample.age = 30;
		sample.salary = new BigDecimal("1000000000000.0000000000000");
		sample.utcDate = OffsetDateTime.now();
		System.out.println(mapper.writeValueAsString(sample));

		var jsonStr = """
    {"name":"John","age":30,"salary":"1000000000000.0000000000000", "utcDate":"2024-03-13T22:12:01.98435+08:00"}
				""";
		var obj = mapper.readValue(jsonStr, Sample.class);
		System.out.println(obj.salary.getClass());
	}

}
