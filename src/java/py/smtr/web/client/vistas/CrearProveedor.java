/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.service.ProveedorService;
import py.smtr.web.client.service.ProveedorServiceAsync;

public class CrearProveedor extends Window {

    private FormPanel form;
    private TextField<String> nombres;
    private TextField<String> ruc;
    private TextField<String> direccion;
    private TextField<String> telefono;
    private TextField<String> email;
    private CheckBox activo;
    private Button guardar;
    private Button cancelar;
    private ProveedorServiceAsync proveedorService = GWT.create(ProveedorService.class);
    private CrearProveedor _this;

    public CrearProveedor() {
        _this = this;           //alambre!!!
        this.setSize(375, 275);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Creación de Proveedores");
        this.setIcon(IconHelper.create("icons/user_add.png"));
        this.setResizable(false);

        form = new FormPanel() {
            @Override
            public boolean isValid() {
                if (super.isValid()) {
                    if(nombres.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El nombre del proveedor no pueder ser vacío.", null);
                    } else if(ruc.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El R.U.C. del proveedor no pueder ser vacío.", null);
                    } else {
                        return true;
                    }
                }
                return false;
            }
        };
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);

        nombres = new TextField<String>();
        nombres.setAllowBlank(false);
        nombres.setFieldLabel("Nombre/Razón Social");
        nombres.getMessages().setBlankText("Este campo es requerido"); 
        nombres.setMaxLength(100);
        
        ruc = new TextField<String>();
        ruc.setAllowBlank(false);
        ruc.setFieldLabel("R.U.C.");
        ruc.getMessages().setBlankText("Este campo es requerido"); 
        ruc.setMaxLength(50);
        
        direccion = new TextField<String>();
        direccion.setFieldLabel("Dirección");
        direccion.setMaxLength(150);
        
        telefono = new TextField<String>();
        telefono.setFieldLabel("Telefono(s)");
        telefono.setMaxLength(50);

        email = new TextField<String>();
        email.setFieldLabel("Email");
        email.setMaxLength(50);
        email.setRegex("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        email.setMessageTarget("side");
        email.getMessages().setRegexText("Debe ingresar un email válido."); 
        
        activo = new CheckBox();
        activo.setFieldLabel("Activo");
        activo.setValue(Boolean.TRUE);

        guardar = new Button("Guardar");
        guardar.setIcon(IconHelper.create("icons/accept.png"));
        guardar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if(form.isValid()) {
                    guardar.setEnabled(false);
                    RootPanel.get("loading").setVisible(false);
                    proveedorService.guardarProveedor(  nombres.getValue(), 
                                                    ruc.getValue(), 
                                                    direccion.getValue(), 
                                                    telefono.getValue(), 
                                                    email.getValue(), 
                                                    activo.getValue(),
                                                    new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            //RootPanel.get("loading").setVisible(false);
                            MessageBox.alert("Error", caught.getMessage(), null);
                            guardar.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            RootPanel.get("loading").setVisible(false);
                            _this.hide();
                            Info.display("Exitoso", "Se ha creado el nuevo proveedor.");
                            ListadoProveedores.loader.load();
                        }
                    });
                }
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

        this.add(form);
        form.add(nombres);
        form.add(ruc);
        form.add(direccion);
        form.add(telefono);
        form.add(email);
        form.add(activo);
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
