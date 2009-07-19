package b2s.regex.gui;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import b2s.regex.DataChangedManager;
import b2s.regex.SelectionChangedManager;
import b2s.regex.Match;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class DataPanel extends JPanel {
    private JEditorPane editorPane;

    public DataPanel(DataChangedManager dataChangedManager) {
        super(new BorderLayout());
        editorPane = new JEditorPane();
        add(new JLabel("Data:"), BorderLayout.NORTH);
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        editorPane.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN
                        || event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    return;
                }

                DataChangedManager.instance().dataChanged(editorPane.getText());
            }
        });

        SelectionChangedManager.instance().addHandler(new SelectionChangedManager.Handler(){
            public void clearSelection() {
                SwingUtil.invokeLater(new Runnable(){
                    public void run() {
                        editorPane.select(0, 0);
                    }
                });
            }

            public void select(final Match.Group group) {
                SwingUtil.invokeLater(new Runnable(){
                    public void run() {
                        editorPane.select(group.getPosition(), group.getEnd());
                    }
                });
            }
        });
    }
}
