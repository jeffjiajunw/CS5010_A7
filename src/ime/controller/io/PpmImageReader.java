package ime.controller.io;

import ime.model.color.ColorImpl;
import ime.model.image.IReadOnlyImageBuilder;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/** This class implements image reading for the PPM format. */
public class PpmImageReader implements ImageReader {
  /**
   * Read the input stream line-by-line, and return a string of the contents. This method throws
   * away comment lines.
   *
   * @param input the input stream.
   * @return the contents.
   */
  private static String getInputStreamContents(InputStream input) {
    Scanner scanner = new Scanner(input);
    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.charAt(0) != '#') {
        builder.append(line).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }

  @Override
  public ReadOnlyImage read(InputStream input) throws IllegalArgumentException {
    Objects.requireNonNull(input);

    String contents = getInputStreamContents(input);
    String[] values = contents.split("\\s+");

    // check if reading valid PPM file
    if (values.length < 4 || !values[0].equals("P3")) {
      throw new IllegalArgumentException("invalid PPM file: plain RAW file should begin with P3");
    }

    int width = Integer.parseInt(values[1]);
    int height = Integer.parseInt(values[2]);
    IReadOnlyImageBuilder image = new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);
    int numPixels = 3 * width * height;
    if (values.length - 4 != numPixels) {
      throw new IllegalArgumentException("missing values");
    }

    int index = 4;
    try {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (index == values.length) {
            break;
          }

          int r = Integer.parseInt(values[index]);
          int g = Integer.parseInt(values[index + 1]);
          int b = Integer.parseInt(values[index + 2]);
          image.setPixel(y, x, new ColorImpl(r, g, b));
          index += 3;
        }
      }
    } catch (NumberFormatException e) {
      // values were malformed
      throw new IllegalArgumentException(e);
    }

    // return the constructed image
    return image.build();
  }
}
