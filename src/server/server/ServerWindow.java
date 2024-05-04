package server.server;

import server.client.ClientWindow;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_POS_X = 200;
    private static final int WINDOW_POS_Y = 300;
    private final ClientWindow clientWindowFirst;
    private final ClientWindow clientWindowSecond;

    public boolean serverStarted = false;

    JButton btnStart = new JButton("Start");
    JButton btnStop = new JButton("Stop");

    JTextArea chatArea = new JTextArea(35, 41);

    public ServerWindow() {
        this.clientWindowFirst = new ClientWindow(this, 750, 300);
        this.clientWindowSecond = new ClientWindow(this, 1300, 300);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Chat server");
        setResizable(false);

        JPanel chat = new JPanel();
        JPanel panelBottom = new JPanel(new GridLayout(1, 2));

        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chat.add(chatArea);
        chat.setVisible(false);

        panelBottom.add(btnStart);
        panelBottom.add(btnStop);

        add(panelBottom, BorderLayout.SOUTH);
        add(chat);

        btnStart.addActionListener(
                e -> {
                    loadChatArea();
                    chat.setVisible(true);
                    serverStarted = true;
                }
        );
        btnStop.addActionListener(
                e -> {
                    chat.setVisible(false);
                    serverStarted = false;
                }
        );

        setVisible(true);
    }

    public void loadChatArea() {
        try {
            chatArea.setText("");
            BufferedReader reader = new BufferedReader(new FileReader("chatHistory.txt"));
            String line = reader.readLine();
            while (line != null) {
                chatArea.setText(chatArea.getText() + line + "\n");
                line = reader.readLine();
            }
            reader.close();
            this.clientWindowFirst.loadChatArea();
            this.clientWindowSecond.loadChatArea();
        } catch (IOException e) {
            System.out.println("Возникла ошибка во время чтения из файла");
        }
    }
}
