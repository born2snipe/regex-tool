package b2s.regex;

import java.util.List;

import b2s.regex.LineSplitter.Line;

import junit.framework.TestCase;

public class LineSplitterTest extends TestCase {
	private LineSplitter splitter;
	
	public void setUp() {
		splitter = new LineSplitter();
	}
	
	public void test_multipleLines_endingLineHasNoLineEnding_CRLF() {
		List<Line> lines = splitter.split("1\r\n2");
		
		assertEquals(2, lines.size());
		assertLine(lines.get(0), "1", "\r\n");
		assertLine(lines.get(1), "2", "");
	}
	
	public void test_multipleLines_CRLF() {
		List<Line> lines = splitter.split("1\r\n2\r\n");
		
		assertEquals(2, lines.size());
		assertLine(lines.get(0), "1", "\r\n");
		assertLine(lines.get(1), "2", "\r\n");
	}
	
	public void test_singleLine_CRLF() {
		List<Line> lines = splitter.split("1\r\n");
		
		assertEquals(1, lines.size());
		Line line = lines.get(0);
		assertLine(line, "1", "\r\n");
	}
	
	public void test_singleLine_LF() {
		List<Line> lines = splitter.split("1\n");
		
		assertEquals(1, lines.size());
		assertLine(lines.get(0), "1", "\n");
	}
	
	public void test_multipleLines_LF() {
		List<Line> lines = splitter.split("1\n2\n");
		
		assertEquals(2, lines.size());
		assertLine(lines.get(0), "1", "\n");
		assertLine(lines.get(1), "2", "\n");
	}
	
	public void test_valueWithNoLines() {
		List<Line> lines = splitter.split("1");
		
		assertEquals(1, lines.size());
		Line line = lines.get(0);
		assertLine(line, "1", "");
	}

	private void assertLine(Line line, String expectedContent, String expectedLineEnding) {
		assertEquals(expectedContent, line.content);
		assertEquals(expectedLineEnding, line.lineEnding);
	}
}
