package py.smtr.web.client;

import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import py.smtr.web.client.vistas.Login;
import py.smtr.web.client.vistas.MenuPrincipal;

public class mainEntryPoint extends Viewport implements EntryPoint {
    
   public mainEntryPoint() {

        ThemeManager.register(new Slate());
        GXT.setDefaultTheme(Slate.SLATE, true); //set default theme to new SLATE skin
        //Theme.BLUE.set("file", "gxt/css/gxt-all.css"); //set custom css'es path for standard blue theme
        Slate.SLATE.set("file", "gxt/themes/slate/css/xtheme-slate.css"); //set custom path for SLATE theme
        //Access.ACCESS.set("file", "gxt/themes/access/css/xtheme-access.css");
        this.setLayout(new CenterLayout());
        Login login = new Login();
        this.add(login);
        RootPanel.get("loading").setVisible(false);
        RootPanel.get().add(this);
        
        /*MenuPrincipal mn = new MenuPrincipal();
        this.add(mn);
        RootPanel.get().add(this);*/
        
    }

    @Override
    public void onModuleLoad() {
    }       
     
 }