package b2s.regex;

import java.util.ArrayList;
import java.util.List;

public class ResultsChangedManager {
    private static final ResultsChangedManager instance = new ResultsChangedManager();
    private List<Handler> handlers = new ArrayList<Handler>();
    
    public static ResultsChangedManager instance() {
        return instance;
    }

    private ResultsChangedManager(){

    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void addMatch(Match match) {
        for (Handler handler : handlers) {
            handler.addMatch(match);
        }
    }

    public void clearResults() {
        for (Handler handler : handlers) {
            handler.clearResults();
        }
    }

    public static interface Handler {
        void clearResults();
        void addMatch(Match match);
    }
}
