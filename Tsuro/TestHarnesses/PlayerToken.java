public class PlayerToken {

	private String name;
	private Location loc;
	private int port;
	
	public PlayerToken(String name) {
		this.name = name;
	}
	
	public boolean onBoard() {
		return loc != null;
	}
	
	public void move(Location loc, int port) {
		this.loc = loc;
		this.port = port;
	}
	
	public Location getLocation() {
		checkOnBoard();
		return loc;
	}
	
	public int getPort() {
		checkOnBoard();
		return port;
	}
	
	public String getName() {
		return name;
	}
	
	public void checkOnBoard() {
		if (!onBoard()) {
			throw new IllegalArgumentException("Cannot access field of PlayerToken: " + name + ", it is not been initialized");
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
