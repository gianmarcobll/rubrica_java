package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RubricaGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private GestioneRubrica gestioneRubrica;
    private JTable contattiTable;
    private DefaultTableModel tableModel;
    private JButton nuovoButton, modificaButton, eliminaButton, logoutButton;
    private JLabel userLabel;
    private JToolBar toolBar;
    
    public RubricaGUI(GestioneRubrica gestore) {
    	super("Rubrica");
    	this.gestioneRubrica = gestore;

    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	gestioneRubrica.logout(); // Esegui il logout quando l'applicazione si chiude
            }
        });
        initComponents();
    }

    private void initComponents() {
    	JPanel topPanel = new JPanel(new BorderLayout());
        userLabel = new JLabel("Utente: Non loggato", SwingConstants.RIGHT);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(userLabel, BorderLayout.EAST);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
        	gestioneRubrica.logout();
            JOptionPane.showMessageDialog(this, "Logout effettuato. Torna alla schermata di login.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

            // Riavvia tutta l'applicazione
            SwingUtilities.invokeLater(() -> {
                Main main = new Main(); // nuova istanza dell'app
                main.start();           // riavvia il flusso come all'inizio
            });
        });
        topPanel.add(logoutButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);


        // --- Configurazione della tabella dei contatti (BorderLayout.CENTER) ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contattiTable = new JTable(tableModel);
        contattiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(contattiTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);


        // --- Inizializzazione e Posizionamento della JToolBar in basso al centro ---

        toolBar = new JToolBar("Comandi Rubrica");
        toolBar.setFloatable(false);
        
        nuovoButton = new JButton("Nuovo");
        try { ImageIcon iconNuovo = new ImageIcon(getClass().getResource("/icons/new.png")); nuovoButton.setIcon(iconNuovo); } catch (Exception e) { System.err.println("Immagine new.png non trovata: " + e.getMessage()); }
        nuovoButton.setToolTipText("Aggiungi un nuovo contatto");
        toolBar.add(nuovoButton);

        toolBar.addSeparator();

        modificaButton = new JButton("Modifica");
        try { ImageIcon iconModifica = new ImageIcon(getClass().getResource("/icons/edit.png")); modificaButton.setIcon(iconModifica); } catch (Exception e) { System.err.println("Immagine edit.png non trovata: " + e.getMessage()); }
        modificaButton.setToolTipText("Modifica il contatto selezionato");
        toolBar.add(modificaButton);

        toolBar.addSeparator();

        eliminaButton = new JButton("Elimina");
        try { ImageIcon iconElimina = new ImageIcon(getClass().getResource("/icons/delete.png")); eliminaButton.setIcon(iconElimina); } catch (Exception e) { System.err.println("Immagine delete.png non trovata: " + e.getMessage()); }
        eliminaButton.setToolTipText("Elimina il contatto selezionato");
        toolBar.add(eliminaButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(toolBar);

        add(bottomPanel, BorderLayout.SOUTH);

        nuovoButton.addActionListener(this);
        modificaButton.addActionListener(this);
        eliminaButton.addActionListener(this);
    }
    
    public void aggiornaGUI() {
        Utente utente = gestioneRubrica.getUtente();
        if (utente != null) {
            userLabel.setText("Utente: " + utente.getUsername());
            aggiornaTabella(); // Carica i contatti dell'utente loggato
        } else {
            userLabel.setText("Utente: Non loggato");
            tableModel.setRowCount(0); // Pulisci la tabella se l'utente è sloggato
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (gestioneRubrica.getUtente() == null) {
            JOptionPane.showMessageDialog(this, "Devi effettuare il login per usare le funzionalità della rubrica.", "Non loggato", JOptionPane.WARNING_MESSAGE);
            return;
        }
         if (e.getSource() == nuovoButton) {
            apriEditorPersona(null);
            aggiornaTabella();
        } else if (e.getSource() == modificaButton) {
            int selectedRow = contattiTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selezionare una persona dalla tabella per modificarla.",
                        "Errore di selezione",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                Persona personaDaModificare = gestioneRubrica.getPersona(selectedRow);
                apriEditorPersona(personaDaModificare);
                aggiornaTabella();
            }
        } else if (e.getSource() == eliminaButton) {
            int selectedRow = contattiTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selezionare una persona dalla tabella per eliminarla.",
                        "Errore di selezione",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                Persona personaDaEliminare = gestioneRubrica.getPersona(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Eliminare la persona " + personaDaEliminare.getNome() + " " + personaDaEliminare.getCognome() + "?",
                        "Conferma Eliminazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                	gestioneRubrica.eliminaPersona(selectedRow);
                    aggiornaTabella();
                }
            }
        }
    }


    public void aggiornaTabella() {
        tableModel.setRowCount(0);

        List<Persona> contatti = gestioneRubrica.getTuttiContatti();
        for (Persona p : contatti) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(p.getNome());
            rowData.add(p.getCognome());
            rowData.add(p.getTelefono());
            tableModel.addRow(rowData);
        }
    }
    
    private void apriEditorPersona(Persona persona) {
        ContattoGUI contatto = new ContattoGUI(this, persona);
        contatto.setVisible(true);

        if (contatto.isSaved()) {
            Persona personaSalvata = contatto.getPersona();
            if (persona == null) {
                gestioneRubrica.aggiungiPersona(personaSalvata);
            } else {
                int selectedRow = contattiTable.getSelectedRow();
                if (selectedRow != -1) {
                    gestioneRubrica.modificaPersona(selectedRow, personaSalvata);
                }
            }
            aggiornaTabella();
        }
    }

}