import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DifficultFrame extends HitMe {

    static JButton easy, medium, hard;
    static JLabel choose;
    static JFrame gameFrame;
    static HitMe gamePanel;
    static Thread gameThread;
    static Timer timer;
    static int countdown = 10;
    static int level;

    public DifficultFrame() {
        initializeDifficultyFrame();
    }

    private void initializeDifficultyFrame() {
        JFrame diframe = new JFrame("CHOOSE DIFFICULTY");
        diframe.setSize(720, 500);
        diframe.setBounds(400, 160, 720, 500);

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\PC\\Desktop\\Hitme\\images\\ns.jpg");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        diframe.setContentPane(backgroundPanel);

        choose = new JLabel("CHOOSE  THE  DIFFICULTY ");
        choose.setFont(new Font("Snap ITC", Font.BOLD, 32));
        choose.setForeground(Color.magenta);
        choose.setBounds(80, 40, 630, 50);
        diframe.add(choose);

        easy = createDifficultyButton("EASY", 120, diframe, 1200);
        medium = createDifficultyButton("MEDIUM", 220, diframe, 900);
        hard = createDifficultyButton("HARD", 325, diframe, 600);

        diframe.setLayout(null);
        diframe.setVisible(true);
        diframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JButton createDifficultyButton(String text, int yPosition, JFrame diframe, int sleepTime) {
        JButton button = new JButton(text);
        button.setFont(new Font("Snap ITC", Font.BOLD, 32));
        button.setBackground(getButtonColor(text));
        button.setBounds(200, yPosition, 300, 70);
        button.setForeground(Color.BLACK);

        button.addActionListener(e -> {
            level = text.equals("EASY") ? 1 : text.equals("MEDIUM") ? 2 : 3;
            startNewGame(sleepTime);
            diframe.dispose();
        });

        diframe.add(button);
        return button;
    }

    private Color getButtonColor(String text) {
        switch (text) {
            case "EASY":
                return new Color(71, 230, 227);
            case "MEDIUM":
                return new Color(73, 62, 189);
            case "HARD":
                return new Color(230, 73, 81);
            default:
                return Color.BLACK;
        }
    }

    private void startNewGame(int sleepTime) {
        // Stop any existing game thread and timer
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        if (timer != null) {
            timer.cancel();
        }

        // Reset game state
        gamePanel = new HitMe();
        gamePanel.hitCount = 0;
        gamePanel.missCount = 0;
        gamePanel.boxPositionX = 0;
        gamePanel.boxPositionY = 0;
        countdown = 10;

        // Initialize the game frame
        gameFrame = new JFrame("Hit-Miss Game");
        gameFrame.setBounds(400, 160, 720, 500);

        JLabel timerLabel = new JLabel("Time left: " + countdown + " seconds");
        timerLabel.setFont(new Font("Snap ITC", Font.BOLD, 24));
        timerLabel.setForeground(Color.BLACK);
        gamePanel.add(timerLabel);

        gamePanel.setBackground(new Color(68, 73, 173));
        gamePanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        gameFrame.getContentPane().add(gamePanel);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setVisible(true);

        // Start the new game thread
        gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                gamePanel.boxPositionX = 0;
                gamePanel.boxPositionY = 0;
                gamePanel.repaint();
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();

        // Start the countdown timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countdown--;
                if (countdown >= 0) {
                    timerLabel.setText("Time left: " + countdown + " seconds");
                } else {
                    timer.cancel();
                    new FinalFrame(gamePanel.hitCount, gamePanel.missCount, level);
                    gameFrame.dispose();
                }
            }
        }, 1000, 1000);
    }

    public static void main(String[] args) {
        new DifficultFrame();
    }
}
