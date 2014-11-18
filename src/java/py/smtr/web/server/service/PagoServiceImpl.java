/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.smtr.ejb.facades.PagosFacade;

import py.smtr.web.client.service.PagoService;

/**
 *
 * @author IDTK
 */
public class PagoServiceImpl extends RemoteServiceServlet implements PagoService {

    @EJB
    PagosFacade pagosFacade;
    private Logger logger = Logger.getLogger("log");
    
        public void procesarPago(Integer idFactura, double monto) throws Exception {
            
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN procesarPago IMPL:" + sesion+ ";" + idFactura+ ";" +monto);
        try {
          
            pagosFacade.procesarPagoParcial(sesion, idFactura, monto);
            pagosFacade.procesarPago(sesion, idFactura, monto);
            
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }     
        logger.info("OUT: OK");
    }
}
