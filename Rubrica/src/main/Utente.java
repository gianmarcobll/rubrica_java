package main;

public class Utente {
	private int id;
	private String username;
	private String password;
	
	public Utente(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public Utente(String username, String password) {
		this(0, username, password);
	}
	
	//Getter
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	//Setter
	public void setId(int id) {
		this.id =  id;
	}
	public void setUsername(String username) {
		this.username =  username;
	}
	public void setPassword(String password) {
		this.password =  password;
	}
	
}
