/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.service.CajaService;
import py.smtr.web.client.service.CajaServiceAsync;
//
//import py.fpuna.web.tp2011.war.client.services.CajaService;
//import py.fpuna.web.tp2011.war.client.services.CajaServiceAsync;



/**
 *
 * @author Strogg
 */
public class AsignarCaja extends Window {

    private FormPanel form;

    private Button guardar;
    private Button cancelar;
    private CajaServiceAsync cajaService = GWT.create(CajaService.class);
    private AsignarCaja _this;
    
    private Integer idCajero = 0;
    private Integer idCaja = 0;
    

    public AsignarCaja() {
        _this = this;
        this.setSize(375, 145);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Asignación de Cajas");
        this.setIcon(IconHelper.create("icons/user_caja.png"));

        form = new FormPanel();
        
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);
      
        
        guardar = new Button("Guardar");
        guardar.setIcon(IconHelper.create("icons/accept.png"));
        guardar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if(form.isValid()) {
                    guardar.setEnabled(false);
                    RootPanel.get("loading").setVisible(true);
                  /*  cajaService.guardarAsignacionCaja(  idCajero, idCaja,  
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
                            Info.display("Exitoso", "Se ha asignado la caja.");
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
        
        ComboCajeros listaCajeros = new ComboCajeros();
        form.add(listaCajeros);
        listaCajeros.setAllowBlank(false);
        listaCajeros.addSelectionChangedListener(new SelectionChangedListener<BaseModel>() {
           
            @Override
            public void selectionChanged(SelectionChangedEvent<BaseModel> se) {
                _this.idCajero = se.getSelectedItem().get("id");
            }
        });
        
        
        ComboCajas listaCajas = new ComboCajas();
        form.add(listaCajas);
        listaCajas.setAllowBlank(false);
        listaCajas.addSelectionChangedListener(new SelectionChangedListener<BaseModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<BaseModel> se) {
                _this.idCaja = se.getSelectedItem().get("id");
            }
        });
        
        
        
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
