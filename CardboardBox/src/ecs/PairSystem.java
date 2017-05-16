package ecs;

import java.util.List;

import org.javatuples.Pair;

public interface PairSystem<A, B> extends NTupleSystem {
	public void stepEntity(Component<A> componentA, Component<B> componentB, float dt);
	
	@SuppressWarnings("unchecked")
	public default List<Pair<Component<A>, Component<B>>> getEntityList() {
		return (List<Pair<Component<A>, Component<B>>>) getEntityListUnsafe();
	}
}
