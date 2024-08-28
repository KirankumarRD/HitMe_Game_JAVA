import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class NameFrame implements ActionListener {
    JLabel hitme, entername;
    public static JTextField name;
    JButton submit;
    static JFrame NameFrame;
    BufferedImage backgroundImage;

    NameFrame() {
        try {
            // Load the background image from a URL
            URL imageUrl = new URL("https://imgs.search.brave.com/Ae2uOdfKve5e5KCG9Tq0myIGfxQkM4wqjZOiGUxnmEU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXZlLmNv/bS9mdXdwL3V3cDQ0/NDA4NjAuanBlZw");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NameFrame = new JFrame();
        NameFrame.setSize(720, 360);
        NameFrame.setBounds(400, 160, 720, 500);

        // Create a custom JPanel with a paintComponent method to draw the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new FlowLayout()); // Set layout to the background panel
        NameFrame.setContentPane(backgroundPanel);

        // Add components
        hitme = new JLabel(" HIT ME ");
        hitme.setForeground(Color.WHITE);
        hitme.setFont(new Font("Snap ITC", Font.ITALIC, 96));
        backgroundPanel.add(hitme);

        entername = new JLabel("ENTER YOUR NAME : ");
        entername.setFont(new Font("Snap ITC", Font.BOLD, 32));
        entername.setForeground(new Color(173, 68, 68));
        backgroundPanel.add(entername);

        name = new JTextField(20);
        name.setFont(new Font("Snap ITC", Font.ITALIC, 35));
        backgroundPanel.add(name);

        submit = new JButton("SUBMIT");
        submit.setFont(new Font("Snap ITC", Font.BOLD, 32));
        submit.setBackground(new Color(40, 100, 250));
        submit.setForeground(Color.BLACK);
        submit.addActionListener(this);
        backgroundPanel.add(submit);

        // Make the frame visible
        NameFrame.setVisible(true);
        NameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new NameFrame();

    }


    public static String globalName;
    @Override
    public void actionPerformed(ActionEvent e) {

        globalName = name.getText().trim(); // Get the text and trim any leading/trailing spaces
        if (globalName.isEmpty()) {
            globalName = "Unknown"; // Default to "Unknown" if the name is empty
        }
        if (e.getSource() == submit) {
            new HomeFrame();
            NameFrame.dispose();
        }
    }
}
