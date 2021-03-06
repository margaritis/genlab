package genlab.random.colt.algos;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.basics.algos.AbstractTableGenerator;
import genlab.core.parameters.IntParameter;

public class RandomIntTableAlgo extends AbstractTableGenerator {

	public static final IntParameter PARAMETER_MIN = new IntParameter("min", "min", "minimum value", new Integer(0));
	
	public static final IntParameter PARAMETER_MAX = new IntParameter("max", "max", "maximum value", new Integer(100));
	
	public RandomIntTableAlgo() {
		super(
				"random int (colt)", 
				"generates a table filled with random integers generated by the CERN/Colt library"
				);
		
		registerParameter(PARAMETER_MIN);
		registerParameter(PARAMETER_MAX);
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		return new RandomIntTableAlgoExec(execution, algoInstance);
	}

}
