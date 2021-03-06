package genlab.igraph.algos.generation;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.basics.flowtypes.DoubleInOut;
import genlab.core.model.meta.basics.flowtypes.IntegerInOut;
import genlab.core.parameters.BooleanParameter;
import genlab.core.parameters.IntParameter;
import genlab.core.usermachineinteraction.ListOfMessages;
import genlab.igraph.natjna.IGraphGraph;
import genlab.igraph.natjna.IGraphLibrary;

public class WattsStrogatzGeneratorAlgo extends AbstractIGraphGenerator {

	public static final IntegerInOut INPUT_N = new IntegerInOut(
			"in_N", 
			"N", 
			"number of vertices to create"
			);
	
	public static final IntegerInOut INPUT_NEI = new IntegerInOut(
			"in_NEI", 
			"nei", 
			"size of the neighborhood for each vertex"
			);
	
	public static final DoubleInOut INPUT_P = new DoubleInOut(
			"in_p", 
			"p", 
			"rewiring probability"
			);
	
	public static final BooleanParameter PARAM_ALLOW_LOOPS = new BooleanParameter(
			"param_allow_loops", 
			"allow loops", 
			"generate loop edges", 
			false
			);
	
	public static final BooleanParameter PARAM_DIRECTED = new BooleanParameter(
			"param_directed", 
			"directed", 
			"generate a directed graph", 
			false
			);
	
	public static final IntParameter PARAM_DIM = new IntParameter(
			"param_dim", 
			"dimensions", 
			"The size of the lattice along each dimension.", 
			new Integer(1)
			);
	
	
	public WattsStrogatzGeneratorAlgo() {
		super(
				"Watts-Strogatz beta generator (igraph)", 
				"This function generates a graph according to the Watts-Strogatz model of small-world networks. The graph is obtained by creating a circular undirected lattice and then rewire the edges randomly with a constant probability."
				);
		
		inputs.add(INPUT_N);
		inputs.add(INPUT_P);
		inputs.add(INPUT_NEI);
		
		registerParameter(PARAM_ALLOW_LOOPS);
		registerParameter(PARAM_DIM);
		registerParameter(PARAM_DIRECTED);
		
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		
		return new AbstractIGraphGeneratorExec(execution, algoInstance) {
			
			@Override
			public long getTimeout() {
				return 1000;
			}
			
			@Override
			protected IGraphGraph generateGraph(IGraphLibrary lib,
					ListOfMessages messages) {

				
				int N = (Integer)getInputValueForInput(INPUT_N);
				int nei = (Integer)getInputValueForInput(INPUT_NEI);
				double p= (Double)getInputValueForInput(INPUT_P);

				
				boolean allowLoops = (Boolean)algoInst.getValueForParameter(PARAM_ALLOW_LOOPS.getId());
				boolean directed = (Boolean)algoInst.getValueForParameter(PARAM_DIRECTED.getId());
				int dim = (Integer)algoInst.getValueForParameter(PARAM_DIM.getId());
										
				return lib.generateWattsStrogatz(N, dim, p, nei, directed, allowLoops);
				
			}
		};
	}

}
