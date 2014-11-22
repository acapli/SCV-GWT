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

public class CajasTree {
    public static TreePanel<ModelData> cajasTree(){
        TreeLoader<ModelData> loaderCajas = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeCajas = new TreeStore<ModelData>(loaderCajas);
        final TreePanel<ModelData> treeCajas = new TreePanel<ModelData>(storeCajas);
        treeCajas.setAutoLoad(true);
        treeCajas.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeCajas.setAutoHeight(true);
        treeCajas.getStyle().setLeafIcon(IconHelper.create("icons/caja.png"));

        loaderCajas.load(getCajas());

        treeCajas.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeCajas.getSelectionModel().deselectAll();
            }
        });
        return treeCajas;
    }
    
    private static BaseTreeModel getCajas() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.cajas);
        raiz.add(abmUsuario);

        return raiz;
    }
    
}
