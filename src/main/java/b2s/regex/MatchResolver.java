package b2s.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingWorker;

public class MatchResolver implements PatternChangedManager.PatternChangeListener, DataChangedManager.Handler {
    private String data;
    private Pattern pattern;

    public void patternChanged(Pattern newRegexPattern) {
        resolve(newRegexPattern, data);
    }

    public void badPattern(String pattern) {
        ResultsChangedManager.instance().clearResults();
    }

    public void dataChanged(String data) {
        resolve(pattern, data);
    }

    private synchronized void resolve(final Pattern pattern, String data) {
        ResultsChangedManager.instance().clearResults();
        this.pattern = pattern;
        this.data = data;

        if (data == null || pattern == null) {
            return;
        }

        final List<String> lines = new ArrayList<String>();
        if ((pattern.flags() & Pattern.MULTILINE) == Pattern.MULTILINE) {
            lines.add(data);
        } else {
            lines.addAll(Arrays.asList(data.split("\r\n|\n")));
        }

        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (String line : lines) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        Match match = new Match();
                        match.add(new Match.Group(matcher.group(0), matcher.start(0), matcher.end(0)));
                        for (int i=1;i<=matcher.groupCount();i++) {
                            match.add(new Match.Group(matcher.group(i), matcher.start(i), matcher.end(i)));
                        }
                        ResultsChangedManager.instance().addMatch(match);
                    }
                }

                return null;
            }
        };
        worker.execute();
        
    }

}
