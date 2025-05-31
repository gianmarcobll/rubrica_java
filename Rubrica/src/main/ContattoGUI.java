package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContattoGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField nomeField, cognomeField, indirizzoField, telefonoField, etaField;
    private JButton salvaButton, annullaButton;
    private Persona persona;
    private boolean saved = false;

    public ContattoGUI(JFrame parent, Persona p) {
        super(parent, "Editor Persona", true);
        this.persona = p;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        initComponents();
        popolaCampi();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        nomeField = new JTextField(20);
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cognomeField = new JTextField(20);
        formPanel.add(cognomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Indirizzo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        indirizzoField = new JTextField(20);
        formPanel.add(indirizzoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        telefonoField = new JTextField(20);
        formPanel.add(telefonoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Età:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        etaField = new JTextField(20);
        formPanel.add(etaField, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        salvaButton = new JButton("Salva");
        annullaButton = new JButton("Annulla");

        salvaButton.addActionListener(this);
        annullaButton.addActionListener(this);

        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void popolaCampi() {
        if (persona != null) {
            nomeField.setText(persona.getNome());
            cognomeField.setText(persona.getCognome());
            indirizzoField.setText(persona.getIndirizzo());
            telefonoField.setText(persona.getTelefono());
            etaField.setText(String.valueOf(persona.getEta()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salvaButton) {
            salvaPersona();
        } else if (e.getSource() == annullaButton) {
            dispose();
        }
    }

    private void salvaPersona() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String indirizzo = indirizzoField.getText().trim();
        String telefono = telefonoField.getText().trim();
        int eta;

        if (nome.isEmpty() || cognome.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nome, Cognome e Telefono non possono essere vuoti.",
                    "Errore di Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            eta = Integer.parseInt(etaField.getText().trim());
            if (eta < 0) {
                JOptionPane.showMessageDialog(this,
                        "L'età deve essere un numero positivo.",
                        "Errore di Input",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "L'età deve essere un numero intero valido.",
                    "Errore di Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (this.persona == null) {
            this.persona = new Persona();
        }
        
    	this.persona.setNome(nome);
    	this.persona.setCognome(cognome);
    	this.persona.setIndirizzo(indirizzo);
    	this.persona.setTelefono(telefono);
    	this.persona.setEta(eta);
    	
        saved = true;
        dispose();
    }

    public Persona getPersona() {
        return persona;
    }

    public boolean isSaved() {
        return saved;
    }
}