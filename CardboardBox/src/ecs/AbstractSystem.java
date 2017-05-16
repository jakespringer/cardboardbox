package ecs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.javatuples.Unit;

public abstract class AbstractSystem implements NTupleSystem {
	private List<Tuple> entityList = new LinkedList<>();
	
	public List<Tuple> getEntityListUnsafe() {
		return Collections.unmodifiableList(entityList);
	}
	
	public void addEntity(Component<?>...components) {
		// todo: check, make sure there is the right number of components
		
		Tuple t;
		switch (components.length) {
		case 1:
			t = Unit.with(components[0]);
			break;
		case 2:
			t = Pair.with(components[0], components[1]);
			break;
		default:
			throw new RuntimeException("Illegal entity");
		}
		
		entityList.add(t);
	}
}
