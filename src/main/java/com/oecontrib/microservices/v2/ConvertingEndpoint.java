package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController @RequestMapping("/v2/structure")
public class ConvertingEndpoint {
    @RequestMapping(method = GET)
    public ResponseEntity convert(@RequestParam("val") String moleculeBody, HttpServletRequest request) {
        String acceptsHeader = paramOrHeader(request, "Accepts");
        Molecule mol = Molecule.parse(moleculeBody);
        return ResponseEntity.ok(mol.toString(acceptsHeader));
    }

    private String paramOrHeader(HttpServletRequest request, String name) {
        return defaultIfBlank(request.getParameter(name), request.getHeader(name));
    }
    private String defaultIfBlank(String str, String defaultString) {
        return str != null && !str.trim().isEmpty() ? str : defaultString;
    }
}
