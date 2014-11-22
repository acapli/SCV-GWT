package py.smtr.web.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import py.smtr.ejb.shared.ResponseLogin;


public interface UsuarioServiceAsync {
    
      public void loginUsuario(String usuario, String contrasena, AsyncCallback<ResponseLogin> asyncCallback);
      
      public void metodoServices(String param1, AsyncCallback<String> asyncCallback);
      
      public void guardarUsuario(String username, String nombres, String apellidos, long documento, String email, String password, Boolean activo, AsyncCallback<Void> callback);
    
      public void borrarUsuario(List<Integer> listaIds, AsyncCallback<Void> callback);
       
      public void logout(AsyncCallback<Void> callback);
       
      public void getListaRoles(Integer idUsuario, AsyncCallback<List<Integer>> callback);
      
      public void listarUsuarios(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BaseModel>> callback);
      
      //public void actualizarUsuario(Integer id, String nombre, long documento, String email, AsyncCallback<Void> callback);
      public void actualizarUsuario(Integer id, String nombre, long documento, String email, Boolean activo, AsyncCallback<Void> callback);
              
      public void actualizarRolesUsuario(Integer idUsuario, Boolean roles[], AsyncCallback<Void> callback);
}