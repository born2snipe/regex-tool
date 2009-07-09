package b2s.regex;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import b2s.regex.gui.PatternPanel;
import java.awt.BorderLayout;


public class App  extends JFrame {
    private PatternPanel patternPanel = new PatternPanel();

    public App() {
        super("Regex Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(patternPanel, BorderLayout.NORTH);
        setSize(640, 480);
    }

    public static void main( String[] args ) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
