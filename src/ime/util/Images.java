package ime.util;

import java.awt.image.BufferedImage;
import java.util.Objects;

import ime.model.color.ColorImpl;
import ime.model.image.IReadOnlyImageBuilder;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;

import static ime.util.Colors.blueFrom;
import static ime.util.Colors.greenFrom;
import static ime.util.Colors.redFrom;

/**
 * Images provides methods to convert a BufferedImage
 * to a ReadOnly image, and vice versa. This facilitates
 * performing operations on an image's pixels and then
 * displaying it to the user.
 */
public class Images {

  /**
   * Convert a ReadOnlyImage to a BufferedImage.
   *
   * @param image The ReadOnlyImage to be converted.
   * @return BufferedImage    The BufferedImage that can be passed
   *                          to the client/user.
   */
  public static BufferedImage fromReadOnlyImage(ReadOnlyImage image) {
    Objects.requireNonNull(image);
    BufferedImage buffer =
            new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        buffer.setRGB(x, y, image.getPixel(y, x));
      }
    }
    return buffer;
  }

  /**
   * Convert a BufferedImage to a ReadOnlyImage.
   *
   * @param buffer The BufferedImage to be converted.
   * @return ReadOnlyImage     The ReadOnlyImage which can have its
   *                           pixels manipulated.
   */
  public static ReadOnlyImage fromBufferedImage(BufferedImage buffer) {
    Objects.requireNonNull(buffer);
    IReadOnlyImageBuilder image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(buffer.getHeight(), buffer.getWidth());
    for (int x = 0; x < buffer.getWidth(); x++) {
      for (int y = 0; y < buffer.getHeight(); y++) {
        int b = buffer.getRGB(x, y);
        image.setPixel(y, x, new ColorImpl(redFrom(b), greenFrom(b), blueFrom(b)));
      }
    }

    // return the final image
    return image.build();
  }
}
