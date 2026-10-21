package com.laboratorio.excepciones;
public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String mensaje) { super(mensaje); }
}
