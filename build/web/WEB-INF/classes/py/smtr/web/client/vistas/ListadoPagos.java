/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
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
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import py.smtr.web.client.service.FacturaService;
import py.smtr.web.client.service.FacturaServiceAsync;
import py.smtr.web.client.service.ListadoPagosService;
import py.smtr.web.client.service.ListadoPagosServiceAsync;

public class ListadoPagos extends TabItem {

   public static PagingLoader<PagingLoadResult<BaseModel>> loader;
    private final ListadoPagosServiceAsync ListadoPagosService = GWT.create(ListadoPagosService.class);
    private Grid<BaseModel> grilla;
    private RpcProxy<PagingLoadResult<BaseModel>> proxy;
    private PagingToolBar pagingToolBar;
   //private ListStore<BaseModel> store;
    private GroupingStore<BaseModel> store;  
 
    
    public ListadoPagos() {
        this.setBorders(false);
        this.setLayout(new FitLayout());
        this.setText("Listado Pagos");
        this.setIcon(IconHelper.create("icons/money.png"));
       
        ContentPanel panelPricipal = new ContentPanel();
//        panelPricipal.setTitle("Holaaaa");
        panelPricipal.setBorders(true);
        panelPricipal.setBodyBorder(false);
        panelPricipal.setHeight(MenuPrincipal.center.getHeight(true) - 33);
        panelPricipal.setHeaderVisible(false);
        panelPricipal.setLayout(new FitLayout());

        this.add(panelPricipal);
  
        
        ToolBar toolBar = new ToolBar();
        toolBar.add(new SeparatorToolItem());
        panelPricipal.setTopComponent(toolBar);
        
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        
      
        //List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        
        SummaryColumnConfig<Integer> productoColumn = new SummaryColumnConfig<Integer>("descripcion", "Factura", 30);
        productoColumn.setSummaryType(SummaryType.COUNT);
        productoColumn.setSummaryRenderer(new SummaryRenderer() {
                    @Override
                    public String render(Number value, Map<String, Number> data) {
                return value.intValue() > 1 ? "<b>(" + value.intValue() + " Productos)</b>" : "<b>(1 Producto)</b>";
            }
        });
    
         SummaryColumnConfig<Integer> SaldoAnterior = new SummaryColumnConfig<Integer>("saldoAnterior", "Saldo Anterior", 25);
        SaldoAnterior.setSummaryType(SummaryType.COUNT);
        SaldoAnterior.setSummaryRenderer(new SummaryRenderer() {
            @Override
            public String render(Number value, Map<String, Number> data) {
                return value.intValue() > 1 ? "<b>(" + value.intValue() + " Pagos)</b>" : "<b>(1 Pago)</b>";
            }
        });
        
        SummaryColumnConfig<Integer> pagoParcial = new SummaryColumnConfig<Integer>("saldoParcial", "Pago Parcial", 20);
        pagoParcial.setSummaryType(SummaryType.COUNT);
        pagoParcial.setSummaryRenderer(new SummaryRenderer() {
            @Override
            public String render(Number value, Map<String, Number> data) {
                return value.intValue() > 1 ? "<b>(" + value.intValue() + " Pagos)</b>" : "<b>(1 Pago)</b>";
            }
        });
        
         SummaryColumnConfig<Integer> SaldoActual = new SummaryColumnConfig<Integer>("saldoActual", "Saldo Actual", 10);
        SaldoActual.setSummaryType(SummaryType.COUNT);
        SaldoActual.setSummaryRenderer(new SummaryRenderer() {
            @Override
            public String render(Number value, Map<String, Number> data) {
                return value.intValue() > 1 ? "<b>(" + value.intValue() + " Pagos)</b>" : "<b>(1 Pago)</b>";
            }
        });
        
        
//        SummaryColumnConfig<Double> reservaColumn = new SummaryColumnConfig<Double>("saldoFinal", "Reserva Total", 35);
//        
//        SummaryColumnConfig<Double> idColumn = new SummaryColumnConfig<Double>("listaPagos", "Pagos Parciales", 15);
//        
       // reservaColumn.setSummaryType(SummaryType.SUM);
        //reservaColumn.setAlignment(HorizontalAlignment.RIGHT);
//         reservaColumn.setSummaryRenderer(new SummaryRenderer() {
//
//            @Override
//            public String render(Number value, Map<String, Number> data) {
//                return "<b>"+value.intValue()+" unidades</b>";
//            }
//        });
        
        columns.add(productoColumn);
       
        columns.add(SaldoAnterior);
        columns.add(pagoParcial);
        columns.add(SaldoActual);
        
//        columns.add(reservaColumn);
//        columns.add(idColumn);
        
        
//        final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
//        cbsm.setSelectionMode(SelectionMode.MULTI);
//        configs.add(cbsm.getColumn());
        
          /*
        ColumnConfig columna = new ColumnConfig();
        columna.setId("id");
        columna.setHeader("idFactura");
        columna.setHidden(true);
        columna.setWidth(30);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("numero");
        columna.setHeader("Numero de Factura");
        columna.setWidth(100);
        configs.add(columna);
        
        columna = new ColumnConfig();
        columna.setId("monto");
        columna.setHeader("Monto Total");
        columna.setWidth(100);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("saldo");
        columna.setHeader("Monto Pago");
        columna.setWidth(100);
        configs.add(columna);
        
        columna = new ColumnConfig();
        columna.setId("saldoFinal");
        columna.setHeader("Saldo");
        columna.setWidth(100);
        configs.add(columna);

        columna = new ColumnConfig();
        columna.setId("created");
        columna.setHeader("Fecha de Pago");
        columna.setWidth(100);
        configs.add(columna);

        */        
  
        //List<ColumnConfig> config = new ArrayList<ColumnConfig>(); 
        
       /*final CheckBoxSelectionModel<BaseModel> cbsm = new CheckBoxSelectionModel<BaseModel>();
        cbsm.setSelectionMode(SelectionMode.MULTI);
        config.add(cbsm.getColumn());
        */
       
        ///////////////////////////////////////
        
        
        final ColumnModel cm = new ColumnModel(columns);  
        
        GroupSummaryView summary = new GroupSummaryView();
        summary.setForceFit(true);
        summary.setShowGroupedColumn(false);
        
        proxy = new RpcProxy<PagingLoadResult<BaseModel>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BaseModel>> callback) {
               ListadoPagosService.listarPagos((PagingLoadConfig) loadConfig, callback);
            }
        };
        
        loader = new BasePagingLoader<PagingLoadResult<BaseModel>>(proxy) {
            @Override
            protected void onLoadFailure(Object loadConfig, Throwable t) {
                grilla.unmask();
                MessageBox.alert("Error", t.getMessage(), null);
            }

            @Override
            protected void onLoadSuccess(Object loadConfig, PagingLoadResult<BaseModel> data) {
                grilla.unmask();
                super.onLoadSuccess(loadConfig, data);
            }
        };
        
        store = new GroupingStore<BaseModel>(loader);
        store.groupBy("descripcion" );
        
        

        grilla = new Grid<BaseModel>(store, cm);
        grilla.setBorders(false);
        grilla.setLoadMask(true);
        grilla.setView(summary);
        grilla.getView().setAutoFill(true);
        grilla.getView().setShowDirtyCells(false);

        panelPricipal.add(grilla);

        loader.load();

        this.show();
        
        
         /*loader = new BasePagingLoader<PagingLoadResult<BaseModel>>(proxy);
        loader.setSortField("numero");
        loader.setSortDir(SortDir.ASC);

        pagingToolBar = new PagingToolBar(ConstantesWAR.CANT_LISTADO);
        pagingToolBar.bind(loader);
        pagingToolBar.setAutoWidth(true);

        
        store = new GroupingStore<BaseModel>(loader);
        
        */
        //store = new ListStore<BaseModel>(loader);
        
        //store.add(TestData.getCompanies());
        
        //////////////////////////////
        //store.groupBy("numero");  
  
         
//        panelPricipal.setBottomComponent(pagingToolBar);
//
//        loader.load(0, ConstantesWAR.CANT_LISTADO);
//
//        this.show();

    }
}
