package b2s.regex;

import java.util.List;
import java.util.ArrayList;


public class SelectionChangedManager {
    private static final SelectionChangedManager instance = new SelectionChangedManager();
    private List<Handler> handlers = new ArrayList<Handler>();

    public static SelectionChangedManager instance() {
        return instance;
    }

    private SelectionChangedManager(){

    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void select(Match.Group group) {
        for (Handler handler : handlers) {
            handler.select(group);
        }
    }

    public void clear() {
        for (Handler handler : handlers) {
            handler.clearSelection();
        }
    }

    public static interface Handler {
        void clearSelection();
        void select(Match.Group group);
    }
}
