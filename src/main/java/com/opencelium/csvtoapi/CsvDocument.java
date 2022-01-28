package com.opencelium.csvtoapi;

import java.util.*;
import java.util.stream.Collectors;

public class CsvDocument {

    private Map<String, List<String>> csvMap;

    public CsvDocument(String csv) {
        csvMap = csvToMap(csv);
    }

    private Map<String, List<String>> csvToMap(String csv) {
        List<List<String>> rows = Arrays.asList(csv.split("\n")).stream()
                                    .map(s -> Arrays.asList(s.split("\",")))
                                    .collect(Collectors.toList());


        rows.remove(0);
        return new HashMap<>();
    }

    public static String csvToJson(List<String[]> rows) {
        String[] keys = rows.get(0);
        StringBuilder json = new StringBuilder("");
        for (int i = 1; i < rows.size(); i++) {
            String[] values = rows.get(i);
            json.append("{");
            for (int j = 0; j < values.length; j++) {
                json.append("\""+keys[j]+"\"").append(":").append("\""+values[j].trim()+"\"").append(",");
            }

            if(json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}").append(",");
        }
        if(json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }
        return  "{\"result\": [ " + json.toString() + "]}";
    }

    public CsvDocument format(Format f){

        return this;
    }

    public CsvDocument filter(String exp){
        return this;
    }

    public CsvDocument sort(String sort, String dir) {
        return this;
    }

    public CsvDocument sort(String sort) {
        return this;
    }

    public Optional<String> get(){
        return null;
    }

}
