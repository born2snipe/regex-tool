package b2s.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternChangedManager {
    private static final PatternChangedManager instance = new PatternChangedManager();
    private List<PatternChangeListener> listeners = new ArrayList<PatternChangeListener>();

    public static PatternChangedManager instance() {
        return instance;
    }

    private PatternChangedManager(){}

    public void patternChanged(Pattern newRegexPattern) {
        for (PatternChangeListener listener : listeners)
            listener.patternChanged(newRegexPattern);
    }

    public void badPattern(String pattern) {
        for (PatternChangeListener listener : listeners)
            listener.badPattern(pattern);
    }

    public void addListener(PatternChangeListener listener) {
        listeners.add(listener);
    }

    public static interface PatternChangeListener {
        void patternChanged(Pattern newRegexPattern);
        void badPattern(String pattern);
    }

}
