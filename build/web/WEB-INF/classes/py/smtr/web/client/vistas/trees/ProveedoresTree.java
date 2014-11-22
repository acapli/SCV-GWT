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

public class ProveedoresTree {
    public static TreePanel<ModelData> proveedoresTree(){
        TreeLoader<ModelData> loaderProveedores = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeProveedores = new TreeStore<ModelData>(loaderProveedores);
        final TreePanel<ModelData> treeProveedores = new TreePanel<ModelData>(storeProveedores);
        treeProveedores.setAutoLoad(true);
        treeProveedores.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeProveedores.setAutoHeight(true);
        treeProveedores.getStyle().setLeafIcon(IconHelper.create("icons/user_gray.png"));

        loaderProveedores.load(getProveedores());

        treeProveedores.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeProveedores.getSelectionModel().deselectAll();
            }
        });
        return treeProveedores;
    }
    
    private static BaseTreeModel getProveedores() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmProveedor = new BaseTreeModel();
        abmProveedor.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.proveedores);
        raiz.add(abmProveedor);

        return raiz;
    }
}
