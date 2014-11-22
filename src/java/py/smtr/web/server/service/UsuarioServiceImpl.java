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
import py.smtr.ejb.entities.Usuarios;
import py.smtr.ejb.exceptions.EJBWithOutRollBackException;
import py.smtr.ejb.exceptions.EJBWithRollBackException;
import py.smtr.ejb.shared.ResponseLogin;
import py.smtr.ejb.exceptions.SMTRExceptionEJB;
import py.smtr.ejb.facades.UsuarioFacade;
import py.smtr.ejb.shared.ResponseEntidad;

import py.smtr.web.client.service.UsuarioService;



public class UsuarioServiceImpl extends RemoteServiceServlet implements UsuarioService {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    private Logger logger = Logger.getLogger("log");
    //private static Logger rootLogger = Logger.getLogger("");

   

    @Override
    public ResponseLogin loginUsuario(String usuario, String contrasena) throws Exception {

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("sesion");
        httpSession.removeAttribute("usuario");
        ResponseLogin resp = null;
        logger.info("\n\n-----------------Accediendo a LoginUsuario----------------\n\n");

        try {
            resp = usuarioFacade.login(usuario, contrasena);
            
        } catch (Exception ex) {
            throw new Exception("Error");
        }
        httpSession.setAttribute("sesion", resp.getSesion());
        logger.info("OUT resp Login Usuario:" + resp);
        return resp;

    }

    public String metodoServices(String param1) throws Exception {
        /* Esto repetir en todos los metodos */
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession httpSession = request.getSession();
        String sesion = (String) httpSession.getAttribute("sesion");
        /***************************************/
        
        try {
            String cambiarMetodo = usuarioFacade.cambiarMetodo(sesion, param1);
            return cambiarMetodo;
        } catch (Exception ex) {
            throw new Exception("Ha ocurrido un error inesperado");
        }

    }

    @Override
  public void guardarUsuario(String username, String nombres, String apellidos, long documento, String email, String password, Boolean activo) throws Exception{
     
            logger.info("\n\n-----------------Accedió Guardar Usuario UsuarioServiceImpl----------------\n\n");
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String sesion = (String) session.getAttribute("sesion");
            logger.info("\n\n-----------------Accedió Guardar Usuario UsuarioServiceImpl----------------\n\n");
         
     try {
            logger.info("\n\n-----------------Accedió try Guardar Usuario UsuarioServiceImpl----------------\n\n");
            usuarioFacade.guardarUsuario(sesion, username, nombres, documento, email, password, activo);
            } catch (SMTRExceptionEJB ex) {
            logger.info("\n\n-----------------Accedió cath Guardar Usuario UsuarioServiceImpl----------------\n\n");
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            throw new Exception("Ha ocurrido un error inesperadooooooo");
        }
       
    }

    @Override
    //public void borrarUsuario(List<Integer> listaIds) throws WARException {
    public void borrarUsuario(List<Integer> listaIds) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("UsuarioServiceImpl Borrar Usuario" + sesion+ ";" + listaIds.toString());
     
        
        try {
            usuarioFacade.borrarListUsuarios(sesion, listaIds);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK"); 
    }
    
    
    @Override
    public void logout(){
        logger.info("\n\n-----------------Accedió al método Logout----------------\n\n");
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        usuarioFacade.logout(sesion);
        session.removeAttribute("sesion"); 
    }
    
    @Override
    public List<Integer> getListaRoles(Integer idUsuario) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        List<Integer> listaRoles = null;
        
        try {
            listaRoles = usuarioFacade.getListaRoles(sesion, idUsuario);        
        } catch (EJBWithOutRollBackException ex) {
        }
        return listaRoles;        
    }
    
    
    
    @Override
    public PagingLoadResult<BaseModel> listarUsuarios(PagingLoadConfig config) throws Exception {

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        //logger.info("\n------------IN:" + sesion + ";" + config.getOffset() + ";" + config.getLimit() + "\n");
        ResponseEntidad respLU = null;
        try {
            respLU = usuarioFacade.listarUsuarios(sesion, config.getOffset(), config.getLimit());
        } catch (EJBWithOutRollBackException ex) {
            //logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        //logger.info("OUT:" + respLU.getCantidadTotal() + ";" + respLU.getListaEntidad().toString());

        List<Usuarios> listaUsuarios = respLU.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Usuarios usu : listaUsuarios) {
            BaseModel bm = new BaseModel();
            bm.set("id", usu.getId());
            bm.set("login", usu.getLogin());
            bm.set("nombre", usu.getNombre());
            bm.set("email", usu.getEmail());
            bm.set("ci", usu.getCi());
            bm.set("activo", usu.getActivo());
            String fechaCreacion;
            String fechaModificacion;
            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                fechaCreacion = formatOut.format(usu.getCreated());
            } catch (Exception ex) {
                fechaCreacion = "";
            }
            try {
                fechaModificacion = formatOut.format(usu.getChanged());
            } catch (Exception ex) {
                fechaModificacion = "";
            }
            bm.set("created", fechaCreacion);
            bm.set("changed", fechaModificacion);

            result.add(bm);
        }
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLU.getCantidadTotal());
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    //public void actualizarUsuario(Integer id, String nombre, long documento, String email) throws Exception {
    public void actualizarUsuario(Integer id, String nombre, long documento, String email, Boolean activo) throws Exception {    
        
        
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        try {
            //usuarioFacade.actualizarUsuario(sesion, id, nombre, documento, email);
            usuarioFacade.actualizarUsuario(sesion, id, nombre, documento, email, activo);
        } catch (Exception ex) {
        }
    }
    
    
     @Override
    public void actualizarRolesUsuario(Integer idUsuario, Boolean roles[]){
        
        logger.info("----Accedió actualizarRolesUsuario de UsuarioServiceImpl----");
        
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");

        try {
            usuarioFacade.actualizarRolesUsuario(sesion, idUsuario, roles);
        } catch (EJBWithRollBackException ex) {
            return;
        }          
    }
    
    
}