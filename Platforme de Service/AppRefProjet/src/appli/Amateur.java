package appli;

public class Amateur implements IUser{
	private String userName;
	private String password;
	public Amateur(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	@Override
	public String getUserName() {return userName;}
	@Override
	public String getPassword() {return password;}
	@Override
	public void setUserName(String Username) {this.userName = Username;}
	@Override
	public void setPassword(String Password) {this.password = Password;}

}
