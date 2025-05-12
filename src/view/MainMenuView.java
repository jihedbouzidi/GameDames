package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainMenuView extends JFrame {
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton statsButton;
    private JButton profileButton;
    private JButton quitButton;

    public MainMenuView() {
        setTitle("Jeu de Dames - Menu Principal");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JLabel titleLabel = new JLabel("Jeu de Dames", SwingConstants.CENTER);
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

        // Buttons panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(40, 150, 40, 150)); // Réduit les marges latérales pour plus d'espace

        // Création des icônes (taille ajustée)
        ImageIcon newGameIcon = new ImageIcon(new ImageIcon("•").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon loadGameIcon = new ImageIcon(new ImageIcon("#").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon statsIcon = new ImageIcon(new ImageIcon("€").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon quitIcon = new ImageIcon(new ImageIcon("#").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        newGameButton = createStyledButton("Nouvelle Partie", new Color(76, 175, 80), newGameIcon);
        loadGameButton = createStyledButton("Reprendre une Partie", new Color(33, 150, 243), loadGameIcon);
        statsButton = createStyledButton("Statistiques", new Color(156, 39, 176), statsIcon);
        profileButton = createStyledButton("Modifier Profil", new Color(255, 152, 0), profileIcon);
        quitButton = createQuitButton("Quitter", quitIcon);

        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(profileButton);
        buttonPanel.add(quitButton);

        panel.add(buttonPanel, gbc);

        // Add hover animation to buttons
        addButtonHoverEffect(newGameButton);
        addButtonHoverEffect(loadGameButton);
        addButtonHoverEffect(statsButton);
        addButtonHoverEffect(profileButton);
        addButtonHoverEffect(quitButton);

        add(panel);
    }

    private JButton createQuitButton(String text, ImageIcon icon) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(244, 67, 54)); // Rouge vif
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 50)); // Boutons plus larges et plus hauts
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(20);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> System.exit(0));
        
        return button;
    }

    private JButton createStyledButton(String text, Color bgColor, ImageIcon icon) {
        JButton button = new JButton(text, icon) {
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 50)); // Boutons plus larges et plus hauts
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(20);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void addButtonHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, originalColor, originalColor.brighter(), 200);
                button.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Texte légèrement agrandi au survol
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, originalColor.brighter(), originalColor, 200);
                button.setFont(new Font("Segoe UI", Font.BOLD, 16));
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

    // Les méthodes addListener et showMessage restent identiques
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    public void addLoadGameListener(ActionListener listener) {
        loadGameButton.addActionListener(listener);
    }

    public void addStatsListener(ActionListener listener) {
        statsButton.addActionListener(listener);
    }

    public void addProfileListener(ActionListener listener) {
        profileButton.addActionListener(listener);
    }

    public void addQuitListener(ActionListener listener) {
        quitButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }
}