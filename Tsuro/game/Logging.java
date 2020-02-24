package game;

/**
 * class for switchable printing
 * if LOGGING_ON = true, then this program prints
 * if LOGGIN_ON = false, it doesn't
 * @author bhend
 *
 */
public class Logging {
	
	private static final boolean LOGGING_ON = true;

	public static void print(String s) {
		if (LOGGING_ON) {
			System.out.println(s);
		}
	}
}
