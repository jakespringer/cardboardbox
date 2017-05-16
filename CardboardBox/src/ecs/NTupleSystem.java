package ecs;

import java.util.List;

import org.javatuples.Tuple;

public interface NTupleSystem {
	public void step(float dt);
	
	List<? extends Tuple> getEntityListUnsafe();
}
