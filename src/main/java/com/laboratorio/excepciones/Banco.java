package com.laboratorio.excepciones;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
public class Banco {
    private final Map<String, BigDecimal> saldos = new HashMap<>();
    public Banco() { saldos.put("CLI-001", new BigDecimal("500.00")); }
    public BigDecimal consultar(String cliente) {
        BigDecimal saldo = saldos.get(cliente);
        if (saldo == null) throw new ClienteNoEncontradoException("Cliente no registrado: " + cliente);
        return saldo;
    }
    public BigDecimal retirar(String cliente, BigDecimal monto) throws SaldoInsuficienteException {
        if (monto == null || monto.signum() <= 0) throw new IllegalArgumentException("El monto debe ser positivo");
        BigDecimal saldo = consultar(cliente);
        if (saldo.compareTo(monto) < 0) throw new SaldoInsuficienteException("Saldo " + saldo + ", retiro solicitado " + monto);
        BigDecimal nuevo = saldo.subtract(monto);
        saldos.put(cliente, nuevo);
        return nuevo;
    }
}
