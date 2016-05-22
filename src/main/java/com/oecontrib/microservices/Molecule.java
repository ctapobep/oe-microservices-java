package com.oecontrib.microservices;

import openeye.oechem.OEFormat;
import openeye.oechem.OEGraphMol;
import openeye.oechem.oemolistream;
import openeye.oechem.oemolostream;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oechem.oechem.OEWriteMolecule;

public class Molecule {
    public enum MoleculeType {
        SMILES(OEFormat.SMI, "string"), ISM(OEFormat.ISM, "string"), USM(OEFormat.USM, "string"),
        MOL2(OEFormat.MOL2, "string"), MOL2H(OEFormat.MOL2H, "string"),
        MMOD(OEFormat.MMOD, "string"),
        CDX(OEFormat.CDX, "binary"), OEB(OEFormat.OEB, "binary"),
        SDF(OEFormat.SDF, "string"),
        SKC(OEFormat.SKC, "string"),
        XYZ(OEFormat.XYZ, "string");
        private boolean isBinary;
        private int oeFormat;

        MoleculeType(int oeFormat, String binaryOrString) {
            this.oeFormat = oeFormat;
            this.isBinary = binaryOrString.equals("binary");
        }

        public boolean equals(String stringRepresentation) {
            return this.toString().equalsIgnoreCase(stringRepresentation);
        }
        public static MoleculeType parse(String stringRepresentation) {
            for(MoleculeType type: values()) {
                if(type.equals(stringRepresentation)) return type;
            }
            throw new WrongMoleculeFormatException(stringRepresentation);
        }
    }

    private final OEGraphMol oeMolecule;

    public Molecule(OEGraphMol oeMolecule) {
        this.oeMolecule = oeMolecule;
    }
    public static Molecule parse(String formatAsString, String moleculeBody) {
        MoleculeType format = MoleculeType.parse(formatAsString);
        OEGraphMol mol = new OEGraphMol();

        oemolistream molInputStream = new oemolistream();
        int molFormat = format.oeFormat;
        molInputStream.SetFormat(molFormat);

        boolean openedStream = molInputStream.openstring(moleculeBody);
        if (!openedStream) throw new IllegalArgumentException("Could not read the molecule");

        boolean successfullyCreated = OEReadMolecule(molInputStream, mol);
        if (!successfullyCreated) throw new IllegalArgumentException("Could not parse specified molecule");
        return new Molecule(mol);
    }
    public static Molecule parse(String moleculeBody) {
        return parse(MoleculeType.SMILES.toString(), moleculeBody);
    }
    public String toString(String format) {
        oemolostream output = new oemolostream();
        output.SetFormat(MoleculeType.parse(format).oeFormat);
        output.openstring();
        OEWriteMolecule(output, this.oeMolecule);
        return output.GetString();
    }

    public OEGraphMol getOeMolecule() {
        return this.oeMolecule;
    }
}
