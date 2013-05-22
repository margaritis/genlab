package genlab.gui;


import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
	
	// file menu 
	private IWorkbenchAction openNewWizard;
    private IWorkbenchAction exportAction;
    private IWorkbenchAction saveAction;
    private IWorkbenchAction saveAllAction;
    private IWorkbenchAction switchWorkspace;
    private IWorkbenchAction exitAction;
    
    // window menu
    private IWorkbenchAction openPreferences;
    private IWorkbenchAction aboutAction;
    
    private IContributionItem viewShortListItem;
    private IContributionItem viewListItem;
    
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

    	// file menu

        openNewWizard = ActionFactory.NEW.create(window);
        register(openNewWizard);
        
        switchWorkspace = IDEActionFactory.OPEN_WORKSPACE.create(window);
        register(switchWorkspace);
        
        exportAction = ActionFactory.EXPORT.create(window);
        register(exportAction);
        
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
          
        
        // window menu
        
        viewShortListItem =  ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        viewListItem =  ContributionItemFactory.VIEWS_SHOW_IN.create(window);
       
        openPreferences = ActionFactory.PREFERENCES.create(window);
        register(openPreferences);
       
        // help menu

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
      
      
        
        //ActionFactory.NEW_EDITOR
        
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(openNewWizard);

        
        fileMenu.add(new Separator());

        fileMenu.add(exportAction);
        fileMenu.add(new Separator());
        
        fileMenu.add(saveAction);
        fileMenu.add(saveAllAction);
        
        fileMenu.add(new Separator());
        fileMenu.add(switchWorkspace);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
       
        
        // Windows
        windowMenu.add(viewListItem);
        windowMenu.add(viewShortListItem);
        windowMenu.add(openPreferences);
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        
    }
}
