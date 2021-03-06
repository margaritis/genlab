package genlab.gui.graphiti.palette;

import genlab.core.model.meta.AlgoCategory;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.ExistingAlgos;
import genlab.core.model.meta.IAlgo;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.graphiti.features.CreateIAlgoInstanceFeature;
import genlab.gui.graphiti.features.OpenParametersFeature;
import genlab.gui.graphiti.features.SeeInfoFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.internal.Messages;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;

public class WorkflowToolBehaviorProvider extends DefaultToolBehaviorProvider {

	protected static int CONTEXT_BUTTON_PARAMETERS = 1 << 4;

	public WorkflowToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}
	
	
	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
		
		IContextButtonPadData data = super.getContextButtonPad(context);
		
		PictogramElement pe = context.getPictogramElement();
	
		// add default buttons 
		setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE);

		// setting behavior
	
		
		// add the "parameters" feature to the generic pad
		{
			CustomContext cc = new CustomContext(new PictogramElement[] {pe});
			cc.setInnerPictogramElement(pe);
			
			OpenParametersFeature feature = new OpenParametersFeature(getFeatureProvider());
			ContextButtonEntry button = new ContextButtonEntry(feature, cc);
			button.setText(feature.getName());
			button.setDescription(feature.getDescription());
			button.setIconId(feature.getImageId());
			data.getGenericContextButtons().add(0, button);

		}
		
		// add the "view infos" feature to the generic pad
		{
			CustomContext cc = new CustomContext(new PictogramElement[] {pe});
			cc.setInnerPictogramElement(pe);
			
			SeeInfoFeature feature = new SeeInfoFeature(getFeatureProvider());
			ContextButtonEntry button = new ContextButtonEntry(feature, cc);
			button.setText(feature.getName());
			button.setDescription(feature.getDescription());
			button.setIconId(feature.getImageId());
			data.getGenericContextButtons().add(0, button);

		}
		
		
		return data;
	}


	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> res = new ArrayList<IPaletteCompartmentEntry>();
		
		
		// add compartments for algos			
		Map<String,PaletteCompartmentEntry> categId2compartment = new HashMap<String, PaletteCompartmentEntry>();
		{
			
			Map<String,AlgoCategory> id2categ = ExistingAlgoCategories.getExistingAlgoCategories().getAllCategories();
			for (String categId : id2categ.keySet()) {
				
				// do not create empty categories
				if (!ExistingAlgos.getExistingAlgos().hasAlgoInCategory(categId))
					continue;
			//for (String parentCategId : ExistingAlgoCategories.getExistingAlgoCategories().getParentCategories()()) {
				AlgoCategory categ = id2categ.get(categId);
				
				PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(categ.getName(), null);
				compartmentEntry.setInitiallyOpen(false);
				res.add(compartmentEntry);
				
				categId2compartment.put(categId, compartmentEntry);
				
			}
			
		}
		
		// now fill these compartments
		{
			IFeatureProvider featureProvider = getFeatureProvider();
			ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
			for (ICreateFeature createFeature : createFeatures) {

				try {
	
					if (createFeature instanceof CreateIAlgoInstanceFeature) {
						
						IAlgo algo = ((CreateIAlgoInstanceFeature)createFeature).getAlgo();
						/*
						String id = ExistingAlgoCategories.getExistingAlgoCategories().getCategoryForId(
								algo.getCategoryId()
								).getTopParent().getId();
						*/
						String id = algo.getCategoryId();
						
						PaletteCompartmentEntry compartmentEntry = categId2compartment.get(id);
						
						if (compartmentEntry == null) {
							GLLogger.errorTech("unable to find a compartement for algo id "+id+"; algo "+algo.getName()+" will not be displayed", getClass());
							continue;
						}
						
						ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
								createFeature.getCreateName(), 
								createFeature.getCreateDescription(),
								createFeature.getCreateImageId(), 
								createFeature.getCreateLargeImageId(), 
								createFeature
								);
	
						compartmentEntry.addToolEntry(objectCreationToolEntry);
						

					}
				

				} catch (RuntimeException e) {
					GLLogger.errorTech(
							"error during the initialization of workflow tools; the tool "+createFeature.getCreateName()+" will not be available", 
							getClass(),
							e
							);
					
				}
			}
			
		}
	
		// sort novel compartments
		Collections.sort(res, new Comparator<IPaletteCompartmentEntry>() {

			@Override
			public int compare(IPaletteCompartmentEntry c1, IPaletteCompartmentEntry c2) {
				return c1.getLabel().compareTo(c2.getLabel());
			}
		});
		
		// and sort inside compartments
		for (PaletteCompartmentEntry compartmentEntry: categId2compartment.values()) {
			Collections.sort(
					compartmentEntry.getToolEntries(), 
					new Comparator<IToolEntry>() {

						@Override
						public int compare(IToolEntry arg0, IToolEntry arg1) {
							return arg0.getLabel().toLowerCase().compareTo(arg1.getLabel().toLowerCase());
						}
				
					});
		}


		// add compartments from super class
		{
			IPaletteCompartmentEntry[] superCompartments = super.getPalette();
			for (int i = 0; i < superCompartments.length; i++) {
				if (!superCompartments[i].getLabel().equals(Messages.DefaultToolBehaviorProvider_1_xfld))
					res.add(0, superCompartments[i]);
			}
			
		}
		
				
		return res.toArray(new IPaletteCompartmentEntry[res.size()]);
	}

	/*
	
	TODO enhance ergonomy by implementing

	@Override
	public IContextMenuEntry[] getContextMenu(ICustomContext context) {
		return super.getContextMenu(context);
	}

	
	public String getToolTip(GraphicsAlgorithm ga) {
		return null;
	}
	
	public String getTitleToolTip() {
		return null;
	}
	
	
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		return null;
	}
	*/
	
}
