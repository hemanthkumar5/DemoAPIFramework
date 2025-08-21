package com.hemanth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DataProviderUtils {

    private static final Logger logger = Logger.getLogger(DataProviderUtils.class.getName());

    private static InputStream getFileFromResource(String path){
        InputStream is = DataProviderUtils.class.getClassLoader().getResourceAsStream(path);

        if(is==null){
            throw new RuntimeException("File not found"+ path);
        }

        return is;
    }

    @DataProvider(name="csvData")
    public static Iterator<Object[]> csvDataprovider() throws IOException,CsvException {

        try(
                InputStream is = getFileFromResource("testdata/users.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(is))
        ){
            reader.readNext();

            return reader.readAll()
                    .stream()
                    .map(line->new Object[]{line[0], line[1]})
                    .iterator();
        }

    }

    // 🔹 JSON Data Provider
    @DataProvider(name = "jsonData")
    public static Iterator<Object[]> jsonDataProvider() throws Exception {
        try (InputStream is = getFileFromResource("testdata/users.json")) {

            // This creates an instance of Jackson’s ObjectMapper, the main class used to convert between JSON and Java objects.
            ObjectMapper mapper = new ObjectMapper();

            // This line reads the JSON from the input stream and deserializes it into a list of maps.
            List<Map<String, String>> users =
                    mapper.readValue(is, new TypeReference<>() {});


            List<Object[]> data = new ArrayList<>();
            for (Map<String, String> user : users) {
                data.add(new Object[]{user.get("name"), user.get("job")});
            }
            return data.iterator();
        }
    }

}
