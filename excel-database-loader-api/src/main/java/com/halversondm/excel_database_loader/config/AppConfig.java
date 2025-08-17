package com.halversondm.excel_database_loader.config;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.service.ExcelNameAndAddressParser;
import com.halversondm.excel_database_loader.service.HomeownerSearch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExcelNameAndAddressParser excelNameAndAddressParser(HomeownerDao homeownerDao) {
        return new ExcelNameAndAddressParser(homeownerDao);
    }

    @Bean
    public HomeownerSearch homeownerSearch(HomeownerDao homeownerDao) {
        return new HomeownerSearch(homeownerDao);
    }

}
