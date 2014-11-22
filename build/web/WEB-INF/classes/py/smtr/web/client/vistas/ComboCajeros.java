/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import py.smtr.web.client.service.UsuarioService;
import py.smtr.web.client.service.UsuarioServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.RolesService;
//import py.fpuna.web.tp2011.war.client.services.RolesServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.UsuarioService;
//import py.fpuna.web.tp2011.war.client.services.UsuarioServiceAsync;

/**
 *
 * @author Strogg
 */
public class ComboCajeros extends ComboBox<BaseModel> {

    private static final String COL_ID = "id";
    private static final String COL_TYPE_COMPETITION = "nombre";
    private static final String COL_NAME = "Cajeros";
    private static final String LOADING_TEXT = "Cargando...";
    private static final String EMPTY_TEXT = "Seleccione un cajero...";
    private static List<BaseModel> listaCajeros = new ArrayList<BaseModel>();
    private ComboCajeros _this;

    public ComboCajeros() {
        super();
        _this = this;

        UsuarioServiceAsync usuariosService = GWT.create(UsuarioService.class);
        RootPanel.get("loading").setVisible(true);        
      /*  usuariosService.listarUsuariosCajerosBaseModel(new AsyncCallback<List<BaseModel>>() {
        

            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("loading").setVisible(false);
                //MessageBox.alert("Error", caught.getMessage(), null);
            }

            @Override
            public void onSuccess(List<BaseModel> result) {
                RootPanel.get("loading").setVisible(false);
                listaCajeros = result;
                ListStore<BaseModel> listS = new ListStore<BaseModel>();
                listS.add(listaCajeros);
                _this.setStore(listS);
                _this.clearState();
                _this.clearSelections();
                _this.clear();
                _this.reset();
                _this.setEnabled(true);
            }
        });*/

        ListStore<BaseModel> listS = new ListStore<BaseModel>();
        listS.add(listaCajeros);
        this.setStore(listS);

        this.setDisplayField(COL_TYPE_COMPETITION);
        this.setValueField(COL_ID);
        this.setName(COL_TYPE_COMPETITION);
        this.setFieldLabel(COL_NAME);
        this.setEditable(false);
        this.setForceSelection(true);
        this.setLoadingText(LOADING_TEXT);
        this.setEmptyText(EMPTY_TEXT);
        this.setTriggerAction(TriggerAction.ALL);
    }
}