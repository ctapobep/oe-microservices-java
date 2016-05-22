package com.oecontrib.microservices.v2;

import com.oecontrib.microservices.Molecule;
import openeye.oechem.oeosstream;
import openeye.oedepict.OE2DMolDisplay;
import openeye.oedepict.OE2DMolDisplayOptions;
import openeye.oedepict.OEImage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static openeye.oedepict.oedepict.OEPrepareDepiction;
import static openeye.oedepict.oedepict.OERenderMolecule;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller @RequestMapping("/v2/structure")
public class ConvertingEndpoint {
    @RequestMapping(method = GET, produces = {
            "text/smiles", "text/usm", "text/ism", "text/sdf", "text/mol2", "text/mol2h", "text/xyz", "text/mmod",
            "image/png", "image/svg+xml"})
    public ResponseEntity convert(@RequestParam("val") String moleculeBody, HttpServletRequest request) {
        MediaType inputMediaType = MediaType.parseMediaType(paramOrHeader(request, "Content-Type"));
        String inputFormat = inputMediaType.getSubtype();

        MediaType outputMediaType = MediaType.parseMediaType(paramOrHeader(request, "Accept"));
        String outputFormat = outputMediaType.getSubtype();

        Molecule mol = Molecule.parse(inputFormat, moleculeBody);

        if(outputMediaType.getType().equalsIgnoreCase("image")){
            return renderMolecule(mol, outputMediaType, request);
        } else {
            return convertMolecule(mol, outputFormat);
        }
    }

    private ResponseEntity renderMolecule(Molecule mol, MimeType outputFormat, HttpServletRequest request) {
        String width = paramOrDefault(request, "width", "200");
        String height = paramOrDefault(request, "height", "200");
        OEImage image = new OEImage(Double.parseDouble(width), Double.parseDouble(height));
        OEPrepareDepiction(mol.getOeMolecule(), false, true);

        OE2DMolDisplayOptions displayOptions = new OE2DMolDisplayOptions();
        displayOptions.SetWidth(image.GetWidth());
        displayOptions.SetHeight(image.GetHeight());
        OE2DMolDisplay display = new OE2DMolDisplay(mol.getOeMolecule(), displayOptions);

        oeosstream os = new oeosstream();
        boolean renderSucceeded = OERenderMolecule(os, outputFormat.getSubtype().replace("+xml", ""), display);
        if(!renderSucceeded)
            throw new IllegalArgumentException("Specified molecule couldn't be depicted. OE Didn't provide any further information, see logs.");
        return ResponseEntity.ok().contentType(imageMediaType(outputFormat)).body(os.getByteArray());
    }

    private MediaType imageMediaType(MimeType mimeType) {
        return new MediaType(mimeType.getType(), mimeType.getSubtype());
    }

    private ResponseEntity convertMolecule(Molecule mol, String outputFormat) {
        return ResponseEntity.ok(mol.toString(outputFormat));
    }

    private String paramOrDefault(HttpServletRequest request, String param, String defaultValue) {
        return defaultIfBlank(request.getParameter(param), defaultValue);
    }
    private String paramOrHeader(HttpServletRequest request, String name) {
        return defaultIfBlank(request.getParameter(name), request.getHeader(name));
    }
    private String defaultIfBlank(String str, String defaultString) {
        return str != null && !str.trim().isEmpty() ? str : defaultString;
    }
}
