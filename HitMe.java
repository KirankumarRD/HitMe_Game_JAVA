import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class HitMe extends JPanel {
    protected static final int BOX_SIZE = 65; // in pixels
    protected static final int PANEL_WIDTH = 720; // in pixels
    protected static final int PANEL_HEIGHT = 500; // in pixels
    protected static final int HIT_MISS_LABEL_WIDTH = 100; // in pixels
    protected static final int HIT_MISS_LABEL_HEIGHT = 20; // in pixels
    protected static final int MAX_BOX_POSITION_X = PANEL_WIDTH - BOX_SIZE;
    protected static final int MAX_BOX_POSITION_Y = PANEL_HEIGHT - BOX_SIZE;
    protected static Color BOX_BORDER_COLOR = Color.RED;
    protected static Color BOX_FILL_COLOR = Color.BLACK;
    protected static final Random RANDOM = new Random();

    protected int boxPositionX;
    protected int boxPositionY;
    public int hitCount;
    public int missCount;

    public HitMe() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                if (point.x >= boxPositionX && point.x <= boxPositionX + BOX_SIZE
                        && point.y >= boxPositionY && point.y <= boxPositionY + BOX_SIZE) {
                    hitCount++;
                } else {
                    missCount++;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the box
        if (boxPositionX == 0 && boxPositionY == 0) { // The box is not visible, so choose a new position
            boxPositionX = RANDOM.nextInt(MAX_BOX_POSITION_X + 1);
            boxPositionY = RANDOM.nextInt(MAX_BOX_POSITION_Y + 1);
        }

        Font font = new Font("Snap ITC", Font.BOLD, 18); // Change the font, style, and size as desired
        g.setFont(font);

        // Draw the border with simulated thickness using drawRoundRect
        int borderWidth = 3; // Simulated border width
        g.setColor(BOX_BORDER_COLOR);
        g.drawRoundRect(boxPositionX, boxPositionY, BOX_SIZE, BOX_SIZE, borderWidth, borderWidth);

        // Draw the filled box
        g.setColor(BOX_FILL_COLOR);
        g.fillRect(boxPositionX + borderWidth, boxPositionY + borderWidth, BOX_SIZE - 2 * borderWidth, BOX_SIZE - 2 * borderWidth);

        // Draw the hit/miss counters
        g.setColor(Color.BLACK);
        String hitMissText = "Hit: " + hitCount + "   Miss: " + missCount;
        g.drawString(hitMissText, 525, 45);
    }



    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Hit-Miss Game");
    //     HitMe gamePanel = new HitMe();
    //     gamePanel.setPreferredSize(new java.awt.Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    //     frame.getContentPane().add(gamePanel);
    //     frame.pack();
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setVisible(true);
    //     while (true) { // Continuously repaint the panel with a delay of 1 second
    //         gamePanel.boxPositionX = 0;
    //         gamePanel.boxPositionY = 0;
    //         gamePanel.repaint();
    //         try {
    //             Thread.sleep(500);
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
}