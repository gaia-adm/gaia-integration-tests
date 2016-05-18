package com.adm.gaia.common;

public class GaiaITestException extends RuntimeException {

    public GaiaITestException(String message) {

        super(message);
    }

    public GaiaITestException(String message, Exception e) {

        super(message, e);
    }
}
