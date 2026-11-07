package com.halversondm.excel_database_loader.service;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.data.Homeowner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
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

    private static InputStream toStream(Workbook workbook) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void writeHeaders(Sheet sheet, String... headers) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    @Test
    void testParseExcelFile_allColumnsXls() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        writeHeaders(sheet, "Lot Number", "Close Date", "First", "Second", "Last", "Address",
                "Alternate", "City", "State", "Zip");
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(7);
        row.createCell(1).setCellValue("2020-01-02");
        row.createCell(2).setCellValue("  Ann ");
        row.createCell(3).setCellValue("Bob");
        row.createCell(4).setCellValue("Jones");
        row.createCell(5).setCellValue("1 Elm St");
        row.createCell(6).setCellValue("PO Box 5");
        row.createCell(7).setCellValue("Springfield");
        row.createCell(8).setCellValue("IL");
        row.createCell(9).setCellValue(62701);

        List<Homeowner> result = parser.parseExcelFile(toStream(workbook), "data.XLS");

        assertEquals(1, result.size());
        Homeowner h = result.getFirst();
        assertEquals(7, h.getLotNumber());
        assertEquals("2020-01-02", h.getCloseDate());
        assertEquals("Ann", h.getFirstName());
        assertEquals("Bob", h.getSecondName());
        assertEquals("Jones", h.getLastName());
        assertEquals("1 Elm St", h.getAddress());
        assertEquals("PO Box 5", h.getAlternateAddress());
        assertEquals("Springfield", h.getCity());
        assertEquals("IL", h.getState());
        assertEquals("62701", h.getZip());
        verify(homeownerDao).saveAll(result);
    }

    @Test
    void testParseExcelFile_cellTypes() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        writeHeaders(sheet, "Lot", "Close Date", "Last", "Address", "City", "State", "Zip");
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(3);
        Cell date = row.createCell(1);
        date.setCellValue(new Date(0L));
        date.setCellStyle(dateStyle);
        row.createCell(2).setCellValue("Lee");
        row.createCell(3).setCellValue(12.5);
        row.createCell(4).setCellValue(true);
        row.createCell(5).setCellFormula("1+1");
        row.createCell(6).setBlank();

        List<Homeowner> result = parser.parseExcelFile(toStream(workbook), "types.xlsx");

        assertEquals(1, result.size());
        Homeowner h = result.getFirst();
        assertEquals(new Date(0L).toString(), h.getCloseDate());
        assertEquals("12.5", h.getAddress());
        assertEquals("true", h.getCity());
        assertEquals("1+1", h.getState());
        assertNull(h.getZip());
    }

    @Test
    void testParseExcelFile_skipsInvalidContacts() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        writeHeaders(sheet, "Lot", "Last", "Second", "Address");
        // valid via second name only
        Row ok = sheet.createRow(1);
        ok.createCell(0).setCellValue(1);
        ok.createCell(2).setCellValue("Pat");
        ok.createCell(3).setCellValue("5 Oak Ave");
        // no name
        Row noName = sheet.createRow(2);
        noName.createCell(0).setCellValue(2);
        noName.createCell(3).setCellValue("6 Oak Ave");
        // no address
        Row noAddress = sheet.createRow(3);
        noAddress.createCell(0).setCellValue(3);
        noAddress.createCell(1).setCellValue("Kim");
        // blank-only name
        Row blankName = sheet.createRow(4);
        blankName.createCell(0).setCellValue(4);
        blankName.createCell(1).setCellValue("   ");
        blankName.createCell(3).setCellValue("8 Oak Ave");

        List<Homeowner> result = parser.parseExcelFile(toStream(workbook), "skip.xlsx");

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getLotNumber());
        assertEquals("Pat", result.getFirst().getSecondName());
    }

    @Test
    void testParseExcelFile_noLotColumnYieldsNothing() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        writeHeaders(sheet, "Last", "Address", null);
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue("Smith");
        row.createCell(1).setCellValue("1 Main");

        List<Homeowner> result = parser.parseExcelFile(toStream(workbook), "nolot.xlsx");

        assertTrue(result.isEmpty());
    }

    @Test
    void testParseExcelFile_selectsSheetByIndex() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("First");
        Sheet second = workbook.createSheet("Second");
        writeHeaders(second, "Lot", "Last", "Address");
        Row row = second.createRow(1);
        row.createCell(0).setCellValue(9);
        row.createCell(1).setCellValue("Diaz");
        row.createCell(2).setCellValue("9 Pine Rd");

        List<Homeowner> result = parser.parseExcelFile(toStream(workbook), "multi.xlsx", 1);

        assertEquals(1, result.size());
        assertEquals("Diaz", result.getFirst().getLastName());
    }

    @Test
    void testParseExcelFile_missingHeaderRow() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("Empty");
        InputStream in = toStream(workbook);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parseExcelFile(in, "empty.xlsx"));
        assertTrue(ex.getMessage().contains("header"));
        verifyNoInteractions(homeownerDao);
    }

    @Test
    void testGetSheetNames_xls() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        workbook.createSheet("One");
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("sheets", ".xls");
        try {
            java.nio.file.Files.write(tempFile, ((ByteArrayInputStream) toStream(workbook)).readAllBytes());
            assertEquals(List.of("One"), parser.getSheetNames(tempFile.toString()));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testGetSheetNames_unsupportedFormat() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("notes", ".txt");
        try {
            assertThrows(IllegalArgumentException.class, () -> parser.getSheetNames(tempFile.toString()));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }
}
