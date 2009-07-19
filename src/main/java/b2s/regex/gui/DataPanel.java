package b2s.regex.gui;

import b2s.regex.DataChangedManager;
import b2s.regex.Match;
import b2s.regex.SelectionChangedManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DataPanel extends JPanel {
    private JEditorPane editorPane;

    public DataPanel(DataChangedManager dataChangedManager) {
        super(new BorderLayout());
        editorPane = new JEditorPane();
        add(new JLabel("Data:"), BorderLayout.NORTH);
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        editorPane.addKeyListener(new KeyAdapter() {
            private String previousContent;

            public void keyReleased(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_SHIFT:
                    case KeyEvent.VK_META:
                        return;
                }
                if (previousContent != null) {
                    if (previousContent.equals(editorPane.getText())) {
                        return;
                    }
                }
                previousContent = editorPane.getText();
                DataChangedManager.instance().dataChanged(editorPane.getText());
            }
        });

        SelectionChangedManager.instance().addHandler(new SelectionChangedManager.Handler() {
            public void clearSelection() {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        editorPane.select(0, 0);
                    }
                });
            }

            public void select(final Match.Group group) {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        editorPane.select(group.getPosition(), group.getEnd());
                    }
                });
            }
        });
    }
}
