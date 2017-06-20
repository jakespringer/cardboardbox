package ecs;

import java.util.BitSet;

public class Family {
	
	private BitSet required = new BitSet();
	private BitSet prohibited = new BitSet();
	
	@SafeVarargs
	public static Family require(Class<? extends Component>... componentClasses) {
		BitSet requiredAggregate = new BitSet();
		for (int i=0; i<componentClasses.length; ++i) {
			int index = ComponentIndex.getFor(componentClasses[i]).getValue();
			requiredAggregate.set(index, true);
		}
		
		Family family = new Family();
		family.addRequired(requiredAggregate);
		return family;
	}
	
	@SuppressWarnings("unchecked")
	public Family prohibit(Class<? extends Component>... componentClasses) {
		BitSet prohibitedAggregate = new BitSet();
		for (int i=0; i<componentClasses.length; ++i) {
			int index = ComponentIndex.getFor(componentClasses[i]).getValue();
			prohibitedAggregate.set(index, true);
		}
		addProhibited(prohibitedAggregate);
		return this;
	}
	
	private void addRequired(BitSet required) {
		this.required.or(required);
	}
	
	private void addProhibited(BitSet prohibited) {
		this.prohibited.or(prohibited);
	}
	
	boolean checkEntity(Entity e) {
		BitSet entityBitsetClone = (BitSet) e.componentBitset.clone();
		entityBitsetClone.and(required);
		return entityBitsetClone.equals(required) && !e.componentBitset.intersects(prohibited);
	}
}
