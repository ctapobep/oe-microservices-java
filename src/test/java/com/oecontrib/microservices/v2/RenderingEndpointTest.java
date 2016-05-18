package com.oecontrib.microservices.v2;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration("/testAppContext.xml") @WebAppConfiguration
public class RenderingEndpointTest {
    @Autowired MockMvc mockMvc;

    @Before public void initMockMvc() {
        mockMvc(mockMvc);
    }

    @Test @Ignore public void test() {
        MockMvcResponse response = given().get("/v2/depict/structure/?val=Fc1cc(c(F)cc1F)C[C@@H](N)CC(=O)N3Cc2nnc(n2CC3)C(F)(F)F");
        assertEquals(200, response.statusCode());
        assertEquals("Fc1cc(c(F)cc1F)C[C@@H](N)CC(=O)N3Cc2nnc(n2CC3)C(F)(F)F", response.asString());
    }
}