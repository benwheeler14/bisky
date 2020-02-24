import java.util.ArrayList;

public class 	RuleChecker {
	
	/**
	 * Checks if the placement of the token is valid
	 * @param board
	 * @param loc
	 * @param port
	 * @return whether or not the placemenet of the token is valid
	 */
	public static boolean isValidPlayerTokenPlacement(Board board, Location loc, int port) {
		if (!board.locationOnEdge(loc)) {
			return false;
		}
		if (!board.portOnEdge(loc, port)) {
			return false;
		}
//		if (!board.hasTile(loc)) {
//			return false;
//		}
//		for (Location l : board.getAdjacentLocations(loc)) {
//			if (board.hasTile(l)) {
//				return false;
//			}
//		}
		return true;
	}
	
	//enforces the rule that a player cannot place a tile that suicides itself, unless every move they can make suicides itself
	//assumes that the passed tile has the desired rotation of the move
	public static boolean validTilePlacement(Board board, String playerName, Tile tile, ArrayList<Tile> otherTiles) {
		Board original = board;  //keep a copy of the original board
		// board = board.clone();  //test validity on copy of board
		
		if (!doesMoveKillPlayer(board, playerName, tile)) {
			return true;
		}


		//else:  check if there are any other moves that would let the player survive
		otherTiles.add(tile); //add the tile to the testable list because we need to check it's other rotations
		for (Tile t : otherTiles) {
			for (int i = 0; i < 4; i++) {
				t.rotate(1);
			//	if (!doesMoveKillPlayer(original.clone(), playerName, t)) {
			//		return false;
			//	}
			}
		}
		return true;
		
	}
	
	
	//returns whether playing the specified tile kills the player or not
	public static boolean doesMoveKillPlayer(Board board, String playerName, Tile tile) {
		return true;
		//board = board.clone();
//
//		board.playerPlayTile(playerName, tile);
//		board.updatePlayerTokenPosition(playerName);
//		return board.isDead(playerName);
		
	}


	public static boolean validTilePlacementForTester(Board board, String playerName,
													  Tile tile, ArrayList<Tile> otherTiles, Location location){
		PlayerToken playerToken = board.getPlayerToken(playerName);
		if(location.equals(board.getAdjacentLocation(playerToken.getLocation(), playerToken.getPort()))){
			return validTilePlacement(board, playerName, tile, otherTiles);
		}
		else{
			return false;
		}
	}

}
