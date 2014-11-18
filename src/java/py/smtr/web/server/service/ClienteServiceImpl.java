/*
 * To change this template, choose Tools | Templates
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
import py.smtr.ejb.entities.Clientes;
import py.smtr.ejb.exceptions.EJBWithOutRollBackException;
import py.smtr.ejb.exceptions.SMTRExceptionEJB;
import py.smtr.ejb.facades.ClientesFacade;
import py.smtr.ejb.facades.UsuarioFacade;
import py.smtr.ejb.shared.ResponseEntidad;
import py.smtr.web.client.service.ClienteService;
import py.smtr.web.shared.ClienteProveedorWAR;
import py.smtr.web.shared.WARException;



/**
 *
 * @author Strogg
 */
public class ClienteServiceImpl extends RemoteServiceServlet implements ClienteService {
    
    @EJB
    private ClientesFacade clientesFacade;
    
    private Logger logger = Logger.getLogger("log");
    public void guardarCliente(String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception{
            logger.info("\n\n-----------------Accedió Guardar Cliente ----------------\n\n");
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String sesion = (String) session.getAttribute("sesion");
            logger.info("\n\n-----------------Accedió Guardar Clienteeeee ----------------\n\n");
         
     try {
            logger.info("\n\n-----------------Accedió try Guardar CLIENTEEEEE UsuarioServiceImpl----------------\n\n");
            
            clientesFacade.guardarCliente(sesion, nombre, ruc, direccion, telefono, email, activo);
            
            } catch (Exception ex) {
            ex.getMessage();
            throw new Exception("Ha ocurrido un error inesperadooooooo");
        }
       
    }
       public void borrarClientes (List<Integer> listaIds) throws Exception{
        
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String sesion = (String) session.getAttribute("sesion");
            
            logger.info("IN:" + sesion+ ";" + listaIds.toString());
            try {
                clientesFacade.borrarListClientes(sesion, listaIds);
            } catch (Exception ex) {
                logger.info("OUT:" + ex.getMessage());
                throw new Exception("Ha ocurrido un error en borrar");
            }
            logger.info("OUT: OK");
         }
       
      public PagingLoadResult<BaseModel> listarClientes(PagingLoadConfig config) throws Exception {
          
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN:" + sesion + ";" + config.getOffset() + ";" + config.getLimit());
        ResponseEntidad respLC = null;
        try {
           respLC = clientesFacade.listarClientes(sesion, config.getOffset(), config.getLimit());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception("Ha ocurrido un error en Listar Clientes!!");
        }
        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());

        List<Clientes> listaClientes = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Clientes usu : listaClientes) {
            BaseModel bm = new BaseModel();
            bm.set("id", usu.getId());
            bm.set("nombre", usu.getNombre());
            bm.set("direccion", usu.getDireccion());
            bm.set("ruc", usu.getRuc());
            bm.set("telefono", usu.getTelefono());
            bm.set("mail", usu.getMail());
            bm.set("activo", usu.getActivo());
            
//            String fechaCreacion;
//            String fechaModificacion;
//            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//            try {
//                fechaCreacion = formatOut.format(usu.getCreated());
//            } catch (Exception ex) {
//                fechaCreacion = "";
//            }
//            try {
//                fechaModificacion = formatOut.format(usu.getChanged());
//            } catch (Exception ex) {
//                fechaModificacion = "";
//            }
//            bm.set("created", fechaCreacion);
//            bm.set("changed", fechaModificacion);

            result.add(bm);
        }
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLC.getCantidadTotal());
    }
      
     
       public void actualizarCliente(Integer id, String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception {
           HttpServletRequest request = this.getThreadLocalRequest();
           HttpSession session = request.getSession();
           String sesion = (String) session.getAttribute("sesion");
           logger.info("IN:" + sesion + ";" + id + ";" + nombre + ";" + ruc + ";" + direccion + ";" + telefono + ";" + email+ ";" +activo);
           try {
               clientesFacade.actualizarCliente(sesion, id, nombre, ruc,direccion, telefono, email, activo);
           } catch (Exception ex) {
               logger.info("OUT:" + ex.getMessage());
               throw new Exception(ex.getMessage());
           }
           logger.info("OUT: OK");
       }
        
       public ClienteProveedorWAR obtenerCliente(String ruc) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN obtenerCliente Impl:" + sesion + ";" + ruc);
        
        ClienteProveedorWAR cliente = new ClienteProveedorWAR();
        try {
            Clientes clienteEJB = clientesFacade.obtenerClienteByRUC(sesion, ruc);
            if(!clienteEJB.getActivo()) {
                throw new WARException("El cliente no está activo en el sistema.");
            }
            cliente.setId(clienteEJB.getId());
            cliente.setNombre(clienteEJB.getNombre());
            cliente.setDireccion(clienteEJB.getDireccion());
            cliente.setTelefonos(clienteEJB.getTelefono());
            cliente.setEmail(clienteEJB.getMail());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new WARException(ex.getMessage());
        }
        logger.info("OUT:"+cliente.toString());
        return cliente;
    }

   
         
}

