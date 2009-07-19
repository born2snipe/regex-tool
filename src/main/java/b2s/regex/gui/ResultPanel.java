package b2s.regex.gui;

import b2s.regex.Match;
import b2s.regex.ResultsChangedManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ResultPanel extends JPanel {
    private JTable resultsTable;
    private ResultModel model = new ResultModel();

    public ResultPanel(ResultsChangedManager resultsChangedManager) {
        super(new BorderLayout());
        resultsTable = new JTable(model);
        add(new JLabel("Matches"), BorderLayout.NORTH);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        resultsChangedManager.addHandler(new ResultsChangedManager.Handler() {
            public void clearResults() {
                SwingUtil.invokeLater(new Runnable() {
                    public void run() {
                        model.updateNumberOfColumns(1);
                        model.clear();
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
        private int numberOfColumns = 6;

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
