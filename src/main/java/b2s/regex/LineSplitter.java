package b2s.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineSplitter {
	private static final Pattern PATTERN = Pattern.compile("(.+)(\r\n|\n|$)");
	
	public List<Line> split(String content) {
		List<Line> lines = new ArrayList<Line>();
		Matcher matcher = PATTERN.matcher(content);
		if (matcher.find())
			do {
				lines.add(new Line(matcher.group(1), matcher.group(2)));
			} while (matcher.find());
		else
			lines.add(new Line(content, null));
		return lines;
	}
	
	public static class Line {
        public final String content;
        public final String lineEnding;

        private Line(String content, String lineEnding) {
            this.content = content;
            this.lineEnding = lineEnding;
        }
    }
}
