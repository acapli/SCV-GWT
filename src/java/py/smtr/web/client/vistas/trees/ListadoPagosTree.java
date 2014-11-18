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


public class ListadoPagosTree {
    public static TreePanel<ModelData> listadopagosTree(){
        TreeLoader<ModelData> loaderPagados = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storePagados = new TreeStore<ModelData>(loaderPagados);
        final TreePanel<ModelData> treePagados = new TreePanel<ModelData>(storePagados);
        treePagados.setAutoLoad(true);
        treePagados.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treePagados.setAutoHeight(true);
        treePagados.getStyle().setLeafIcon(IconHelper.create("icons/money.png"));

        loaderPagados.load(getFacturas());

        treePagados.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treePagados.getSelectionModel().deselectAll();
            }
        });
        
        return treePagados;
    }
    
    private static BaseTreeModel getFacturas() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");

        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.listadopagos);
        raiz.add(abmUsuario);

        return raiz;
    }
}
