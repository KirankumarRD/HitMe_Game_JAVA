import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame implements ActionListener {
    private static final Font FONT_SNAP_ITC = new Font("Snap ITC", Font.BOLD, 32);
    private static final Color BUTTON_COLOR = new Color(40, 100, 250);
    private static JFrame homeframe;

    public HomeFrame() {
        homeframe = new JFrame("HOME FRAME");
        homeframe.setBounds(400, 160, 720, 500);

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Kirankumar\\Desktop\\IMG_20221014_180756.jpg");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        homeframe.setContentPane(backgroundPanel);

        String welcomeText = NameFrame.globalName.equals("Unknown")
                ? "*** Welcome to the game ***"
                : "Welcome to the game " + NameFrame.globalName + "!";

        JLabel welcome = new JLabel(welcomeText, SwingConstants.CENTER);
        welcome.setFont(new Font("Snap ITC", Font.BOLD, 37));
        welcome.setForeground(Color.magenta);
        welcome.setBounds(17, 7, 700, 100);
        homeframe.add(welcome);



        // Create buttons with common settings
        String[] buttonNames = {"NEW GAME", "High Scores", "SETTINGS", "EXIT"};
        int yPosition = 100;
        for (String name : buttonNames) {
            JButton button = createButton(name, yPosition);
            homeframe.add(button);
            yPosition += 85; // Adjusted the spacing to fit the additional button
        }

        homeframe.setLayout(null);
        homeframe.setVisible(true);
        homeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JButton createButton(String text, int yPosition) {
        JButton button = new JButton(text);
        button.setFont(FONT_SNAP_ITC);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.BLACK);
        button.setBounds(200, yPosition, 300, 70);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        switch (command) {
            case "NEW GAME" -> {
                new DifficultFrame();
                homeframe.dispose();
            }
            case "High Scores" -> {
                new DisplayScoreFrame(); // Assuming you have a class to display scores
                homeframe.dispose();
            }
            case "SETTINGS" -> {
                new SettingFrame();
                homeframe.dispose();
            }
            case "EXIT" -> homeframe.dispose();
        }
    }

    public static void main(String[] args) {
        new HomeFrame();
    }
}
