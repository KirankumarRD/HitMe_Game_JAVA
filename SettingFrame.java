import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingFrame extends HitMe implements ActionListener {
    JButton back;
    static JFrame SettingFrame;
    JButton[] colorButton = new JButton[4];
    JButton[] backgroundColor = new JButton[4];
    JLabel boxColorLabel, borderColorLabel;
    int selectedBoxColorIndex = 0;
    int selectedBorderColorIndex = 0;

    SettingFrame() {
        SettingFrame = new JFrame();
        SettingFrame.setSize(720, 500);
        SettingFrame.setBounds(400, 160, 720, 500);

        // Remove the background image, so we create a plain JPanel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        SettingFrame.setContentPane(backgroundPanel);

        // Set up the labels
        boxColorLabel = new JLabel("Box Color");
        boxColorLabel.setFont(new Font("Snap ITC", Font.BOLD, 28));
        boxColorLabel.setForeground(Color.BLACK);
        boxColorLabel.setBounds(160, 30, 200, 30);
        backgroundPanel.add(boxColorLabel);

        borderColorLabel = new JLabel("Box Border Color");
        borderColorLabel.setFont(new Font("Snap ITC", Font.BOLD, 28));
        borderColorLabel.setForeground(Color.BLACK);
        borderColorLabel.setBounds(160, 160, 300, 30);
        backgroundPanel.add(borderColorLabel);

        // Set up the "BACK" button
        back = new JButton("BACK");
        SettingFrame.add(back);
        back.setBounds(270, 330, 150, 50);
        back.setFont(new Font("Snap ITC", Font.BOLD, 32));
        back.setBackground(new Color(40, 100, 250));

        // Colors to choose from for the buttons
        Color[] colors = {Color.PINK, Color.yellow, Color.CYAN, Color.orange, Color.RED,  Color.WHITE, Color.GREEN, Color.MAGENTA };

        // Create color and background color buttons
        for (int i = 0; i < 4; i++) {
            colorButton[i] = new JButton();
            colorButton[i].setBackground(colors[i]);
            colorButton[i].setBounds(160 + i * 100, 70, 70, 70);
            colorButton[i].addActionListener(this);
            backgroundPanel.add(colorButton[i]);

            backgroundColor[i] = new JButton();
            backgroundColor[i].setBackground(colors[i+4]);
            backgroundColor[i].setBounds(160 + i * 100, 200, 70, 70);
            backgroundColor[i].addActionListener(this);
            backgroundPanel.add(backgroundColor[i]);
        }

        // Highlight the default selected buttons
        highlightSelectedButton(colorButton[selectedBoxColorIndex], true);
        highlightSelectedButton(backgroundColor[selectedBorderColorIndex], true);

        // Finalize the JFrame setup
        SettingFrame.setLayout(null);
        SettingFrame.setVisible(true);
        SettingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        back.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            new HomeFrame();
            SettingFrame.setVisible(false);
        } else {
            for (int i = 0; i < 4; i++) {
                if (e.getSource() == colorButton[i]) {
                    highlightSelectedButton(colorButton[selectedBoxColorIndex], false);
                    selectedBoxColorIndex = i;
                    highlightSelectedButton(colorButton[i], true);
                    BOX_FILL_COLOR = colorButton[i].getBackground();
                } else if (e.getSource() == backgroundColor[i]) {
                    highlightSelectedButton(backgroundColor[selectedBorderColorIndex], false);
                    selectedBorderColorIndex = i;
                    highlightSelectedButton(backgroundColor[i], true);
                    BOX_BORDER_COLOR = backgroundColor[i].getBackground();
                }
            }
        }
    }

    private void highlightSelectedButton(JButton button, boolean selected) {
        if (selected) {
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
        } else {
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    public static void main(String[] args) {
        new SettingFrame();
    }
}
