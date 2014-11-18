/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import py.smtr.web.client.service.ProductoService;
import py.smtr.web.client.service.ProductoServiceAsync;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;
import py.smtr.web.shared.ProveedorSimple;
//import py.web.client.services.ProductoService;
//import py.fpuna.web.tp2011.war.client.services.ProductoServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.UsuarioService;
//import py.fpuna.web.tp2011.war.client.services.UsuarioServiceAsync;
//import py.fpuna.web.tp2011.war.shared.ConstantesWAR;
//import py.fpuna.web.tp2011.war.shared.ProveedorSimple;

/**
 *
 * @author null
 */
public class AsignarProveedor extends Window {

    //roles        
    private CheckBox roles[];      //4 roles 0 = Administrador, 1 = Comprador,, 2 = Vendedor, 3 = Cajero    
    
    private FormPanel form;
    private ContentPanel superior, inferior;
    
    
    private Button guardar;
    private Button cancelar;
    
    private UsuarioServiceAsync usuarioService = GWT.create(UsuarioService.class);
    private ProductoServiceAsync productoService = GWT.create(ProductoService.class);
        
    private AsignarProveedor _this;
    private Integer idProducto;
    private List<ProveedorSimple> myProveedores;

    public AsignarProveedor(Integer idProducto, List<ProveedorSimple> myProveedores) {
        _this = this;
        _this.idProducto = idProducto;
        _this.myProveedores = myProveedores;
        
        this.setSize(375, 245);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Lista de Proveedores");
        this.setIcon(IconHelper.create("icons/user_gray.png"));
        this.setResizable(false);

        this.roles = new CheckBox[myProveedores.size()];
        int i = 0;
        for (ProveedorSimple proveedorSimple : myProveedores) {
            this.roles[i] = new CheckBox();
            this.roles[i].setFieldLabel(proveedorSimple.getNombre());
            this.roles[i].setValue(proveedorSimple.getActivo());
            
            this.roles[i].setData("id", proveedorSimple.getId());   
            this.roles[i].setData("nombre", proveedorSimple.getNombre());   
            this.roles[i].setData("ruc", proveedorSimple.getRuc());   
            this.roles[i].setData("activo", proveedorSimple.getActivo());   
            
            i++;
        }               

        form = new FormPanel();   
        
        superior = new ContentPanel();
        inferior = new ContentPanel();
        
        superior.setScrollMode(Scroll.AUTOY);
        superior.setHeight(170);  
        superior.setHeaderVisible(false);
        inferior.setHeaderVisible(false);
        
        //superior.setLayout(new FormLayout(FormPanel.LabelAlign.LEFT));
        superior.setLayout(new RowLayout(Orientation.VERTICAL));   
        
        
        
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setBodyBorder(false);
        form.setBorders(false);        
     
        guardar = new Button("Guardar");
        guardar.setIcon(IconHelper.create("icons/accept.png"));
        
        guardar.addSelectionListener(new SelectionListener<ButtonEvent>() {
        
            @Override
            public void componentSelected(ButtonEvent ce) {
                guardar.setEnabled(false);                                
                List<ProveedorSimple> proveedoresActualizados = new ArrayList<ProveedorSimple>();                
                
                for(int i = 0; i < _this.roles.length; i++ ) {
                    ProveedorSimple ps = new ProveedorSimple();                    
                    ps.setId((Integer) _this.roles[i].getData("id"));
                    ps.setNombre((String) _this.roles[i].getData("nombre"));
                    ps.setRuc((String) _this.roles[i].getData("ruc"));
                    ps.setActivo(_this.roles[i].getValue()); 
                    
                    proveedoresActualizados.add(ps);
                }
                                                
                productoService.actualizarProveedoresProducto(_this.idProducto, proveedoresActualizados , new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                        RootPanel.get("loading").setVisible(false);
                        MessageBox.alert("Error", caught.getMessage(), null);  
                        guardar.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                         RootPanel.get("loading").setVisible(false);
                        _this.hide();
                        Info.display("Exitoso", "Se ha actualizado el grupo de proveedores.");
                        ListadoProductos.loader.load();
                    }
                });                     
            }
        });
         
        cancelar = new Button("Cancelar");
        cancelar.setIcon(IconHelper.create("icons/cancel.png"));
        cancelar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                _this.setVisible(false);
            }
        });

        //this.add(form);
        this.add(superior);
        this.add(inferior);
        for (i = 0; i < this.roles.length; i++) {
            form.add(this.roles[i]);
            //superior.add(this.roles[i], new FormData());
        }
        inferior.setButtonAlign(HorizontalAlignment.CENTER);
        inferior.addButton(guardar);
        inferior.addButton(cancelar);
        superior.add(form);
        
        //form.add(superior);
        //form.add(inferior);
        
        //form.addButton(guardar);
        //form.addButton(cancelar);
    }
}
