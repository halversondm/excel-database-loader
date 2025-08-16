package com.halversondm.excel_database_loader.data;

import java.util.HashMap;
import java.util.Map;

public class FieldMapping {

    private Map<String, Integer> columnMap = new HashMap<>();

    public void addMapping(String fieldName, int columnIndex) {
        columnMap.put(fieldName.toLowerCase(), columnIndex);
    }

    public Integer getColumnIndex(String fieldName) {
        return columnMap.get(fieldName.toLowerCase());
    }

    public boolean hasField(String fieldName) {
        return columnMap.containsKey(fieldName.toLowerCase());
    }
}
