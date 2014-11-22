/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.service.PagoService;
import py.smtr.web.client.service.PagoServiceAsync;
//import py.fpuna.web.tp2011.war.client.services.PagoService;
//import py.fpuna.web.tp2011.war.client.services.PagoServiceAsync;


class ProcesarPago extends Window {

    private FormPanel form;
    private NumberField monto;
    private Button pagar;
    private Button cancelar;
    private PagoServiceAsync pagoService = GWT.create(PagoService.class);
    private ProcesarPago _this;
    private final LabelField label;

    public ProcesarPago(final Integer id, final Double saldo) {
        _this = this;
        this.setSize(375, 145);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setFrame(true);
        this.setHeading("Procesar Pago");
        this.setIcon(IconHelper.create("icons/money_add.png"));
        this.setResizable(false);

        form = new FormPanel(); 
        form.setLabelWidth(110);
        form.setHeaderVisible(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);

        monto = new NumberField();
        monto.setAllowBlank(false);
        monto.setAllowNegative(false);
        monto.setMaxValue(saldo);
        monto.setFieldLabel("Monto a Pagar");
        monto.setMinValue(new Double(0.000000001));
        monto.getMessages().setBlankText("Este campo es requerido");

        label = new LabelField();
        label.setFieldLabel("Saldo Restante");
        label.setText(String.valueOf(saldo));
        

        pagar = new Button("Pagar");
        pagar.setIcon(IconHelper.create("icons/accept.png"));
        pagar.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (form.isValid()) {
                    pagar.setEnabled(false);
                    RootPanel.get("loading").setVisible(true);
                    pagoService.procesarPago(id, monto.getValue().doubleValue(), new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            RootPanel.get("loading").setVisible(false);
                            MessageBox.alert("Error", caught.getMessage(), null);
                            pagar.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            RootPanel.get("loading").setVisible(false);
                            _this.hide();
                            Info.display("Exitoso", "Se ha realizado el pago.");
                            ListadoFactura.loader.load();
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
        form.add(label);
        form.add(monto);
        form.addButton(pagar);
        form.addButton(cancelar);

    }
}