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
public class ClienteProveedorWAR implements Serializable {

    private Integer id;
    private String nombre;
    private String direccion;
    private String telefonos;
    private String email;

    public ClienteProveedorWAR() {
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    @Override
    public String toString() {
        return "[" + id + "," + nombre + "," + direccion + "," + telefonos + "," + email + "]";
    }
}
