package com.sample.test.demo.tests;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.testng.annotations.Test;
import com.sample.test.demo.utils.BaseTest;

import java.io.File;

public class OrdersTest extends BaseTest{

    // GET

    @Test
    public void getallOrdersHappyPath() {

        given().get("/orders").then()
                .assertThat().statusCode(200)
                .assertThat().body("size()", is(3))
                .assertThat().body(matchesJsonSchemaInClasspath("orders-schema.json"));
    }

    @Test
    public void getOrdersDataIntegrity() {
        JsonPath expectedBody = getJsonPath("order-testdata.json"); // use json file as a data provider for convenience but could be db/api due to dynamic nature of data

        given().get("/orders/2").then()
                .assertThat().statusCode(200)
                .assertThat().body("", equalTo(expectedBody.getMap("")));
    }

    @Test
    public void getOrdersNegative() {

        given().get("/orders/-1").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    @Test
    public void getOrdersLowBoundary() {

        given().get("/orders/0").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    @Test
    public void getOrdersBoundaryPositive() {

        given().get("/orders/1").then()
                .assertThat().statusCode(200)
                .assertThat().body("size()", is(2));
    }

    @Test
    public void getOrdersEquivalencePositive() {

        given().get("/orders/2").then()
                .assertThat().statusCode(200)
                .assertThat().body("size()", is(2));
    }

    @Test
    public void getOrdersBoundaryMax() {

        given().get("/orders/3").then()
                .assertThat().statusCode(200)
                .assertThat().body("size()", is(2));
    }

    @Test
    public void getOrdersBoundaryMaxPlusOne() {

        given().get("/orders/4").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    @Test
    public void getOrdersEquivalenceMax() {

        given().get("/orders/12").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    @Test
    public void getOrdersPositiveQueryString() {

        given().get("/orders?id=2").then()
                .assertThat().statusCode(200)
                .assertThat().body("size()", is(3)); // inconsistent response body with ordersEquivalencePositive() - could be a bug, I'd check requirements and ask the team. I know it is not documented :)
    }

    @Test
    public void getOrdersNegativeSpecialCharacters() {

        given().get("/orders/_@$%").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    @Test
    public void getOrdersNegativeLetters() {

        given().get("/orders/a").then()
                .assertThat().statusCode(404)
                .assertThat().body("size()", is(0));
    }

    //POST

    @Test
    public void postOrdersPositive() {

        given().body(getJsonPath("orders-post-happypath.json").toString())
                .post("/orders") // use json file as a data provider for convenience but could be db/api due to dynamic nature of data
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("size()", is(1))
                .assertThat().body("$", hasKey("id"));
    }

    @Test
    public void postOrdersIncorrectToppings() {

        given().body(getJsonPath("orders-incorrect-toppings.json").toString())
                .post("/orders") // use json file as a data provider for convenience but could be db/api due to dynamic nature of data
                .then()
                .assertThat().statusCode(406); // bug according to reqs
    }

    @Test
    public void postOrdersInvalidPizza() {

        given().body(getJsonPath("orders-post-invalid-pizza.json").toString())
                .post("/orders") // use json file as a data provider for convenience but could be db/api due to dynamic nature of data
                .then()
                .assertThat().statusCode(407); // bug according to reqs
    }

    @Test
    public void postOrdersPizzaNotSpecified() {

        given().body(getJsonPath("orders-post-pizza-notspecified.json").toString())
                .post("/orders") // use json file as a data provider for convenience but could be db/api due to dynamic nature of data
                .then()
                .assertThat().statusCode(408); // bug according to reqs
    }

    @Test
    public void postOrdersEmptyBody() {

        given().contentType(ContentType.JSON).post("/orders").then()
                .assertThat().statusCode(408)
                .assertThat().body("size()", is(0)); // accepts an empty body and creates an empty order, probably a bug
    }

    @Test
    public void postOrdersIncorrectContentType() {

        given().contentType(ContentType.XML).body(getJsonPath("orders-postdata.json").toString())
                .post("/orders") // use json file as a data provider for convenience but could be db/api due to dynamic nature of data
                .then()
                .assertThat().statusCode(415); // accepts JSON body with content type set to XML, probably a bug
    }

    @Test
    public void postOrdersSqlInjection() {
        String body = "<script>alert('XSS')</script>"; // another injection goes through as well "' or 1 = 1-- "

        given().body(body)
                .post("/orders")
                .then()
                .assertThat().statusCode(415); // vulnerable to SQL injections... not good
    }

    @Test
    public void postOrdersXmlWithJsonContentType() {
        String body = "<script>"; // another one works as well "' or 1 = 1-- "

        given().contentType(ContentType.JSON).body(body)
                .post("/orders")
                .then()
                .assertThat().body("size()", is(0)); // returns 500 with a stacktrace... another issue
    }
}
