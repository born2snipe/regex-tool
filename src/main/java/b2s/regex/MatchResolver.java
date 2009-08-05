package b2s.regex;

import javax.swing.*;

import b2s.regex.LineSplitter.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchResolver implements PatternChangedManager.PatternChangeListener, DataChangedManager.Handler {
    private static final LineSplitter splitter = new LineSplitter();
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

        final List<Line> lines = new ArrayList<Line>();
        if ((pattern.flags() & Pattern.MULTILINE) == Pattern.MULTILINE) {

        } else {
            lines.addAll(splitter.split(data));
        }

        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                int offset = 0;

                for (Line line : lines) {
                    Matcher matcher = pattern.matcher(line.content);
                    while (matcher.find()) {
                        Match match = new Match();
                        match.add(new Match.Group(matcher.group(0), offset + matcher.start(0), offset + matcher.end(0)));
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            match.add(new Match.Group(matcher.group(i), offset + matcher.start(i), offset + matcher.end(i)));
                        }
                        ResultsChangedManager.instance().addMatch(match);
                    }
                    offset += line.content.length() + line.lineEnding.length();
                }

                return null;
            }
        };
        worker.execute();

    }


}
