package b2s.regex;

import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import b2s.regex.gui.PatternPanel;
import b2s.regex.gui.DataPanel;
import b2s.regex.gui.ResultPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;


public class App  extends JFrame {
    private PatternPanel patternPanel = new PatternPanel(PatternChangedManager.instance());
    private DataPanel dataPanel = new DataPanel(DataChangedManager.instance());
    private ResultPanel resultPanel = new ResultPanel(ResultsChangedManager.instance());
    private MatchResolver matchResolver = new MatchResolver();

    public App() {
        super("Regex Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(patternPanel, BorderLayout.NORTH);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dataPanel, resultPanel);
        splitPane.setDividerLocation(200);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        setSize(800, 480);

        PatternChangedManager.instance().addListener(matchResolver);
        DataChangedManager.instance().addHandler(matchResolver);
    }

    public static void main( String[] args ) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                new App().setVisible(true);
            }
        });
    }


}
