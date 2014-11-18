/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import java.util.List;
import py.smtr.ejb.shared.DetalleCV;

/**
 *
 * @author IDTK
 */
@RemoteServiceRelativePath("service/compraservice")
public interface CompraService extends RemoteService {

    public void guardarCompra(Integer idProveedor, Date fecha, double total, List<DetalleCV> detalles) throws Exception;
}
