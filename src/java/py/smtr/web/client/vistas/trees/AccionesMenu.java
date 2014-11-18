package py.smtr.web.client.vistas.trees;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.mainEntryPoint;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;
import py.smtr.web.client.vistas.ComprasVista;
import py.smtr.web.client.vistas.ListadoCajas;
import py.smtr.web.shared.ConstantesWAR;
import py.smtr.web.client.vistas.ListadoClientes;
import py.smtr.web.client.vistas.ListadoFactura;
import py.smtr.web.client.vistas.ListadoPagos;
import py.smtr.web.client.vistas.ListadoProductos;
import py.smtr.web.client.vistas.ListadoProveedores;
import py.smtr.web.client.vistas.ListadoUsuario;
import py.smtr.web.client.vistas.MenuPrincipal;
import py.smtr.web.client.vistas.VentasVista;

public class AccionesMenu {
    
    public static void accion(String menuItem) {
        
       if (ConstantesWAR.usuarios.equals(menuItem)) {
            ListadoUsuario listadoUsuario = new ListadoUsuario();
            listadoUsuario.setClosable(true);   
            MenuPrincipal.advanced.add(listadoUsuario);
            MenuPrincipal.advanced.setSelection(listadoUsuario);
        }
     
        if (ConstantesWAR.clientes.equals(menuItem)) {
            ListadoClientes listadoCliente = new ListadoClientes();
            listadoCliente.setClosable(true);         
            MenuPrincipal.advanced.add(listadoCliente);
            MenuPrincipal.advanced.setSelection(listadoCliente);
            
        }
        
         if (ConstantesWAR.productos.equals(menuItem)) {
            ListadoProductos listadoProductos = new ListadoProductos();
            listadoProductos.setClosable(true);
            MenuPrincipal.advanced.add(listadoProductos);
            MenuPrincipal.advanced.setSelection(listadoProductos);
        }
        
         if (ConstantesWAR.ventas.equals(menuItem)) {
            VentasVista ventas = new VentasVista();
            ventas.setClosable(true);
            MenuPrincipal.advanced.add(ventas);
            MenuPrincipal.advanced.setSelection(ventas);
        }
         
          if (ConstantesWAR.compras.equals(menuItem)) {
            ComprasVista compras = new ComprasVista();
            compras.setClosable(true);
            MenuPrincipal.advanced.add(compras);
            MenuPrincipal.advanced.setSelection(compras);
        }
          
         if (ConstantesWAR.proveedores.equals(menuItem)) {
            ListadoProveedores listadoProveedores = new ListadoProveedores();
            listadoProveedores.setClosable(true);
            MenuPrincipal.advanced.add(listadoProveedores);
            MenuPrincipal.advanced.setSelection(listadoProveedores);
        }
         
        if (ConstantesWAR.cajas.equals(menuItem)) {
            ListadoCajas listadoCajas = new ListadoCajas();
            listadoCajas.setClosable(true);
            MenuPrincipal.advanced.add(listadoCajas);
            MenuPrincipal.advanced.setSelection(listadoCajas);
        }
        
        if (ConstantesWAR.pagos.equals(menuItem)) {
             ListadoFactura listadoFactura = new ListadoFactura();
             listadoFactura.setClosable(true);
             MenuPrincipal.advanced.add(listadoFactura);
            MenuPrincipal.advanced.setSelection(listadoFactura);
        }
        
        if (ConstantesWAR.listadopagos.equals(menuItem)) {
            ListadoPagos listadoFactura = new ListadoPagos();
            listadoFactura.setClosable(true);
            MenuPrincipal.advanced.add(listadoFactura);
            MenuPrincipal.advanced.setSelection(listadoFactura);
        }
 
    
       if (ConstantesWAR.salir.equals(menuItem)) {
            MessageBox.confirm("Confirmar", "Está seguro que desea salir del sistema?", new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getText().equalsIgnoreCase("yes")) {
                        final UsuarioServiceAsync userService = GWT.create(UsuarioService.class);
                        userService.logout(new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                RootPanel.get().clear();
                                RootPanel.get().add(new mainEntryPoint());
                            }

                            @Override
                            public void onSuccess(Void result) {
                                RootPanel.get().clear();
                                RootPanel.get().add(new mainEntryPoint());
                            }
                        });
                    }
                }
            });
        }
            
    }
              
              
}