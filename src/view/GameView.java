package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import model.Board;
import model.Piece;

public class GameView extends JFrame {
    private static final int CELL_SIZE = 70;
    private static final Color LIGHT_COLOR = new Color(232, 235, 239);
    private static final Color DARK_COLOR = new Color(66, 92, 128);
    private static final Color HIGHLIGHT_COLOR = new Color(100, 255, 100, 100);
    private static final Color SELECTED_COLOR = new Color(255, 193, 7, 150);
    private static final Color BACKGROUND_COLOR1 = new Color(20, 30, 48);
    private static final Color BACKGROUND_COLOR2 = new Color(36, 59, 85);
    
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JButton saveButton;
    private JButton quitButton;
    private JButton menuButton;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean[][] possibleMoves = new boolean[Board.SIZE][Board.SIZE];

    private ImageIcon whitePawnIcon;
    private ImageIcon blackPawnIcon;
    private ImageIcon whiteQueenIcon;
    private ImageIcon blackQueenIcon;

    public GameView() {
        initWindow();
        loadIcons();
        initComponents();
        setupModernStyle();
    }
    
    private void initWindow() {
        setTitle("Jeu de Dames - Modern");
        setSize(Board.SIZE * CELL_SIZE + 100, Board.SIZE * CELL_SIZE + 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void setupModernStyle() {
        try {
            setUndecorated(true);
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        } catch (Exception e) {
            System.err.println("Les bordures arrondies ne sont pas supportées : " + e.getMessage());
            setUndecorated(false);
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR1, getWidth(), getHeight(), BACKGROUND_COLOR2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        createBoardPanel();
        createStatusLabel();
        createButtonPanel();
        
        mainPanel.add(createShadowPanel(boardPanel), BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (selectedRow != -1 && selectedCol != -1) {
                    for (int row = 0; row < Board.SIZE; row++) {
                        for (int col = 0; col < Board.SIZE; col++) {
                            if (possibleMoves[row][col]) {
                                g2d.setColor(HIGHLIGHT_COLOR);
                                g2d.fillRoundRect(col * CELL_SIZE + 2, row * CELL_SIZE + 2, 
                                              CELL_SIZE - 4, CELL_SIZE - 4, 10, 10);
                            }
                        }
                    }
                }
                g2d.dispose();
            }
        };
        boardPanel.setOpaque(false);
        boardPanel.setPreferredSize(new Dimension(Board.SIZE * CELL_SIZE, Board.SIZE * CELL_SIZE));
    }
    
    private void createStatusLabel() {
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        try {
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        } catch (Exception e) {
            statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        }
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        saveButton = createStyledButton("Sauvegarder", new Color(33, 150, 243));
        menuButton = createStyledButton("Menu", new Color(156, 39, 176));
        quitButton = createStyledButton("Quitter", new Color(244, 67, 54));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(menuButton);
        buttonPanel.add(quitButton);
        
        return buttonPanel;
    }
    
    private JPanel createShadowPanel(JComponent component) {
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
        shadowPanel.add(component);
        return shadowPanel;
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
        };
        
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 45));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 17));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 16));
            }
        });
        
        return button;
    }
    
    private void loadIcons() {
        try {
            whitePawnIcon = createPieceIcon(new Color(245, 245, 245), false);
            blackPawnIcon = createPieceIcon(new Color(33, 33, 33), false);
            whiteQueenIcon = createPieceIcon(new Color(245, 245, 245), true);
            blackQueenIcon = createPieceIcon(new Color(33, 33, 33), true);
        } catch (Exception e) {
            System.err.println("Erreur création icônes : " + e.getMessage());
            whitePawnIcon = blackPawnIcon = whiteQueenIcon = blackQueenIcon = null;
        }
    }
    
    private ImageIcon createPieceIcon(Color color, boolean isQueen) {
        BufferedImage image = new BufferedImage(CELL_SIZE-10, CELL_SIZE-10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Cercle avec gradient
        GradientPaint gradient = new GradientPaint(
            5, 5, color.brighter(),
            CELL_SIZE-15, CELL_SIZE-15, color.darker());
        g2.setPaint(gradient);
        g2.fillOval(5, 5, CELL_SIZE-20, CELL_SIZE-20);
        
        // Bordure
        g2.setColor(color.darker().darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(5, 5, CELL_SIZE-20, CELL_SIZE-20);
        
        // Couronne pour dame
        if (isQueen) {
            g2.setColor(color.equals(Color.WHITE) ? new Color(33, 33, 33) : new Color(245, 245, 245));
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            String crown = "♛";
            FontMetrics fm = g2.getFontMetrics();
            int x = (CELL_SIZE-10 - fm.stringWidth(crown)) / 2;
            int y = ((CELL_SIZE-10 - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(crown, x, y);
        }
        
        g2.dispose();
        return new ImageIcon(image);
    }
    
    public void drawBoard(Board board) {
        boardPanel.removeAll();
        possibleMoves = new boolean[Board.SIZE][Board.SIZE];
        
        if (selectedRow != -1 && selectedCol != -1) {
            Piece selectedPiece = board.getPiece(selectedRow, selectedCol);
            if (selectedPiece != null) {
                for (int row = 0; row < Board.SIZE; row++) {
                    for (int col = 0; col < Board.SIZE; col++) {
                        if (selectedPiece.isValidMove(row, col, board) || 
                            selectedPiece.canCapture(row, col, board)) {
                            possibleMoves[row][col] = true;
                        }
                    }
                }
            }
        }
        
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                JPanel cell = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    }
                };
                cell.setOpaque(false);
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                
                if (row == selectedRow && col == selectedCol) {
                    cell.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 3));
                }
                
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    JLabel pieceLabel = new JLabel();
                    pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    ImageIcon icon = null;
                    if (piece instanceof model.Pawn) {
                        icon = piece.getColor().equals("white") ? whitePawnIcon : blackPawnIcon;
                    } else if (piece instanceof model.Queen) {
                        icon = piece.getColor().equals("white") ? whiteQueenIcon : blackQueenIcon;
                    }
                    
                    if (icon != null) {
                        pieceLabel.setIcon(icon);
                        cell.add(pieceLabel, BorderLayout.CENTER);
                    } else {
                        // Fallback textuel
                        String symbol = piece instanceof model.Queen ? 
                            (piece.getColor().equals("white") ? "♕" : "♛") : 
                            (piece.getColor().equals("white") ? "●" : "◉");
                        JLabel fallback = new JLabel(symbol, SwingConstants.CENTER);
                        fallback.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
                        fallback.setForeground(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);
                        cell.add(fallback, BorderLayout.CENTER);
                    }
                }
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
    // Méthodes d'accès
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
    
    public void addMenuListener(ActionListener listener) {
        menuButton.addActionListener(listener);
    }
    
    public void addQuitListener(ActionListener listener) {
        quitButton.addActionListener(listener);
    }
    
    public void addBoardClickListener(MouseAdapter adapter) {
        boardPanel.addMouseListener(adapter);
    }
    
    public int getCellSize() { 
        return CELL_SIZE; 
    }
    
    public void setSelected(int row, int col) { 
        selectedRow = row; 
        selectedCol = col; 
    }
    
    public void resetSelection() { 
        selectedRow = -1; 
        selectedCol = -1; 
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}