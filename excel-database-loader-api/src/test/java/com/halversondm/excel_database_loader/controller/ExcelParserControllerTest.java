package com.halversondm.excel_database_loader.controller;

import com.halversondm.excel_database_loader.data.Homeowner;
import com.halversondm.excel_database_loader.service.ExcelNameAndAddressParser;
import com.halversondm.excel_database_loader.service.HomeownerSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ExcelParserControllerTest {

    private ExcelNameAndAddressParser excelNameAndAddressParser;
    private HomeownerSearch homeownerSearch;
    private ExcelParserController controller;

    @BeforeEach
    void setUp() {
        excelNameAndAddressParser = mock(ExcelNameAndAddressParser.class);
        homeownerSearch = mock(HomeownerSearch.class);
        controller = new ExcelParserController(excelNameAndAddressParser, homeownerSearch);
    }

    @Test
    void testParseExcel_success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "dummy content".getBytes()
        );
        List<Homeowner> homeowners = Arrays.asList(new Homeowner(), new Homeowner());
        when(excelNameAndAddressParser.parseExcelFile(any(), anyString())).thenReturn(homeowners);

        String result = controller.parseExcel(file);

        assertEquals("Excel parsed successfully!", result);
        verify(excelNameAndAddressParser, times(1)).parseExcelFile(any(), anyString());
    }

    @Test
    void testParseExcel_exception() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "dummy content".getBytes()
        );
        when(excelNameAndAddressParser.parseExcelFile(any(), anyString())).thenThrow(new IOException("Parse error"));

        String result = controller.parseExcel(file);

        assertEquals("Excel parsed successfully!", result);
        verify(excelNameAndAddressParser, times(1)).parseExcelFile(any(), anyString());
    }

    @Test
    void testRetrieveHomeowner_found() {
        List<Homeowner> homeowners = Arrays.asList(new Homeowner(), new Homeowner());
        when(homeownerSearch.searchHomeowners("Smith")).thenReturn(homeowners);

        ResponseEntity<List<Homeowner>> response = controller.retrieveHomeowner("Smith");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(homeowners, response.getBody());
    }

    @Test
    void testRetrieveHomeowner_notFound() {
        when(homeownerSearch.searchHomeowners("Unknown")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Homeowner>> response = controller.retrieveHomeowner("Unknown");

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());
    }
}

