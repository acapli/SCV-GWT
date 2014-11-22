/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas.trees;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import java.util.List;
import py.smtr.web.shared.ConstantesWAR;

public class PagosTree {
    public static TreePanel<ModelData> pagosTree(){
        TreeLoader<ModelData> loaderFacturas = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeFacturas = new TreeStore<ModelData>(loaderFacturas);
        final TreePanel<ModelData> treePagos = new TreePanel<ModelData>(storeFacturas);
        treePagos.setAutoLoad(true);
        treePagos.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treePagos.setAutoHeight(true);
        treePagos.getStyle().setLeafIcon(IconHelper.create("icons/money.png"));

        loaderFacturas.load(getFacturas());

        treePagos.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treePagos.getSelectionModel().deselectAll();
            }
        });
        
        return treePagos;
    }
    
    private static BaseTreeModel getFacturas() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.pagos);
        raiz.add(abmUsuario);

        return raiz;
    }
}
