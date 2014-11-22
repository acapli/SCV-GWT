/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.client.vistas;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import java.util.List;
import py.smtr.web.client.vistas.trees.CajasTree;
import py.smtr.web.client.vistas.trees.ClientesTree;
import py.smtr.web.client.vistas.trees.ComprasTree;
import py.smtr.web.client.vistas.trees.ListadoPagosTree;

import py.smtr.web.client.vistas.trees.MonitorearTree;
import py.smtr.web.client.vistas.trees.PagosTree;
import py.smtr.web.client.vistas.trees.PasswordTree;
import py.smtr.web.client.vistas.trees.ProductosTree;
import py.smtr.web.client.vistas.trees.ProveedoresTree;
import py.smtr.web.client.vistas.trees.SalirTree;
import py.smtr.web.client.vistas.trees.TagTree;
import py.smtr.web.client.vistas.trees.UsuariosTree;
import py.smtr.web.client.vistas.trees.VentasTree;

public class MenuPrincipal extends Viewport {

    public static TabPanel advanced;
    public static ContentPanel center;
    

    public MenuPrincipal(List<Integer> roles) {
     
        final BorderLayout layout = new BorderLayout();

        this.setLayout(layout);
        this.setStyleAttribute("padding", "3px");

        ContentPanel north = new ContentPanel();
        north.setHeaderVisible(false);
        north.setLayout(new FitLayout());
        north.setBodyStyle("background-position:center;background-image: url(\"images/north.png\"); background-repeat: repeat-x;");

        //Html html = new Html();
        //html.setHtml("<table width=\"100%\" border=\"0\" style=\"background-position:center;background-image: url(\"images/north.jpg\"); background-repeat: repeat-x;\">  <tr>    <th ><div align=\"center\"><img src=\"images/battle100.png\" ></div></th>    <td><div align=\"center\"><img src=\"images/logo-bf3.png\" ></div></td>    <td><div align=\"center\"><img src=\"images/Battlefield100.png\" ></div></td>  </tr></table>");        
        //north.add(html);
        
        ContentPanel west = new ContentPanel();
        west.setHeading("Opciones disponibles");
        
        
        center = new ContentPanel();
        center.setHeaderVisible(false);
        center.setScrollMode(Scroll.AUTO);
        center.setLayout(new FitLayout());
        advanced = new TabPanel();
        advanced.setAutoHeight(true);
        advanced.setBodyBorder(false);
        advanced.setBorders(false);
        advanced.setMinTabWidth(115);
        advanced.setResizeTabs(true);
        advanced.setAnimScroll(true);
        advanced.setTabScroll(true);
        center.add(advanced);
        //center.setBodyStyle("background-position:center;background-image: url(\"images/QuirofanoHC.png\"); background-repeat: repeat-x;");
      
       
        ContentPanel south = new ContentPanel();
        south.setHeaderVisible(false);
        south.setBodyStyle("background-position:center;background-image: url(\"images/south.png\"); background-repeat: repeat-x;");
        //html = new Html();
        //html.setHtml("<center><table width=\"100%\" border=\"0\"><tr><td>&nbsp;</td><td><div> <strong><h2 align=center style=\"font-family:Verdana, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold \">&nbsp;</h2> </strong> <strong><h2 align=center style=\"font-family:Verdana, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold \">Universidad Nacional de Asunci&oacute;n - Facultad Polit&eacute;cnica</h2> </strong><strong><h2 align=center style=\"font-family:Verdana, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold \">Ingenier&iacute;a en Inform&aacute;tica - Programaci&oacute;n Web </h2> </strong> <p align=center style=\"font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px\">Desarrollado por: <a href=\"mailto:tony.realmadrid@gmail.com\" style=\"color:#FFFFFF;\">Marco Alvarez</a>| <a href=\"mailto:sebas.strogg@gmail.com\" style=\"color:#FFFFFF;\">Sebasti&aacute;n Lena</a>| <a href=\"mailto:lalvarez.pol@gmail.com\" style=\"color:#FFFFFF;\">Luis Alvarez</a></p></div></td><td>&nbsp;</td></tr></table></center>");
        //south.add(html);

        /*************************** LayoutData's *****************************/
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 100);
        northData.setHideCollapseTool(true);
        northData.setMargins(new Margins(0, 0, 3, 0));

        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200);
        westData.setMargins(new Margins(0, 0, 3, 0));

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));

        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 80);
        southData.setMargins(new Margins(0));
        /**********************************************************************/
        //de acuerdo a lo que tengo en roles...dibujar o no el menu   
        Viewport westPanel = new Viewport();
        
        westPanel.setLayout(new FitLayout());
       // westPanel.add(UsuariosTree.ususariosTree());
       // westPanel.add(ClientesTree.clientesTree());
        //westPanel.add(ProveedoresTree.proveedoresTree());      
        //westPanel.add(VentasTree.ventasTree());
       // westPanel.add(ProductosTree.productosTree());
       // westPanel.add(ComprasTree.comprasTree());
       // westPanel.add(CajasTree.cajasTree());
       // westPanel.add(PagosTree.pagosTree());
        //westPanel.add(ListadoPagosTree.listadopagosTree());
       // westPanel.add(PasswordTree.passwordTree());
       // westPanel.add(SalirTree.salirTree());    
       // west.add(westPanel);                           //todos pueden salir del sistema...jaja
        
         if (roles.contains(ConstantesWAR.ROL_ADMINISTRADOR)) {
            //se habilitan todas las funcionalidades, no importa si se tiene otro rol
            westPanel.setLayout(new FitLayout());
            westPanel.add(UsuariosTree.ususariosTree());
          //  westPanel.add(RolesTree.rolesTree());
              westPanel.add(CajasTree.cajasTree());
            westPanel.add(ClientesTree.clientesTree());
            westPanel.add(ProveedoresTree.proveedoresTree());
            westPanel.add(ProductosTree.productosTree());
            westPanel.add(ComprasTree.comprasTree());
            westPanel.add(VentasTree.ventasTree());
            westPanel.add(PagosTree.pagosTree());
            westPanel.add(ListadoPagosTree.listadopagosTree());
//            westPanel.add(CierreTree.cierreTree());
        } else {
            if (roles.contains(ConstantesWAR.ROL_VENDEDOR)) {
//                //habilitar las funcionalidades de vendedor
                  westPanel.add(ClientesTree.clientesTree());
                  westPanel.add(VentasTree.ventasTree());
            }
            if (roles.contains(ConstantesWAR.ROL_COMPRADOR)) {
                //habilitar las funcionalidades de comprador
                westPanel.add(ProveedoresTree.proveedoresTree());
                westPanel.add(ProductosTree.productosTree());
                westPanel.add(ComprasTree.comprasTree());
            }

            if (roles.contains(ConstantesWAR.ROL_CAJERO)) {
                //habilitar las funcionalidades de cajero
                westPanel.add(PagosTree.pagosTree());
                //westPanel.add(CierreTree.cierreTree());
            }
        }
        westPanel.add(PasswordTree.passwordTree());
        westPanel.add(SalirTree.salirTree());           //si o si
        west.add(westPanel);                           //todos pueden salir del sistema
        
        this.add(south, southData);
        this.add(north, northData);
        this.add(west, westData);
        this.add(center, centerData);
       

    }

}
