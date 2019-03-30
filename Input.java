import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Input {
  public static InputStreamReader generateISR(InputStream IS) {
    return new InputStreamReader(IS);
  }

  public static BufferedReader generateBR(InputStream IS) {
    return new BufferedReader(generateISR(IS));
  }
}
