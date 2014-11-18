/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import py.smtr.ejb.shared.ResponseLogin;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;

//import py.smtr.ejb.shared.ResponseLogin;
//import py.smtr.web.client.service.UsuarioService;
//import py.smtr.web.client.service.UsuarioServiceAsync;
//import py.smtr.web.client.vistas.MenuPrincipal;



public class Login extends ContentPanel {

    private FormPanel form;
    private TextField<String> user;
    private TextField<String> pass;
    private Button botonIngresar;
    private final UsuarioServiceAsync usuarioService = GWT.create(UsuarioService.class);

    public Login() {
        this.setHeaderVisible(false);
        this.setSize("300", "auto");

        form = new FormPanel();
        form.setButtonAlign(HorizontalAlignment.CENTER);
        form.setHeading("Acceso al Sistema");
        form.setLabelAlign(FormPanel.LabelAlign.LEFT);
        form.setFieldWidth(185);

        user = new TextField<String>();
        user.setFieldLabel("Nombre de Usuario");
        user.setAllowBlank(false);
        user.getMessages().setBlankText("Este campo es requerido");

        pass = new TextField<String>();
        pass.setFieldLabel("Contraseña");
        pass.setAllowBlank(false);
        pass.setPassword(true);
        pass.getMessages().setBlankText("Este campo es requerido");
        pass.addKeyListener(new KeyListener() {

        @Override
        public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    if (form.isValid()) {
                        login();
                    }
                }
            }
        });

        botonIngresar = new Button("Ingresar");
        botonIngresar.setIcon(IconHelper.create("icons/house_go.png"));
        botonIngresar.addSelectionListener(new SelectionListener<ButtonEvent>() {

        @Override
            public void componentSelected(ButtonEvent ce) {
               if (form.isValid()) {
                   login();
                }
            }
        });

        this.add(form);
        form.add(user);
        form.add(pass);
        form.addButton(botonIngresar);
        
    }

    private void login() {
        
        botonIngresar.setEnabled(false);
        RootPanel.get("loading").setVisible(true);
  
        usuarioService.loginUsuario(user.getValue(), pass.getValue(), new AsyncCallback<ResponseLogin>() {
            @Override
            public void onFailure(Throwable caught) {                
                RootPanel.get("loading").setVisible(true);
                MessageBox.alert("Error", caught.getMessage(), null);
                RootPanel.get("loading").setVisible(false);
                botonIngresar.setEnabled(true);
            }
            @Override
            public void onSuccess(ResponseLogin result) { 
               RootPanel.get("loading").setVisible(false);
               RootPanel.get().clear();
                
                //RootPanel.get().add(new MenuPrincipal());
                //Integer rolesInt = result.getRol();                
                //RootPanel.get().add(new MenuPrincipal(rolesInt));
               
                List<Integer> rolesInt = new ArrayList<Integer>();
                rolesInt.addAll(Arrays.asList(result.getRoles()));                

                RootPanel.get().add(new MenuPrincipal(rolesInt));
           
            }
        });
    }
}