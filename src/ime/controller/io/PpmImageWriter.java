package ime.controller.io;

import ime.model.image.ReadOnlyImage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import static ime.util.Colors.blueFrom;
import static ime.util.Colors.greenFrom;
import static ime.util.Colors.redFrom;

/** This class implements image writing for the PPM format. */
public class PpmImageWriter implements ImageWriter {
  @Override
  public void write(OutputStream output, ReadOnlyImage image) throws IllegalArgumentException {
    Objects.requireNonNull(output);
    Objects.requireNonNull(image);

    try {
      OutputStreamWriter writer = new OutputStreamWriter(output);
      writer.append("P3\n# Made by IME\n");
      writer.append(String.format("%d\n", image.getWidth()));
      writer.append(String.format("%d\n", image.getHeight()));
      writer.append("255\n");
      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          int r = redFrom(image.getPixel(y, x));
          int g = greenFrom(image.getPixel(y, x));
          int b = blueFrom(image.getPixel(y, x));
          writer.append(String.format("%d\n%d\n%d\n", r, g, b));
        }
      }
      writer.close();
    } catch (IOException e) {
      // could not write to the output stream
      throw new IllegalArgumentException("image could not be written");
    }
  }
}
