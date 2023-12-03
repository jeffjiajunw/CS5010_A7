package ime.controller.io;

import ime.model.image.ReadOnlyImage;
import ime.util.Images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/** This class implements image writing for common files like PNG/JPEG/JPG. */
public class BufferedImageWriter implements ImageWriter {
  private final String formatName;

  /**
   * Construct a new buffered image writer.
   *
   * @param formatName the name of the output format, typically "jpg", "jpeg", or "png".
   */
  public BufferedImageWriter(String formatName) {
    this.formatName = formatName;
  }

  @Override
  public void write(OutputStream output, ReadOnlyImage image) throws IllegalArgumentException {
    Objects.requireNonNull(output);
    Objects.requireNonNull(image);

    BufferedImage buffer = Images.fromReadOnlyImage(image);
    try {
      ImageIO.write(buffer, formatName, output);
      output.flush();
      output.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("image could not be written");
    }
  }
}
