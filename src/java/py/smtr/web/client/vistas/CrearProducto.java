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
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.service.ProductoService;
import py.smtr.web.client.service.ProductoServiceAsync;

/**
 *
 * @author 
 */
public class CrearProducto extends Window {

    private FormPanel form;
    private TextField<String> nombres;
    private TextField<String> codigo;
    private NumberField existencia;
    private NumberField costo;
    private NumberField precioVenta;
    private SpinnerField porcentajeGanancia;
    private CheckBox activo;
    private Button guardar;
    private Button cancelar;
    private ProductoServiceAsync productoService = GWT.create(ProductoService.class);
    private CrearProducto _this;

    public CrearProducto() {
        _this = this;
        this.setSize(375, 300);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Creación de Productos");
        this.setIcon(IconHelper.create("icons/package_add.png"));
        this.setResizable(false);

        form = new FormPanel() {
            @Override
            public boolean isValid() {
                if (super.isValid()) {
                    if(nombres.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El nombre del producto no pueder ser vacío.", null);
                    } else if(codigo.getValue().trim().isEmpty()) {
                        MessageBox.alert("Atención", "El código del producto no pueder ser vacío.", null);
                    } else {
                        return true;
                    }
                }
                return false;
            }
        };
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);

        codigo = new TextField<String>();
        codigo.setAllowBlank(false);
        codigo.setFieldLabel("Código");
        codigo.getMessages().setBlankText("Este campo es requerido"); 
        codigo.setMaxLength(20);
        
        nombres = new TextField<String>();
        nombres.setAllowBlank(false);
        nombres.setFieldLabel("Nombre");
        nombres.getMessages().setBlankText("Este campo es requerido"); 
        nombres.setMaxLength(100);
        
        existencia = new NumberField();
        existencia.setAllowBlank(false);
        existencia.setAllowNegative(false);
        existencia.setFieldLabel("Existencia");
        
        costo = new NumberField();
        costo.setAllowBlank(false);
        costo.setAllowNegative(false);
        costo.setFieldLabel("Costo");
        costo.addKeyListener(new KeyListener() {

            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    //setearCampos();
                    precioVenta.setValue(costo.getValue().floatValue()*(1+porcentajeGanancia.getValue().floatValue()/100));
                    //Number number = numberFormat.parse("666,666.77");
                }
            }
        });
        
        
        porcentajeGanancia = new SpinnerField();
        porcentajeGanancia.setFieldLabel("% Ganancia");
        porcentajeGanancia.setAllowBlank(false);
        porcentajeGanancia.setAllowDecimals(false);
        porcentajeGanancia.setAllowNegative(false);
        porcentajeGanancia.setIncrement(1);
        porcentajeGanancia.setFormat(NumberFormat.getFormat("00.00"));
        porcentajeGanancia.setMinValue(0);   
        porcentajeGanancia.setEditable(true);
        porcentajeGanancia.setMaxValue(100);
        porcentajeGanancia.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    event.getKeyCode();
                    precioVenta.setValue(costo.getValue().floatValue()*(1+porcentajeGanancia.getValue().floatValue()/100));
                }
            }
        });
        
                
        precioVenta= new NumberField();
        precioVenta.setFieldLabel("Precio de Venta");
        precioVenta.setEditable(true);
        precioVenta.setAllowNegative(false);
        precioVenta.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    event.getKeyCode();
                    //precioVenta.setValue(costo.getValue().floatValue()*(1+porcentajeGanancia.getValue().floatValue()/100));
                    porcentajeGanancia.setValue((precioVenta.getValue().floatValue()/costo.getValue().floatValue() -1)*100);
                }
            }
        });
        
        
        
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
                     
                    productoService.guardarProducto(nombres.getValue(), 
                                                    codigo.getValue(), 
                                                    porcentajeGanancia.getValue().doubleValue(), 
                                                    costo.getValue().doubleValue(), 
                                                    existencia.getValue().doubleValue(), 
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
                            //RootPanel.get("loading").setVisible(false);
                            _this.hide();
                            Info.display("Exitoso", "Se ha creado el nuevo producto.");
                            ListadoProductos.loader.load();
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
        form.add(codigo);
        form.add(nombres);
        form.add(existencia);
        form.add(costo);
        form.add(porcentajeGanancia);
        form.add(precioVenta);
        form.add(activo);
        form.addButton(guardar);
        form.addButton(cancelar);
    }
}
