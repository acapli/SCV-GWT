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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.smtr.ejb.entities.Clientes;
import py.smtr.ejb.entities.Productos;
import py.smtr.ejb.entities.ProductosProveedores;
import py.smtr.ejb.entities.Proveedores;
import py.smtr.ejb.exceptions.EJBWithOutRollBackException;
import py.smtr.ejb.exceptions.EJBWithRollBackException;
import py.smtr.ejb.facades.ClientesFacade;
import py.smtr.ejb.facades.ProductosFacade;
import py.smtr.ejb.facades.ProveedoresFacade;
import py.smtr.ejb.shared.ResponseEntidad;
import py.smtr.web.shared.ProveedorSimple;
import py.smtr.web.shared.WARException;

import py.smtr.web.client.service.ProductoService;
import py.smtr.web.shared.ClienteProveedorWAR;

/**
 *
 * @author
 */
public class ProductoServiceImpl extends RemoteServiceServlet implements ProductoService {

    @EJB
    ProductosFacade productosFacade;
    
    @EJB
    ProveedoresFacade proveedoresFacade;
    
     @EJB
    ClientesFacade clientesFacade;
    
    private Logger logger = Logger.getLogger("log");

   // @Override
    public PagingLoadResult<BaseModel> listarProductos(PagingLoadConfig config) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        logger.info("IN:" + sesion + ";" + config.getOffset() + ";" + config.getLimit());
        ResponseEntidad respLC = null;
        try {
            respLC = productosFacade.listarProductos(sesion, config.getOffset(), config.getLimit());
        } catch (Exception ex) {
            logger.info("Error en listar productos!!");
            throw new Exception(ex.getMessage());
        }

