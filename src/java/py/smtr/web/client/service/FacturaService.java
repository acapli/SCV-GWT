/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author IDTK
 */
@RemoteServiceRelativePath("service/facturaservice")
public interface FacturaService extends RemoteService {
 public PagingLoadResult<BaseModel> listarFacturas(PagingLoadConfig config) throws Exception;
}
