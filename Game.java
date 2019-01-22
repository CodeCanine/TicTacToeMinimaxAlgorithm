import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    private Position position = new Position();
    private JButton[] buttons = new JButton[Position.SIZE];

    private JButton createButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(100, 100));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFont(new Font(null, Font.PLAIN, 80));
        add(button);
        return button;
    }

    private Game() {
        setLayout(new GridLayout(Position.DIM, Position.DIM));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        for (int i = 0; i < Position.SIZE; i++) {

            JButton button = createButton();
            buttons[i] = button;
            int index = i;

            button.addActionListener(e -> {
                button.setText(String.valueOf(position.turn));
                position.move(index);
                if (!position.isGameEnd()) {
                    int best = position.bestMove();
                    buttons[best].setText(String.valueOf(position.turn));
                    position.move(best);

                    buttons[index].setEnabled(false);
                    buttons[best].setEnabled(false);
                }
                if (position.isGameEnd()) {
                    String message = position.isWinFor('x') ? "You won!" : position.isWinFor('o') ? "Computer won!" : "Draw!";
                    int reply = JOptionPane.showConfirmDialog(null, message + " Retry?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {

                        new Game();
                        dispose();
                    } else {
                        System.exit(0);
                    }
                }
            });
        }
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Game();
    }


}
