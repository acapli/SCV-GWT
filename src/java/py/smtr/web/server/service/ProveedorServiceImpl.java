/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.server.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.smtr.ejb.entities.Proveedores;
import py.smtr.ejb.exceptions.EJBWithOutRollBackException;
import py.smtr.ejb.facades.ProveedoresFacade;
import py.smtr.ejb.shared.ResponseEntidad;

import py.smtr.web.client.service.ProveedorService;
import py.smtr.web.shared.ClienteProveedorWAR;


public class ProveedorServiceImpl extends RemoteServiceServlet implements ProveedorService {
    
     @EJB
     ProveedoresFacade proveedoresFacade;
     private Logger logger = Logger.getLogger("log");

   
      @Override
    public void guardarProveedor(String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        logger.info("IN:" + sesion + ";" + nombre + ";" + ruc + ";" + direccion + ";" + telefono + ";" + email + ";" + activo);
        try {
          proveedoresFacade.guardarProveedor(sesion, nombre, ruc, direccion, telefono, email, activo);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    
    @Override
    public void actualizarProveedor(Integer id, String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN:" + sesion + ";" + id + ";" + nombre + ";" + ruc + ";" + direccion + ";" + telefono + ";" + email + ";" + activo);
        
        try {
            proveedoresFacade.actualizarProveedor(sesion, id, nombre, ruc, direccion, telefono, email, activo);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    
     @Override
    public PagingLoadResult<BaseModel> listarProveedores(PagingLoadConfig config) throws Exception {
       
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        logger.info("IN:" + sesion + ";" + config.getOffset() + ";" + config.getLimit());
        ResponseEntidad respLC = null;
        try {
            respLC = proveedoresFacade.listarProveedores(sesion, config.getOffset(), config.getLimit());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());

        List<Proveedores> listaProveedores = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Proveedores proveedor : listaProveedores) {
            BaseModel bm = new BaseModel();
            bm.set("id", proveedor.getId());
            bm.set("nombre", proveedor.getNombre());
            bm.set("direccion", proveedor.getDireccion());
            bm.set("ruc", proveedor.getRuc());
            bm.set("telefono", proveedor.getTelefono());
            bm.set("email", proveedor.getEmail());
            bm.set("activo", proveedor.getActivo());
            String fechaCreacion;
            String fechaModificacion;
            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                fechaCreacion = formatOut.format(proveedor.getCreated());
            } catch (Exception ex) {
                fechaCreacion = "";
            }
            try {
                fechaModificacion = formatOut.format(proveedor.getChanged());
            } catch (Exception ex) {
                fechaModificacion = "";
            }
            bm.set("created", fechaCreacion);
            bm.set("changed", fechaModificacion);

            result.add(bm);
        }
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLC.getCantidadTotal());
    }
    
    @Override
    public void borrarProveedores(List<Integer> listaIds) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN:" + sesion + ";" + listaIds.toString());
        try {
            proveedoresFacade.borrarListProveedores(sesion, listaIds);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    
     public ClienteProveedorWAR obtenerProveedor(String ruc) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN:" + sesion + ";" + ruc);
        ClienteProveedorWAR proveedor = new ClienteProveedorWAR();
        try {
            Proveedores proveedorEJB = proveedoresFacade.obtenerProveedorByRuc(sesion, ruc);
            if(!proveedorEJB.getActivo()) {
                throw new Exception("El proveedor no está activo en el sistema.");
            }
            proveedor.setId(proveedorEJB.getId());
            proveedor.setNombre(proveedorEJB.getNombre());
            proveedor.setDireccion(proveedorEJB.getDireccion());
            proveedor.setTelefonos(proveedorEJB.getTelefono());
            proveedor.setEmail(proveedorEJB.getEmail());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT:"+proveedor.toString());
        return proveedor;
    }


   
}
