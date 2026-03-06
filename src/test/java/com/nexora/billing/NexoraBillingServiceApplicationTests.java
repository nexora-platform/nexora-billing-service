package com.nexora.billing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestPostgresContainer.class)
class NexoraBillingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
