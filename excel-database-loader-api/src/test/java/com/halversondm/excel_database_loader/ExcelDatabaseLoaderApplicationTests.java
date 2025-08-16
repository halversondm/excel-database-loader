package com.halversondm.excel_database_loader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
@ImportTestcontainers
class ExcelDatabaseLoaderApplicationTests {

	@Test
	void contextLoads() {
	}

}
