package ime.controller.io;

import ime.model.image.ReadOnlyImage;
import ime.util.Images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/** This class implements image reading for common files like PNG/JPEG/JPG. */
public class BufferedImageReader implements ImageReader {
  @Override
  public ReadOnlyImage read(InputStream inputStream) throws IllegalArgumentException {
    Objects.requireNonNull(inputStream);

    // try to read the file into a buffered image
    BufferedImage buffer;
    try {
      buffer = ImageIO.read(inputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("input could not be read");
    }

    // convert the buffer into our own model type
    return Images.fromBufferedImage(buffer);
  }
}
