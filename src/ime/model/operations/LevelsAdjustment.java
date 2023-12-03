package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Adjusts the levels of each pixel by applying a quadratic equation, a(x^2) + bx + c, to each
 * channel of each pixel in the image being processed.
 */
public class LevelsAdjustment extends AbstractLinearTransformation implements ImageOperation {

  private final float a;
  private final float b;
  private final float c;

  /**
   * Find the a, b, and c values for the quadratic equation in order to be able to find the adjusted
   * values for each pixel.
   *
   * @param b The black point on the horizontal axis.
   * @param m The middle point on the horizontal axis.
   * @param w The white point on the horizontal axis.
   */
  public LevelsAdjustment(int b, int m, int w) {

    int bigA = ((b * b) * (m - w)) - (b * (m * m - w * w)) + (w * m * m) - (m * w * w);
    float aSubA = (-1 * b * (128 - 255)) + (128 * w) - (255 * m);
    float aSubB = (b * b * (128 - 255)) + (255 * m * m) - (128 * w * w);
    float aSubC = (b * b * ((255 * m) - (128 * w))) - (b * ((255 * m * m) - (128 * w * w)));

    this.a = aSubA / bigA;
    this.b = aSubB / bigA;
    this.c = aSubC / bigA;
  }

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    int red = image.getColor(i, j).getRed();
    return Math.round((a * red * red) + (b * red) + c);
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    int green = image.getColor(i, j).getGreen();
    return Math.round((a * green * green) + (b * green) + c);
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    int blue = image.getColor(i, j).getBlue();
    return Math.round((a * blue * blue) + (b * blue) + c);
  }

  @Override
  public String toString() {
    return String.format("<Levels a=\"%.3f\" b=\"%.3f\" c=\"%.3f\">", a, b, c);
  }
}
