package org.example.Client.display;

import org.example.Client.GameClient;
import org.example.CommonFiles.Statistics;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static org.example.Client.GameClient.timeFormat;

public class StartOverlay extends JPanel {

    private final JTextField nameField = new JTextField();
    private final DefaultTableModel tableModel;
    public boolean isRestart = false;
    public String nickname = "Tank.io";
    public Statistics statistics;

    public StartOverlay(GameClient client, Statistics ps) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        setFocusable(false);
        setFocusTraversalKeysEnabled(false);
        if (ps != null) {
            this.statistics = ps;
            System.out.println(ps.nickname);
            nickname = ps.nickname;
            isRestart = true;
        }
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(0, 0, 0, 0));
        if (isRestart) {
            card.setPreferredSize(new Dimension(500, 418));
        } else {
            card.setPreferredSize(new Dimension(500, 320));
        }

        tableModel = new DefaultTableModel(
                new String[]{"Nickname", "Date", "XP", "Lifetime", "Kills", "Orbs"}, 0
        );

        JTable table = new JTable(tableModel);
        table.setOpaque(false);
        ((JComponent) table.getDefaultRenderer(Object.class)).setOpaque(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.WHITE);
                c.setBackground(isSelected ? Color.BLUE : new Color(0, 0, 0, 180));
                return c;
            }
        });

        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.WHITE);
                c.setBackground(new Color(0, 0, 0, 180));
                return c;
            }
        });

        nameField.setForeground(Color.white);
        nameField.setBackground(new Color(0, 0, 0, 180));
        nameField.setCaretColor(Color.white);
        nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        nameField.setText(nickname);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel label = new JLabel("Enter nickname to play:   ");
        label.setForeground(Color.WHITE);
        topPanel.add(label, BorderLayout.WEST);
        topPanel.add(nameField, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0, 0, 0, 180));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            client.startWithName(nameField.getText());
            setVisible(false);
        });

        JLabel tableLabel = new JLabel("Top 10 best players");
        tableLabel.setForeground(Color.WHITE);
        tableLabel.setFont(new Font("Arial", Font.BOLD, 25));
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultsLabel = null;
        JLabel resultsLabel2 = null;
        if (isRestart) {
            resultsLabel = new JLabel("Score: " + statistics.level + "    Time survived: "
                    + timeFormat(statistics.lifetime));
            resultsLabel.setForeground(Color.WHITE);
            resultsLabel.setFont(new Font("Arial", Font.BOLD, 25));
            resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsLabel2 = new JLabel("Kills: " + statistics.kills + "    Orbs destroyed: " + statistics.orbs);
            resultsLabel2.setForeground(Color.WHITE);
            resultsLabel2.setFont(new Font("Arial", Font.BOLD, 25));
            resultsLabel2.setHorizontalAlignment(SwingConstants.CENTER);
            resultsLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.setOpaque(false);

        if (isRestart) {
            verticalPanel.add(resultsLabel);
            verticalPanel.add(Box.createVerticalStrut(10));
            verticalPanel.add(resultsLabel2);
            verticalPanel.add(Box.createVerticalStrut(30));
        }
        verticalPanel.add(topPanel);
        verticalPanel.add(Box.createVerticalStrut(10));
        verticalPanel.add(startButton);
        verticalPanel.add(Box.createVerticalStrut(30));
        verticalPanel.add(tableLabel);
        verticalPanel.add(Box.createVerticalStrut(10));
        verticalPanel.add(scroll);

        card.add(verticalPanel, BorderLayout.CENTER);
        add(card);
    }

    public void updateTop(List<Statistics> stats) {
        tableModel.setRowCount(0);

        for (Statistics s : stats) {
            tableModel.addRow(new Object[]{
                    s.nickname,
                    s.date,
                    s.level,
                    timeFormat(s.lifetime),
                    s.kills,
                    s.orbs
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
