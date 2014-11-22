/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import py.smtr.web.shared.ProveedorSimple;
import py.smtr.web.shared.WARException;

/**
 *
 * @author 
 */
@RemoteServiceRelativePath("service/productoservice")
public interface ProductoService extends RemoteService {

    public PagingLoadResult<BaseModel> listarProductos(PagingLoadConfig config) throws Exception;
    public void actualizarProducto(Integer id, String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo) throws Exception;
    public void guardarProducto(String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo) throws Exception;
    public void borrarProductos(List<Integer> listaIds) throws Exception;
    public List<ProveedorSimple> obtenerListaProveedoresAsignados(Integer idProducto); // throws WARException;
    public void actualizarProveedoresProducto(Integer idProducto, List<ProveedorSimple> proveedores); // throws WARException;
    public List<BaseModel> obtenerProductosVenta() throws Exception;
    public List<BaseModel> obtenerProductosByProveedor(Integer idProveedor) throws Exception;


}
