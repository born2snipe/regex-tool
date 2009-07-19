package b2s.regex;

import java.util.ArrayList;
import java.util.List;


public class DataChangedManager {
    private static final DataChangedManager instance = new DataChangedManager();
    private List<Handler> handlers = new ArrayList<Handler>();

    public static DataChangedManager instance() {
        return instance;
    }

    private DataChangedManager() {

    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void dataChanged(String data) {
        for (Handler handler : handlers) {
            handler.dataChanged(data);
        }
    }

    public static interface Handler {
        void dataChanged(String data);
    }
}
