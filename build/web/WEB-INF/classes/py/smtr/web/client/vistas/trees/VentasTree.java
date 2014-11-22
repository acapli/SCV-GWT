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
import py.smtr.web.client.vistas.ConstantesWAR;


/**
 *
 * @author Strogg
 */
public class VentasTree {
    public static TreePanel<ModelData> ventasTree(){
        TreeLoader<ModelData> loaderVentas = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeVentas = new TreeStore<ModelData>(loaderVentas);
        final TreePanel<ModelData> treeVentas = new TreePanel<ModelData>(storeVentas);
        treeVentas.setAutoLoad(true);
        treeVentas.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeVentas.setAutoHeight(true);
        treeVentas.getStyle().setLeafIcon(IconHelper.create("icons/cart_remove.png"));

        loaderVentas.load(getVentas());

        treeVentas.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeVentas.getSelectionModel().deselectAll();
            }
        });
        
        return treeVentas;
    }
    
    private static BaseTreeModel getVentas() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.ventas);
        raiz.add(abmUsuario);

        return raiz;
    }
}
