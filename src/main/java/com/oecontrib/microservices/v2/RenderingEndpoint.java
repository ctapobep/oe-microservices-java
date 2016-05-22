package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import openeye.oechem.OEGraphMol;
import openeye.oechem.oeosstream;
import openeye.oechem.oeostream;
import openeye.oedepict.OE2DMolDisplay;
import openeye.oedepict.OE2DMolDisplayOptions;
import openeye.oedepict.OEImage;
import org.omg.CORBA.portable.OutputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oedepict.oedepict.OEPrepareDepiction;
import static openeye.oedepict.oedepict.OERenderMolecule;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller @RequestMapping("/v2/depict/structure")
public class RenderingEndpoint {
    @RequestMapping(method = GET, value = "/", produces = "image/png")
    public ResponseEntity depict(
            @RequestParam("val") String moleculeBody,
            @RequestParam(defaultValue = "200") String width, @RequestParam(defaultValue = "200") String height) {
        OEGraphMol mol = Molecule.parse(moleculeBody).getOeMolecule();
        OEImage image = new OEImage(Double.parseDouble(width), Double.parseDouble(height));
        OEPrepareDepiction(mol, false, true);

        OE2DMolDisplayOptions displayOptions = new OE2DMolDisplayOptions(image.GetWidth(), image.GetHeight(), 50);
        OE2DMolDisplay display = new OE2DMolDisplay(mol, displayOptions);

        oeosstream os = new oeosstream();
//        boolean renderSucceeded = OERenderMolecule(image, display);
        boolean renderSucceeded = OERenderMolecule(os, "png", display);
//        OEWriteImageToString("png", image)
        return ResponseEntity.ok(os.getByteArray());
    }

}
