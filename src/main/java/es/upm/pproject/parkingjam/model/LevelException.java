package es.upm.pproject.parkingjam.model;

public class LevelException extends Exception {
    private static final long serialVersionUID = 1L;

    // Constructor por defecto
    public LevelException() {
        super();
    }

    // Constructor que acepta un mensaje
    public LevelException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public LevelException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor que acepta una causa
    public LevelException(Throwable cause) {
        super(cause);
    }
}
