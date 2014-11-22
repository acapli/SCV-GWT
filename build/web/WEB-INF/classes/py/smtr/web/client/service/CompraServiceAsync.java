/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import java.util.List;
import py.smtr.ejb.shared.DetalleCV;

/**
 *
 * @author IDTK
 */
public interface CompraServiceAsync {

   public void guardarCompra(Integer idProveedor, Date fecha, double total, List<DetalleCV> detalles, AsyncCallback<Void> callback);
}
