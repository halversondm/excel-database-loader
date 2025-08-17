package com.halversondm.excel_database_loader.service;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.data.FieldMapping;
import com.halversondm.excel_database_loader.data.Homeowner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Excel parser for name and address data
 * Supports both .xls and .xlsx formats
 */
public class ExcelNameAndAddressParser {

    private HomeownerDao homeownerDao;

    public ExcelNameAndAddressParser(HomeownerDao homeownerDao) {
        this.homeownerDao = homeownerDao;
    }

    /**
     * Parse Excel file and extract contact data
     */
    public List<Homeowner> parseExcelFile(InputStream fis, String fileName) throws IOException {
        return parseExcelFile(fis, fileName, 0); // Default to first sheet
    }

    /**
     * Parse specific sheet from Excel file
     */
    public List<Homeowner> parseExcelFile(InputStream fis, String fileName, int sheetIndex) throws IOException {
        List<Homeowner> contacts = new ArrayList<>();

        Workbook workbook = createWorkbook(fileName, fis);
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        // Auto-detect field mapping from header row
        FieldMapping mapping = detectFieldMapping(sheet);

        // Parse data rows
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Homeowner contact = parseContactFromRow(row, mapping);

            if (contact != null && isValidContact(contact)) {
                contacts.add(contact);
            }
        }

        workbook.close();

        homeownerDao.saveAll(contacts);

        return contacts;
    }

    /**
     * Create appropriate workbook based on file extension
     */
    private Workbook createWorkbook(String filePath, InputStream fis) throws IOException {
        if (filePath.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported.");
        }
    }

    /**
     * Auto-detect field mapping from header row
     */
    private FieldMapping detectFieldMapping(Sheet sheet) {
        FieldMapping mapping = new FieldMapping();
        Row headerRow = sheet.getRow(0);

        if (headerRow == null) {
            throw new IllegalArgumentException("Excel file appears to be empty or missing header row");
        }

        for (Cell cell : headerRow) {
            String headerValue = getCellValueAsString(cell);
            if (headerValue == null) {
                continue;
            }
            headerValue = headerValue.toLowerCase().trim();
            int columnIndex = cell.getColumnIndex();

            // Map common field variations to standard field names
            if (matchesField(headerValue, "Lot Number", "lot", "lotno", "lot_no")) {
                mapping.addMapping("lotNumber", columnIndex);
            } else if (matchesField(headerValue, "Close Date", "close date", "closing date", "date closed")) {
                mapping.addMapping("closeDate", columnIndex);
            } else if (matchesField(headerValue, "last", "lname", "lastname", "surname", "family")) {
                mapping.addMapping("lastName", columnIndex);
            } else if (matchesField(headerValue, "first", "fname", "firstname", "given name", "forename")) {
                mapping.addMapping("firstName", columnIndex);
            } else if (matchesField(headerValue, "second")) {
                mapping.addMapping("secondName", columnIndex);
            } else if (matchesField(headerValue, "address", "street", "addr", "address1")) {
                mapping.addMapping("address", columnIndex);
            } else if (matchesField(headerValue, "alternate", "alternate address", "alt address", "address2")) {
                mapping.addMapping("alternateAddress", columnIndex);
            } else if (matchesField(headerValue, "city", "town", "municipality")) {
                mapping.addMapping("city", columnIndex);
            } else if (matchesField(headerValue, "state", "province", "region", "st")) {
                mapping.addMapping("state", columnIndex);
            } else if (matchesField(headerValue, "zip", "postal", "postcode", "zipcode")) {
                mapping.addMapping("zip", columnIndex);
            }
        }

        return mapping;
    }

    /**
     * Check if header matches any of the given field variations
     */
    private boolean matchesField(String header, String... variations) {
        for (String variation : variations) {
            if (header.startsWith(variation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse a Contact object from a data row
     */
    private Homeowner parseContactFromRow(Row row, FieldMapping mapping) {
        if (row == null || isEmptyRow(row)) {
            return null;
        }

        Homeowner homeowner = new Homeowner();

        if (mapping.hasField("lotNumber")) {
            homeowner.setLotNumber(Integer.valueOf(getCellValueAsString(row.getCell(mapping.getColumnIndex("lotNumber")))));
        }

        if (mapping.hasField("closeDate")) {
            homeowner.setCloseDate(getCellValueAsString(row.getCell(mapping.getColumnIndex("closeDate"))));
        }

        if (mapping.hasField("firstName")) {
            homeowner.setFirstName(getCellValueAsString(row.getCell(mapping.getColumnIndex("firstName"))));
        }

        if (mapping.hasField("secondName")) {
            homeowner.setSecondName(getCellValueAsString(row.getCell(mapping.getColumnIndex("secondName"))));
        }

        if (mapping.hasField("lastName")) {
            homeowner.setLastName(getCellValueAsString(row.getCell(mapping.getColumnIndex("lastName"))));
        }

        if (mapping.hasField("address")) {
            homeowner.setAddress(getCellValueAsString(row.getCell(mapping.getColumnIndex("address"))));
        }

        if (mapping.hasField("alternateAddress")) {
            homeowner.setAlternateAddress(getCellValueAsString(row.getCell(mapping.getColumnIndex("alternateAddress"))));
        }

        if (mapping.hasField("city")) {
            homeowner.setCity(getCellValueAsString(row.getCell(mapping.getColumnIndex("city"))));
        }

        if (mapping.hasField("state")) {
            homeowner.setState(getCellValueAsString(row.getCell(mapping.getColumnIndex("state"))));
        }

        if (mapping.hasField("zip")) {
            homeowner.setZip(getCellValueAsString(row.getCell(mapping.getColumnIndex("zip"))));
        }

        return homeowner;
    }

    /**
     * Get cell value as string, handling different cell types
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Handle whole numbers vs decimals
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * Check if row is empty
     */
    private boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            String value = getCellValueAsString(cell);
            if (value != null && !value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate if contact has minimum required data
     */
    private boolean isValidContact(Homeowner contact) {
        // At lot number, least name and address should be present
        boolean hasName = (contact.getLastName() != null && !contact.getLastName().trim().isEmpty()) ||
                (contact.getSecondName() != null && !contact.getSecondName().trim().isEmpty());

        boolean hasAddress = contact.getAddress() != null && !contact.getAddress().trim().isEmpty();

        boolean hasLotNumber = contact.getLotNumber() != null;

        return hasLotNumber && hasName && hasAddress;
    }

    /**
     * Get list of sheet names from Excel file
     */
    public List<String> getSheetNames(String filePath) throws IOException {
        List<String> sheetNames = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = createWorkbook(filePath, fis);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }

            workbook.close();
        }

        return sheetNames;
    }
}