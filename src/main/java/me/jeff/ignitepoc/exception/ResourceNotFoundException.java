package me.jeff.ignitepoc.exception;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Instantiates a new {@link ResourceNotFoundException}.
     *
     * @param message the message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
