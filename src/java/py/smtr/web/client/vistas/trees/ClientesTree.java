/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.smtr.web.client.vistas.trees;

/**
 *
 * @author Norita
 */
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
public class ClientesTree {
    
     public static TreePanel<ModelData> clientesTree(){
        TreeLoader<ModelData> loaderClientes = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeClientes = new TreeStore<ModelData>(loaderClientes);
        final TreePanel<ModelData> treeClientes = new TreePanel<ModelData>(storeClientes);
        treeClientes.setAutoLoad(true);
        treeClientes.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeClientes.setAutoHeight(true);
        treeClientes.getStyle().setLeafIcon(IconHelper.create("icons/group.png"));

        loaderClientes.load(getClientes());

        treeClientes.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeClientes.getSelectionModel().deselectAll();
            }
        });
        return treeClientes;
    }
    
    private static BaseTreeModel getClientes() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");
        BaseTreeModel abmCliente = new BaseTreeModel();
        abmCliente.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.clientes);
        raiz.add(abmCliente);
        return raiz;
    }
  
}
