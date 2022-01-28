package com.opencelium.csvtoapi;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UnknownFormatConversionException;

@RestController
public class ApiController {

    @GetMapping("/")
    public ResponseEntity<?> get(@RequestParam Map<String, String> queryParams){
        Arrays.asList(CsvDocument.class.getDeclaredFields()).forEach(f -> System.out.println(f.getName()));
        String source = queryParams.get("source");
        Format format = getFormat(queryParams.get("format"));
        String sort = queryParams.get("sort");
        String sort_dir = queryParams.get("sort_dir");
        String filter = getFilter(queryParams);

        try {
            Reader reader = getScvFile(source);
            CSVReader csvReader  = new CSVReaderBuilder(reader).build();
            List<String[]> allData = csvReader.readAll();
            String str = CsvDocument.csvToJson(allData);
            return ResponseEntity.ok(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }

    // getting csv file from source
    private Reader getScvFile(String source) throws Exception {
        InputStream input = new URL(source).openStream();
        return new InputStreamReader(input, "UTF-8");
    }

    private String getFilter(Map<String, String> params) {
        String key = params.keySet().stream().filter(k -> k.contains("=")).findFirst().orElse(null);

        return null;
    }

    private Format getFormat(String format) {
        if (format == null) return null;

         switch (format) {
             case "xml":
                 return Format.XML;
             case "html":
                 return Format.HTML;
             case "json":
                 return Format.JSON;
             default:
                 break;
         }

         throw new UnknownFormatConversionException("Format " + format + " not found");
    }
}
