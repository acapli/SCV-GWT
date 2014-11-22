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


public class SalirTree {
    public static TreePanel<ModelData> salirTree(){
        TreeLoader<ModelData> loaderSalir = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeSalir = new TreeStore<ModelData>(loaderSalir);
        final TreePanel<ModelData> treeSalir = new TreePanel<ModelData>(storeSalir);
        treeSalir.setAutoLoad(true);
        treeSalir.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeSalir.setAutoHeight(true);
        treeSalir.getStyle().setLeafIcon(IconHelper.create("icons/cross.png"));

        loaderSalir.load(getSalir());

        treeSalir.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeSalir.getSelectionModel().deselectAll();
            }
        });
        return treeSalir;
    }
    
    private static BaseTreeModel getSalir() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel logout = new BaseTreeModel();
        logout.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.salir);
        raiz.add(logout);

        return raiz;
    }
}