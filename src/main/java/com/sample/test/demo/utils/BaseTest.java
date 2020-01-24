package com.sample.test.demo.utils;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeSuite;

import java.io.File;

public class BaseTest {

    protected final String baseURI = "https://my-json-server.typicode.com/sa2225/demo";

    @BeforeSuite
    public void beforeSuite(){
        RestAssured.baseURI = baseURI;
    }

    protected JsonPath getJsonPath(String relativePath){
        return new JsonPath(new File(this.getClass().getClassLoader().getResource(relativePath).getPath()));
    }
}
