package ime.controller.io;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** This is a utility class that allows for ImageWriter maps to easily be created. */
public class Writers {
  /**
   * Get the map of writers, from their extensions.
   *
   * @param args the collection of file extensions (such as "ppm", "jpeg", "jpg", or "png").
   * @return the writer supplier map.
   */
  public static Map<String, Supplier<ImageWriter>> get(String... args) {
    Map<String, Supplier<ImageWriter>> readers = new HashMap<>();
    for (String arg : args) {
      if (arg.equals("ppm")) {
        readers.put("ppm", PpmImageWriter::new);
      } else if (arg.equals("jpeg") || arg.equals("jpg") || arg.equals("png")) {
        readers.put(arg, () -> new BufferedImageWriter(arg));
      }
    }
    return readers;
  }
}
