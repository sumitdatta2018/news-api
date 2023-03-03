package com.mydomain.newsapi.tests.functional.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.mydomain.newsapi.tests.utils.FileUtils;
import com.mydomain.newsapi.tests.utils.UrlUtils;
import io.restassured.http.Header;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;



import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.config.RedirectConfig.redirectConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class NewsApiSteps {
	
	public static Response response;
    String newsApiBase = System.getenv().getOrDefault("NEWS_API_BASE", "http://localhost:8080");

	@When("a GET request is sent to the News Api {string} endpoint")
    public void doGetRequestEndPoint(String endPoint) throws MalformedURLException {
        response = doNewsApiGetRequestReturningJson(newsApiBase, endPoint);
    }

	@Then("the response code will be {int}")
    public void theStatusCodeWillBe(int expectedStatusCode) {
        int actualCode = response.statusCode();
        assertThat(actualCode).as("Status code is correct").isEqualTo(expectedStatusCode);
    }

	@Then("the json response body should match to {string}")
    public void theResponseBodyShouldMatchTheJson(String responseFile)
        throws IOException, JSONException {
      String baseResponse = FileUtils.readDataFile(responseFile);
      String responseBody = response.body().asPrettyString();
      JSONAssert.assertEquals(baseResponse, responseBody, false);
    }


    private Response doNewsApiGetRequestReturningJson(String base, String endPoint) throws MalformedURLException {
        RequestSpecification request = makeRequestReturningJson();
        URL url = UrlUtils.create(base, endPoint);
        return request.when().get(url).then().extract().response();
    }


    private RequestSpecification makeRequestReturningJson() {
        return given().config(RestAssured.config().redirect(redirectConfig().followRedirects(false)));
    }
    

}
