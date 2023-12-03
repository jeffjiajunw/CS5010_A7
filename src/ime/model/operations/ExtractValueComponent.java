package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Extract the value component by getting the maximum value of
 * a pixel's three channels, and then set the value for each
 * of the three channels to this maximum.
 */
public class ExtractValueComponent extends AbstractLinearTransformation implements ImageOperation {

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    return maxValue(i, j, image);
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    return maxValue(i, j, image);
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    return maxValue(i, j, image);
  }

  private int maxValue(int i, int j, ReadOnlyImage image) {

    int r = image.getColor(i, j).getRed();
    int g = image.getColor(i, j).getGreen();
    int b = image.getColor(i, j).getBlue();

    return Math.max(Math.max(r, g), b);
  }

}
