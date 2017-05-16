package ecs;

import java.util.List;

import org.javatuples.Unit;

public interface UnitSystem<A> extends NTupleSystem {
	public void stepEntity(Component<A> componentA, float dt);
	
	@SuppressWarnings("unchecked")
	public default List<Unit<Component<A>>> getEntityList() {
		return (List<Unit<Component<A>>>) getEntityListUnsafe();
	}
}
