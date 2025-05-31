package main;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Main {

	private GestioneRubrica gestioneRubrica;
    private RubricaGUI rubricaGUI;

    public Main() {
    	gestioneRubrica = new GestioneRubrica();
    }

    public void start() {
        rubricaGUI = new RubricaGUI(gestioneRubrica);
        rubricaGUI.setVisible(false); // Inizialmente invisibile

        LoginGUI loginGUI = new LoginGUI(rubricaGUI, gestioneRubrica);
        loginGUI.setVisible(true);

        if (loginGUI.isLoggedIn()) {
        	rubricaGUI.aggiornaGUI();
        	rubricaGUI.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Accesso non riuscito o annullato. L'applicazione verrà chiusa.", "Avviso", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.start();
        });
    }

}
