package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import javax.swing.*;
import model.Board;
import model.Piece;

public class GameView extends JFrame {
    private static final int CELL_SIZE = 60;
    private static final Color LIGHT_COLOR = new Color(240, 217, 181);
    private static final Color DARK_COLOR = new Color(181, 136, 99);
    
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JButton saveButton;
    private JButton quitButton;
    private int selectedRow = -1;
    private int selectedCol = -1;

    // Icônes pour les pièces
    private ImageIcon whitePawnIcon;
    private ImageIcon blackPawnIcon;
    private ImageIcon whiteQueenIcon;
    private ImageIcon blackQueenIcon;

    public GameView() {
        setTitle("Jeu de Dames");
        setSize(Board.SIZE * CELL_SIZE + 50, Board.SIZE * CELL_SIZE + 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Charger les icônes
        loadIcons();
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        boardPanel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));
        boardPanel.setPreferredSize(new Dimension(Board.SIZE * CELL_SIZE, Board.SIZE * CELL_SIZE));
        
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        saveButton = new JButton("Sauvegarder");
        quitButton = new JButton("Quitter");
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);
        
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadIcons() {
        try {
            // Création des icônes de base (cercles colorés)
            whitePawnIcon = createPieceIcon(Color.WHITE, false);
            blackPawnIcon = createPieceIcon(Color.BLACK, false);
            whiteQueenIcon = createPieceIcon(Color.WHITE, true);
            blackQueenIcon = createPieceIcon(Color.BLACK, true);
            
        } catch (Exception e) {
            System.err.println("Erreur de création des icônes : " + e.getMessage());
            // Solution de secours ultra simple
            whitePawnIcon = null;
            blackPawnIcon = null;
            whiteQueenIcon = null;
            blackQueenIcon = null;
        }
    }
    
    private ImageIcon createPieceIcon(Color color, boolean isQueen) {
        BufferedImage image = new BufferedImage(CELL_SIZE-10, CELL_SIZE-10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner le pion
        g2.setColor(color);
        g2.fillOval(2, 2, CELL_SIZE-14, CELL_SIZE-14);
        g2.setColor(color.darker());
        g2.drawOval(2, 2, CELL_SIZE-14, CELL_SIZE-14);
        
        // Ajouter une couronne si c'est une dame
        if (isQueen) {
            g2.setColor(color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String crown = color.equals(Color.WHITE) ? "♕" : "♛";
            g2.drawString(crown, (CELL_SIZE-10)/2-8, (CELL_SIZE-10)/2+8);
        }
        
        g2.dispose();
        return new ImageIcon(image);
    }
    
    public void drawBoard(Board board) {
        boardPanel.removeAll();
        
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                
                if (row == selectedRow && col == selectedCol) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                }
                
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    JLabel pieceLabel = new JLabel();
                    pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    if (piece instanceof model.Pawn) {
                        pieceLabel.setIcon(piece.getColor().equals("white") ? whitePawnIcon : blackPawnIcon);
                    } else if (piece instanceof model.Queen) {
                        pieceLabel.setIcon(piece.getColor().equals("white") ? whiteQueenIcon : blackQueenIcon);
                    }
                    
                    // Solution de secours si les icônes n'ont pas pu être créées
                    if (pieceLabel.getIcon() == null) {
                        pieceLabel.setOpaque(true);
                        pieceLabel.setBackground(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);
                        if (piece instanceof model.Queen) {
                            JLabel crown = new JLabel(piece.getColor().equals("white") ? "♕" : "♛", SwingConstants.CENTER);
                            crown.setFont(new Font("Arial", Font.PLAIN, 24));
                            crown.setForeground(piece.getColor().equals("white") ? Color.BLACK : Color.WHITE);
                            pieceLabel.setLayout(new BorderLayout());
                            pieceLabel.add(crown, BorderLayout.CENTER);
                        }
                    }
                    
                    cell.add(pieceLabel, BorderLayout.CENTER);
                }
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
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
        JOptionPane.showMessageDialog(this, message);
    }
}