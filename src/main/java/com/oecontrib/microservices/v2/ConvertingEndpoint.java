package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static openeye.oechem.oechem.OEWriteMolecule;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController @RequestMapping("/v2/structure")
public class ConvertingEndpoint {
    @RequestMapping(method = GET)
    public ResponseEntity convert(@RequestParam("val") String moleculeBody,
                                  @RequestHeader(value = "Accepts", defaultValue = "smiles") String acceptsHeader) {
        Molecule mol = Molecule.parse(moleculeBody);
        return ResponseEntity.ok(mol.toString(acceptsHeader));
    }

}
