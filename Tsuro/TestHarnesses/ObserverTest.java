import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ObserverTest {

    /**
     * Converts a string ("A" to "H") to one of our indicies (0 to 7)
     * @param s String A to H
     * @return index
     */
    public static int getIndex(String s){
        switch(s){
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            default:
                throw new IllegalArgumentException("Invalid Port In the 35 Tiles - Check the txt file for the 35 permutations");
        }
    }

    /**
     * Converts given degrees (0,90,180,270) to one of our rotation numbers (0,1,2,3)
     * @param i degree
     * @return rotation
     */
    public static int getRotation(long i){
        switch((int) i){
            case 0:
                return 0;
            case 90:
                return 1;
            case 180:
                return 2;
            case 270:
                return 3;
            default:
                throw new IllegalArgumentException("Degree must be 0, 90, 180, or 270");
        }
    }


    /**
     * Gets the specified tile index from the 35 tiles given in the txt file and returns that tile as an Arraylist of
     * integers (it's port numbers)
     * @param tileIndex Index
     * @return ArrayList of ports (ints)
     * @throws ParseException if the file cant be found
     */
    public static ArrayList<Integer> getOneOf35(int tileIndex) throws ParseException {

        InputStream is = ObserverTest.class.getResourceAsStream("35Tiles.txt");
        Scanner inFile = new Scanner(is);
        JSONArray jaTile = new JSONArray();

        while (inFile.hasNextLine()) {
            String lineOf35 = inFile.next();
            Object objOf35 = new JSONParser().parse(lineOf35);
            JSONArray jaOf35 = (JSONArray) objOf35;
            long numOf35 = (Long) jaOf35.get(0);
            if (numOf35 == tileIndex) {
                jaTile = (JSONArray) jaOf35.get(1);
                break;
            }
        }

        ArrayList<Integer> tileOfInts = new ArrayList<>();
        Iterator itrOfTile = jaTile.iterator();
        for (int i = 0; i < 4; i++) {
            JSONArray jaTwoPorts = (JSONArray) itrOfTile.next();
            String portString1 = (String) jaTwoPorts.get(0);
            int portInt1 = getIndex(portString1);
            String portString2 = (String) jaTwoPorts.get(1);
            int portInt2 = getIndex(portString2);

            tileOfInts.add(portInt1);
            tileOfInts.add(portInt2);
        }

        return tileOfInts;

    }

    /**
     * Checks whether the given color is legal (one of the 5 specified)
     * @param color color of player
     * @throws IllegalArgumentException If the color is not one of the 5 specified
     */
    public static void checkColor(String color) throws IllegalArgumentException{
        if (!(color.equals("white") || color.equals("red") || color.equals("black")
                || color.equals("green") || color.equals("blue"))) {
            throw new IllegalArgumentException("Color must be either white, red, black, green, or blue");
        }
    }














    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            //Building an iterator to parse through the given JSON input
            String line = scanner.nextLine();
            Object obj = new JSONParser().parse(line);
            JSONArray entireJson = (JSONArray) obj;
            Iterator itrEntireJson = entireJson.iterator();

            //Lists for the initial placements and the intermediate placements. Also the final turn specification.
            ArrayList<JSONArray> initialPlacements = new ArrayList<>();
            ArrayList<JSONArray> intermediatePlacements = new ArrayList<>();
            JSONArray turnSpecification = new JSONArray();

            //Makes two arrays of the given JSONArray of placements. One of initial placements and one of intermediate.
            //Also adds the given turn specification
            while (itrEntireJson.hasNext()) {
                JSONArray individualJA = (JSONArray) itrEntireJson.next();

                if (individualJA.size() == 6) {
                    initialPlacements.add(individualJA);
                } else if (individualJA.size() == 5) {
                    intermediatePlacements.add(individualJA);
                } else if (individualJA.size() == 3) {
                    turnSpecification = individualJA;
                } else {
                    throw new IllegalArgumentException("Invalid input. Array is not of size 6 (for initial) or 5 (for intermediate)");
                }
            }

            Board b = new Board();


            //Goes through initial placements and places them on the board
            for (JSONArray jaInitial : initialPlacements) {

                //Gets each part of the JSON array
                long tileIndex = (Long) jaInitial.get(0);
                long degrees = (Long) jaInitial.get(1);
                String color = (String) jaInitial.get(2);
                String portString = (String) jaInitial.get(3);
                long x = (Long) jaInitial.get(4);
                long y = (Long) jaInitial.get(5);

                //Throws error if the color is not one of the 5 listed
                checkColor(color);

                //Sets the correct rotation, port, and creates a position object
                int rotation = getRotation(degrees);
                Point point = new Point((int)x, (int)y);
                int port = getIndex(portString);

                Position position = new Position(point, port);

                //Gets the correct tile from the 35 permutations
                ArrayList<Integer> tileOfInts = getOneOf35((int)tileIndex);

                //Creates a tile object of the given tile index
                Tile finalTile = new Tile(tileOfInts);

                //Rotates the tile
                finalTile.rotate(rotation);

                //Places the initial tile and the player token in the proper positions
                b.placeFirstTileForPlayer(finalTile, color, position);
            }


            //Goes through intermediate placements and places them on the board
            for (JSONArray jaIntermediate : intermediatePlacements) {

                //Gets each part of the JSON array
                String color = (String) jaIntermediate.get(0);
                long tileIndex = (Long) jaIntermediate.get(1);
                long degrees = (Long) jaIntermediate.get(2);
                long x = (Long) jaIntermediate.get(3);
                long y = (Long) jaIntermediate.get(4);

                //Throws error if the color is not one of the 5 listed
                checkColor(color);

                //Sets the correct rotation and creates a position object
                int rotation = getRotation(degrees);
                Point point = new Point((int)x, (int)y);

                Position position = new Position(point, 0);

                //Gets the correct tile from the 35 permutations
                ArrayList<Integer> tileOfInts = getOneOf35((int)tileIndex);

                //Creates a tile object of the given tile index
                Tile finalTile = new Tile(tileOfInts);

                //Rotates the tile
                finalTile.rotate(rotation);

                //Adds the intermediate tile to the board
                b.addTile(finalTile, position);
            }


            //Pushes all the player tokens the farthest they can go down their path on the board
            b.updatePlayerTokens();


            if (turnSpecification.size() > 0) {
                //Gets each part of the turn specification
                JSONArray turnMove = (JSONArray) turnSpecification.get(0);
                long tileHand1 = (Long) turnSpecification.get(1);
                long tileHand2 = (Long) turnSpecification.get(2);

                //Gets each part of the move of the turn specification
                String color = (String) turnMove.get(0);
                long tileIndex = (Long) turnMove.get(1);
                long degrees = (Long) turnMove.get(2);
                long x = (Long) turnMove.get(3);
                long y = (Long) turnMove.get(4);

                //Creates a position object
                Point point = new Point((int)x, (int)y);

                Position position = new Position(point, 0);

                //Gets the correct tile from the 35 permutations
                ArrayList<Integer> tileOfInts1 = getOneOf35((int)tileIndex);
                ArrayList<Integer> tileOfInts2;


                //Checks if the first hand is equal to the tile index of the turn specificiton, if it is,
                //sets the second tile to the second hand, else it sets the second tile to the first hand
                if(tileIndex == tileHand1){
                    tileOfInts2 = getOneOf35((int)tileHand2);
                }
                else{
                    tileOfInts2 = getOneOf35((int)tileHand1);
                }

                //Creates a tile object of the given tile index
                Tile moveTile = new Tile(tileOfInts1);

                //Creates an array for the hands given
                Hand hand = new Hand();

                Tile otherTile = new Tile(tileOfInts2);
                hand.addTile(moveTile);
                hand.addTile(otherTile);

                b.addTile(moveTile, position);
                Observer observer = new Observer();
                observer.observeGame(b, hand, color);
            }

            // If no turn specification, displays board without a hand
            else {
                Hand hand = new Hand();

                Observer observer = new Observer();
                observer.observeGame(b, hand, "");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

