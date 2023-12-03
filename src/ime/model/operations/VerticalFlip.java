package ime.model.operations;

import ime.model.color.Color;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;

/**
 * Flip the image vertically without changing its
 * dimensions. Flipping vertically can be achieved
 * by taking a pointer from the top and bottom sides
 * of the image, and then incrementally swapping their
 * pixel colors and moving them towards each other.
 * Once the top and bottom pointers move past each other,
 * move them one column to the right until there are no more
 * columns left.
 */
public class VerticalFlip implements ImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    int endI;
    Color colorBottom;
    Color colorTop;

    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);
    for (int j = 0; j < width; j += 1) {

      endI = height - 1;
      for (int i = 0; i <= endI; i += 1, endI -= 1) {

        colorBottom = image.getColor(i, j);
        colorTop = image.getColor(endI, j);

        resImageBuilder.setPixel(endI, j, colorBottom);
        resImageBuilder.setPixel(i, j, colorTop);

      }

    }

    return resImageBuilder.build();
  }

}
