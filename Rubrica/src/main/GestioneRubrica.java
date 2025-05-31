package main;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class GestioneRubrica {
	private List<Persona> contatti;
	private Persistenza persistenza;
	private Utente utente;

    public GestioneRubrica() {
        contatti = new ArrayList<Persona>();
        persistenza = new Persistenza();
    }
    
    //Ritorna l'utente corrente
    public Utente getUtente() {
    	return utente;
    }
    
    public boolean login(String username, String password) {
    	Utente utente = persistenza.autenticaUtente(username, password);
    	if (utente != null) {
    		this.utente = utente;
    		caricaRubricaUtente(utente.getId());
    		return true;
    	}
    	return false;
    }
    
    public boolean registraUtente(String username, String password) {
        Utente nuovoUtente = new Utente(username, password);
        return persistenza.registraUtente(nuovoUtente);
    }
    
    public void logout() {
    	this.utente = null;
    	this.contatti.clear();
    }
    
    public void aggiungiPersona(Persona p) {
    	if (utente != null) {
    		p.setIdUtente(utente.getId());
    		if (persistenza.aggiungiPersona(p)) {
    			contatti.add(p);
    		}
    	}
    	else {
            System.err.println("Errore: Nessun utente loggato per aggiungere una persona.");
            JOptionPane.showMessageDialog(null, "Devi essere loggato per aggiungere contatti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificaPersona(int index, Persona p) {
    	if (utente != null && index >= 0 && index < contatti.size()) {
    		Persona personaOriginale = contatti.get(index);
    		p.setIdUtente(utente.getId());
    		p.setId(personaOriginale.getId());
    		if (persistenza.modificaPersona(p)) {
    			contatti.set(index, p);
    		}
    	}
    	else {
            System.err.println("Errore: Nessun utente loggato per modificare una persona.");
            JOptionPane.showMessageDialog(null, "Devi essere loggato e selezionare un contatto valido per modificarlo.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminaPersona(int index) {
    	if (utente != null && index >= 0 && index < contatti.size()) {
    		Persona personaDaEliminare = contatti.get(index);
    		if (persistenza.eliminaPersona(personaDaEliminare.getId(), utente.getId())) {
    			contatti.remove(index);
    		}
    	}
    	else {
    		System.err.println("Errore: Nessun utente loggato o indice non valido per eliminare una persona.");
            JOptionPane.showMessageDialog(null, "Devi essere loggato e selezionare un contatto valido per eliminarlo.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Persona> getTuttiContatti() {
        return new ArrayList<>(contatti);
    }

    public Persona getPersona(int index) {
        if (index >= 0 && index < contatti.size()) {
            return contatti.get(index);
        }
        return null;
    }

    public int getNumeroContatti() {
        return contatti.size();
    }

    private void caricaRubricaUtente(int idUtente) {
    	this.contatti = persistenza.caricaContatti(idUtente);
    	if (this.contatti == null) { // Nel caso di errore di caricamento
            this.contatti = new ArrayList<>();
        }
    }

}
