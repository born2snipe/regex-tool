package b2s.regex.gui;

import b2s.regex.Match;
import b2s.regex.ResultsChangedManager;
import b2s.regex.SelectionChangedManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ResultPanel extends JPanel {
    private JTable resultsTable;
    private ResultModel model = new ResultModel();
    private final SelectionChangedManager selectionChangedManager = SelectionChangedManager.instance();

    public ResultPanel(ResultsChangedManager resultsChangedManager) {
        super(new BorderLayout());
        resultsTable = new JTable(model);
        add(new JLabel("Matches"), BorderLayout.NORTH);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        resultsTable.setColumnSelectionAllowed(true);
        resultsTable.setRowSelectionAllowed(true);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        ListSelectionListener selectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int col = resultsTable.getSelectedColumn();
                int row = resultsTable.getSelectedRow();
                if (col == 0 || col == -1 || row == -1) {
                    return;
                }
                Match.Group group = model.get(row, col - 1);
                selectionChangedManager.select(group);
            }
        };

        resultsTable.getSelectionModel().addListSelectionListener(selectionListener);
        resultsTable.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

        resultsChangedManager.addHandler(new ResultsChangedManager.Handler() {
            public void clearResults() {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        model.updateNumberOfColumns(1);
                        model.clear();
                        selectionChangedManager.clear();
                    }
                });
            }

            public void addMatch(final Match match) {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        if (match.getGroupCount() > 0) {
                            model.updateNumberOfColumns(match.getGroupCount() + 1);
                        }
                        model.addMatch(match);
                    }
                });
            }
        });
    }

    private static class ResultModel extends AbstractTableModel {
        private List<Match> matches = new ArrayList<Match>();
        private int numberOfColumns = 1;

        public Match.Group get(int row, int group) {
            return matches.get(row).get(group);
        }

        public int getRowCount() {
            return matches.size();
        }

        public int getColumnCount() {
            return numberOfColumns;
        }

        @Override
        public String getColumnName(int column) {
            if (column >= 1) {
                return "Group " + (column - 1);
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return rowIndex + 1;
            }
            final Match match = matches.get(rowIndex);
            if (columnIndex - 1 < match.getGroupCount()) {
                return match.get(columnIndex - 1).getContent();
            }
            return "";
        }

        public void addMatch(Match match) {
            matches.add(match);
            fireTableDataChanged();
        }

        public void clear() {
            matches.clear();
            fireTableDataChanged();
        }

        public void updateNumberOfColumns(int columns) {
            numberOfColumns = columns;
            fireTableStructureChanged();
        }
    }

}
