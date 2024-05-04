package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClientWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 500;

    JButton btnSend = new JButton("Send");
    JButton btnLogin = new JButton("Login");

    JTextField ip = new JTextField(10);
    JTextField port = new JTextField(10);
    JTextField login = new JTextField(10);
    JTextField password = new JTextField(10);
    JTextField message = new JTextField(25);

    JLabel ipLabel = new JLabel("IP");
    JLabel portLabel = new JLabel("Port");
    JLabel loginLabel = new JLabel("Login");
    JLabel passwordLabel = new JLabel("Password");
    JLabel messageLabel = new JLabel("Message");

    JTextArea chatArea = new JTextArea(28, 41);

    public ClientWindow(ServerWindow serverWindow, int posX, int posY) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(posX, posY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Chat client");
        setResizable(false);

        JPanel chat = new JPanel();
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelSubTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelFinalTop = new JPanel(new GridLayout(2,3));
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chat.add(chatArea);
        chat.setVisible(false);

        ipLabel.setLabelFor(ip);
        portLabel.setLabelFor(port);
        loginLabel.setLabelFor(login);
        passwordLabel.setLabelFor(password);
        messageLabel.setLabelFor(message);

        panelTop.add(ipLabel);
        panelTop.add(ip);
        panelTop.add(portLabel);
        panelTop.add(port);

        panelSubTop.add(loginLabel);
        panelSubTop.add(login);
        panelSubTop.add(passwordLabel);
        panelSubTop.add(password);
        panelSubTop.add(btnLogin);

        panelFinalTop.add(panelTop);
        panelFinalTop.add(panelSubTop);

        panelBottom.add(messageLabel);
        panelBottom.add(message);
        panelBottom.add(btnSend);

        add(panelFinalTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        add(chat);

        btnLogin.addActionListener(
                e -> {
                    if (isLoggedIn() && isServerStarted(serverWindow)) {
                        serverWindow.sendMessage(login.getText() + " подключился к беседе.");
                        serverWindow.loadChatArea();
                        chat.setVisible(true);
                    }
                }
        );

        message.addActionListener(
                e -> attemptToSendMessage(serverWindow)
        );

        btnSend.addActionListener(
                e -> attemptToSendMessage(serverWindow)
        );

        setVisible(true);
    }

    public boolean isServerStarted(ServerWindow serverWindow) {
        return serverWindow.serverStarted;
    }

    public boolean isLoggedIn() {
        return !login.getText().isEmpty() && !password.getText().isEmpty() && !ip.getText().isEmpty() && !port.getText().isEmpty();
    }

    private void attemptToSendMessage(ServerWindow serverWindow) {
        if (isLoggedIn() && !message.getText().isEmpty() && isServerStarted(serverWindow)) {
            serverWindow.sendMessage(login.getText() + ": " + message.getText());
            message.setText("");
            serverWindow.loadChatArea();
        }
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
        } catch (IOException e) {
            System.out.println("Возникла ошибка во время чтения из файла");
        }
    }
}
