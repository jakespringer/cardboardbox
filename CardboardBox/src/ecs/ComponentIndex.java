package ecs;

import java.util.HashMap;

class ComponentIndex {
	private static int globalIndex = 0;
	private static HashMap<Class<? extends Component>, ComponentIndex> componentIndexMapper = new HashMap<>();
	
	private final int index;
	
	private ComponentIndex() {
		index = globalIndex++;
	}
	
	public static ComponentIndex getFor(Class<? extends Component> componentClass) {
		ComponentIndex index = componentIndexMapper.get(componentClass);
		if (index == null) {
			index = new ComponentIndex();
			componentIndexMapper.put(componentClass, index);
		}
		return index;
	}
	
	public int getValue() {
		return index;
	}
}
