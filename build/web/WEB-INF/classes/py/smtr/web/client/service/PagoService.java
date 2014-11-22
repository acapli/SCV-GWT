/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author IDTK
 */
@RemoteServiceRelativePath("service/pagoservice")
public interface PagoService extends RemoteService {

    public void procesarPago(Integer idFactura, double monto) throws Exception;
}
