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
@RemoteServiceRelativePath("service/clienteservice")
public interface ClienteService extends RemoteService {

  public void guardarCliente(String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception;
  public void borrarClientes(List<Integer> listaIds) throws Exception;
  public PagingLoadResult<BaseModel> listarClientes(PagingLoadConfig config)  throws Exception;
  public void actualizarCliente(Integer id, String nombre, String ruc, String direccion, String telefono, String email, Boolean activo) throws Exception;
  public ClienteProveedorWAR obtenerCliente(String ruc) throws Exception;
}
