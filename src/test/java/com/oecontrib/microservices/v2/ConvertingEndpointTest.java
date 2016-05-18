package com.oecontrib.microservices.v2;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static io.qala.datagen.RandomShortApi.alphanumeric;
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
        String smiles = "c1c(c(cc(c1F)F)F)C[C@H](CC(=O)N2CCn3c(nnc3C(F)(F)F)C2)N";
        MockMvcResponse response = convert(smiles, "smiles");
        assertEquals(smiles + "\n", response.asString());
    }

    @Test public void convertSmilesToSmilesReturnsDifferentRepresentationOfSmiles() {
        MockMvcResponse response = convert("Fc1cc(c(F)cc1F)C[C@@H](N)CC(=O)N3Cc2nnc(n2CC3)C(F)(F)F");
        assertEquals("c1c(c(cc(c1F)F)F)C[C@H](CC(=O)N2CCn3c(nnc3C(F)(F)F)C2)N\n", response.asString());
    }

    @Test public void convertMoleculeToInvalidFormatReturns400BadRequest() {
        String wrongFormat = alphanumeric(1, 100);
        MockMvcResponse response = convert("c1ccc1", wrongFormat);
        assertEquals(400, response.statusCode());
        assertEquals("Unknown molecule format: " + wrongFormat, response.asString());
    }
    @Test public void convertMoleculeToMol2() {
        MockMvcResponse response = convert("c1ccc1", "mol2");
        assertEquals("@<TRIPOS>MOLECULE\n" +
                "*****\n" +
                "    4     4     0     0     0\n" +
                "SMALL\n" +
                "NO_CHARGES\n" +
                "\n" +
                "@<TRIPOS>ATOM\n" +
                "      1 C1          0.0000    0.0000    0.0000 C.2       1 <0>         0.0000\n" +
                "      2 C2          0.0000    0.0000    0.0000 C.2       1 <0>         0.0000\n" +
                "      3 C3          0.0000    0.0000    0.0000 C.2       1 <0>         0.0000\n" +
                "      4 C4          0.0000    0.0000    0.0000 C.2       1 <0>         0.0000\n" +
                "@<TRIPOS>BOND\n" +
                "     1    1    4 2\n" +
                "     2    1    2 1\n" +
                "     3    2    3 2\n" +
                "     4    3    4 1\n", response.asString());
    }

    private MockMvcResponse convert(String smiles) {
        return convert(smiles, "smiles");
    }
    private MockMvcResponse convert(String smiles, String outputFormat) {
        mockMvc(mockMvc);
        return given().
                param("val", smiles).
                header("Accepts", outputFormat).
                get("/v2/structure");
    }
}