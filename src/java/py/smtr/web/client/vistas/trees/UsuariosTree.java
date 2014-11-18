
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

public class UsuariosTree {
    public static TreePanel<ModelData> ususariosTree(){
        TreeLoader<ModelData> loaderUsuarios = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
        TreeStore<ModelData> storeUsuarios = new TreeStore<ModelData>(loaderUsuarios);
        final TreePanel<ModelData> treeUsuarios = new TreePanel<ModelData>(storeUsuarios);
        treeUsuarios.setAutoLoad(true);
        treeUsuarios.setDisplayProperty(ConstantesWAR.NOMBRE_ITEM);
        treeUsuarios.setAutoHeight(true);
        treeUsuarios.getStyle().setLeafIcon(IconHelper.create("icons/group.png"));

        loaderUsuarios.load(getUsuarios());

        treeUsuarios.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                AccionesMenu.accion(se.getSelectedItem().get(ConstantesWAR.NOMBRE_ITEM).toString());
                treeUsuarios.getSelectionModel().deselectAll();
            }
        });
        return treeUsuarios;
    }
    
    private static BaseTreeModel getUsuarios() {
        BaseTreeModel raiz = new BaseTreeModel();
        raiz.set(ConstantesWAR.NOMBRE_ITEM, "raiz");
        BaseTreeModel abmUsuario = new BaseTreeModel();
        abmUsuario.set(ConstantesWAR.NOMBRE_ITEM, ConstantesWAR.usuarios);
        raiz.add(abmUsuario);
        return raiz;
    }
}