package genlab.core.model.instance;

import java.util.Collection;

/**
 * Represents an algo which contains other algos
 * 
 * @author Samuel Thiriot
 *
 */
public interface IAlgoContainerInstance extends IAlgoInstance {

	/**
	 * The children in this algo container
	 * @return
	 */
	public Collection<IAlgoInstance> getChildren();
	
	public void addChildren(IAlgoInstance child);
	
	public void removeChildren(IAlgoInstance child);

	public Collection<IAlgoInstance> getAlgoInstancesDependingToOurChildren();

	public Collection<IConnection> getConnectionsComingFromOutside();
	
	public Collection<IConnection> getConnectionsGoingToOutside();

	/**
	 * 
	 * @param bo
	 * @return
	 */
	public boolean canContain(IAlgoInstance bo);
	
	
}
