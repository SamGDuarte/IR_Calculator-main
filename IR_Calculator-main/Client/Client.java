import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client {
    private JFrame frame;
    private JTextField usuarioField;
    private JPasswordField senhaField;
    private JTextField[] dataFields;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel de login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints loginConstraints = new GridBagConstraints();
        loginConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginConstraints.insets = new Insets(10, 10, 10, 10);

        // Rótulo de login
        JLabel usuarioLabel = new JLabel("Usuário:");
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 0;
        loginPanel.add(usuarioLabel, loginConstraints);

        // Campo de login
        usuarioField = new JTextField();
        usuarioField.setColumns(15); // Ajusta o tamanho do campo de resposta do login
        loginConstraints.gridx = 1;
        loginConstraints.gridy = 0;
        loginPanel.add(usuarioField, loginConstraints);

        // Rótulo de senha
        JLabel senhaLabel = new JLabel("Senha:");
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 1;
        loginPanel.add(senhaLabel, loginConstraints);

        // Campo de senha
        senhaField = new JPasswordField();
        senhaField.setColumns(15); // Ajusta o tamanho do campo de resposta da senha
        loginConstraints.gridx = 1;
        loginConstraints.gridy = 1;
        loginPanel.add(senhaField, loginConstraints);

        // Botão de login
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 2;
        loginConstraints.gridwidth = 2;
        loginPanel.add(loginButton, loginConstraints);

        frame.getContentPane().add(loginPanel);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectFromServer(); // Desconecta-se do servidor
                frame.dispose(); // Libera recursos e fecha a janela
            }
        });
    }

    private void createCalculationGUI() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Rótulos e campos de dados
        String[] labels = {
                "Nome:",
                "Renda:",
                "Idade:",
                "Despesas com Ensino:",
                "Despesas Médicas:",
                "Despesas com Pensão:",
                "Previdência Social:",
                "Imposto Retido:"
        };
        int numDataFields = labels.length;
        dataFields = new JTextField[numDataFields];

        for (int i = 0; i < numDataFields; i++) {
            // Rótulo
            JLabel label = new JLabel(labels[i]);
            constraints.gridx = 0;
            constraints.gridy = i;
            panel.add(label, constraints);

            // Campo de dado
            JTextField textField = new JTextField();
            textField.setColumns(10);
            constraints.gridx = 1;
            constraints.gridy = i;
            panel.add(textField, constraints);

            dataFields[i] = textField;
        }

        // Botão de cálculo
        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(new CalculateButtonListener());
        constraints.gridx = 0;
        constraints.gridy = numDataFields;
        constraints.gridwidth = 2;
        panel.add(calculateButton, constraints);

        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnectFromServer() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean verificarLogin(String usuario, String senha) {
        try {
            // Envia as credenciais para o servidor
            out.println(usuario);
            out.println(senha);

            // Aguarda a resposta do servidor
            boolean autenticado = Boolean.parseBoolean(in.readLine());

            // Retorna true se as credenciais forem válidas
            return autenticado;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usuario = usuarioField.getText();
            String senha = new String(senhaField.getPassword());

            connectToServer(); // Conecta-se ao servidor

            boolean autenticado = verificarLogin(usuario, senha);

            if (autenticado) {
                createCalculationGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Usuário ou senha inválidos", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                disconnectFromServer(); // Desconecta-se do servidor em caso de falha de autenticação
            }
        }
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Recupera os valores dos campos de dados
            String[] inputData = new String[dataFields.length];
            for (int i = 0; i < dataFields.length; i++) {
                inputData[i] = dataFields[i].getText();
            }

            // Envia os valores para o servidor
            for (String value : inputData) {
                out.println(value);
            }

            // Aguarda a resposta do servidor
            try {
                String resposta = in.readLine();
                JOptionPane.showMessageDialog(frame, resposta, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
