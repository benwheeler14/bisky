package players;

import java.awt.Point;

import game.Hand;
import game.Position;
import game.TestableBoard;
import game.Tile;

/**
 * Dummy Interface that chooses tile with deterministic strategy.
 */
public class Dumb implements Player {

    //Name of the Player.
    private String name;
    //Age is determined by connection. Faster the player is connected, older the player is.
    private int age;

    public Dumb(String name) {
        this.name = name;
        this.age = -1;

    }

    @Override
    public Tile pickTile(TestableBoard board, Hand hand) {

        Tile t = hand.get(0);

        return t;
    }

    @Override
    public Tile pickInitialTile(TestableBoard board, Hand hand) {

        Tile t = hand.get(Hand.HAND_SIZE - 1);

        return t;
    }

    @Override
    public Position pickInitialPlacement(TestableBoard board) {
    	Point point = new Point(0, 0);
    	while(board.hasTile(point)) {
    		point.translate(2, 0);
    	}
    	return new Position(point, 0);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }


}
