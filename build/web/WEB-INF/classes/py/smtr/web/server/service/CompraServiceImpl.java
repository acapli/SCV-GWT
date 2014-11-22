/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.smtr.ejb.facades.ComprasFacade;
import py.smtr.ejb.shared.DetalleCV;

import py.smtr.web.client.service.CompraService;

/**
 *
 * @author IDTK
 */
public class CompraServiceImpl extends RemoteServiceServlet implements CompraService {

    @EJB
    ComprasFacade comprasFacade;
    private Logger logger = Logger.getLogger("log");
    
        public void guardarCompra(Integer idProveedor, Date fecha, double total, List<DetalleCV> detalles) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN:" + sesion+ ";" + idProveedor+ ";" +fecha.toString()+ ";" +total+ ";" +detalles.toString());
        try {
            comprasFacade.guardarCompra(sesion, idProveedor, fecha, total, detalles);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT: OK");
    }
}
