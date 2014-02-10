package com.venefica.connect;

/**
 * The exception is thrown when the user tries to establish second connection to
 * the same social network.
 *
 * @author Sviatoslav Grebenchukov
 */
@SuppressWarnings("serial")
public class MultipleConnectionsException extends Exception {

    public MultipleConnectionsException() {
        super("Only one connection per social framework is allowed!");
    }
}
