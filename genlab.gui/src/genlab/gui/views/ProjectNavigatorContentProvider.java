package genlab.gui.views;

import genlab.core.usermachineinteraction.GLLogger;

import java.io.File;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TODO view http://devdesignandstuff.blogspot.fr/2010/10/contributing-to-eclipse-common.html
 * 
 * @author Samuel Thiriot
 *
 */
public class ProjectNavigatorContentProvider implements ITreeContentProvider {

	public ProjectNavigatorContentProvider() {
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// read only !
	}

	@Override
	public Object[] getElements(Object inputElement) {
		
		// get children :-)
		if (inputElement instanceof IWorkspaceRoot) {
			IWorkspaceRoot workspaceRoot = (IWorkspaceRoot)inputElement;
			return workspaceRoot.getProjects();
		} 
		
		GLLogger.warnTech("oops, getElement called for something which is not a project: "+inputElement, getClass());
		
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		System.err.println("project navigator: get children");
		if (parentElement instanceof IWorkspaceRoot) {
			IWorkspaceRoot workspaceRoot = (IWorkspaceRoot)parentElement;
			return workspaceRoot.getProjects();
		} 
		
		if (parentElement instanceof org.eclipse.core.resources.IProject) {
			org.eclipse.core.resources.IProject project = (org.eclipse.core.resources.IProject)parentElement;
			try {
				return project.members();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new Object[0];
			}
		} 
		
		System.err.println("oops ! is in getChildren but not a child...");

		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		
		if (element instanceof org.eclipse.core.resources.IProject) {
			return ((org.eclipse.core.resources.IProject)element).getParent();
		}
		if (element instanceof File) {
			return ((File)element).getParentFile();
		}
		System.err.println("oops ! is in getParent but not a file...");

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (
				(element instanceof IWorkspaceRoot)
				||
				(element instanceof org.eclipse.core.resources.IProject)
				||
				(
					element instanceof File &&
					((File)element).isDirectory() 
					)
				);
	}
	
}