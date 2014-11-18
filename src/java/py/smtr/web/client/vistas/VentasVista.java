/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import py.smtr.ejb.shared.DetalleCV;
import py.smtr.web.client.service.ClienteService;
import py.smtr.web.client.service.ClienteServiceAsync;
import py.smtr.web.client.service.ProductoService;
import py.smtr.web.client.service.ProductoServiceAsync;
import py.smtr.web.client.service.VentaService;
import py.smtr.web.client.service.VentaServiceAsync;
import py.smtr.web.shared.ClienteProveedorWAR;


public class VentasVista extends TabItem {

    private GridDropTarget target;
    private GridDropTarget target2;
    private final FormPanel formPanel;
    private Integer idCliente;
    private final TextField<String> telefono;
    private final TextField<String> nombre;
    private final TextField<String> direccion;
    private final TextField<String> email;
    private final TextField<String> ruc;
    private final Grid<BaseModel> gridProductos;
    private final ContentPanel produtosPanel;
    private final ProductoServiceAsync productoService = GWT.create(ProductoService.class);
    private final VentaServiceAsync ventaService = GWT.create(VentaService.class);
    private final EditorGrid<BaseModel> gridDetalles;
    private final ListStore<BaseModel> detallesVenta;
    private final ListStore<BaseModel> detalleProductos;
    private final LabelField labelTotal;
    private List<DetalleCV> detalles;
    private final DateField fecha;
    private final Button botonGuardar;
    private final Button botonPDF;
    private VentasVista _this;

    public VentasVista() {
        _this = this;
        this.setBorders(false);
        this.setLayout(new ColumnLayout());
        this.setText("Ventas");
        this.setIcon(IconHelper.create("icons/cart_remove.png"));
        this.setScrollMode(Scroll.AUTO);

        ContentPanel compras = new ContentPanel();
        compras.setSize("-1", "510");
        compras.setHeading("Ventas");
        compras.setIcon(IconHelper.create("icons/cart_remove.png"));
        compras.setFrame(true);
        compras.setLayout(new FitLayout());

        FormData formData = new FormData("100%");
        formPanel = new FormPanel();
        formPanel.setFrame(true);
        formPanel.setHeaderVisible(false);
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);
        formPanel.setLabelAlign(LabelAlign.LEFT);
        compras.add(formPanel);

