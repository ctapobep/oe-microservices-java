package com.oecontrib.microservices;

import openeye.oechem.OEFormat;
import openeye.oechem.OEGraphMol;
import openeye.oechem.oemolistream;
import openeye.oechem.oemolostream;

import static openeye.oechem.oechem.OEReadMolecule;
import static openeye.oechem.oechem.OEWriteMolecule;

public class Molecule {
    public enum MoleculeType {
        SMILES("string"), MOL2("string");
        private boolean isBinary;

        MoleculeType(String binaryOrString) {
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
    public String toFasta() {
        oemolostream output = new oemolostream();
        output.SetFormat(OEFormat.MOL2);
        output.openstring();
        OEWriteMolecule(output, this.oeMolecule);
        return output.GetString();
    }
    public String toString(String format) {
        switch (MoleculeType.parse(format)) {
            case SMILES: return toSmiles();
            case MOL2: return this.toFasta();
            default: throw new WrongMoleculeFormatException(format);
        }
    }

    public OEGraphMol getOeMolecule() {
        return this.oeMolecule;
    }
}
