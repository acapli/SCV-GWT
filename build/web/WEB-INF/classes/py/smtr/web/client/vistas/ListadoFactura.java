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
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import py.smtr.web.client.service.FacturaService;
import py.smtr.web.client.service.FacturaServiceAsync;

public class ListadoFactura extends TabItem {

    public static PagingLoader<PagingLoadResult<BaseModel>> loader;
    private final FacturaServiceAsync facturaService = GWT.create(FacturaService.class);
    private Grid<BaseModel> grilla;
    private RpcProxy<PagingLoadResult<BaseModel>> proxy;
    private PagingToolBar pagingToolBar;
    private ListStore<BaseModel> store;

    public ListadoFactura() {
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.setText("Pagos");
        this.setIcon(IconHelper.create("icons/money.png"));

        ContentPanel panelPricipal = new ContentPanel();
        panelPricipal.setBorders(true);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setHeight(MenuPrincipal.center.getHeight(true) - 33);
        panelPricipal.setHeaderVisible(false);
        panelPricipal.setLayout(new FitLayout());

        this.add(panelPricipal);

        Button botonProcesarPago = new Button("Procesar Pago");
        botonProcesarPago.setIcon(IconHelper.create("icons/money_add.png"));
        botonProcesarPago.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                List<BaseModel> lista = grilla.getSelectionModel().getSelectedItems();
                if (lista.size() > 0) {
                    ProcesarPago procesarPago = new ProcesarPago((Integer) lista.get(0).get("id"), (Double) lista.get(0).get("saldo"));
                    procesarPago.show();
                } else {
                    MessageBox.alert("Atencion", "Debe seleccionar una factura", null);
                }
            }
        });

        ToolBar toolBar = new ToolBar();
        toolBar.add(botonProcesarPago);
        panelPricipal.setTopComponent(toolBar);

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
        cbsm.setSelectionMode(SelectionMode.MULTI);
        configs.add(cbsm.getColumn());

        ColumnConfig columna = new ColumnConfig();
        columna.setId("id");
        columna.setHeader("idFactura");
        columna.setHidden(true);
        columna.setWidth(30);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("numero");
        columna.setHeader("Numero");
        columna.setWidth(100);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("saldo");
        columna.setHeader("Saldo");
        columna.setWidth(100);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("created");
        columna.setHeader("Fecha Creación");
        columna.setWidth(100);
        configs.add(columna);

        proxy = new RpcProxy<PagingLoadResult<BaseModel>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BaseModel>> callback) {
                facturaService.listarFacturas((PagingLoadConfig) loadConfig, callback);
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<BaseModel>>(proxy);
        loader.setSortField("numero");
        loader.setSortDir(SortDir.ASC);

        pagingToolBar = new PagingToolBar(ConstantesWAR.CANT_LISTADO);
        pagingToolBar.bind(loader);
        pagingToolBar.setAutoWidth(true);

        store = new ListStore<BaseModel>(loader);

        ColumnModel cm = new ColumnModel(configs);

        grilla = new Grid<BaseModel>(store, cm);
        grilla.setSelectionModel(cbsm);
        grilla.setBorders(true);
        grilla.setLoadMask(true);
        grilla.getView().setAutoFill(true);
        grilla.getView().setForceFit(true);
        grilla.addPlugin(cbsm);

        panelPricipal.add(grilla);
        panelPricipal.setBottomComponent(pagingToolBar);

        loader.load(0, ConstantesWAR.CANT_LISTADO);

        this.show();

    }
}
