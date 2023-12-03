package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Extract the red component of the current image by
 * returning 0 for the other channels and returning the
 * red channel's current value for the new red channel.
 */
public class ExtractRedComponent extends AbstractLinearTransformation implements ImageOperation {

  @Override
  public int findRed(int i, int j, ReadOnlyImage image) {

    return image.getColor(i, j).getRed();
  }

  @Override
  public int findGreen(int i, int j, ReadOnlyImage image) {

    return 0;
  }

  @Override
  public int findBlue(int i, int j, ReadOnlyImage image) {

    return 0;
  }

}
