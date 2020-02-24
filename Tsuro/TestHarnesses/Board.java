import java.util.ArrayList;

/**
 * ADJACENT PORTS are ports that touch on two seperate tiles
 * RELATED PORTS are ports on the same tile connected by a line
 * @author bhend
 *
 */

public class Board {
	
	public static final int BOARD_SIZE = 10;

	ArrayList<ArrayList<Tile>> tiles;
	
	ArrayList<PlayerToken> playerTokens = new ArrayList<>();
	
	public void createEmptyBoard() {
		tiles = new ArrayList<ArrayList<Tile>>();
		
		for (int i = 0; i < 10; i++) {
			ArrayList<Tile> column = new ArrayList<Tile>();
			for (int j = 0; j < 10; j++) {
				column.add(null);
			}
			tiles.add(column);
		}
	}
	
	public Board() {
		createEmptyBoard();
	}

	/**
	 * Creates a board with Initial placements
	 * @param tileLocations
	 * @param tiles
	 * @param playerLocations
	 * @param playerPorts
	 * @param playerTokens
	 * @return
	 */
	public static Board createBoardFromInitialPlacements(ArrayList<Location> tileLocations, ArrayList<Tile> tiles, ArrayList<Location> playerLocations, ArrayList<Integer> playerPorts, ArrayList<PlayerToken> playerTokens) {
		Board board = new Board();
		int length = tileLocations.size();
		if (length != tiles.size() || length != playerLocations.size() || length != playerPorts.size() || length != playerTokens.size()) {
			throw new IllegalArgumentException("All lists must have the same length");
		}
		for (int i = 0; i < length; i++) {
			board.placeFirstTile(tiles.get(i), tileLocations.get(i));
		}
		for (int i = 0; i < length; i++) {
			board.placePlayerToken(playerTokens.get(i), playerLocations.get(i), playerPorts.get(i));
		}
		return board;
	}


	/**
	 * Creates a board with Intermediate placments
	 * @param tileLocations
	 * @param tiles
	 * @param playerLocations
	 * @param playerPorts
	 * @param playerTokens
	 * @return
	 */
	public static Board createBoardFromIntermediatePlacements(ArrayList<Location> tileLocations, ArrayList<Tile> tiles, ArrayList<Location> playerLocations, ArrayList<Integer> playerPorts, ArrayList<PlayerToken> playerTokens) {
		Board board = new Board();
		int length = tileLocations.size();
		if (length != tiles.size() || length != playerLocations.size() || length != playerPorts.size() || length != playerTokens.size()) {
			throw new IllegalArgumentException("All lists must have the same length");
		}
		for (int i = 0; i < length; i++) {
			board.addTile(tiles.get(i), tileLocations.get(i));
		}
		for (int i = 0; i < length; i++) {
			PlayerToken token = playerTokens.get(i);
			board.addPlayerToken(token);
			token.move(playerLocations.get(i), playerPorts.get(i));
		}
		return board;
	}


	/**
	 * Adds a tile to the board at a specified location
	 * @param tile
	 * @param loc
	 */
	public void addTile(Tile tile, Location loc) {
		if (!loc.validTilePosition()) {
			throw new IllegalArgumentException(loc.toString() + " Not a valid position for a Tile");
		}
		if (hasTile(loc)) {
			throw new IllegalArgumentException("Cannot add a tile to " + loc.x() + ", " + loc.y() + ": A Tile already exists at that position");
		}
		tiles.get(loc.x()).set(loc.y(), tile);
	}


	/**
	 * Places the player's first tile on the board
	 * @param tile
	 * @param loc
	 */
	public void placeFirstTile(Tile tile, Location loc) {
		if (!locationOnEdge(loc)) {
			throw new IllegalArgumentException("Cannot place a first Tile at Location: " + loc.toString() + ", for it is not on the edge of the board");
		}
		for (Location neighbor : this.getAdjacentLocations(loc)) {
			if (this.hasTile(neighbor)) {
				throw new IllegalArgumentException("Cannot place a first Tile " + loc.toString() + " next to an existing Tile " + neighbor.toString());
			}
		}
		addTile(tile, loc);
	}


