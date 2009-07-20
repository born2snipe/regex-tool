package b2s.regex.gui;

import b2s.regex.DataChangedManager;
import b2s.regex.Match;
import b2s.regex.SelectionChangedManager;

import javax.swing.*;
import java.awt.*;

public class DataPanel extends JPanel {
    private JEditorPane editorPane;

    public DataPanel(DataChangedManager dataChangedManager) {
        super(new BorderLayout());
        editorPane = new JEditorPane();
        add(new JLabel("Data:"), BorderLayout.NORTH);
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        final Thread thread = new Thread(new Runnable() {
            private String previousContent = "";

            public void run() {
                while (true) {
                    synchronized (editorPane) {
                        if (!editorPane.getText().equals(previousContent)) {
                            previousContent = editorPane.getText();
                            if (previousContent.trim().length() > 0) {
                                DataChangedManager.instance().dataChanged(editorPane.getText());
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

        SelectionChangedManager.instance().addHandler(new SelectionChangedManager.Handler() {
            public void clearSelection() {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        editorPane.select(editorPane.getCaretPosition(), editorPane.getCaretPosition());
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
