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
//import py.smtr.web.war.shared.ConstantesWAR;
//import py.fpuna.web.tp2011.war.shared.ConstantesWAR;

/**
 *
 * @author Strogg
 */
public class RolesTree {
    public static TreePanel<ModelData> rolesTree(){
        TreeLoader<ModelData> loaderRoles = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeRoles = new TreeStore<ModelData>(loaderRoles);
        final TreePanel<ModelData> treeRoles = new TreePanel<ModelData>(storeRoles);
        treeRoles.setAutoLoad(true);
        treeRoles.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeRoles.setAutoHeight(true);
        treeRoles.getStyle().setLeafIcon(IconHelper.create("icons/shield.png"));

        loaderRoles.load(getRoles());

        treeRoles.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeRoles.getSelectionModel().deselectAll();
            }
        });
        return treeRoles;
    }
    
    private static BaseTreeModel getRoles() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel listaRoles = new BaseTreeModel();
        listaRoles.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.roles);
        raiz.add(listaRoles);

        return raiz;
    }
}
