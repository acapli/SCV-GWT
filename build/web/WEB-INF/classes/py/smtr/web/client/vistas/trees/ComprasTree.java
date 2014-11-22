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
public class ComprasTree {

    public static TreePanel<ModelData> comprasTree(){
        TreeLoader<ModelData> loaderCompras = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeCompras = new TreeStore<ModelData>(loaderCompras);
        final TreePanel<ModelData> treeCompras = new TreePanel<ModelData>(storeCompras);
        treeCompras.setAutoLoad(true);
        treeCompras.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeCompras.setAutoHeight(true);
        treeCompras.getStyle().setLeafIcon(IconHelper.create("icons/basket_put.png"));

        loaderCompras.load(getCompras());

        treeCompras.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeCompras.getSelectionModel().deselectAll();
            }
        });
        
        return treeCompras;
    }
    
    private static BaseTreeModel getCompras() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.compras);
        raiz.add(abmUsuario);

        return raiz;
    }
    
}
