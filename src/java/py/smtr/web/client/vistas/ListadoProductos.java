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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;
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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import py.smtr.web.client.service.ProductoService;
import py.smtr.web.client.service.ProductoServiceAsync;
//import py.smtr.war.shared.ConstantesWAR;
import py.smtr.web.client.vistas.ConstantesWAR;
import py.smtr.web.shared.ProveedorSimple;

/**
 *
 * @author 
 */
public class ListadoProductos extends TabItem {

    public static PagingLoader<PagingLoadResult<BaseModel>> loader;
    private final ProductoServiceAsync productoService = GWT.create(ProductoService.class);
    private Grid<BaseModel> grilla;
    private RpcProxy<PagingLoadResult<BaseModel>> proxy;
    private PagingToolBar pagingToolBar;
    private ListStore<BaseModel> store;
    private Integer idProducto;                  //para poder referenciar el usuario seleccionado para asignacion de roles...

    
    private Logger logger = Logger.getLogger("log");
    
    public ListadoProductos() {
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.setText("Productos");
        this.setIcon(IconHelper.create("icons/package.png"));

        ContentPanel panelPricipal = new ContentPanel();
        panelPricipal.setBorders(true);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setHeight(MenuPrincipal.center.getHeight(true)-33);
        panelPricipal.setHeaderVisible(false);
        panelPricipal.setLayout(new FitLayout());

        this.add(panelPricipal);

        Button botonNuevo = new Button("Nuevo");
        botonNuevo.setIcon(IconHelper.create("icons/package_add.png"));
        botonNuevo.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
               logger.info(" Entroo en crear producto");
               CrearProducto crearProducto = new CrearProducto();
               crearProducto.show();
            }
        });

        Button botonBorrar = new Button("Borrar");
        botonBorrar.setIcon(IconHelper.create("icons/package_delete.png"));
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
                                       // RootPanel.get("loading").setVisible(true);
                                        productoService.borrarProductos(ids, new AsyncCallback<Void>() {

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                //RootPanel.get("loading").setVisible(false);
                                                MessageBox.alert("Error", caught.getMessage(), null);
                                            }
                                            @Override
                                            public void onSuccess(Void result) {
                                               // RootPanel.get("loading").setVisible(false);
                                                loader.load();
                                           }
                                        });

                                    }
                                }
                            });
                } else {
                    MessageBox.alert("Atención", "Debe seleccionar al menos un producto", null);
                }
            }
        });
        
        /*Asignacion de proveedores!*/
        Button botonAsignarProveedor = new Button("Asignar Proveedor");
        botonAsignarProveedor.setIcon(IconHelper.create("icons/user_gray.png"));
        botonAsignarProveedor.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //final List<BaseModel> lista = grilla.getSelectionModel().getSelectedItems();
                final List<BaseModel> listaSeleccionados = grilla.getSelectionModel().getSelectedItems();
                if (listaSeleccionados.size() == 1) {                                         
                    //Integer idUsuario = 0;
                    for (BaseModel bm : listaSeleccionados) {
                        idProducto = ((Integer) bm.get("id"));       //debe funcionar jaja
                    }
                                                                                
                    RootPanel.get("loading").setVisible(true);               
                    productoService.obtenerListaProveedoresAsignados(idProducto, new AsyncCallback<List<ProveedorSimple>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            //throw new UnsupportedOperationException("Not supported yet.");
                            RootPanel.get("loading").setVisible(false);
                            MessageBox.alert("Error", caught.getMessage(), null);
                        }

                        @Override
                        public void onSuccess(List<ProveedorSimple> result) {
                            //throw new UnsupportedOperationException("Not supported yet.");
                            RootPanel.get("loading").setVisible(false);
                            loader.load();
                           AsignarProveedor ventanaAsignarProveedor = new AsignarProveedor(idProducto, result);
                           ventanaAsignarProveedor.show();
                        }
                    });
                    
                } else {
                    if(listaSeleccionados.size() > 1){
                        MessageBox.alert("Atención", "Debe seleccionar solo un producto", null);                        
                    }else if(listaSeleccionados.isEmpty()){
                        MessageBox.alert("Atención", "Debe seleccionar un producto", null);
                    }                    
                }
            }
      }); 

        ToolBar toolBar = new ToolBar();
        toolBar.add(botonNuevo);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(botonBorrar);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(botonAsignarProveedor);  
        panelPricipal.setTopComponent(toolBar);

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
        cbsm.setSelectionMode(SelectionMode.MULTI);
        configs.add(cbsm.getColumn());

        ColumnConfig columna = new ColumnConfig();
        columna.setId("id");
        columna.setHeader("idProducto");
        columna.setHidden(true);
        columna.setWidth(30);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("codigo");
        columna.setHeader("Código");
        columna.setWidth(100);
        TextField<String> codigo = new TextField<String>();
        codigo.setAllowBlank(false);
        codigo.setMaxLength(20);
        codigo.getMessages().setBlankText("Este campo es requerido");
        columna.setEditor(new CellEditor(codigo));
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
        
        columna = new ColumnConfig();
        columna.setId("cantidad_existencia");
        columna.setHeader("Existencia");
        columna.setWidth(150);
        NumberField existencia = new NumberField();
        columna.setEditor(new CellEditor(existencia));
        configs.add(columna); 

        columna = new ColumnConfig();
        columna.setId("costo");
        columna.setHeader("Costo");
        columna.setWidth(150);
        NumberField costo = new NumberField();
        columna.setEditor(new CellEditor(costo));
        configs.add(columna);
        
        columna = new ColumnConfig();
        columna.setId("porcentaje_ganancia");
        columna.setHeader("% Ganancia");
        columna.setWidth(100);
        SpinnerField porcentajeGanancia = new SpinnerField();   
        porcentajeGanancia.setIncrement(1);   
        porcentajeGanancia.getPropertyEditor().setType(Double.class);   
        porcentajeGanancia.getPropertyEditor().setFormat(NumberFormat.getFormat("00.00")); 
        porcentajeGanancia.setAllowNegative(false);
        porcentajeGanancia.setMinValue(0);   
        porcentajeGanancia.setEditable(true);
        porcentajeGanancia.setMaxValue(100);   
        porcentajeGanancia.setAllowBlank(false);
        columna.setEditor(new CellEditor(porcentajeGanancia));
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
                productoService.listarProductos((PagingLoadConfig) loadConfig, callback);
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
                    MessageBox.alert("Atención", "El nombre del producto no pueder ser vacío.", null);
                    loader.load();
                    return;
                }
                String codigoRecord = (String) r.getModel().get("codigo");
                if(codigoRecord.trim().isEmpty()) {
                    MessageBox.alert("Atención", "El código del producto no pueder ser vacío.", null);
                    loader.load();
                    return;
                }
                RootPanel.get("loading").setVisible(true);
                productoService.actualizarProducto((Integer) r.getModel().get("id"),
                        nombreRecord,
                        codigoRecord,
                        (Double) r.getModel().get("porcentaje_ganancia"),
                        (Double) r.getModel().get("costo"),
                        (Double) r.getModel().get("cantidad_existencia"),
                        (Boolean) r.getModel().get("activo"),
                        new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                       // RootPanel.get("loading").setVisible(false);
                        MessageBox.alert("Error", caught.getMessage(), null);
                        loader.load();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        //RootPanel.get("loading").setVisible(false);
                        loader.load();
                        Info.display("Exitoso", "Se ha realizado la actualización de los campos.");
                        RootPanel.get("loading").setVisible(false);
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
        /*grilla.setAutoWidth(true);
        grilla.setAutoHeight(true);*/
        grilla.addPlugin(checkColumn);
        grilla.addPlugin(re);
        grilla.addPlugin(cbsm);

        panelPricipal.add(grilla);
        panelPricipal.setBottomComponent(pagingToolBar);

        loader.load(0, ConstantesWAR.CANT_LISTADO);

        this.show();

    }
}
