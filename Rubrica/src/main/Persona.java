package main;

public class Persona{
	private int id;
	private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;
    private int idUtente;

    public Persona(int id, String nome, String cognome, String indirizzo, String telefono, int eta, int idUtente) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
        this.idUtente = idUtente;
    }

    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta, int idUtente) {
        this(0, nome, cognome, indirizzo, telefono, eta, idUtente); // ID 0 come valore temporaneo/segnaposto
    }
    
    public Persona() {
        this(0, "", "", "", "", 0, 0);
    }
    
    //Getter
    public int getId() {
    	return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public String getIndirizzo() {
        return indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEta() {
        return eta;
    }
    
    public int getIdUtente() {
        return idUtente;
    }
    
    //Setter
    public void setId(int id) { 
    	this.id = id; 
    
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setCognome(String cognome) {
        this.cognome =  cognome;
    }
    
    public void setIndirizzo(String indirizzo) {
        this.indirizzo =  indirizzo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }
    
    public void setIdUtente(int idUtente) { 
    	this.idUtente = idUtente;
    }
}