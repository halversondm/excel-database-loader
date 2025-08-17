package com.halversondm.excel_database_loader.service;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.data.Homeowner;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelNameAndAddressParserTest {

    private HomeownerDao homeownerDao;
    private ExcelNameAndAddressParser parser;

    @BeforeEach
    void setUp() {
        homeownerDao = mock(HomeownerDao.class);
        parser = new ExcelNameAndAddressParser(homeownerDao);
    }

    @Test
    void testParseExcelFile_validSheet() throws Exception {
        // Create in-memory Excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Lot Number");
        header.createCell(1).setCellValue("Last");
        header.createCell(2).setCellValue("Address");
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(101);
        row.createCell(1).setCellValue("Smith");
        row.createCell(2).setCellValue("123 Main St");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        List<Homeowner> result = parser.parseExcelFile(in, "test.xlsx");
        assertEquals(1, result.size());
        Homeowner h = result.getFirst();
        assertEquals(101, h.getLotNumber());
        assertEquals("Smith", h.getLastName());
        assertEquals("123 Main St", h.getAddress());
        verify(homeownerDao, times(1)).saveAll(anyList());
    }

    @Test
    void testParseExcelFile_invalidRow() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Lot Number");
        header.createCell(1).setCellValue("Last");
        header.createCell(2).setCellValue("Address");
        // Empty row
        sheet.createRow(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        List<Homeowner> result = parser.parseExcelFile(in, "test.xlsx");
        assertTrue(result.isEmpty());
        verify(homeownerDao, times(1)).saveAll(anyList());
    }

    @Test
    void testGetSheetNames() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("Alpha");
        workbook.createSheet("Beta");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        String tempFile = "temp_test.xlsx";
        java.nio.file.Files.write(java.nio.file.Paths.get(tempFile), out.toByteArray());

        List<String> names = parser.getSheetNames(tempFile);
        assertEquals(2, names.size());
        assertTrue(names.contains("Alpha"));
        assertTrue(names.contains("Beta"));

        java.nio.file.Files.delete(java.nio.file.Paths.get(tempFile));
    }

    @Test
    void testParseExcelFile_unsupportedFormat() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> parser.parseExcelFile(in, "test.csv"));
    }
}

