package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Extract the intensity value by getting the average value of
 * a pixel's three channels, and then set the value for each
 * of the three channels to this average.
 */
public class ExtractIntensityComponent
        extends AbstractLinearTransformation
        implements ImageOperation {

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    return averageValue(i, j, image);
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    return averageValue(i, j, image);
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    return averageValue(i, j, image);
  }

  private int averageValue(int i, int j, ReadOnlyImage image) {

    int r = image.getColor(i, j).getRed();
    int g = image.getColor(i, j).getGreen();
    int b = image.getColor(i, j).getBlue();

    return Math.round((r + g + b) / 3f);
  }

}
