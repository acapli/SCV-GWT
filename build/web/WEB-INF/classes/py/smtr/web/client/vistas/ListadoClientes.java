/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import py.smtr.web.client.service.ClienteService;
import py.smtr.web.client.service.ClienteServiceAsync;


/**
 *
 * @author Strogg
 */
public class ListadoClientes extends TabItem {

    public static PagingLoader<PagingLoadResult<BaseModel>> loader;
    private final ClienteServiceAsync clienteService = GWT.create(ClienteService.class);
    private Grid<BaseModel> grilla;
    private RpcProxy<PagingLoadResult<BaseModel>> proxy;
    private PagingToolBar pagingToolBar;
    private ListStore<BaseModel> store;
    
     private Logger logger = Logger.getLogger("log");

    public ListadoClientes() {
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.setText("Clientes");
        this.setIcon(IconHelper.create("icons/user_green.png"));

        ContentPanel panelPricipal = new ContentPanel();
        panelPricipal.setBorders(true);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setHeight(MenuPrincipal.center.getHeight(true)-33);
        panelPricipal.setHeaderVisible(false);
        panelPricipal.setLayout(new FitLayout());

        this.add(panelPricipal);

        Button botonNuevo = new Button("Nuevo");
        botonNuevo.setIcon(IconHelper.create("icons/user_add.png"));
        botonNuevo.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                
                logger.info("\n\n-----------------Accedió a Listado Cliente!!----------------\n\n");
                CrearCliente crearCliente = new CrearCliente();
                crearCliente.show();
            }
        });

        Button botonBorrar = new Button("Borrar");
        botonBorrar.setIcon(IconHelper.create("icons/user_delete.png"));
        botonBorrar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final List<BaseModel> lista = grilla.getSelectionModel().getSelectedItems();
                if (lista.size() > 0) {
                    MessageBox.confirm("Confirmar", "Está seguro?",
                            new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    if (be.getButtonClicked().getText().equalsIgnoreCase("yes")) {
                                        List<Integer> ids = new ArrayList<Integer>();
                                        for (BaseModel bm : lista) {
                                            ids.add((Integer) bm.get("id"));
                                        }
                                        RootPanel.get("loading").setVisible(true);
                                        clienteService.borrarClientes(ids, new AsyncCallback<Void>() {

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                RootPanel.get("loading").setVisible(false);
                                                MessageBox.alert("Error", caught.getMessage(), null);
                                            }

                                            @Override
                                            public void onSuccess(Void result) {
                                                RootPanel.get("loading").setVisible(false);
                                                loader.load();
                                            }
                                        });

                                    }
                                }
                            });
                } else {
                    MessageBox.alert("Atención", "Debe seleccionar al menos un cliente", null);
                }
            }
        });

        ToolBar toolBar = new ToolBar();
        toolBar.add(botonNuevo);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(botonBorrar);
        panelPricipal.setTopComponent(toolBar);

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
        cbsm.setSelectionMode(SelectionMode.MULTI);
        configs.add(cbsm.getColumn());

        ColumnConfig columna = new ColumnConfig();
        columna.setId("id");
        columna.setHeader("id");
        columna.setHidden(true);
        columna.setWidth(30);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("nombre");
        columna.setHeader("Nombre/Razón Social");
        columna.setWidth(150);
        TextField<String> nombreText = new TextField<String>();
        nombreText.setAllowBlank(false);
        nombreText.setMaxLength(100);
        nombreText.getMessages().setBlankText("Este campo es requerido");
        columna.setEditor(new CellEditor(nombreText));
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("ruc");
        columna.setHeader("C.I./R.U.C.");
        columna.setWidth(100);
        TextField<String> ruc = new TextField<String>();
        ruc.setAllowBlank(false);
        ruc.setMaxLength(50);
        ruc.getMessages().setBlankText("Este campo es requerido");
        columna.setEditor(new CellEditor(ruc));
        configs.add(columna);
        
        columna = new ColumnConfig();
        columna.setId("direccion");
        columna.setHeader("Dirección");
        columna.setWidth(150);
        TextField<String> direccion = new TextField<String>();
        direccion.setMaxLength(150);
        columna.setEditor(new CellEditor(direccion));
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("telefono");
        columna.setHeader("Teléfono(s)");
        columna.setWidth(150);
        TextField<String> telefono = new TextField<String>();
        telefono.setMaxLength(100);
        columna.setEditor(new CellEditor(telefono));
        configs.add(columna);
        
        columna = new ColumnConfig();
        columna.setId("mail");
        columna.setHeader("Email");
        columna.setWidth(200);
        TextField<String> emailText = new TextField<String>();
        emailText.setMaxLength(50);
        emailText.setRegex("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        emailText.setMessageTarget("side");
        emailText.getMessages().setRegexText("Debe ingresar un email válido.");
        columna.setEditor(new CellEditor(emailText));
        configs.add(columna);
        
        CheckColumnConfig checkColumn = new CheckColumnConfig("activo", "Activo?", 55);
        CellEditor checkBoxEditor = new CellEditor(new CheckBox());
        checkColumn.setEditor(checkBoxEditor);
        configs.add(checkColumn);

//        columna = new ColumnConfig();
//        columna.setId("created");
//        columna.setHeader("Fecha Creación");
//        columna.setWidth(100);
//        configs.add(columna);
//
//        columna = new ColumnConfig();
//        columna.setId("changed");
//        columna.setHeader("Fecha Modificación");
//        columna.setWidth(100);
//        configs.add(columna);

        proxy = new RpcProxy<PagingLoadResult<BaseModel>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BaseModel>> callback) {
                clienteService.listarClientes((PagingLoadConfig) loadConfig, callback);
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<BaseModel>>(proxy);
        loader.setSortField("nombre");
        loader.setSortDir(SortDir.ASC);

        pagingToolBar = new PagingToolBar(ConstantesWAR.CANT_LISTADO);
        pagingToolBar.bind(loader);
        pagingToolBar.setAutoWidth(true);

        store = new ListStore<BaseModel>(loader);

        store.addStoreListener(new StoreListener<BaseModel>() {

            @Override
            public void storeUpdate(final StoreEvent<BaseModel> se) {
                Record r = se.getRecord();
                String nombreRecord = (String) r.getModel().get("nombre");
                if(nombreRecord.trim().isEmpty()) {
                    MessageBox.alert("Atención", "El nombre del cliente no pueder ser vacío.", null);
                    loader.load();
                    return;
                }
                String rucRecord = (String) r.getModel().get("ruc");
                if(rucRecord.trim().isEmpty()) {
                    MessageBox.alert("Atención", "C.I./R.U.C. del cliente no pueder ser vacío.", null);
                    loader.load();
                    return;
                }
                RootPanel.get("loading").setVisible(true);
                clienteService.actualizarCliente((Integer) r.getModel().get("id"),
                        nombreRecord,
                        rucRecord,
                        (String) r.getModel().get("direccion"),
                        (String) r.getModel().get("telefono"),
                        (String) r.getModel().get("mail"),
                        (Boolean) r.getModel().get("activo"),
                        new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("loading").setVisible(false);
                        MessageBox.alert("Error", caught.getMessage(), null);
                        loader.load();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        RootPanel.get("loading").setVisible(false);
                        Info.display("Exitoso", "Se ha realizado la actualización de los campos.");
                        loader.load();
                    }
                });
            }
        });

        ColumnModel cm = new ColumnModel(configs);

        final RowEditor<BaseModel> re = new RowEditor<BaseModel>();
        re.setClicksToEdit(ClicksToEdit.TWO);
        grilla = new Grid<BaseModel>(store, cm);
        grilla.setSelectionModel(cbsm);
        grilla.setBorders(true);
        grilla.setLoadMask(true);
        grilla.getView().setAutoFill(true);
        grilla.getView().setForceFit(true);
        grilla.addPlugin(checkColumn);
        grilla.addPlugin(re);
        grilla.addPlugin(cbsm);

        panelPricipal.add(grilla);
        panelPricipal.setBottomComponent(pagingToolBar);

        loader.load(0, ConstantesWAR.CANT_LISTADO);

        this.show();

    }
}