	/**
	 * Places the player token at a specified location and port
	 * @param token
	 * @param loc
	 * @param port
	 */
	public void placePlayerToken(PlayerToken token, Location loc, int port) {
		if (!this.locationOnEdge(loc)) {
			throw new IllegalArgumentException("Cannot place PlayerToken: " + token.toString() + ", at location: " + loc.toString() + ", for it is not on the edge of the board");
		}
		if (!this.portOnEdge(loc, port)) {
			throw new IllegalArgumentException("Cannot place PlayerToken: " + token.toString() + ", at location: " + loc.toString() + ", with port: " + port + "for it is not on the edge of the board");
		}
		if (this.hasPlayerToken(token.getName())) {
			throw new IllegalArgumentException("Cannot place PlayerToken: " + token.toString() + ", because a token with that name already exists");
		}
		this.addPlayerToken(token);
		token.move(loc, port);
		movePlayerTokenAcrossTile(token.getName());
	}


	public void placePlayerTokenn(String color, Location loc, int port) {

	}


	public void updatePlayerTokens(){

	}

	public void placeFirstTileForPlayer(Tile t, String c, Position p){

	}

	public void addTile(Tile t, Position p){

	}
	/**
	 * Adds a player token to the board
	 * @param token
	 */
	public void addPlayerToken(PlayerToken token) {
		if (this.hasPlayerToken(token.getName())) {
			throw new IllegalArgumentException("Cannot place PlayerToken: " + token.toString() + ", because a token with that name already exists");
		}
		this.playerTokens.add(token);
	}


	/**
	 * Places an intermediate tile for a player
	 * @param playerName
	 * @param loc
	 * @param tile
	 */
	public void addTileForPlayer(String playerName, Location loc, Tile tile) {
		if(hasPlayerToken(playerName)) {
            addTile(tile, loc);
            checkMove();
        }
	}


	/**
	 * Moves the players token across the tile to the corresponding port
	 * @param playerName
	 */
	public void movePlayerTokenAcrossTile(String playerName) {
		PlayerToken token = this.getPlayerToken(playerName);
		Location loc = token.getLocation();
		int newPort = this.getRelatedPort(loc, token.getPort());
		token.move(loc, newPort);
	}


	/**
	 * Moves the players token from one tile to the correct port in the tile next to it
	 * @param playerName
	 */
	public void movePlayerTokenBetweenTiles(String playerName) {
		PlayerToken token = this.getPlayerToken(playerName);
		int port = token.getPort();
		Location newLoc = this.getAdjacentLocation(token.getLocation(), port);
		int newPort = this.getAdjacentPort(port);
		token.move(newLoc, newPort);
	}




	//returns the adjacent tile corresponding to the selected port
	//returns null if the adjacent location is off the board
	public Location getAdjacentLocation(Location loc, int port) {
		Location result = null;
		if (port == 0 || port == 1) {
			result = new Location(loc.x(), loc.y() - 1);
		}
		if (port == 2 || port == 3) {
			result = new Location(loc.x() + 1, loc.y());
		}
		if (port == 4 || port == 5) {
			result = new Location(loc.x(), loc.y() + 1);
		}
		if (port == 6 || port == 7) {
			result = new Location(loc.x() - 1, loc.y());
		}
		if (!result.validTilePosition()) {
			result = null;
		}
		return result;
	}

    //returns the adjacent tile corresponding to the selected port
    //returns null if the adjacent location is off the board
    public boolean checkForAdjacentLocation(Location loc, int port) {
        Location result = null;
        if (port == 0 || port == 1) {
            result = new Location(loc.x(), loc.y() - 1);
        }
        if (port == 2 || port == 3) {
            result = new Location(loc.x() + 1, loc.y());
        }
        if (port == 4 || port == 5) {
            result = new Location(loc.x(), loc.y() + 1);
        }
        if (port == 6 || port == 7) {
            result = new Location(loc.x() - 1, loc.y());
        }
        if (!result.validTilePosition()) {
            return false;
        }
        else{
            return tiles.get(result.x()).get(result.y()) != null;
        }
    }

    /**
	 * Gets a list of the locations around surrounding the given location
	 * @param loc
	 * @return
	 */
	public ArrayList<Location> getAdjacentLocations(Location loc) {
		ArrayList<Location> result = new ArrayList<Location>();
		for (int i = 0; i < 7; i++) {
			Location neighbor = this.getAdjacentLocation(loc, i);
			if (neighbor != null) {
				result.add(neighbor);
			}
		}
		return result;
	}


