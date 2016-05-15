package com.oecontrib.microservices.v1;

import openeye.oechem.OEFormat;
import openeye.oechem.OEGraphMol;
import openeye.oechem.oemolistream;
import openeye.oedepict.OEImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oedepict.oedepict.OEPrepareDepiction;

@RestController("/v2/depict/structure")
public class RenderingEndpoint {
    @RequestMapping("/{format}") ResponseEntity depict(
            @PathVariable String format, @RequestParam("val") String moleculeBody,
            @RequestParam(defaultValue = "200") String width, @RequestParam(defaultValue = "200") String height) {
        OEGraphMol mol = parseMolecule(moleculeBody);
        OEImage image = new OEImage(Double.parseDouble(width), Double.parseDouble(height));
        OEPrepareDepiction(mol, false, true);

//        OE2DMolDisplayOptions displayOptions = new OE2DMolDisplayOptions(image.GetWidth(), image.GetHeight());
        return ResponseEntity.ok().build();
    }

    private OEGraphMol parseMolecule(@RequestParam("val") String moleculeBody) {
        OEGraphMol mol = new OEGraphMol();

        oemolistream molInputStream = new oemolistream();
        int molFormat = OEFormat.SMI;
        molInputStream.SetFormat(molFormat);

        boolean openedStream = molInputStream.openstring(moleculeBody);
        if (!openedStream) throw new IllegalArgumentException("Could not read the molecule");

        boolean successfullyCreated = OEReadMolecule(molInputStream, mol);
        if (!successfullyCreated) throw new IllegalArgumentException("Could not parse specified molecule");
        return mol;
    }

}
