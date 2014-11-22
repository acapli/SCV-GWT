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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.List;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;


/**
 *
 * @author 
 */
public class AsignarRol extends Window {

    //roles        
    private CheckBox roles[];      //4 roles 0 = Administrador, 1 = Comprador,, 2 = Vendedor, 3 = Cajero
    private String nombreRoles[] = {"Administrador", "Operador", "Vendedor", "Cajero"};
    private Integer valoresRoles[] = {ConstantesWAR.ROL_ADMINISTRADOR, ConstantesWAR.ROL_COMPRADOR, ConstantesWAR.ROL_VENDEDOR, ConstantesWAR.ROL_CAJERO};     
    private FormPanel form;
    private Button guardar;
    private Button cancelar;
    private UsuarioServiceAsync usuarioService = GWT.create(UsuarioService.class);
    private AsignarRol _this;
    private Integer idUsuario;

    public AsignarRol(Integer idUsuario, List<Integer> myRoles) {
        _this = this;
        _this.idUsuario = idUsuario;
        this.setSize(375, 195);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Asignación de Roles");
        this.setIcon(IconHelper.create("icons/shield.png"));
        this.setResizable(false);

        this.roles = new CheckBox[4];
        for (int i = 0; i < 4; i++) {
            this.roles[i] = new CheckBox();
            this.roles[i].setFieldLabel(nombreRoles[i]);
            
            if (myRoles.contains(valoresRoles[i])) {
                this.roles[i].setValue(Boolean.TRUE);
            }
        }

        form = new FormPanel();
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);
     
        guardar = new Button("Guardar");
        guardar.setIcon(IconHelper.create("icons/accept.png"));

        
        guardar.addSelectionListener(new SelectionListener<ButtonEvent>() {
        
            @Override
            public void componentSelected(ButtonEvent ce) {
                guardar.setEnabled(false);
                Boolean nuevosRoles[] = {false, false, false, false};
                for(int j = 0; j < _this.roles.length; j++){
                    //tengo que pasarle el array de roles (true, false, false, true) como extranho la facil indexacion de php
                    if(_this.roles[j].getValue()){
                        nuevosRoles[j] = true;                        
                    }
                }
                
                usuarioService.actualizarRolesUsuario(_this.idUsuario, nuevosRoles, new AsyncCallback<Void>() {

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
                        Info.display("Exitoso", "Se ha actualizado el grupo de roles.");
                        ListadoUsuario.loader.load();
                        
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

        this.add(form);
        for (int i = 0; i < 4; i++) {
            form.add(this.roles[i]);
        }       
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
