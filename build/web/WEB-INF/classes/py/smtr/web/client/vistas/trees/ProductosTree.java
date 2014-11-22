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


public class ProductosTree {
    public static TreePanel<ModelData> productosTree(){
        TreeLoader<ModelData> loaderProductos = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeProductos = new TreeStore<ModelData>(loaderProductos);
        final TreePanel<ModelData> treeProductos = new TreePanel<ModelData>(storeProductos);
        treeProductos.setAutoLoad(true);
        treeProductos.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeProductos.setAutoHeight(true);
        treeProductos.getStyle().setLeafIcon(IconHelper.create("icons/package.png"));

        loaderProductos.load(getProductos());

        treeProductos.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeProductos.getSelectionModel().deselectAll();
            }
        });
        return treeProductos;
    }
    
    private static BaseTreeModel getProductos() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmProveedor = new BaseTreeModel();
        abmProveedor.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.productos);
        raiz.add(abmProveedor);

        return raiz;
    }
}
