package b2s.regex.gui;

import b2s.regex.DataChangedManager;
import b2s.regex.Match;
import b2s.regex.SelectionChangedManager;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;

public class DataPanel extends JPanel {
    private JEditorPane editorPane;
    private static final MyHighlightPainter PAINTER = new MyHighlightPainter();

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
                        clearHighlighters();
                    }
                });
            }

            public void select(final Match.Group group) {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        clearHighlighters();
                        addHighlighter(group);
                        editorPane.setCaretPosition(group.getPosition());
                    }
                });
            }
        });
    }

    private void addHighlighter(Match.Group group) {
        try {
            editorPane.getHighlighter().addHighlight(group.getPosition(), group.getEnd(), PAINTER);
        } catch (BadLocationException e) {
        }
    }

    private void clearHighlighters() {
        Highlighter hilite = editorPane.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }

    }

    private static class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter() {
            super(Color.green.brighter().brighter());
        }
    }

}