	/**
	 * Checks if the two locations are adjacent
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public boolean isAdjacent(Location loc1, Location loc2) {
		ArrayList<Location> loc1neighbors = this.getAdjacentLocations(loc1);
		for (Location loc : loc1neighbors) {
			if (loc.equals(loc2)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Gets the port adjacent to the given port
	 * @param port
	 * @return
	 */
	public int getAdjacentPort(int port) {
		switch (port) {
			case 0:
				return 5;
			case 1:
				return 4;
			case 2:
				return 7;
			case 3:
				return 6;
			case 4:
				return 1;
			case 5:
				return 0;
			case 6:
				return 3;
			case 7:
				return 2;
			default:
				return -1; //invalid port
		}
	}



	public boolean canPlayerTokenBeMoved(String p){
		return true;
	}

	public void movePlayerToken(String s){

	}




	public int getRelatedPort(Location loc, int port) {
		if (!this.hasTile(loc)) {
			return port;
		}
		Tile tile = this.getTile(loc);
		return tile.getPort(port);
	}




	public boolean hasPlayerToken(String name) {
		return getPlayerToken(name) != null;
	}




	public PlayerToken getPlayerToken(String name) {
            for (PlayerToken token : this.playerTokens) {
                if (token.getName().equals(name)) {
                    return token;
                }
        }
		return null;
	}




	private boolean hasTile(Location loc) {
		return getTile(loc) != null;
	}




	public Tile getTile(Location loc) {
		return tiles.get(loc.x()).get(loc.y());
	}




	public boolean portOnEdge(Location loc, int port) {
		switch (whichEdgeOfBoard(loc)) {
			case "no edge":
				return false;
			case "top left":
				return port == 0 || port == 1 || port == 6 || port == 7;
			case "top right":
				return port == 0 || port == 1 || port == 2 || port == 3;
			case "bottom right":
				return port == 2 || port == 3 || port == 4 || port == 5;
			case "bottom left":
				return port == 4 || port == 5 || port == 6 || port == 7;
			case "top":
				return port == 0 || port == 1;
			case "right":
				return port == 2 || port == 3;
			case "bottom":
				return port == 4 || port == 5;
			case "left":
				return port == 6 || port == 7;
			default:
				return false;
		}
	}


	public void checkMove(){
		for(PlayerToken player : playerTokens){
			while(this.checkForAdjacentLocation(player.getLocation(), player.getPort())){
				movePlayerTokenBetweenTiles(player.getName());
				movePlayerTokenAcrossTile(player.getName());
			}
		}
	}



	public boolean locationOnEdge(Location loc) {
		return !whichEdgeOfBoard(loc).equals("no edge");
	}

	public boolean checkCollision(PlayerToken playerToken){
	    Location originalLocation = playerToken.getLocation();
	    int originalPort = playerToken.getPort();
	    movePlayerTokenAcrossTile(playerToken.getName());
	    while(this.checkForAdjacentLocation(playerToken.getLocation(), playerToken.getPort())){
	        //System.out.println("while" + playerToken.getLocation());
	        movePlayerTokenBetweenTiles(playerToken.getName());
	        movePlayerTokenAcrossTile(playerToken.getName());
        }

	    for(PlayerToken player : playerTokens) {
            if (!player.getName().equals(playerToken.getName())) {
                if (player.getLocation().equals(playerToken.getLocation())) {
                    playerToken.move(originalLocation, originalPort);
                    return true;
                }
            }
        }
        playerToken.move(originalLocation, originalPort);
	    return false;
    }




	//function determines where a tile is in relation to the periphery,
	//returns strings
	public String whichEdgeOfBoard(Location loc) {
		int x = loc.x();
		int y = loc.y();
		if (x == 0 && y == 0) {
			return "top left";
		}
		if (x == BOARD_SIZE - 1&& y == 0) {
			return "top right";
		}
		if (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) {
			return "bottom right";
		}
		if (x == 0 && y == BOARD_SIZE - 1) {
			return "bottom left";
		}
		if (x == 0) {
			return "left";
		}
		if (x == BOARD_SIZE - 1) {
			return "right";
		}
		if (y == 0) {
			return "top";
		}
		if (y == BOARD_SIZE - 1) {
			return "bottom";
		}
		return "no edge";
	}
}
