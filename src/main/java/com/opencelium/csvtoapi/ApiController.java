package com.opencelium.csvtoapi;

import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UnknownFormatConversionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/hal+json")
public class ApiController {

    @GetMapping("/")
    public ResponseEntity<?> get(@RequestParam Map<String, String> queryParams){
 //       Arrays.asList(CsvDocument.class.getDeclaredFields()).forEach(f -> System.out.println(f.getName()));
        String source = queryParams.get("source");
	
	if (source == null) {
    		String msg = "{" +
        	    "'property' : 'Hi! I am the csv2api connector. Please read more about me here https://github.com/opencelium/csv2api'"
        	    + "}";
    		return ResponseEntity.ok().body(msg);
	}

        try {
            source = decodeValue(source);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Format format = getFormat(queryParams.get("format"));
        String sort = queryParams.get("sort");
        String sort_dir = queryParams.get("sort_dir");
        String filter = getFilter(queryParams);

//        System.out.println(queryParams);
        try {
            Reader reader = getScvFile(source);
            CsvParserSettings settings = new CsvParserSettings();
            settings.detectFormatAutomatically();
            CsvParser csvParser = new CsvParser(settings);
            List<String[]> allData = csvParser.parseAll(reader);
            CsvDocument csv = new CsvDocument(allData);
//            System.out.println("Parsed Content : " + csvParser.getContext().currentParsedContent());
//            System.out.println("Parsed toString : " + csvParser.toString());
            String data = csv.filter(filter).sort(sort, sort_dir)
                            .get(format).orElseThrow(() -> new RuntimeException(""));
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }

//    @GetMapping("/test")
//    public ResponseEntity<?> test(@RequestParam("file") MultipartFile file){
//
//        try {
//            Reader reader = getScvFile(file.getInputStream());
//            CsvParserSettings settings = new CsvParserSettings();
//            settings.detectFormatAutomatically();
//            CsvParser csvParser = new CsvParser(settings);
//            List<String[]> allData = csvParser.parseAll(reader);
//
//            CsvDocument csv = new CsvDocument(allData);
//
//            Format format = getFormat("json");
//            String data = csv.get(format).orElseThrow(() -> new RuntimeException(""));
//            return ResponseEntity.ok(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    // getting csv file from source
    private Reader getScvFile(String source) throws Exception {
        InputStream input = new URL(source).openStream();
//        String result = new BufferedReader(new InputStreamReader(input))
//                .lines().collect(Collectors.joining("\n"));
//        System.out.println(result);
        return new InputStreamReader(input, "UTF-8");
    }

//    private Reader getScvFile(InputStream source) throws Exception {
//        return new InputStreamReader(source, "UTF-8");
//    }

    private String getFilter(Map<String, String> params) {
        String key = params.keySet().stream().filter(k -> k.contains("=")).findFirst().orElse(null);

        return null;
    }

    private String decodeValue(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
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
