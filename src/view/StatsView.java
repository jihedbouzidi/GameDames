package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StatsView extends JFrame {
    private JLabel playerNameLabel;
    private JLabel gamesPlayedLabel;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel drawsLabel;
    private JButton closeButton;

    public StatsView() {
        setTitle("Statistiques du Joueur");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        // Main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(20, 30, 48);
                Color color2 = new Color(36, 59, 85);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title with animation
        JLabel titleLabel = new JLabel("Statistiques du Joueur", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 255, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Animate title
        Timer titleTimer = new Timer(50, e -> {
            float hue = (System.currentTimeMillis() % 5000) / 5000f;
            titleLabel.setForeground(Color.getHSBColor(hue, 0.7f, 0.9f));
        });
        titleTimer.start();

        // Stats labels with modern style
        Font statLabelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font statValueFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(200, 200, 200);
        Color valueColor = Color.WHITE;

        // Player name
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel nameLabel = createStatLabel("Joueur:");
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        playerNameLabel = createStatValueLabel();
        panel.add(playerNameLabel, gbc);

        // Games played
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createStatLabel("Parties jouées:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gamesPlayedLabel = createStatValueLabel();
        panel.add(gamesPlayedLabel, gbc);

        // Wins
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createStatLabel("Victoires:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        winsLabel = createStatValueLabel();
        panel.add(winsLabel, gbc);

        // Losses
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createStatLabel("Défaites:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        lossesLabel = createStatValueLabel();
        panel.add(lossesLabel, gbc);

        // Draws
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createStatLabel("Matchs nuls:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        drawsLabel = createStatValueLabel();
        panel.add(drawsLabel, gbc);

        // Close button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        closeButton = createStyledButton("Fermer", new Color(33, 150, 243));
        addButtonHoverEffect(closeButton);
        panel.add(closeButton, gbc);

        add(panel);
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(200, 200, 200));
        return label;
    }

    private JLabel createStatValueLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void addButtonHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, originalColor, originalColor.brighter(), 200);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, originalColor.brighter(), originalColor, 200);
            }
        });
    }

    private void animateButtonColor(JButton button, Color from, Color to, int duration) {
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < duration) {
                    float progress = (float)(System.currentTimeMillis() - startTime) / duration;
                    int red = (int)(from.getRed() + (to.getRed() - from.getRed()) * progress);
                    int green = (int)(from.getGreen() + (to.getGreen() - from.getGreen()) * progress);
                    int blue = (int)(from.getBlue() + (to.getBlue() - from.getBlue()) * progress);
                    button.setBackground(new Color(red, green, blue));
                    button.repaint();
                    Thread.sleep(10);
                }
                button.setBackground(to);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setPlayerName(String name) {
        playerNameLabel.setText(name);
    }

    public void setGamesPlayed(int count) {
        gamesPlayedLabel.setText(String.valueOf(count));
    }

    public void setWins(int count) {
        winsLabel.setText(String.valueOf(count));
    }

    public void setLosses(int count) {
        lossesLabel.setText(String.valueOf(count));
    }

    public void setDraws(int count) {
        drawsLabel.setText(String.valueOf(count));
    }

    public void addCloseListener(ActionListener listener) {
        closeButton.addActionListener(listener);
    }
}