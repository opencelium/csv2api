package com.opencelium.csvtoapi;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CsvDocument {

    private List<String[]> csv;
    private String format = "";

    public CsvDocument(List<String[]> csv) {
        this.csv = csv;
    }

    private Map<String, List<String>> csvToMap(String csv) {
        List<List<String>> rows = Arrays.asList(csv.split("\n")).stream()
                                    .map(s -> Arrays.asList(s.split("\",")))
                                    .collect(Collectors.toList());


        rows.remove(0);
        return new HashMap<>();
    }

    public String asJson() {
        String[] keys = csv.get(0);
        //System.out.println("keys : " + Arrays.toString(keys));
        StringBuilder json = new StringBuilder("");
        for (int i = 1; i < csv.size(); i++) {
            String[] values = csv.get(i);
            //System.out.println("values : " + Arrays.toString(values));
            json.append("{");
            for (int j = 0; j < values.length; j++) {
                String value = values[j];
                if (value == null) {
                    value = "";
                }
                json.append("\"").append(keys[j].replace("\"", "").replace("\'",""))
                        .append("\"").append(":")
                        .append("\"").append(value.trim().replace("\"","").replace("\'",""))
                        .append("\"").append(",");
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

    public CsvDocument filter(String exp){
        if (exp == null || exp.isEmpty()) {
            return this;
        }
        String[] head = csv.get(0);
        int index = Arrays.binarySearch(head, exp);
        Predicate<String[]> f = null;
        csv = csv.stream().filter(f).collect(Collectors.toList());
        return this;
    }

    public CsvDocument sort(String sort, String dir) {
        if (sort == null || dir == null) {
            return this;
        }
        String[] head = csv.get(0);
        int index = Arrays.binarySearch(head, sort);
        csv.remove(0);
        csv = csv.stream().sorted(Comparator.comparing(r -> r[index])).collect(Collectors.toList());
        if (dir != null && dir.equalsIgnoreCase("desc")) {
            reverse(csv);
        }
        return this;
    }

    private Predicate<String[]> buildFilter(String exp) {
        return null;
    }

    private <T> List<T> reverse(List<T> list) {
        int j = list.size();
        T tmp = null;
        for(int i = 0; i < j; i++) {
            tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
            j--;
        }

        return list;
    }

    public CsvDocument sort(String sort) {
        return this;
    }

    public Optional<String> get(Format format){
        return Optional.of(asJson());
    }

}
