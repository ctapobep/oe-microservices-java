package com.oecontrib.microservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration("/testAppContext.xml") @WebAppConfiguration
public class HomeEndpointTest {
    @Autowired MockMvc mockMvc;

    @Before public void initMockMvc() {
        mockMvc(mockMvc);
    }

    @Test public void homeReturnsInformationAboutProduct() {
        given().when().get("/").
                then().statusCode(200).body(equalTo("Microservices that expose OpenEye Toolkits functionality as REST services."));
    }
}