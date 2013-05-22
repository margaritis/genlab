package genlab.core.algos;

import genlab.core.commons.IWithAssociatedData;
import genlab.core.projects.IGenlabProject;

import java.io.File;
import java.util.Collection;

/**
 * 
 * Contributors may store objects in order to persist data related to workflows.
 * 
 * 
 * 
 * @author Samuel Thiriot
 *
 */
public interface IGenlabWorkflow extends IAlgo, IWithAssociatedData {

	public String getName();
	
	public boolean isValid();
	
	public void addAlgoInstance(IAlgoInstance algoInstance);
	
	public void removeAlgoInstance(IAlgoInstance algoInstance);
	
	public void connect(IAlgoInstance algoFrom, IInputOutput<?> fromOutput, IAlgoInstance algoTo, IInputOutput<?> toInput);
	
	public File getFilePersisted();
	
	public String getRelativePath();
	
	public String getRelativeFilename();

	public String getFilename();

	/**
	 * Returns the absolute filename
	 * @return
	 */
	public String getAbsolutePath();

	public IGenlabProject getProject();
	
	public Collection<IAlgoInstance> getAlgoInstances();

	public boolean containsAlgoInstance(IAlgoInstance algoInstance);
	
	public boolean containsAlgoInstance(String algoInstanceId);

	public IAlgoInstance getAlgoInstanceForId(String algoInstanceId);

	
}
