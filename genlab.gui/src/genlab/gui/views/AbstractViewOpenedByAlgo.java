package genlab.gui.views;

import genlab.core.exec.IExecution;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.algos.AbstractOpenViewAlgoExec;
import genlab.gui.perspectives.OutputsGUIManagement;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.part.ViewPart;

/**
 * Basic class for a view which is opened by the execution of an algorithm.
 * 
 * 
 * @author Samuel Thiriot
 *
 */
public abstract class AbstractViewOpenedByAlgo extends ViewPart implements IPropertyChangeListener, IExecutionView  {

	public static final String PROPERTY_ALGOVIEW_EXEC = "algoview_id";
	
	protected IExecution execution = null;
	
	public AbstractViewOpenedByAlgo() {
		addPartPropertyListener(this);
		
	}
	
	protected abstract String getName(AbstractOpenViewAlgoExec exec);


	@Override
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		
		GLLogger.traceTech("received property: "+event.getProperty(), getClass());
		
		if (event.getProperty().equals(PROPERTY_ALGOVIEW_EXEC)) {
			AbstractOpenViewAlgoExec exec = AbstractOpenViewAlgoExec.getViewExecForId((String) event.getNewValue());
			if (exec == null) {
				GLLogger.warnTech("unable to retrieve the content to display for exec: "+event.getNewValue(), getClass());
				return;
			}
			exec.callbackRegisterView(this);
			
			setPartName(getName(exec));
			
			this.execution = exec.getExecution();

			OutputsGUIManagement.singleton.registerOutputGUI(this);
			
		}
	}
	
	@Override
	public IExecution getExecution() {
		return execution;
	}

}
