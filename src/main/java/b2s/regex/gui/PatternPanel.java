
package b2s.regex.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JTextField;
import b2s.regex.PatternChangedManager;
import javax.swing.BorderFactory;

public class PatternPanel extends JPanel {
    private final JTextField patternField = new JTextField();

    public PatternPanel(final PatternChangedManager patternChangedManager) {
        super(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Expression"));
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
                                    patternChangedManager.patternChanged(Pattern.compile(lastPattern));
                                } catch (java.util.regex.PatternSyntaxException e) {
                                    patternChangedManager.badPattern(lastPattern);
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

        
        patternChangedManager.addListener(new PatternChangedManager.PatternChangeListener() {

            public void patternChanged(Pattern newRegexPattern) {
                SwingUtil.invokeLater(new Runnable(){
                    public void run() {
                        patternField.setForeground(Color.black);
                    }
                });
            }

            public void badPattern(String pattern) {
                SwingUtil.invokeLater(new Runnable(){
                    public void run() {
                        patternField.setForeground(Color.red);
                    }
                });
            }
        });
    }
}
