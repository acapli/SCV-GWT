/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.war.server;

import py.smtr.web.war.*;
import java.util.List;
import py.smtr.ejb.entities.Clientes;
import py.smtr.ejb.shared.DetalleCV;


/**
 *
 * @author Strogg
 */
public class FacturaImprimir {

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public List<DetalleCV> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleCV> detalle) {
        this.detalle = detalle;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    private List<DetalleCV> detalle;
    private String nroFactura;
    private Clientes cliente;
    private Double total;
    
}
