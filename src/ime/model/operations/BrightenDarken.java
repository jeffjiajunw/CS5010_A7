package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Brightens or darkens the image by adding a constant factor (can be positive or negative) to each
 * pixel's channels.
 */
public class BrightenDarken extends AbstractLinearTransformation implements ImageOperation {

  private final int factor;

  /**
   * Initialize the factor by which each pixel's three channels should be incremented/decremented.
   *
   * @param factor The factor cannot be changed after initialization.
   */
  public BrightenDarken(int factor) {

    this.factor = factor;
  }

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    int r = image.getColor(i, j).getRed();

    return r + factor;
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    int g = image.getColor(i, j).getGreen();

    return g + factor;
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    int b = image.getColor(i, j).getBlue();

    return b + factor;
  }

  @Override
  public String toString() {
    return String.format("<Brighten factor=%d>", factor);
  }
}
