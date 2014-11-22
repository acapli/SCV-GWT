/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import py.smtr.ejb.entities.Facturas;
import py.smtr.ejb.entities.HistorialPagos;
import py.smtr.ejb.entities.Pagos;
import py.smtr.ejb.facades.FacturasFacade;
import py.smtr.ejb.shared.ResponseEntidad;

import py.smtr.web.client.service.ListadoPagosService;

/**
 *
 * @author IDTK
 */
public class ListadoPagosServiceImpl extends RemoteServiceServlet implements ListadoPagosService {
 @EJB
    FacturasFacade facturaFacade;
    private Logger logger = Logger.getLogger("log");
    
        public PagingLoadResult<BaseModel> listarPagos(PagingLoadConfig config) throws Exception {
            
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN Listar HistorialPagos:" + sesion + ";" + config.getOffset() + ";" + config.getLimit());
        ResponseEntidad respLC = null;
        try {
            respLC = facturaFacade.listarFaturasPagadas(sesion, config.getOffset(), config.getLimit());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
      // logger.info("OUT Lista Final:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());
        
        logger.info("OUT Listar HistorialPagos Final:" );
                
        List<HistorialPagos> listaPagos = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (HistorialPagos historialPagos : listaPagos) {
            BaseModel bm = new BaseModel();
            
            bm.set("id", historialPagos.getId());
          
            bm.set("idFactura", historialPagos.getIdFactura().getNumero());
            
//            bm.set("idPagos", historialPagos.getIdPagos());
//            bm.set("descripcion" , "Nro. de Factura:"+historialPagos.getIdFactura().getNumero() + "Nombre del Cliente: " +historialPagos.getIdCliente().getNombre());
//            
              bm.set("descripcion" , "Nro. de Factura:"+historialPagos.getIdFactura().getNumero());
//            bm.set("idUsuario", historialPagos.getIdUsuario());
            
            bm.set("saldoAnterior", historialPagos.getSaldoAnterior());
            bm.set("saldoParcial", historialPagos.getSaldoParcial());
            bm.set("saldoActual", historialPagos.getSaldoActual());

            String fechaCreacion;
            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                fechaCreacion = formatOut.format(historialPagos.getChanged());
            } catch (Exception ex) {
                fechaCreacion = "";
            }
            //bm.set("created", fechaCreacion);

            result.add(bm);
          
        }
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLC.getCantidadTotal());
    }
}
