/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import py.smtr.web.client.service.CajaService;

/**
 *
 * @author IDTK
 */
public class CajaServiceImpl extends RemoteServiceServlet implements CajaService {

    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        return "Server says: " + s;
    }
}
