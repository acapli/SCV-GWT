/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author IDTK
 */
public interface CajaServiceAsync {

    public void myMethod(String s, AsyncCallback<String> callback);
}
