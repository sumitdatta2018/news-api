package com.mydomain.newsapi.tests.functional.steps;

import java.io.IOException;
import com.github.tomakehurst.wiremock.client.WireMock;


import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.mydomain.newsapi.tests.utils.FileUtils.readDataFile;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class NewsApiMockSteps {

	String mockProtocol = "http";
	String mockHost = System.getenv().getOrDefault("MOCK-NEWS-HOST", "localhost");
	int mockPort = Integer.parseInt(System.getenv().getOrDefault("MOCK-NEWS-PORT", "9050"));
	WireMock wireMockServer;

	public NewsApiMockSteps() {
	    wireMockServer = new WireMock(mockHost, mockPort);
	    configureThisWiremock();
	  }

	private void configureThisWiremock() {
		WireMock.configureFor(mockProtocol, mockHost, mockPort);
	}

	private void registerGetMappingWithResponseData(String endpoint, String dataFileName) throws IOException {
		String responseData = readDataFile(dataFileName);
		registerStubForGet(endpoint, responseData);
	}
	
	private void registerGetMappingWithErrorResponseData(String endpoint, String dataFileName) throws IOException {
		String responseData = readDataFile(dataFileName);
		registerStubForGetError(endpoint, responseData);
	}
	

	private void registerStubForGet(String endpoint, String responseData) {
		wireMockServer.register(stubFor(get(urlPathMatching(endpoint)).willReturn(okJson(responseData))));
	}
	
	private void registerStubForGetError(String endpoint, String responseData) {
		wireMockServer.register(stubFor(get(urlPathMatching(endpoint)).willReturn(jsonResponse(responseData, 500))));
	}

	@Given("the news api mock is reset")
	public void theMockIsReset() throws IOException {
		configureThisWiremock();
	}

	@Given("the News API mock GET {string} endpoint returns data from file {string}")
	public void theMockGetReturnsData(String endpoint,String dataFileName) throws IOException {
		registerGetMappingWithResponseData(endpoint, dataFileName);
	}

}
