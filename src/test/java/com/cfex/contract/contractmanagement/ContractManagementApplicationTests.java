package com.cfex.contract.contractmanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Base64;
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

	@Test
	void testClock() {
		// Clock
		Clock clock = Clock.systemDefaultZone();
		System.out.println("Current time with default time-zone: " + clock.instant());

		Clock clock2 = Clock.system(ZoneId.of("America/Los_Angeles"));
		System.out.println("Current time with log time-zone: " + clock2.instant());

		// Instant
		Instant instant = Instant.now();
		System.out.println("Current instant: " + instant);

		// Convert Instant to a specific time-zone
		System.out.println("Instant in UTC: " + instant.atZone(ZoneId.of("America/Los_Angeles")));
	}

	@Test
	void geneterateKeys() {
		var a = KeyGenerators.secureRandom(64);
		var b = KeyGenerators.secureRandom(16);

		System.out.println(Base64.getEncoder().encodeToString(a.generateKey()));
		System.out.println(Base64.getEncoder().encodeToString(b.generateKey()));
	}

	@Test
	void testUri() throws URISyntaxException {
		var uri = new URI("../test/right");
		System.out.println(uri.getPath());
		System.out.println(uri.getHost());
	}

}
