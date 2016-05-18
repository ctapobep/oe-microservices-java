package com.oecontrib.microservices;

import openeye.oechem.OEFormat;
import openeye.oechem.OEGraphMol;
import openeye.oechem.oemolistream;
import openeye.oechem.oemolostream;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oechem.oechem.OEWriteMolecule;

public class Molecule {
    private final OEGraphMol oeMolecule;

    public Molecule(OEGraphMol oeMolecule) {
        this.oeMolecule = oeMolecule;
    }

    public static Molecule parse(String moleculeBody) {
        OEGraphMol mol = new OEGraphMol();

        oemolistream molInputStream = new oemolistream();
        int molFormat = OEFormat.SMI;
        molInputStream.SetFormat(molFormat);

        boolean openedStream = molInputStream.openstring(moleculeBody);
        if (!openedStream) throw new IllegalArgumentException("Could not read the molecule");

        boolean successfullyCreated = OEReadMolecule(molInputStream, mol);
        if (!successfullyCreated) throw new IllegalArgumentException("Could not parse specified molecule");
        return new Molecule(mol);
    }

    public String toSmiles() {
        oemolostream output = new oemolostream();
        output.openstring();
        OEWriteMolecule(output, this.oeMolecule);
        return output.GetString();
    }
    public String toString(String format) {
        if("smiles".equalsIgnoreCase(format)) {
            return toSmiles();
        }
        throw new WrongMoleculeFormatException(format);
    }

    public OEGraphMol getOeMolecule() {
        return this.oeMolecule;
    }
}
