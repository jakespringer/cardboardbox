package ecs;

public class ComponentMapper<T extends Component> {
	private final int componentIndex;
	
	public static <J extends Component> ComponentMapper<J> getFor(Class<J> componentClass) {
		return new ComponentMapper<J>(componentClass);
	}
	
	private ComponentMapper(Class<T> componentClass) {
		componentIndex = ComponentIndex.getFor(componentClass).getValue();
	}
	
	@SuppressWarnings("unchecked")
	public T get(Entity entity) {
		return (T) entity.componentArray.get(componentIndex);
	}
}
