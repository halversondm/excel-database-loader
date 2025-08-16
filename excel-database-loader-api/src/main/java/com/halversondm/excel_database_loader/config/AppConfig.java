package com.halversondm.excel_database_loader.config;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.service.ExcelNameAndAddressParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExcelNameAndAddressParser excelNameAndAddressParser(HomeownerDao homeownerDao) {
        return new ExcelNameAndAddressParser(homeownerDao);
    }

}
