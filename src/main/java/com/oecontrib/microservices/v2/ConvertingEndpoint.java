package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller @RequestMapping("/v2/structure")
public class ConvertingEndpoint {
    @RequestMapping(method = GET)

    public ResponseEntity convert(@RequestParam("val") String moleculeBody, HttpServletRequest request) {
        MediaType inputMediaType = MediaType.parseMediaType(paramOrHeader(request, "Content-Type"));
        String inputFormat = inputMediaType.getSubtype();

        MediaType outputMediaType = MediaType.parseMediaType(paramOrHeader(request, "Accept"));
        String outputFormat = outputMediaType.getSubtype();

        Molecule mol = Molecule.parse(inputFormat, moleculeBody);
        return ResponseEntity.ok(mol.toString(outputFormat));
    }

    private String paramOrHeader(HttpServletRequest request, String name) {
        return defaultIfBlank(request.getParameter(name), request.getHeader(name));
    }
    private String defaultIfBlank(String str, String defaultString) {
        return str != null && !str.trim().isEmpty() ? str : defaultString;
    }
}
