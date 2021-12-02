/**
 * 
 */
package mx.tec.project.vo;

public class Credentials {
private static final long serialVersionUID = 4679985845204576425L;
	
	private String username;
	private String password;
	
	public Credentials() {
	}
	
	public Credentials (final String username, final String password) {
		this.setPassword(password);
		this.setUsername(username);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
