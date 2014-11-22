package py.smtr.web.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import py.smtr.ejb.shared.ResponseLogin;


@RemoteServiceRelativePath("service/usuarioservice")
public interface UsuarioService extends RemoteService {

   public ResponseLogin loginUsuario(String usuario, String contrasena) throws Exception;
   public String metodoServices(String param1) throws Exception;
   public void guardarUsuario(String username, String nombres, String apellidos, long documento, String email, String password, Boolean activo) throws Exception;
   public void borrarUsuario(List<Integer> listaIds) throws Exception;
   public void logout();
   public List<Integer> getListaRoles(Integer idUsuario);
   public PagingLoadResult<BaseModel> listarUsuarios(PagingLoadConfig config)  throws Exception;
   //public void actualizarUsuario(Integer id, String nombre, long documento, String email) throws Exception;
   public void actualizarUsuario(Integer id, String nombre, long documento, String email, Boolean activo) throws Exception;
   public void actualizarRolesUsuario(Integer idUsuario, Boolean roles[]);
}
