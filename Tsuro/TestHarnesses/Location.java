public class Location {

	private int x, y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//determines if this location is a valid tile position on the board
	public boolean validTilePosition() {
		return x >= 0 && x < Board.BOARD_SIZE && y >= 0 && y < Board.BOARD_SIZE;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Location)) {
			return false;
		}
		Location l = (Location) o;
		return l.x() == this.x() && l.y() == this.y();
	}
	
	@Override
	public String toString() {
		return "(" + x() + ", " + y() + ")";
	}
}