        botonPDF = new Button("Imprimir");
        formPanel.addButton(botonPDF);
        botonPDF.setIcon(IconHelper.create("icons/page_white_acrobat.png"));
        botonPDF.setEnabled(false);
        botonPDF.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Window.open("ImprimirFactura", "", null);
            }
        });
        
        botonGuardar = new Button("Procesar");
        formPanel.addButton(botonGuardar);
        botonGuardar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                botonGuardar.setEnabled(false);
                if (formPanel.isValid()) {
                    boolean isComplete = true;
                    ListStore<BaseModel> store = gridDetalles.getStore();
                    List<BaseModel> models = store.getModels();
                    if (!models.isEmpty()) {
                        detalles = new ArrayList<DetalleCV>();
                        for (BaseModel prod : models) {
                            Integer id = (Integer) prod.get("id");
                            Double cantidad = (Double) prod.get("cantidad");
                            String nombre = (String) prod.get("nombre");
                            Double costoCompra = (Double) prod.get("costo_venta");
                            Double subtotal = (Double) prod.get("total");
                            if (cantidad == null || costoCompra == null) {
                                isComplete = false;
                                break;
                            }
                            detalles.add(new DetalleCV(id, cantidad, nombre, costoCompra, subtotal));
                        }
                    } else {
                        isComplete = false;
                    }

                    if (isComplete) {
                        RootPanel.get("loading").setVisible(true);
                        ventaService.guardarVenta(idCliente, fecha.getValue(), Double.valueOf(labelTotal.getText()), detalles, new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                RootPanel.get("loading").setVisible(false);
                                MessageBox.alert("Error", caught.getMessage(), null);
                                botonGuardar.setEnabled(true);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                RootPanel.get("loading").setVisible(false);
                                Info.display("Exitoso", "Se ha realizado la venta.");
                                botonPDF.setEnabled(true);
                            }
                        });
                    } else {
                        detalles = null;
                        botonGuardar.setEnabled(true);
                        MessageBox.alert("Atenci&oacute;n", "Faltan datos en los detalles.", null);
                    }
                } else {
                    botonGuardar.setEnabled(true);
                }
            }
        });

        LayoutContainer cabecera = new LayoutContainer();
        formPanel.add(cabecera, formData);

        FieldSet datosProveedor = new FieldSet();
        datosProveedor.setHeading("Datos del Cliente");
        FormLayout layout = new FormLayout();
        layout.setLabelAlign(LabelAlign.TOP);
        datosProveedor.setLayout(layout);
        cabecera.add(datosProveedor);

        LayoutContainer subCabecera = new LayoutContainer();
        subCabecera.setLayout(new ColumnLayout());
        datosProveedor.add(subCabecera);

        LayoutContainer left = new LayoutContainer();
        left.setStyleAttribute("paddingRight", "10px");
        layout = new FormLayout();
        layout.setLabelAlign(LabelAlign.TOP);
        left.setLayout(layout);

        nombre = new TextField<String>();
        nombre.setFieldLabel("Nombre o Razón Social");
        nombre.setEnabled(false);
        left.add(nombre, formData);

        direccion = new TextField<String>();
        direccion.setFieldLabel("Dirección");
        direccion.setEnabled(false);
        left.add(direccion, formData);

        email = new TextField<String>();
        email.setFieldLabel("Email");
        email.setEnabled(false);
        left.add(email, formData);

        LayoutContainer right = new LayoutContainer();
        layout = new FormLayout();
        layout.setLabelAlign(LabelAlign.TOP);
        right.setLayout(layout);

        ruc = new TextField<String>();
        ruc.setAllowBlank(false);
        ruc.setFieldLabel("R.U.C.");
        ruc.addKeyListener(new KeyListener() {

            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    setearCampos();
                }
            }
        });
        ruc.addListener(Events.Blur, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent event) {
                setearCampos();
            }
        });
        right.add(ruc, formData);

        telefono = new TextField<String>();
        telefono.setFieldLabel("Teléfono(s)");
        telefono.setEnabled(false);
        right.add(telefono, formData);

        subCabecera.add(left, new ColumnData(.5));
        subCabecera.add(right, new ColumnData(.5));

        fecha = new DateField();
        fecha.setFieldLabel("Fecha");
        fecha.setValue(new Date(System.currentTimeMillis()));
        right.add(fecha, formData);

        detallesVenta = new ListStore<BaseModel>();
        detallesVenta.addStoreListener(new StoreListener<BaseModel>() {

            @Override
            public void storeUpdate(StoreEvent<BaseModel> se) {
                Record record = se.getRecord();
                Double cantidad = (Double) record.getModel().get("cantidad");
                Double costoVenta = (Double) record.getModel().get("costo_venta");
                if (cantidad != null && costoVenta != null) {
                    double d = cantidad * costoVenta;
                    NumberFormat format = NumberFormat.getFormat("#.##");
                    record.getModel().set("total", Double.valueOf(format.format(d)));
                }
                record.commit(true);

                actualizarTotal();
            }
        });

        gridDetalles = new EditorGrid<BaseModel>(detallesVenta, createColumnDetalles());
        gridDetalles.setBorders(false);
        gridDetalles.setAutoExpandColumn("nombre");
        gridDetalles.setBorders(true);
        gridDetalles.getView().setForceFit(true);
        gridDetalles.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);

        LayoutContainer detallesLayout = new LayoutContainer();
        detallesLayout.setLayout(new FitLayout());
        detallesLayout.add(gridDetalles);
        detallesLayout.setHeight("180");

        FieldSet fsDetalle = new FieldSet();
        fsDetalle.setHeading("Detalles de la venta (Arrastre aqu&iacute; los productos)");
        layout = new FormLayout();
        layout.setLabelAlign(LabelAlign.TOP);
        fsDetalle.setLayout(layout);
        fsDetalle.add(detallesLayout);
        formPanel.add(fsDetalle);

        labelTotal = new LabelField();
        labelTotal.setFieldLabel("<b>Total: </b>");
        Double total = new Double(.0);
        labelTotal.setText(total.toString());
        formPanel.add(labelTotal);

        detalleProductos = new ListStore<BaseModel>();

        gridProductos = new Grid<BaseModel>(detalleProductos, createColumnProductos());
        gridProductos.setBorders(false);
        gridProductos.setAutoExpandColumn("nombre");
        gridProductos.setBorders(true);

        produtosPanel = new ContentPanel();
        produtosPanel.setHeading("Productos");
        produtosPanel.setHeight(510);
        produtosPanel.setStyleAttribute("marginLeft", "10px");
        produtosPanel.setLayout(new FitLayout());
        produtosPanel.setIcon(IconHelper.create("icons/package.png"));
        produtosPanel.add(gridProductos);

      productoService.obtenerProductosVenta(new AsyncCallback<List<BaseModel>>() {

            @Override
            public void onFailure(Throwable caught) {
                Info.display("Error", caught.getMessage());
            }

            @Override
            public void onSuccess(List<BaseModel> result) {
                ListStore<BaseModel> detalleVenta = new ListStore<BaseModel>();
                detalleVenta.add(result);
                gridProductos.reconfigure(detalleVenta, createColumnProductos());
                produtosPanel.layout(true);
            }
        });

        GridDragSource gridDetallesDragSource = new GridDragSource(gridDetalles);
        gridDetallesDragSource.addDNDListener(new DNDListener() {

            @Override
            public void dragDrop(DNDEvent e) {
                detallesVenta.commitChanges();
                detalleProductos.commitChanges();
                actualizarTotal();
                actualizarStoreProductos();
            }
        });
        GridDragSource gridProductosDragSource = new GridDragSource(gridProductos);
        gridProductosDragSource.addDNDListener(new DNDListener() {

            @Override
            public void dragDrop(DNDEvent e) {
                detallesVenta.commitChanges();
                detalleProductos.commitChanges();
                actualizarTotal();
                actualizarStoreProductos();
            }
        });

        target = new GridDropTarget(gridDetalles);
        target.setAllowSelfAsSource(false);

        target2 = new GridDropTarget(gridProductos);
        target2.setAllowSelfAsSource(false);

        ColumnData columnData = new ColumnData(.6);
        this.add(compras, columnData);

        columnData = new ColumnData(.3);
        this.add(produtosPanel, columnData);

        setStyleAttribute("margin", "10px");
    }

    private ColumnModel createColumnProductos() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("codigo");
        column.setHeader("Cod. Producto");
        column.setWidth(80);
        column.setMenuDisabled(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("nombre");
        column.setHeader("Descripción");
        column.setMenuDisabled(true);
        column.setWidth(250);
        configs.add(column);

        return new ColumnModel(configs);
    }

    private ColumnModel createColumnDetalles() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("Id Producto");
        column.setWidth(100);
        column.setHidden(true);
        column.setMenuDisabled(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cantidad");
        column.setHeader("Cantidad");
        column.setMenuDisabled(true);
        column.setSortable(false);
        column.setWidth(40);
        NumberField cantidad = new NumberField();
        cantidad.setAllowDecimals(false);
        cantidad.setAllowNegative(false);
        cantidad.addListener(Events.Blur, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                detallesVenta.commitChanges();
            }
        });
        column.setEditor(new CellEditor(cantidad));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("codigo");
        column.setHeader("C&oacute;digo");
        column.setSortable(false);
        column.setMenuDisabled(true);
        column.setWidth(40);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("nombre");
        column.setHeader("Descripci&oacute;n");
        column.setSortable(false);
        column.setMenuDisabled(true);
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("costo_venta");
        column.setHeader("Costo Unitario");
        column.setMenuDisabled(true);
        column.setSortable(false);
        column.setWidth(50);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("total");
        column.setHeader("Sub-Total");
        column.setSortable(false);
        column.setMenuDisabled(true);
        column.setWidth(50);
        configs.add(column);

        return new ColumnModel(configs);
    }

    private void setearCampos() {
        String rucValue = ruc.getValue();
        if (rucValue != null || !ruc.getValue().trim().isEmpty()) {
            ClienteServiceAsync clienteService = GWT.create(ClienteService.class);
            clienteService.obtenerCliente(rucValue, new AsyncCallback<ClienteProveedorWAR>() {

                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", caught.getMessage(), null);
                    nombre.setValue("");
                    direccion.setValue("");
                    telefono.setValue("");
                    email.setValue("");
                    idCliente = null;
                    detalles = null;
                }

                @Override
                public void onSuccess(ClienteProveedorWAR result) {
                    nombre.setValue(result.getNombre());
                    direccion.setValue(result.getDireccion());
                    telefono.setValue(result.getTelefonos());
                    email.setValue(result.getEmail());
                    idCliente = result.getId();
                }
            });
        }
    }

    public void actualizarTotal() {
        List<BaseModel> models = detallesVenta.getModels();
        Double total = new Double(.0);
        for (BaseModel baseModel : models) {
            Double subtotal = (Double) baseModel.get("total");
            if (subtotal != null) {
                total += subtotal;
            }
        }
        labelTotal.setText(total.toString());
    }

    public void actualizarStoreProductos() {
        List<BaseModel> models = detalleProductos.getModels();
        for (BaseModel baseModel : models) {
            detalleProductos.remove(baseModel);
            baseModel.set("cantidad", null);
            baseModel.set("total", null);
            detalleProductos.add(baseModel);
        }
        produtosPanel.layout(true);
    }
}
