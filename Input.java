import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is a wrapper class for inputs used in the program.
 */
public class Input {
  /**
   * Generate an InputStreamReader instance from a provided InputStream object.
   *
   * @param IS InputStream object
   * @return InputStreamReader instance
   */
  public static InputStreamReader generateISR(InputStream IS) {
    return new InputStreamReader(IS);
  }

  /**
   * Generate a BufferedReader instance from a provided InputStream object.
   * @param IS InputStream object
   * @return BufferedReader instance
   */
  public static BufferedReader generateBR(InputStream IS) {
    return new BufferedReader(generateISR(IS));
  }
}
