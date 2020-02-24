package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import game.Board;

public class main {

	
	
    public static void main(String[] args) throws IOException {

        Observer view = new Observer(new Board(new ArrayList<String>(Arrays.asList("blue", "red", "green", "yellow"))));
        
        view.setVisible(true);
        System.out.print("Button not clicked");
        view.waitForButtonClick();
        System.out.print("Button clicked");
       view.refresh();
    }
}
