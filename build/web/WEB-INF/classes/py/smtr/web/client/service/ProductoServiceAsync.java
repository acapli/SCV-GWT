/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import py.smtr.web.shared.ProveedorSimple;

/**
 *
 * @author IDTK
 */
public interface ProductoServiceAsync {

    public void listarProductos(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BaseModel>> callback);
    public void actualizarProducto(Integer id, String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo, AsyncCallback<Void> callback);
    public void guardarProducto(String nombre, String codigo, double porcentajeGanancia, double costo, double cantidadExistencia, Boolean activo, AsyncCallback<Void> callback);
    public void borrarProductos(List<Integer> listaIds, AsyncCallback<Void> callback);
    public void obtenerListaProveedoresAsignados(Integer idProducto, AsyncCallback<List<ProveedorSimple>> callback); 
    public void actualizarProveedoresProducto(Integer idProducto, List<ProveedorSimple> proveedores, AsyncCallback<Void> callback); 
    public void obtenerProductosVenta(AsyncCallback<List<BaseModel>> callback);
    public void obtenerProductosByProveedor(Integer idProveedor, AsyncCallback<List<BaseModel>> callback);
}
