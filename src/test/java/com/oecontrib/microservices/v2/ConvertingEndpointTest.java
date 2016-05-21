package com.oecontrib.microservices.v2;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.bool;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration("/testAppContext.xml") @WebAppConfiguration
public class ConvertingEndpointTest {
    @Autowired MockMvc mockMvc;

    @Test public void successfulConversionReturns200() {
        String smiles = "c1ccc1";
        MockMvcResponse response = convert(smiles);
        assertEquals(200, response.statusCode());
    }

    @Test public void convertSmilesToSmilesReturnsSameString() {
        MockMvcResponse response = convert(molecule1("smiles"), "smiles");
        assertEquals(molecule1("smiles"), response.asString());
    }

    @Test public void convertSmilesToSmilesReturnsDifferentRepresentationOfSmiles() {
        MockMvcResponse response = convert(molecule1("smiles-not-normalized"));
        assertEquals(molecule1("smiles"), response.asString());
    }

    @Test public void convertMoleculeToInvalidFormatReturns400BadRequest() {
        String wrongFormat = alphanumeric(1, 100);
        MockMvcResponse response = convert("c1ccc1", wrongFormat);
        assertEquals(400, response.statusCode());
        assertEquals("Unknown molecule format: " + wrongFormat, response.asString());
    }
    @Test public void convertMoleculeToMol2() {
        MockMvcResponse response = convert("c1ccc1", "mol2");
        assertEquals(molecule1("mol2"), response.asString());
    }

    private MockMvcResponse convert(String smiles) {
        return convert(smiles, "smiles");
    }
    private MockMvcResponse convert(String smiles, String outputFormat) {
        mockMvc(mockMvc);
        MockMvcRequestSpecification request = given().param("val", smiles);
        boolean useHeader = bool();
        if(useHeader) request.header("Accepts", outputFormat);
        if(!useHeader || bool()) request.param("Accepts", outputFormat);
        return request.get("/v2/structure");
    }

    private String molecule1(String format) {
        try {
            Path path = Paths.get(getClass().getResource("/testmolecules/m1." + format).toURI());
            return new String(Files.readAllBytes(path));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("Bug in test - wrong molecule format was requested: " + format);
        }
    }
}