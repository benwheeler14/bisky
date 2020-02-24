package players;

import java.awt.Point;
import java.util.ArrayList;

import game.*;

public class LegalPlayer implements Player {
	
	String name;
	FirstMove firstMove;

	public LegalPlayer(String name) {
		this.name = name;
		this.firstMove = null;
	}
	
	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		ArrayList<Tile> legalTiles = this.getAllLegalTiles(board, hand);
		if (legalTiles.size() == 0) {
			return hand.get(0);
		}
		return legalTiles.get(0);
	}
	/**
	 * Constructs an ArrayList of legal Tiles for the given Board and Hand
	 * @param board the given Board
	 * @param hand the given Hand
	 * @return an ArrayList of legal Tiles
	 */
	public ArrayList<Tile> getAllLegalTiles(TestableBoard board, Hand hand) {
		ArrayList<Tile> legalTiles = new ArrayList<Tile>();
		for (Tile tile : hand.getAllTiles()) {
			for (int i = 0; i < 4; i++) {
				if (!RuleChecker.doesMoveKillPlayer(board, this.getName(), tile)) {
					legalTiles.add(tile.clone());
				}
				tile.rotate(1);
			}
		}
		return legalTiles;
	}
	
	@Override
	public FirstMove pickFirstMove(TestableBoard board, Hand hand) {
		Position initialPosition = this.getAllLegalInitialPlacements(board).get(0);
		Tile initialTile = this.getAllLegalInitialTiles(board, hand, initialPosition).get(0);
		FirstMove firstMove = new FirstMove();
		firstMove.position = initialPosition;
		firstMove.tile = initialTile;
		return firstMove;
	}
	/**
	 * Constructs an ArrayList of legal InitialTiles for the given Board, Hand, and initialPosition
	 * @param board the given Board
	 * @param hand the given Hand
	 * @param initialPosition the given InitialPosition
	 * @return an ArrayList of legal InitialTiles
	 */
	public ArrayList<Tile> getAllLegalInitialTiles(TestableBoard board, Hand hand, Position initialPosition) {
		ArrayList<Tile> legalTiles = new ArrayList<Tile>();
		for (Tile tile : hand.getAllTiles()) {
			for (int i = 0; i < 4; i++) {
				if (RuleChecker.validFirstTilePlacement(board, this.getName(), tile, hand, initialPosition)) {
					legalTiles.add(tile.clone());
				}
				tile.rotate(1);
			}
		}
		return legalTiles;
	}

	/**
	 * Constructs an ArrayList of legal Initial Positions for the given Board
	 * 
	 * @return
	 */
	public ArrayList<Position> getAllLegalInitialPlacements(TestableBoard board) {
		ArrayList<Position> allEdgePositions = new ArrayList<Position>();
		for (int x = 0; x < board.BOARD_SIZE; x++) { //add Positions from top and bottom of board
			Position top = new Position(new Point(x, 0), 0); //create top Position with port 0
			Position bottom = new Position(new Point(x, board.BOARD_SIZE - 1), 4); //create bottom Position with port 4
			allEdgePositions.add(top);
			allEdgePositions.add(bottom);
		}
		for (int y = 0; y < board.BOARD_SIZE; y++) {
			Position left = new Position(new Point(0, y), 6); //add Positions from left of board
			Position right = new Position(new Point(board.BOARD_SIZE - 1, y), 2); //add Positions from left of board
			allEdgePositions.add(left);
			allEdgePositions.add(right);
		}
		ArrayList<Position> result = new ArrayList<Position>();
		for (Position position : allEdgePositions) {
			if (!this.placementTouchingTile(board, position)) {
				result.add(position);
			}
		}
		return result;
	}
	/**
	 * Determines if the given Position has a Tile or is touching a Tile on the given Board
	 * @param board
	 * @param position
	 * @return whether the given Position has a Tile or is touching a Tile on the given Board
	 */
	public boolean placementTouchingTile(TestableBoard board, Position position) {
		ArrayList<Point> adjacentPoints = position.getAdjacentPoints();
		adjacentPoints.add(position.getPoint());
		for (Point point : adjacentPoints) {
			if (board.hasTile(point)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	

}
