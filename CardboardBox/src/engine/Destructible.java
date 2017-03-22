package engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public abstract class Destructible {

    private static final Stack<Destructible> STACK = new Stack<>();

    private Destructible parent;
    private final Set<Destructible> children = new HashSet<>();

    public Destructible() {
        if (!STACK.isEmpty()) {
            setParent(STACK.peek());
        }
    }

    /**
     * Creates the destructible. All other destructibles created inside its
     * create method will be added as children to this destructible.
     */
    public void create() {
        STACK.push(this);
        createInner();
        STACK.pop();
    }

    /**
     * This method is overridden to determine how the destructible is created.
     */
    protected void createInner() {
    }

    /**
     * Destroys this destructible. It will destroy all the children of this
     * destructible. It will remove this destructible as a child from its
     * parent.
     */
    public void destroy() {
        destroyInner();
        setParent(null);
        new ArrayList<>(children).forEach(Destructible::destroy);
    }

    /**
     * This method is overridden to determine how the destructible is destroyed.
     */
    protected void destroyInner() {
    }

    /**
     * Sets the parent of this destructible to the given value. Modifies the
     * children of both its past parent and its new parent to reflect the
     * change.
     *
     * @param d The new parent of this destructible.
     */
    public final void setParent(Destructible d) {
        if (parent != null) {
            parent.children.remove(this);
        }
        parent = d;
        if (parent != null) {
            parent.children.add(this);
        }
    }
}
