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
import py.smtr.web.shared.ClienteProveedorWAR;

/**
 *
 * @author IDTK
 */
@RemoteServiceRelativePath("service/proveedorservice")
public interface ProveedorService extends RemoteService {

    public void guardarProveedor(String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception;
    public PagingLoadResult<BaseModel> listarProveedores(PagingLoadConfig config)  throws Exception;
    public void actualizarProveedor(Integer id, String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception;
    public void borrarProveedores(List<Integer> listaIds) throws Exception;
     public ClienteProveedorWAR obtenerProveedor(String ruc) throws Exception;
}
