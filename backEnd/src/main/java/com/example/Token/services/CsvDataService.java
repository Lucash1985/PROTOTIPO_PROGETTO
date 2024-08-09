package com.example.Token.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvDataService {

    private List<Map<String, String>> rows = new ArrayList<>();

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public List<Map<String, String>> getRows() {
        return this.rows;
    }
}
