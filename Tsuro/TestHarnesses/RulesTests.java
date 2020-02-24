import java.lang.reflect.Array;
import java.util.*;
import java.io.InputStream;
import java.util.Scanner;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class RulesTests {

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

        InputStream is = RulesTests.class.getResourceAsStream("35Tiles.txt");
        Scanner inFile = new Scanner(is);
        JSONArray jaTile = new JSONArray();

        while (inFile.hasNextLine()) {
            String lineOf35 = inFile.next();
            Object objOf35 = new JSONParser().parse(lineOf35);
            JSONArray jaOf35 = (JSONArray) objOf35;
            int numOf35 = (Integer) jaOf35.get(0);
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
        Board b = new Board();
        RuleChecker ruleChecker = new RuleChecker();

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
                }
                else if (individualJA.size() == 3){
                    turnSpecification = individualJA;
                }
                else {
                    throw new IllegalArgumentException("Invalid input. Array is not of size 6 (for initial) or 5 (for intermediate)");
                }
            }

            //Goes through initial placements and places them on the board
            for (JSONArray jaInitial : initialPlacements) {

                //Gets each part of the JSON array
                int tileIndex = (Integer) jaInitial.get(0);
                int degrees = (Integer) jaInitial.get(1);
                String color = (String) jaInitial.get(2);
                String portString = (String) jaInitial.get(3);
                int x = (Integer) jaInitial.get(4);
                int y = (Integer) jaInitial.get(5);

                //Throws error if the color is not one of the 5 listed
                checkColor(color);

                //Sets the correct rotation, port, and creates a location object
                int rotation = getRotation(degrees);
                Location location = new Location(x, y);
                int port = getIndex(portString);

                //Gets the correct tile from the 35 permutations
                ArrayList<Integer> tileOfInts = getOneOf35(tileIndex);

                //Creates a tile object of the given tile index
                Tile finalTile = new Tile(tileOfInts);

                //Rotates the tile
                finalTile.rotate(rotation);

                //Places the initial tile and the player token in the proper positions
                b.placeFirstTile(finalTile, location);
                b.placePlayerTokenn(color, location, port);
            }


            //Goes through intermediate placements and places them on the board
            for (JSONArray jaIntermediate : intermediatePlacements) {

                //Gets each part of the JSON array
                String color = (String) jaIntermediate.get(0);
                int tileIndex = (Integer) jaIntermediate.get(1);
                int degrees = (Integer) jaIntermediate.get(2);
                int x = (Integer) jaIntermediate.get(3);
                int y = (Integer) jaIntermediate.get(4);

                //Throws error if the color is not one of the 5 listed
                checkColor(color);

                //Sets the correct rotation and creates a location object
                int rotation = getRotation(degrees);
                Location location = new Location(x, y);

                //Gets the correct tile from the 35 permutations
                ArrayList<Integer> tileOfInts = getOneOf35((tileIndex));

                //Creates a tile object of the given tile index
                Tile finalTile = new Tile(tileOfInts);

                //Rotates the tile
                finalTile.rotate(rotation);

                //Adds the intermediate tile to the board
                b.addTile(finalTile, location);
            }


            //Pushes all the player tokens the farthest they can go down their path on the board
            for(PlayerToken playerToken : b.playerTokens) {
                while (b.canPlayerTokenBeMoved(playerToken.getName())) {
                    b.movePlayerToken(playerToken.getName());
                }
            }

            //Gets each part of the turn specification
            JSONArray turnMove = (JSONArray) turnSpecification.get(0);
            int tileHand1 = (Integer) turnSpecification.get(1);
            int tileHand2 = (Integer) turnSpecification.get(2);

            //Gets each part of the move of the turn specification
            String color = (String) turnMove.get(0);
            int tileIndex = (Integer) turnMove.get(1);
            int degrees = (Integer) turnMove.get(2);
            int x = (Integer) turnMove.get(3);
            int y = (Integer) turnMove.get(4);

            //Creates a location object
            Location location = new Location(x, y);

            //Gets the correct tile from the 35 permutations
            ArrayList<Integer> tileOfInts = getOneOf35(tileIndex);

            //Creates a tile object of the given tile index
            Tile moveTile = new Tile(tileOfInts);

            //Creates an array for the hands given
            ArrayList<Tile> handArray = new ArrayList<Tile>();


            //Sets initial valid to false
            boolean isValid = false;

            if(tileIndex == tileHand1){
                //Adds the tile not picked from the hand to the array
                Tile otherTile = new Tile(getOneOf35(tileHand2));
                handArray.add(otherTile);

                //Checks if the placement is valid
                isValid = ruleChecker.validTilePlacementForTester(b, color, moveTile, handArray, location);
            }
            else if(tileIndex == tileHand2){
                //Adds the tile not picked from the hand to the array
                Tile otherTile = new Tile(getOneOf35(tileHand1));
                handArray.add(otherTile);

                //Checks if the placement is valid
                isValid = ruleChecker.validTilePlacementForTester(b, color, moveTile, handArray, location);
            }

            //Prints legal if the placement is valid
            if(isValid){
                System.out.println("Legal");
            }

            //Prints Illegal if the placement is not valid
            else{
                System.out.println("Illegal");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
