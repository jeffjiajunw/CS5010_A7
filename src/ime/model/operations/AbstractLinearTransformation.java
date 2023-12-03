package ime.model.operations;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;

/**
 * AbstractLinearTransformation facilitates the retrieval of
 * colors for the three channels. The calculation of values for
 * the channels involves simple, linear operations.
 */
public abstract class AbstractLinearTransformation implements ImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    ReadOnlyImageImpl.ReadOnlyImageBuilder componentImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    Color tempColor;
    int r;
    int g;
    int b;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = findRed(i, j, image);
        g = findGreen(i, j, image);
        b = findBlue(i, j, image);

        tempColor = new ColorImpl(r, g, b);
        componentImageBuilder.setPixel(i, j, tempColor);

      }

    }

    return componentImageBuilder.build();
  }

  /**
   * Find the new value of the red component
   * at the current pixel for the current image.
   *
   * @param i     The ith row of the current pixel.
   * @param j     The jth column of the current pixel.
   * @param image The current image being processed.
   * @return redValue   The new value for the red channel of
   *                    the pixel in this position.
   */
  public abstract int findRed(int i, int j, ReadOnlyImage image);

  /**
   * Find the new value of the green component
   * at the current pixel for the current image.
   *
   * @param i     The ith row of the current pixel.
   * @param j     The jth column of the current pixel.
   * @param image The current image being processed.
   * @return greenValue The new value for the green channel of
   *                    the pixel in this position.
   */
  public abstract int findGreen(int i, int j, ReadOnlyImage image);

  /**
   * Find the new value of the blue component
   * at the current pixel for the current image.
   *
   * @param i     The ith row of the current pixel.
   * @param j     The jth column of the current pixel.
   * @param image The current image being processed.
   * @return blueValue  The new value for the blue channel of
   *                    the pixel in this position.
   */
  public abstract int findBlue(int i, int j, ReadOnlyImage image);

}
