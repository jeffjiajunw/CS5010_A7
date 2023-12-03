package ime.model.operations;

import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;
import ime.model.color.Color;

/**
 * Flip the image horizontally without changing its
 * dimensions. Flipping horizontally can be achieved
 * by taking a pointer from the left and right sides
 * of the image, and then incrementally swapping their
 * pixel colors and moving them towards each other.
 * Once the left and right pointers move past each other,
 * move them down a row until there are no more rows left.
 */
public class HorizontalFlip implements ImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    int endJ;
    Color colorLeft;
    Color colorRight;

    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);
    for (int i = 0; i < height; i += 1) {

      endJ = width - 1;
      for (int j = 0; j <= endJ; j += 1, endJ -= 1) {

        colorLeft = image.getColor(i, j);
        colorRight = image.getColor(i, endJ);

        resImageBuilder.setPixel(i, endJ, colorLeft);
        resImageBuilder.setPixel(i, j, colorRight);

      }

    }

    return resImageBuilder.build();
  }

}
