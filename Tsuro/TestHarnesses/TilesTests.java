import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import java.util.Scanner;

public class TilesTests {


    /**
     * Takes the string and returns the corresponding index we use to represent ports
     * @param s
     * @return
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
     * Takes the index and returns the corresponding String the tests use to represent ports
     * @param i
     * @return
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
     *  Takes the rotation angle and returns the corresponding index we use to represent rotation
     * @param i
     * @return
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
     * Takes a Tile index, degree, and port and returns what the output port would be.
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                //Building an iterator to parse through the given JSON input
                String line = scanner.nextLine();
                Object obj = new JSONParser().parse(line);
                JSONArray ja = (JSONArray) obj;
                Iterator itr = ja.iterator();
                long tileIndex = (Long) itr.next();

                //Checks if provided tileIndex is valid
                if(tileIndex > 34 || tileIndex < 0){
                    throw new IllegalArgumentException("Tile Index must be between 0 and 34");
                }

                //Checks if provided degree is valid
                long degrees = (Long) itr.next();
                if(degrees != 0 && degrees != 90 && degrees != 180 && degrees != 270){
                    throw new IllegalArgumentException("Degree must be 0, 90, 180, or 270");
                }

                //Formates rotation to our rotation index
                int rotationNum = getRotation(degrees);

                //Checks if provided port is valid
                String portString = itr.next().toString();
                if(portString.equals("A") && portString.equals("B") && portString.equals("C") && portString.equals("D") &&
                        portString.equals("E") && portString.equals("F") && portString.equals("G") && portString.equals("H")){
                    throw new IllegalArgumentException("Port must be 'A', 'B', 'C', 'D', 'E', 'F', 'G', or 'H'");
                }
                int portInt = getIndex(portString);

                //Returns the corresponding tile of the TileIndex from the 35 tiles txt file
                InputStream is = TilesTests.class.getResourceAsStream("35Tiles.txt");
                Scanner inFile = new Scanner(is);
                JSONArray jaTile = new JSONArray();
                while(inFile.hasNextLine()){
                    String lineOf35 = inFile.next();
                    Object objOf35 = new JSONParser().parse(lineOf35);
                    JSONArray jaOf35 = (JSONArray) objOf35;
                    Iterator itrOf35 = jaOf35.iterator();
                    long numOf35 = (Long) itrOf35.next();
                    if(numOf35 == tileIndex){
                        jaTile = (JSONArray) itrOf35.next();
                        break;
                    }
                }


                //Takes the Tile and turns it into our representation of tiles (with ints).
                ArrayList<Integer> tileOfInts = new ArrayList<>();
                Iterator itrOfTile = jaTile.iterator();
                for(int i = 0; i < 4; i++){
                    JSONArray jaTwoPorts = (JSONArray) itrOfTile.next();
                    String portString1 = (String) jaTwoPorts.get(0);
                    int portInt1 = getIndex(portString1);
                    String portString2 = (String) jaTwoPorts.get(1);
                    int portInt2 = getIndex(portString2);

                    tileOfInts.add(portInt1);
                    tileOfInts.add(portInt2);
                }


                //Makes a Tile of the array formed above
                Tile finalTile = new Tile(tileOfInts);

                //Rotates the tile with the appropriate rotation
                finalTile.rotate(rotationNum);

                //Gets the string of the exit port
                String exitPortString = getPortLetter(finalTile.getPort(portInt));


                //Builds a JSONArray of the final output to be printed out
                JSONArray finalOutput = new JSONArray();

                finalOutput.add("if ");
                finalOutput.add(portString);
                finalOutput.add(" is the entrance, ");
                finalOutput.add(exitPortString);
                finalOutput.add(" is the exit.");

                System.out.println(finalOutput);

            } catch (Exception ex) {
                //incorrectly formed JSON expression
                //ex.printStackTrace();
                scanner.close();
                System.out.println("End of input");
                break;
            }
        }
    }

}