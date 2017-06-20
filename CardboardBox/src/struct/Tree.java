package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Tree<T extends Object> {
	private Tree<T> parent;
	private List<Tree<T>> children;
	private T data;
	
	private Tree(T data, Tree<T> parent, List<Tree<T>> children) {
		this.data = data;
		this.parent = parent;
		this.children = children;
		children.forEach(c -> c.setParent(Tree.this));
	}
	
	public Tree(T data, List<Tree<T>> children) {
		this(data, null, new ArrayList<>(children));
	}
	
	@SafeVarargs
	public Tree(T data, Tree<T>... children) {
		this(data, Arrays.asList(children));
	}
	
	public Tree(T data) {
		this(data, null, new ArrayList<>());
	}
	
	public Tree() {
		this(null);
	}
	
	public List<Tree<T>> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	public Tree<T> getParent() {
		return parent;
	}
	
	public T getValue() {
		return data;
	}
	
	public void insert(Tree<T> child) {
		children.add(child);
	}
	
	public Tree<T> getChildAt(int index) {
		return children.get(index);
	}
	
	public void removeChildAt(int index) {
		children.remove(index);
	}
	
	public void removeAllChildren() {
		children.clear();
	}
	
	@Override
	public Tree<T> clone() {
		return mapAll(Function.identity());
	}
	
	private Tree<T> setParent(Tree<T> parent) {
		this.parent = parent;
		return this;
	}
	
	private Tree<T> setChildren(List<Tree<T>> children) {
		this.children = children;
		return this;
	}
		
	public <R> Tree<R> mapAll(Function<T, R> mappingFunction) {
		R mappedData = mappingFunction.apply(data);
		Tree<R> mappedTree = new Tree<R>(mappedData, null, null);
		List<Tree<R>> mappedChildren = children.stream()
				.map(c -> c.mapAll(mappingFunction).setParent(mappedTree))
				.collect(Collectors.toList());
		mappedTree.setChildren(mappedChildren);
		return mappedTree;
	}
	
	public <R> Tree<R> mapReduce(R immutableAccumulator, BiFunction<R, T, R> mapReductionFunction) {
		R accumulation = mapReductionFunction.apply(immutableAccumulator, data);
		Tree<R> mapReducedTree = new Tree<R>(accumulation, null, null);
		List<Tree<R>> mapReducedChildren = children.stream()
				.map(c -> c.mapReduce(accumulation, mapReductionFunction).setParent(mapReducedTree))
				.collect(Collectors.toList());
		mapReducedTree.setChildren(mapReducedChildren);
		return mapReducedTree;
	}
}
