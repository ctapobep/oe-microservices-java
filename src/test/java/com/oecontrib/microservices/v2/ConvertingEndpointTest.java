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
import static io.qala.datagen.RandomShortApi.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration("/testAppContext.xml") @WebAppConfiguration
public class ConvertingEndpointTest {
    @Autowired MockMvc mockMvc;

    @Test public void convertSmilesToSmilesReturnsSameString() {
        MockMvcResponse response = convert("smiles", molecule1("smiles"));
        assertEquals(molecule1("smiles"), response.asString());
    }

    @Test public void convertSmilesToSmilesReturnsDifferentRepresentationOfSmiles() {
        MockMvcResponse response = convert("smiles", "smiles", molecule1("smiles-not-normalized"));
        assertEquals(molecule1("smiles"), response.asString());
    }

    @Test public void convertMoleculeToInvalidFormatReturns406NotAcceptable() {
        String wrongFormat = alphanumeric(1, 100);
        MockMvcResponse response = convert("smiles", wrongFormat, "c1ccc1");
        assertEquals(406, response.statusCode());
        assertThat(response.asString()).isEqualToIgnoringCase("Unknown molecule format: " + wrongFormat);
    }
    @Test public void successfulConversionReturns200() {
        String inputFormat = textFormat();
        MockMvcResponse response = convert(inputFormat, textFormat(), molecule1(inputFormat));
        assertEquals(200, response.statusCode());
    }
    @Test public void convertMoleculeFromAnyToAny() {
        String inputFormat = stableFormat();
        String outputFormat = stableFormat();
        MockMvcResponse response = convert(inputFormat, outputFormat, molecule1(inputFormat));
        assertEquals("Converting from " + inputFormat + " to " + outputFormat, molecule1(outputFormat), response.asString());
    }
    // other formats are adding whitespaces, re-arrange records from time to time,
    // so we can't compare them to static string :(
    private String stableFormat() {
        return sample("smiles", "ism");
    }
    private String textFormat() {
        return sample("smiles", "ism", "usm", "mmod", "mol2", "mol2h", "sdf", "xyz");
    }
    private String binaryFormat() {
        return sample("cdx", "oeb");
    }
    private MockMvcResponse convert(String formatFrom, String formatTo, String value) {
        mockMvc(mockMvc);
        MockMvcRequestSpecification request = given().param("val", value);
        oneOrMore(
                () -> request.header("Accept", "text/" + formatTo),
                () -> request.param("Accept", "text/" + formatTo));
        oneOrMore(
                () -> request.header("Content-Type", "text/" + formatFrom),
                () -> request.param("Content-Type", "text/" + formatFrom));
        return request.get("/v2/structure");
    }
    private MockMvcResponse convert(String outputFormat, String smiles) {
        return convert("smiles", outputFormat, smiles);
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