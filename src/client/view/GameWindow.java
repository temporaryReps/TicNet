package client.view;

import client.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements Controller.Updatable {
    private static final int SIZE = 3;

    private JLabel resume;
    private JButton[][] buttons;
    private Controller controller;

    @Override
    public void update(String[][] field, String report) {
        updateUI(field, report);
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
                        controller.doShoot(new int[]{finalI, finalJ});
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

        // start dialog
        createDialog();

        setVisible(true);
    }

    /**
     * update UI according new data
     *
     * @param data   for cells
     * @param report
     */
    private void updateUI(String[][] data, String report) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setText(data[i][j]);
            }
        }

        resume.setText(report);
    }

    /**
     * create dialog which asks user if who he want to game
     */
    private void createDialog() {
        JDialog dialog = new JDialog(this);
        dialog.setTitle("С кем хотите играть?");
        dialog.setLayout(new GridLayout(1, 2));
        dialog.setSize(350, 70);
        dialog.setLocationRelativeTo(null);

        JButton computerButton = new JButton("С компьютером");
        computerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setGamer(Controller.COMPUTER);
                dialog.dispose();
            }
        });

        JButton userButton = new JButton("С игроком");
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setGamer(Controller.USER);
                dialog.dispose();
            }
        });

        dialog.add(computerButton);
        dialog.add(userButton);

        dialog.setVisible(true);
    }
}