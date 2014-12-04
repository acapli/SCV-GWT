/*
 Esto es nuevo Nora 4/12/14
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
import py.smtr.web.client.service.CajaService;
import py.smtr.web.client.service.CajaServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.CajaService;
//import py.fpuna.web.tp2011.war.client.services.CajaServiceAsync;

/**
 *
 * @author Strogg
 */
public class CrearCaja extends Window {
    private CheckBox activo;
    private FormPanel form;
    private TextField<String> nombre;
    private Button guardar;
    private Button cancelar;
    private CajaServiceAsync cajaService = GWT.create(CajaService.class);
    private CrearCaja _this;

    public CrearCaja() {
        _this = this;
        this.setSize(375, 145);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Creación de Cajas");
        this.setIcon(IconHelper.create("icons/caja_add.png"));
        this.setResizable(false);

        form = new FormPanel() {
            @Override
            public boolean isValid() {
                if (super.isValid()) {
                    if(nombre.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El nombre de la caja no pueder ser vacío.", null);
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

        nombre = new TextField<String>();
        nombre.setAllowBlank(false);
        nombre.setFieldLabel("Nombre de la Caja");
        nombre.getMessages().setBlankText("Este campo es requerido"); 
        nombre.setMaxLength(100);
        
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
                    RootPanel.get("loading").setVisible(true);
                /*    cajaService.guardarCaja(  nombre.getValue(), activo.getValue(),  
                                                    new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            RootPanel.get("loading").setVisible(false);
                            MessageBox.alert("Error", caught.getMessage(), null);
                            guardar.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            RootPanel.get("loading").setVisible(false);
                            _this.hide();
                            Info.display("Exitoso", "Se ha creado la nueva caja.");
                            ListadoCajas.loader.load();
                        }
                    });*/
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
        form.add(nombre);
        form.add(activo);
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
