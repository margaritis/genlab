package genlab.igraph.natjna;

import genlab.core.commons.WrongParametersException;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Reflects the igraph internal igraph_vector_t structure.
 * Warning, it should first by initialized using the igraph init vector method.
 * @author Samuel Thiriot
 *
 */
public class InternalVectorStruct extends Structure {
	
	public Pointer stor_begin;
	public Pointer stor_end;
	public Pointer end;

	public static class ByReference extends InternalVectorStruct implements Structure.ByReference {}

	public InternalVectorStruct() {
		stor_begin = new Pointer(Pointer.SIZE);
		stor_end = new Pointer(Pointer.SIZE);
		end = new Pointer(Pointer.SIZE);
		ensureAllocated();
//	    allocateMemory();

	}
	
	public InternalVectorStruct(Pointer p) {
		super(p);
		read();
	}
	
	@Override
	protected List getFieldOrder() {
		return Arrays.asList(new String[] { "stor_begin", "stor_end", "end" });
	}
	
	/*
	BASE FUNCTION(igraph_vector,e)         (const TYPE(igraph_vector)* v, long int pos) {
		assert(v != NULL);
		assert(v->stor_begin != NULL);
		return * (v->stor_begin + pos);
	}
	*/
	
	/**
	 * Returns the element with this index, supposing it is a integer.
	 * Warning: if the vector was changed (memory copy)
	 * and not update, something will become really wrong here.
	 * @param idx
	 * @return
	 */
	public int getInt(int idx) {
		//Pointer p = stor_begin.getPointer(idx);
		//return p.getInt(0);
		return stor_begin.getInt(idx);
	}
	
	public int[] asIntArray(int size) {
		return stor_begin.getIntArray(0, size);
	}
	public double[] asDoubleArray(int size) {
		return stor_begin.getDoubleArray(0, size);
	}
	
	
	public void fillWithArray(double[] values, int length) {
		
		long requiredSize = Native.getNativeSize(Double.class)*length;
		long pointerLastFuture = Pointer.nativeValue(stor_begin)+requiredSize;
		
		if (pointerLastFuture > Pointer.nativeValue(stor_end))
			throw new WrongParametersException("too many values for the size of this pointer !");
		
		stor_begin.write(0, values, 0, length);
		Pointer.nativeValue(end, pointerLastFuture);
		
	}
	
	

}
