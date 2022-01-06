package de.bv.utils.dbiep.tfanalyzer.exceptions;

public class DBIEPException extends RuntimeException {
    public DBIEPException() {
        super();
    }

    public DBIEPException(String message) {
        super(message);
    }

    public DBIEPException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBIEPException(Throwable cause) {
        super(cause);
    }

    protected DBIEPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
