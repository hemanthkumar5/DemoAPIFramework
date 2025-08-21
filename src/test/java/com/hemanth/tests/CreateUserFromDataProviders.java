package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.core.Endpoints;
import com.hemanth.core.Specs;
import com.hemanth.util.DataProviderUtils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserFromDataProviders extends BaseTest {

    @Test(dataProvider = "csvData", dataProviderClass = DataProviderUtils.class)
    public void createUserFromCSV(String name, String job){
        String body = """
                {
                "name":"%s",
                "job":"%s"
                }
                """.formatted(name,job);

        Response res = requestFactory.post(Endpoints.users(), body);

        res.then().spec(Specs.created());

        assertThat(res.jsonPath().getString("name"), equalTo(name));
        assertThat(res.jsonPath().getString("job"), equalTo(job));


    }
}
