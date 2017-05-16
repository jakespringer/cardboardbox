package ecs;

public final class Component<T> {
	public T value;
	
	public Component(T initialValue) {
		this.value = initialValue;
	}
	
	public Component() {
		this(null);
	}
}
