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
import py.smtr.ejb.exceptions.EJBWithOutRollBackException;
import py.smtr.ejb.facades.FacturasFacade;
import py.smtr.ejb.shared.ResponseEntidad;

import py.smtr.web.client.service.FacturaService;
import py.smtr.web.shared.WARException;

/**
 *
 * @author IDTK
 */
public class FacturaServiceImpl extends RemoteServiceServlet implements FacturaService {

    @EJB
    FacturasFacade facturaFacade;
    private Logger logger = Logger.getLogger("log");
    
        public PagingLoadResult<BaseModel> listarFacturas(PagingLoadConfig config) throws Exception {
            
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String sesion = (String) session.getAttribute("sesion");
        
        logger.info("IN Listar Facturas:" + sesion + ";" + config.getOffset() + ";" + config.getLimit());
        ResponseEntidad respLC = null;
        try {
            respLC = facturaFacade.listarFaturasPedientes(sesion, config.getOffset(), config.getLimit());
        } catch (Exception ex) {
            logger.info("OUT:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        logger.info("OUT:" + respLC.getCantidadTotal() + ";" + respLC.getListaEntidad().toString());

        List<Facturas> listaFacturas = respLC.getListaEntidad();
        List<BaseModel> result = new ArrayList<BaseModel>();

        for (Facturas usu : listaFacturas) {
            BaseModel bm = new BaseModel();
            bm.set("id", usu.getId());
            bm.set("numero", usu.getNumero());
            bm.set("estado", usu.getEstado());
            bm.set("saldo", usu.getSaldo());
            String fechaCreacion;
            SimpleDateFormat formatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                fechaCreacion = formatOut.format(usu.getCreated());
            } catch (Exception ex) {
                fechaCreacion = "";
            }
            bm.set("created", fechaCreacion);

            result.add(bm);
        }
        return new BasePagingLoadResult<BaseModel>(result, config.getOffset(), respLC.getCantidadTotal());
    }
}
