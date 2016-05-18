package com.oecontrib.microservices;

public class WrongMoleculeFormatException extends RuntimeException {
    public WrongMoleculeFormatException(String format) {
        super("Unknown molecule format: " + format);
    }
}
