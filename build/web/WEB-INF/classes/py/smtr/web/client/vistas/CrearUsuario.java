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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;

public class CrearUsuario extends Window {

    private FormPanel form;
    private TextField<String> usernameItem;
    private NumberField documento;
    private TextField<String> password;
    private TextField<String> passwordRepeat;
    private TextField<String> nombres;
    private TextField<String> apellidos;
    private TextField<String> email;
    private CheckBox activo; 
    private Button guardar;
    private Button cancelar;
    private UsuarioServiceAsync usuarioService = GWT.create(UsuarioService.class);
    private CrearUsuario _this;

    public CrearUsuario() {
        _this = this;
        this.setSize(375, 325);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Creación de Usuarios");
        this.setIcon(IconHelper.create("icons/user_add.png"));
        this.setResizable(false);

        form = new FormPanel() {
            @Override
            public boolean isValid() {
                if (super.isValid()) {
                    if(usernameItem.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El nombre de usuario no pueder ser vacío.", null);
                    } else if (password.getValue() == null && passwordRepeat.getValue() == null) {
                        return false;
                    } else if (password.getValue().equals(passwordRepeat.getValue())) {
                        return true;
                    } else {
                        MessageBox.alert("Atención", "Las contraseñas no coinciden.", null);
                    }
                }
                return false;
            }
        };
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);

        usernameItem = new TextField<String>();
        usernameItem.setAllowBlank(false);
        usernameItem.setFieldLabel("Usuario");
        usernameItem.setMaxLength(50);
        usernameItem.setToolTip("Utilizado para acceso al sistema");
        usernameItem.getMessages().setBlankText("Este campo es requerido"); 

        nombres = new TextField<String>();
        nombres.setAllowBlank(false);
        nombres.setFieldLabel("Nombres");
        nombres.getMessages().setBlankText("Este campo es requerido"); 
        nombres.setMaxLength(100);

        apellidos = new TextField<String>();
        apellidos.setAllowBlank(false);
        apellidos.setFieldLabel("Apellidos");
        apellidos.getMessages().setBlankText("Este campo es requerido"); 
        apellidos.setMaxLength(100);
        
        documento = new NumberField();
        documento.setAllowBlank(false);
        documento.setAllowDecimals(false);
        documento.setAllowNegative(false);
        documento.setFieldLabel("Documento Nº");
        documento.getMessages().setBlankText("Este campo es requerido"); 
        documento.setMaxValue(Integer.MAX_VALUE);
        
        email = new TextField<String>();
        email.setFieldLabel("Email");
        email.setMaxLength(100);
        email.setRegex("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        email.setMessageTarget("side");
        email.getMessages().setRegexText("Debe ingresar un email válido."); 

        password = new TextField<String>();
        password.setAllowBlank(false);
        password.setFieldLabel("Contraseña");
        password.setMaxLength(100);
        password.setPassword(true);
        password.getMessages().setBlankText("Este campo es requerido"); 

        passwordRepeat = new TextField<String>();
        passwordRepeat.setAllowBlank(false);
        passwordRepeat.setFieldLabel("Repetir contraseña");
        passwordRepeat.setMaxLength(100);
        passwordRepeat.setPassword(true);
        passwordRepeat.getMessages().setBlankText("Este campo es requerido"); 
        
        //int rolInteger= Integer.parseInt(rol.getValue());

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
                    usuarioService.guardarUsuario(usernameItem.getValue(), 
                                                    nombres.getValue(),
                                                    apellidos.getValue(),
                                                    documento.getValue().longValue(), 
                                                    email.getValue(), 
                                                    password.getValue(),
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
                           // RootPanel.get("loading").setVisible(false);
                            _this.hide();
                            Info.display("Exitoso", "Se ha creado el nuevo usuario.");
                            ListadoUsuario.loader.load();
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
        form.add(apellidos);
        form.add(usernameItem);
        form.add(documento);
        form.add(email);
        form.add(password);
        form.add(passwordRepeat);
        form.add(activo);
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
