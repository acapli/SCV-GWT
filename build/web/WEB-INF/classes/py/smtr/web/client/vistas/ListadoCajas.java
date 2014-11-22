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
import py.smtr.web.client.service.CajaService;
import py.smtr.web.client.service.CajaServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.CajaService;
//import py.fpuna.web.tp2011.war.client.services.CajaServiceAsync;
//import py.fpuna.web.tp2011.war.shared.ConstantesWAR;

/**
 *
 * @author Strogg
 */
public class ListadoCajas extends TabItem {

    public static PagingLoader<PagingLoadResult<BaseModel>> loader;
    private final CajaServiceAsync cajaService = GWT.create(CajaService.class);
    private Grid<BaseModel> grilla;
    private RpcProxy<PagingLoadResult<BaseModel>> proxy;
    private PagingToolBar pagingToolBar;
    private ListStore<BaseModel> store;

    public ListadoCajas() {
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.setText("Cajas");
        this.setIcon(IconHelper.create("icons/caja.png"));

        ContentPanel panelPricipal = new ContentPanel();
        panelPricipal.setBorders(true);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setHeight(MenuPrincipal.center.getHeight(true)-33);
        panelPricipal.setHeaderVisible(false);
        panelPricipal.setLayout(new FitLayout());

        this.add(panelPricipal);

        Button botonNuevo = new Button("Nuevo");
        botonNuevo.setIcon(IconHelper.create("icons/caja_add.png"));
        botonNuevo.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                CrearCaja crearCaja = new CrearCaja();
                crearCaja.show();
            }
        });

        Button botonBorrar = new Button("Borrar");
        botonBorrar.setIcon(IconHelper.create("icons/caja_delete.png"));
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
                                      /* cajaService.borrarCaja(ids, new AsyncCallback<Void>() {

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
                                        });*/

                                    }
                                }
                            });
                } else {
                    MessageBox.alert("Atención", "Debe seleccionar al menos una caja", null);
                }
            }
        });
        
        
        Button botonAsignarCaja = new Button("Asignar Caja");
        botonAsignarCaja.setIcon(IconHelper.create("icons/user_caja.png"));
        botonAsignarCaja.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                AsignarCaja asignarCaja = new AsignarCaja();
                asignarCaja.show();
            }
        });
        

        ToolBar toolBar = new ToolBar();
        toolBar.add(botonNuevo);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(botonBorrar);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(botonAsignarCaja);
        
        panelPricipal.setTopComponent(toolBar);

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
        cbsm.setSelectionMode(SelectionMode.MULTI);
        configs.add(cbsm.getColumn());

        ColumnConfig columna = new ColumnConfig();
        columna.setId("id");
        columna.setHeader("idCaja");
        columna.setHidden(true);
        columna.setWidth(30);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("nombre");
        columna.setHeader("Nombre");
        columna.setWidth(150);
        TextField<String> nombreText = new TextField<String>();
        nombreText.setAllowBlank(false);
        nombreText.setMaxLength(100);
        nombreText.getMessages().setBlankText("Este campo es requerido");
        columna.setEditor(new CellEditor(nombreText));
        configs.add(columna);
        
        CheckColumnConfig checkColumn = new CheckColumnConfig("activo", "Activo?", 55);
        CellEditor checkBoxEditor = new CellEditor(new CheckBox());
        checkColumn.setEditor(checkBoxEditor);
        configs.add(checkColumn);

        columna = new ColumnConfig();
        columna.setId("created");
        columna.setHeader("Fecha Creación");
        columna.setWidth(100);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("changed");
        columna.setHeader("Fecha Modificación");
        columna.setWidth(100);
        configs.add(columna);

        proxy = new RpcProxy<PagingLoadResult<BaseModel>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BaseModel>> callback) {
                //cajaService.listarCajas((PagingLoadConfig) loadConfig, callback);
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<BaseModel>>(proxy);
        loader.setSortField("nombre");
        loader.setSortDir(SortDir.ASC);

        pagingToolBar = new PagingToolBar(ConstantesWAR.CANT_LISTADO);
        pagingToolBar.bind(loader);
        pagingToolBar.setAutoWidth(true);

        store = new ListStore<BaseModel>(loader);

       /* store.addStoreListener(new StoreListener<BaseModel>() {

            @Override
            public void storeUpdate(final StoreEvent<BaseModel> se) {
                Record r = se.getRecord();
                String nombreRecord = (String) r.getModel().get("nombre");
                if(nombreRecord.trim().isEmpty()) {
                    MessageBox.alert("Atención", "El nombre de la caja no pueder ser vacío.", null);
                    loader.load();
                    return;
                }

                RootPanel.get("loading").setVisible(true);
                cajaService.actualizarCaja((Integer) r.getModel().get("id"),
                        nombreRecord,
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
        });*/

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
