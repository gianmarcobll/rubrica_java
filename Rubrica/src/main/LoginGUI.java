package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private GestioneRubrica gestoreRubrica;
    private boolean loggedIn = false;

    public LoginGUI(RubricaGUI parent, GestioneRubrica gestore) {
        super(parent, "Login Rubrica", true); // Modale
        this.gestoreRubrica = gestore;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(parent);

        initComponents();
    }
    
    public LoginGUI(GestioneRubrica gestoreRubrica) {
    	this(null, gestoreRubrica);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton = new JButton("Login");
        registerButton = new JButton("Registrati");
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (gestoreRubrica.login(username, password)) {
                this.loggedIn = true;
                JOptionPane.showMessageDialog(this, "Login avvenuto con successo!", "Benvenuto", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Chiude il dialogo di login
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali non valide.", "Login Fallito", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == registerButton) {
            // Implementa una finestra di registrazione
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username e Password non possono essere vuoti.", "Errore Registrazione", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Qui potresti chiedere anche l'email se necessaria
            boolean registrato = gestoreRubrica.registraUtente(username, password);
            if (registrato) {
                JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo! Puoi ora effettuare il login.", "Registrazione", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
            // Se la registrazione fallisce, il manager già mostra un messaggio
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}