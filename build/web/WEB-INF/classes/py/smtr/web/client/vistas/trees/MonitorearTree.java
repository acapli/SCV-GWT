/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Norita
 */
public class MonitorearTree {
    
    public static TreePanel<ModelData> monitorearTree(){
        
        TreeLoader<ModelData> loaderMonitorear = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeMonitorear = new TreeStore<ModelData>(loaderMonitorear);
        final TreePanel<ModelData> treeMonitorear = new TreePanel<ModelData>(storeMonitorear);
        treeMonitorear.setAutoLoad(true);
        treeMonitorear.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeMonitorear.setAutoHeight(true);
        treeMonitorear.getStyle().setLeafIcon(IconHelper.create("icons/group.png"));

        loaderMonitorear.load(getUsuarios());

        treeMonitorear.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeMonitorear.getSelectionModel().deselectAll();
            }
        });
        
        return treeMonitorear;
    }
    
    private static BaseTreeModel getUsuarios() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.monitorear);
        raiz.add(abmUsuario);

        return raiz;
    }
  
    
}
