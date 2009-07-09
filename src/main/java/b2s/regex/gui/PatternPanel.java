
package b2s.regex.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PatternPanel extends JPanel {
    private final JTextField patternField = new JTextField();
    private List<PatternChangeListener> listeners = new ArrayList<PatternChangeListener>();

    public PatternPanel() {
        super(new BorderLayout());

        add(new JLabel("Pattern:"), BorderLayout.WEST);
        add(patternField, BorderLayout.CENTER);
        final Thread thread = new Thread(new Runnable(){
            private String lastPattern = "";

            public void run() {
                while (true) {
                    synchronized(patternField) {
                        if (!patternField.getText().equals(lastPattern)) {
                            lastPattern = patternField.getText();
                            if (lastPattern.trim().length() > 0) {
                                try {
                                    notifyListenersOfNewPattern(Pattern.compile(lastPattern));
                                } catch (java.util.regex.PatternSyntaxException e) {
                                    notifyListenersOfBaddPattern(lastPattern);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException err) {

                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        addPatternChangeListener(new PatternChangeListener() {

            public void patternChanged(Pattern newRegexPattern) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        patternField.setForeground(Color.black);
                    }
                });
            }

            public void badPattern(String pattern) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        patternField.setForeground(Color.red);
                    }
                });
            }
        });
    }

    private void notifyListenersOfNewPattern(Pattern pattern) {
        for (PatternChangeListener listener : listeners) {
            listener.patternChanged(pattern);
        }
    }

    private void notifyListenersOfBaddPattern(String pattern) {
        for (PatternChangeListener listener : listeners) {
            listener.badPattern(pattern);
        }
    }

    public void addPatternChangeListener(PatternChangeListener listener) {
        listeners.add(listener);
    }

    public static interface PatternChangeListener {
        void patternChanged(Pattern newRegexPattern);
        void badPattern(String pattern);
    }
}
