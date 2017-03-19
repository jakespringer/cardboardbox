package engine;

import java.util.List;

public interface Activatable {

    public void activate();

    public void deactivate();

    public static void with(Runnable r, Activatable... as) {
        for (Activatable a : as) {
            a.activate();
        }
        r.run();
        for (Activatable a : as) {
            a.deactivate();
        }
    }

    public static void with(List<Activatable> as, Runnable r) {
        for (Activatable a : as) {
            a.activate();
        }
        r.run();
        for (Activatable a : as) {
            a.deactivate();
        }
    }
}
