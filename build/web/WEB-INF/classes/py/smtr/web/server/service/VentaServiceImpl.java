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
import py.smtr.ejb.facades.VentasFacade;
import py.smtr.ejb.shared.DetalleCV;

import py.smtr.web.client.service.VentaService;

/**
 *
 * @author IDTK
 */
public class VentaServiceImpl extends RemoteServiceServlet implements VentaService {
 @EJB
    VentasFacade ventasFacade;
    private Logger logger = Logger.getLogger("log");
    
    @Override
    public void guardarVenta(Integer idCliente, Date fecha, double total, List<DetalleCV> detalles) throws Exception {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN:" + sesion+ ";" + idCliente+ ";" +fecha.toString()+ ";" +total+ ";" +detalles.toString());
        String numeroFactura;
        try {
            numeroFactura = ventasFacade.guardarVenta(sesion, idCliente, fecha, total, detalles);
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT:"+numeroFactura);
        session.setAttribute("nroFactura", numeroFactura);
        session.setAttribute("idCliente", idCliente);
        session.setAttribute("fecha", fecha);
        session.setAttribute("total", total);
        session.setAttribute("detalleFact", detalles);

    }

    
}
