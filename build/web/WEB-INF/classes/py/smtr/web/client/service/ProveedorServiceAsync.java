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
import py.smtr.web.shared.ClienteProveedorWAR;


public interface ProveedorServiceAsync {

     public void guardarProveedor(String nombre, String ruc, String direccion, String telefono, String email, Boolean activo, AsyncCallback<Void> callback);
     public void listarProveedores(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BaseModel>> callback);
     public void actualizarProveedor(Integer id, String nombre, String ruc, String direccion, String telefono, String email, Boolean activo, AsyncCallback<Void> callback);
     public void borrarProveedores(List<Integer> listaIds, AsyncCallback<Void> callback);
     public void obtenerProveedor(String ruc, AsyncCallback<ClienteProveedorWAR> callback);
}
