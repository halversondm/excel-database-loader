package com.halversondm.excel_database_loader.controller;

import com.halversondm.excel_database_loader.data.Homeowner;
import com.halversondm.excel_database_loader.service.ExcelNameAndAddressParser;
import com.halversondm.excel_database_loader.service.HomeownerSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class ExcelParserController {

    private ExcelNameAndAddressParser excelNameAndAddressParser;

    private HomeownerSearch homeownerSearch;

    public ExcelParserController(ExcelNameAndAddressParser excelNameAndAddressParser, HomeownerSearch homeownerSearch) {
        this.excelNameAndAddressParser = excelNameAndAddressParser;
        this.homeownerSearch = homeownerSearch;
    }

    @PostMapping("/api/v1/excel/parse")
    public String parseExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            List<Homeowner> homeowners = excelNameAndAddressParser.parseExcelFile(file.getInputStream(), file.getResource().getFilename());

            log.info("Parsed {} homeowners from file", homeowners.size());

            log.info("First 10 homeowner sampling for review:");
            homeowners.stream()
                    .limit(10)
                    .forEach(homeowner -> log.info("Homeowner: {}", homeowner));

        } catch (IOException e) {
            log.error("Error parsing Excel file: {}", e.getMessage());
            log.error(e.toString());
        }
        return "Excel parsed successfully!";
    }

    @GetMapping("/api/v1/excel/retrieve")
    public ResponseEntity<List<Homeowner>> retrieveHomeowner(@RequestParam("search") String searchString) {
        List<Homeowner> homeowners = homeownerSearch.searchHomeowners(searchString);
        log.info("Retrieved {} homeowners matching search string: {}", homeowners.size(), searchString);
        if (homeowners.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(homeowners);
        }
    }
}
