package ecs;

import java.util.BitSet;

import util.InsertOnlyArray;

public class Entity {
	/* package */ InsertOnlyArray<Component> componentArray = new InsertOnlyArray<>();
	/* package */ BitSet componentBitset = new BitSet();
	
	public <T extends Component> void addComponent(T component) {
		Class<? extends Component> componentClass = component.getClass();
		int componentIndex = ComponentIndex.getFor(componentClass).getValue();
		componentArray.set(componentIndex, component);
		componentBitset.set(componentIndex, true);
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass) {
		int componentIndex = ComponentIndex.getFor(componentClass).getValue();
		componentArray.set(componentIndex, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> componentClass) {
		int componentIndex = ComponentIndex.getFor(componentClass).getValue();
		return (T) componentArray.get(componentIndex);
	}
}
