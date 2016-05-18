package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import openeye.oechem.OEGraphMol;
import openeye.oedepict.OE2DMolDisplay;
import openeye.oedepict.OE2DMolDisplayOptions;
import openeye.oedepict.OEImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oedepict.oedepict.OEPrepareDepiction;
import static openeye.oedepict.oedepict.OERenderMolecule;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController @RequestMapping("/v2/depict/structure")
public class RenderingEndpoint {
    @RequestMapping(method = GET, value = "/") public ResponseEntity depict(
            @RequestParam("val") String moleculeBody,
            @RequestParam(defaultValue = "200") String width, @RequestParam(defaultValue = "200") String height) {
        OEGraphMol mol = Molecule.parse(moleculeBody).getOeMolecule();
        OEImage image = new OEImage(Double.parseDouble(width), Double.parseDouble(height));
        OEPrepareDepiction(mol, false, true);

        OE2DMolDisplayOptions displayOptions = new OE2DMolDisplayOptions(image.GetWidth(), image.GetHeight(), 1);
        OE2DMolDisplay display = new OE2DMolDisplay(mol, displayOptions);
        OERenderMolecule(image, display);
//        OEWriteImageToString(image_format, image)
        return ResponseEntity.ok().build();
    }

}
