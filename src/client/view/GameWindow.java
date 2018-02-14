package client.view;

import client.controller.Controller;
import client.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements Controller.Updatable{
    public static final int SIZE = 3;

    private JLabel resume;
    private JButton[][] buttons;
    private Controller controller;

    @Override
    public void update(String[][] data) {
        updateUI(data);
    }

    public void init() {
        controller = Controller.getInstance();
        controller.setWindow(this);
        setSize(400, 400);
        setTitle("XO Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(SIZE, SIZE));

        buttons = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton jButton = new JButton();
                final int finalJ = j;
                final int finalI = i;
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String buttonText = e.getActionCommand();
                        System.out.printf("Button: %s, x: %d, y: %d%n", buttonText, finalJ, finalI);
                        controller.doShoot(new Point(finalI, finalJ));
                    }
                });

                buttons[i][j] = jButton;
                jPanel.add(jButton);
            }
        }

        resume = new JLabel("Играем");
        resume.setHorizontalAlignment(JLabel.CENTER);

        add(jPanel);
        add(resume, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Shows game resume
     * @param s string that will be shown
     */
    public void showResume(String s) {
        resume.setText(s);
    }

    /**
     * add X or O into sell
     * @param x coordinate
     * @param y coordinate
     * @param s X or O
     */
    public void showCell(int x, int y, String s) {
        JButton button = buttons[x][y];
        button.setText(s);
    }

    private void updateUI(String[][] data) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setText(data[i][j]);
            }
        }
    }
}
