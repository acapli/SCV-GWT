/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.shared;

import java.io.Serializable;

/**
 *
 * @author Strogg
 */
public class ProveedorSimple implements Serializable {

    private Integer id;
    private String nombre;
    private String ruc;  
    private Boolean activo;
   

    public ProveedorSimple() {
    }        

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Boolean getActivo() {
        return activo;
    }

    public String getRuc() {
        return ruc;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
 
    @Override
    public String toString() {
        return "[" + id + "," + nombre + "," + ruc + "," + activo + "]";
    }
}
