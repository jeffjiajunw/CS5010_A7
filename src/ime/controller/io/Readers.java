package ime.controller.io;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** This is a utility class that allows for ImageReader maps to easily be created. */
public class Readers {
  /**
   * Get the map of readers, from their extensions.
   *
   * @param args the collection of file extensions (such as "ppm", "jpeg", "jpg", or "png").
   * @return the reader supplier map.
   */
  public static Map<String, Supplier<ImageReader>> get(String... args) {
    Map<String, Supplier<ImageReader>> readers = new HashMap<>();
    for (String arg : args) {
      if (arg.equals("ppm")) {
        readers.put("ppm", PpmImageReader::new);
      } else if (arg.equals("jpeg") || arg.equals("jpg") || arg.equals("png")) {
        readers.put(arg, BufferedImageReader::new);
      }
    }
    return readers;
  }
}
