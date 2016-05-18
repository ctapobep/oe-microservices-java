package com.oecontrib.microservices.v2;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration("/testAppContext.xml") @WebAppConfiguration
public class ConvertingEndpointTest {
    @Autowired MockMvc mockMvc;

    @Before public void initMockMvc() {
        mockMvc(mockMvc);
    }

    @Test public void successfulConversionReturns200() {
        String smiles = "c1ccc1";
        MockMvcResponse response = convert(smiles);
        assertEquals(200, response.statusCode());
    }
    @Test public void convertSmilesToSmilesReturnsSameString() {
        String smiles = "c1c(c(cc(c1F)F)F)C[C@H](CC(=O)N2CCn3c(nnc3C(F)(F)F)C2)N";
        MockMvcResponse response = convert(smiles);
        assertEquals(smiles + "\n", response.asString());
    }

    @Test public void convertSmilesToSmilesReturnsDifferentRepresentationOfSmiles() {
        MockMvcResponse response = convert("Fc1cc(c(F)cc1F)C[C@@H](N)CC(=O)N3Cc2nnc(n2CC3)C(F)(F)F");
        assertEquals("c1c(c(cc(c1F)F)F)C[C@H](CC(=O)N2CCn3c(nnc3C(F)(F)F)C2)N\n", response.asString());
    }

    private MockMvcResponse convert(String smiles) {
        return given().
                param("val", smiles).
                get("/v2/structure");
    }
}