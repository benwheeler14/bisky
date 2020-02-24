import java.lang.reflect.Array;
import java.util.*;
import java.io.InputStream;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class BoardTests {

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
     * Converts one of our indicies (0 to 7) to a port letter (A to H)
     * @param i index 0 to 7
     * @return port letter
     */
    public static String getPortLetter(int i){
        switch(i){
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            default:
                throw new IllegalArgumentException("Invalid Port Int");
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
     * Converts one of our rotations (0,1,2,3) to degrees (0,90,180,270)
     * @param i rotation
     * @return degree
     */
    public static int getDegrees(int i){
        switch(i){
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
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

        //
        InputStream is = BoardTests.class.getResourceAsStream("35Tiles.txt");
        Scanner inFile = new Scanner(is);
        JSONArray jaTile = new JSONArray();

        while (inFile.hasNextLine()) {
            String lineOf35 = inFile.next();
            Object objOf35 = new JSONParser().parse(lineOf35);
            JSONArray jaOf35 = (JSONArray) objOf35;
            Iterator itrOf35 = jaOf35.iterator();
            long numOf35 = (Long) itrOf35.next();
            if (numOf35 == tileIndex) {
                jaTile = (JSONArray) itrOf35.next();
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
     * Gets the index (one of the 35 in the txt file) of the given tile
     * @param tile Tile
     * @return index (0-34)
     * @throws ParseException if the file can't be found
     */
    public static int getTileIndex(Tile tile) throws ParseException{
        InputStream is = BoardTests.class.getResourceAsStream("35Tiles.txt");
        Scanner inFile = new Scanner(is);
        JSONArray jaTile = new JSONArray();
        long index = 0;
        int rotation = tile.rotation;
        tile.rotation = 0;
        List<Integer> listOfPorts = tile.getListOfPorts();

        while (inFile.hasNextLine()) {
            String lineOf35 = inFile.next();
            Object objOf35 = new JSONParser().parse(lineOf35);
            JSONArray jaOf35 = (JSONArray) objOf35;
            JSONArray jaOfPorts = (JSONArray) jaOf35.get(1);
            boolean isTheSame = true;
            int i = 0;
            for(Object objOfPair : jaOfPorts){
                if(isTheSame) {
                    JSONArray jaOfPair = (JSONArray) objOfPair;
                    int portOne = getIndex((String)jaOfPair.get(0));
                    int portTwo = getIndex((String)jaOfPair.get(1));
                    if ((listOfPorts.get(i) != portOne || listOfPorts.get(i + 1) != portTwo) &&
                            (listOfPorts.get(i) != portTwo || listOfPorts.get(i + 1) != portOne)) {
                        isTheSame = false;
                    }
                }
                i = i + 2;
            }
            if(isTheSame){
                index = (long)jaOf35.get(0);
            }
        }
        tile.rotation = rotation;
        return (int)index;
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

            try {
                //Building an iterator to parse through the given JSON input
                String line = scanner.nextLine();
                Object obj = new JSONParser().parse(line);
                JSONArray jaWhole = (JSONArray) obj;
                Iterator itrWhole = jaWhole.iterator();


                //Makes two arrays of the given JSONArray of placements. One of initial placements and one of intermediate
                ArrayList<JSONArray> initialPlacements = new ArrayList<>();
                ArrayList<JSONArray> intermediatePlacements = new ArrayList<>();

                while(itrWhole.hasNext()) {
                    JSONArray jaIndividual = (JSONArray) itrWhole.next();

                    if (jaIndividual.size() == 6) {
                        initialPlacements.add(jaIndividual);
                    }

                    else if (jaIndividual.size() == 5) {
                       intermediatePlacements.add(jaIndividual);
                    }

                    else {
                        throw new IllegalArgumentException("Invalid input. Array is not of size 6 (for initial) or 5 (for intermediate)");
                    }
                }

                //Goes through initial placements and places them on the board
                for(JSONArray jaInitial : initialPlacements) {
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
                for(JSONArray jaIntermediate : intermediatePlacements){
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



                //JSONArray that will build the system output
                JSONArray finalOutput = new JSONArray();


                //Makes an array of all the player colors (names) on the board
                ArrayList<String> playerNames = new ArrayList<>();
                for(PlayerToken playerToken : b.playerTokens) {
                    playerNames.add(playerToken.getName());
                }

                //Checks if there are colors (names) in the intermediate placements that weren't in the initial
                //Placements. Outputs them as "never played"
                for(JSONArray jaIntermediate : intermediatePlacements){
                    if(!playerNames.contains(jaIntermediate.get(0))){
                        JSONArray partOfOutput = new JSONArray();
                        partOfOutput.add(jaIntermediate.get(0));
                        partOfOutput.add(" never played");
                        finalOutput.add(partOfOutput);
                    }
                }


                //Goes through the player tokens and checks and outputs if they collided, exited, or are
                //still in play and where
                for(PlayerToken playerToken : b.playerTokens){
                    JSONArray partOfOutput = new JSONArray();

                    if(b.portOnEdge(playerToken.getLocation(), playerToken.getPort())){
                        if(b.checkCollision(playerToken)){
                            partOfOutput.add(playerToken.getName());
                            partOfOutput.add(" collided");
                            finalOutput.add(partOfOutput);
                        }
                        else{
                            partOfOutput.add(playerToken.getName());
                            partOfOutput.add(" exited");
                            finalOutput.add(partOfOutput);
                        }
                    }
                    else{
                        partOfOutput.add(playerToken.getName());
                        partOfOutput.add(getTileIndex(b.getTile(playerToken.getLocation())));
                        partOfOutput.add(getDegrees(b.getTile(playerToken.getLocation()).rotation));
                        partOfOutput.add(getPortLetter(playerToken.getPort()));
                        partOfOutput.add(playerToken.getLocation().x());
                        partOfOutput.add(playerToken.getLocation().y());
                        finalOutput.add(partOfOutput);
                    }

                }

                //Outputs each players final position/how they lost
                    System.out.println(finalOutput);
            }
            catch(Exception e){
                e.printStackTrace();
            }

    }
}
