package it.univaq.disim.mwt.j2etpapp.business;

public class FileTypeException extends Exception {
    public FileTypeException() {
        super();
    }

    public FileTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTypeException(String message) {
        super(message);
    }

    public FileTypeException(Throwable cause) {
        super(cause);
    }
}
