package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Return the weighted sum of the pixel's current values and set this
 * weight sum to the new value of each channel.
 */
public class ExtractLumaComponent extends AbstractLinearTransformation implements ImageOperation {

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    return weightedSum(i, j, image);
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    return weightedSum(i, j, image);
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    return weightedSum(i, j, image);
  }

  private int weightedSum(int i, int j, ReadOnlyImage image) {

    int r = image.getColor(i, j).getRed();
    int g = image.getColor(i, j).getGreen();
    int b = image.getColor(i, j).getBlue();

    return Math.round((0.2126f * r)
            + (0.7152f * g)
            + (0.0722f * b));
  }

}
