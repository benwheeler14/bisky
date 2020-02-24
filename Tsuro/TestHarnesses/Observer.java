import java.awt.*;
import java.io.IOException;

public class Observer {

    public void observeGame(Board board, Hand hand, String color) throws IOException {

        ViewPanel vp = new ViewPanel();
        vp.setTiles(board.getTiles());
        vp.setPlayers(board.getPlayerTokens());

        ViewPlayerPanel vpp = new ViewPlayerPanel();
        vpp.setHand(hand.getAllTiles());
        vpp.setPlayer(color);

        View v = new ViewImpl(vp, vpp);

        v.makeVisible();

        v.toPNG();

    }

}