        List<Productos> listaProductos = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Productos producto : listaProductos) {
            BaseModel bm = new BaseModel();
            bm.set("id", producto.getId());
            bm.set("nombre", producto.getNombre());
            bm.set("codigo", producto.getCodigo());
            bm.set("porcentaje_ganancia", producto.getPorcentajeGanancia());
            bm.set("costo", producto.getCosto());
            bm.set("precio_venta",producto.getCosto()+producto.getCosto()*producto.getPorcentajeGanancia()/100);
            bm.set("cantidad_existencia", producto.getCantidadExistencia());
            bm.set("activo", producto.getActivo());
            
            String fechaCreacion;
            String fechaModificacion;
            
            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                fechaCreacion = formatOut.format(producto.getCreated());
            } catch (Exception ex) {
                fechaCreacion = "";
            }
            try {
                fechaModificacion = formatOut.format(producto.getChanged());
            } catch (Exception ex) {
                fechaModificacion = "";
            }
            bm.set("created", fechaCreacion);
            bm.set("changed", fechaModificacion);

            result.add(bm);
        }

        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLC.getCantidadTotal());
    }
    
    public void actualizarProducto(Integer id, String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN:" + sesion + ";" + id + ";" + nombre + ";" + codigo + ";" + porcentajeGanancia + ";" + costo + ";" + cantidadExistencia + ";" + activo);
        
        try {
            productosFacade.actualizarProducto(sesion, id, nombre, codigo, porcentajeGanancia, costo, cantidadExistencia, activo);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
        
     //@Override
    public void guardarProducto(String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo) throws Exception {
        
        logger.info(" Entro en guardar Productoooo!!");
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        logger.info("IN:" + sesion + ";" + nombre + ";" + codigo + ";" + porcentajeGanancia + ";" + costo + ";" + cantidadExistencia + ";" + activo);
        try {
            productosFacade.guardarProducto(sesion, nombre, codigo, porcentajeGanancia, costo, cantidadExistencia, activo);
        } catch (Exception ex) {
            logger.info(" Error en guardar producto!!");
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    
    
      //@Override
     public void borrarProductos(List<Integer> listaIds) throws WARException {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN:" + sesion + ";" + listaIds.toString());
        try {
            productosFacade.borrarListProductos(sesion, listaIds);
        } catch (EJBWithRollBackException ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new WARException(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    //@Override
  /*  public List<BaseModel> obtenerProductos() throws WARException {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN:" + sesion);

        ResponseEntidad respLC = null;
        try {
            respLC = productosFacade.listarProductos(sesion, null, null);
        } catch (EJBWithOutRollBackException ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new WARException(ex.getMessage());
        }

        List<Productos> listaProductos = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Productos producto : listaProductos) {
            BaseModel bm = new BaseModel();
            bm.set("id", producto.getId());
            bm.set("nombre", producto.getNombre());
            bm.set("codigo", producto.getCodigo());
            bm.set("porcentaje_ganancia", producto.getPorcentajeGanancia());
            bm.set("costo", producto.getCosto());
            result.add(bm);
        }

        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());
        return result;
    }
  */
     
    @Override
    public void actualizarProveedoresProducto(Integer idProducto, List<ProveedorSimple> proveedores) /*throws WARException*/ {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        logger.info("IN:" + sesion);

        try {
            List<Proveedores> proveedoresEJB = new ArrayList<Proveedores>();

            for (ProveedorSimple proveedorWAR : proveedores) {
                Proveedores p = new Proveedores();
                p.setId(proveedorWAR.getId());
                p.setNombre(proveedorWAR.getNombre());
                p.setRuc(proveedorWAR.getRuc());
                p.setActivo(proveedorWAR.getActivo());
                proveedoresEJB.add(p);
            }
            productosFacade.actualizarProveedoresProducto(sesion, idProducto, proveedoresEJB);
        } catch (EJBWithRollBackException ex) {
            logger.info("OUT:" + ex.getMessage());
            //throw new WARException(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
    
    //@Override
    public List<ProveedorSimple> obtenerListaProveedoresAsignados(Integer idProducto) /*throws WARException*/ {
        
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN:" + sesion);
        
        logger.info("Entro en Obtener Lista Proveedores Asignados!!!");
               
        List<Proveedores> listaProveedores = null;        
        List<Proveedores> allProveedores = null;
        
        List<ProveedorSimple> listaProveedoresWAR = null;
        List<Integer> idsProveedores = null;

        try {
            listaProveedores = productosFacade.getListaProveedoresAsignados(sesion, idProducto);        //los proveedores asignados
            ResponseEntidad<Proveedores> respEntidad = proveedoresFacade.listarProveedores(sesion, null, null); // productosFacade.getProveedores(sesion);                                    //todos los proveedores
            allProveedores = respEntidad.getListaEntidad();
            idsProveedores = new ArrayList<Integer>();
            listaProveedoresWAR = new ArrayList<ProveedorSimple>();
            
             logger.info("Obtuvo la ListaProveedores" );
            
            for (Proveedores proveedor : listaProveedores) {
                idsProveedores.add(proveedor.getId());
            }
            
            for (Proveedores proveedor : allProveedores) {
                ProveedorSimple ps = new ProveedorSimple();
                ps.setId(proveedor.getId());
                ps.setNombre(proveedor.getNombre());
                ps.setRuc(proveedor.getRuc());
                if(idsProveedores.contains(proveedor.getId())){
                    ps.setActivo(true);
                }else{
                    ps.setActivo(false);
                }
                listaProveedoresWAR.add(ps);
            }                        
        } catch (EJBWithOutRollBackException ex) {
            logger.info("OUT:" + ex.getMessage());
            logger.info("Error en Obtener Lista Proveedores Asignados" );
            //throw new WARException(ex.getMessage());
        }        
        logger.info("OUT:" + listaProveedores.toString());
        return listaProveedoresWAR;
    }
    
    //@Override
    public List<BaseModel> obtenerProductosVenta() throws Exception {
        
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN - Entro en obtenerProductosVenta :" + sesion);

        ResponseEntidad respLC = null;
        try {
            respLC = productosFacade.listarProductos(sesion, null, null);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new WARException(ex.getMessage());
        }

        List<Productos> listaProductos = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Productos producto : listaProductos) {
            BaseModel bm = new BaseModel();
            bm.set("id", producto.getId());
            bm.set("nombre", producto.getNombre());
            bm.set("codigo", producto.getCodigo());
            bm.set("porcentaje_ganancia", producto.getPorcentajeGanancia());
            double costo = producto.getCosto();
            double precioVenta = (costo * (producto.getPorcentajeGanancia() / 100)) + costo;
            DecimalFormat df = new DecimalFormat("#.##");
            bm.set("costo_venta", Double.valueOf((df.format(precioVenta).replaceAll(",", "."))));
            result.add(bm);
        }

        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());
        return result;
    }
    
    public List<BaseModel> obtenerProductosByProveedor(Integer idProveedor) throws Exception {
        
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        logger.info("IN obtenerProductosByProveedor:" + sesion);
        
        List<ProductosProveedores> productosProveedores = null;
        try {
            productosProveedores = productosFacade.getProductosProveedoresByProveedor(sesion, idProveedor);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new WARException(ex.getMessage());
        }

        List<BaseModel> result = new ArrayList<BaseModel>();

        for (ProductosProveedores productoProveedor : productosProveedores) {
            Productos producto = productoProveedor.getIdProducto();
            BaseModel bm = new BaseModel();
            bm.set("id", producto.getId());
            bm.set("nombre", producto.getNombre());
            bm.set("codigo", producto.getCodigo());
            bm.set("porcentaje_ganancia", producto.getPorcentajeGanancia());
            bm.set("costo", producto.getCosto());
            result.add(bm);
        }

        logger.info("OUT:" + productosProveedores.size() + ";" + productosProveedores);
        return result;
    }
    
       


}
