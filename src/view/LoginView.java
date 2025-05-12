package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton quitButton; // Nouveau bouton Quitter

    public LoginView() {
        setTitle("Jeu de Dames - Connexion");
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
        JLabel titleLabel = new JLabel("Connexion");
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

        // Username field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel userLabel = new JLabel("Nom d'utilisateur:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 200, 200));
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        usernameField = createStyledTextField(15);
        panel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passLabel = new JLabel("Mot de passe:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(new Color(200, 200, 200));
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = createStyledPasswordField(15);
        panel.add(passwordField, gbc);

        // Buttons panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        loginButton = createStyledButton("Connexion", new Color(76, 175, 80));
        registerButton = createStyledButton("Inscription", new Color(33, 150, 243));
        quitButton = createQuitButton("Quitter"); // Création du bouton Quitter

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(quitButton); // Ajout du bouton Quitter
        
        panel.add(buttonPanel, gbc);

        // Add hover animation to buttons
        addButtonHoverEffect(loginButton);
        addButtonHoverEffect(registerButton);
        addButtonHoverEffect(quitButton);

        // Add focus animation to text fields
        addFieldFocusEffect(usernameField);
        addFieldFocusEffect(passwordField);

        add(panel);
    }

    private JButton createQuitButton(String text) {
        JButton button = new JButton(text) {
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> System.exit(0)); // Action pour quitter l'application
        
        return button;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(255, 255, 255, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 80)), 
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(255, 255, 255, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 80)), 
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
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

    private void addFieldFocusEffect(JComponent field) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                animateBorderColor(field, 
                    new Color(255, 255, 255, 80), 
                    new Color(33, 150, 243, 200), 
                    200);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                animateBorderColor(field, 
                    new Color(33, 150, 243, 200), 
                    new Color(255, 255, 255, 80), 
                    200);
            }
        });
    }

    private void animateBorderColor(JComponent field, Color from, Color to, int duration) {
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < duration) {
                    float progress = (float)(System.currentTimeMillis() - startTime) / duration;
                    int red = (int)(from.getRed() + (to.getRed() - from.getRed()) * progress);
                    int green = (int)(from.getGreen() + (to.getGreen() - from.getGreen()) * progress);
                    int blue = (int)(from.getBlue() + (to.getBlue() - from.getBlue()) * progress);
                    int alpha = (int)(from.getAlpha() + (to.getAlpha() - from.getAlpha()) * progress);
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(red, green, blue, alpha)), 
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                    ));
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}