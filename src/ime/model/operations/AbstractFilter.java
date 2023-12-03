package ime.model.operations;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;

/**
 * The abstract filter applies a matrix filter to each pixel
 * within an image. The kernel's center goes through every
 * pixel in the image and applies the filter by multiplying
 * each channel of each pixel that overlaps, and then adding
 * together the sum of each channel's products.
 */
public abstract class AbstractFilter implements ImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        Color currColor = getColorFromFilter(i, j, image);
        resImageBuilder.setPixel(i, j, currColor);

      }

    }

    return resImageBuilder.build();
  }

  /**
   * Go through the entire matrix filter and check whether
   * each of its pixels overlap with that of the image's.
   * Only multiply pixels that overlap and add those to the
   * sum for each distinct channel.
   *
   * @param i         The ith row of the current pixel.
   * @param j         The jth column of the current pixel.
   * @param image     The current image being filtered.
   * @return color    The new color as a result of the sum-product.
   */
  public Color getColorFromFilter(int i, int j, ReadOnlyImage image) {

    float[][] filter = provideKernel();
    int[] kernelCenter = provideKernelCenterCoord();
    int centerI = kernelCenter[0];
    int centerJ = kernelCenter[1];

    int distX;
    int distY;
    int imageJ;
    int imageI;
    float currSumRed = 0;
    float currSumGreen = 0;
    float currSumBlue = 0;
    for (int y = 0; y < filter.length; y += 1) {

      for (int x = 0; x < filter[y].length; x += 1) {

        distX = centerJ - x;
        distY = centerI - y;
        imageJ = j - distX;
        imageI = i - distY;

        if (checkValidCoord(imageI, imageJ, image)) {

          currSumRed += filter[y][x] * image.getColor(imageI, imageJ).getRed();
          currSumGreen += filter[y][x] * image.getColor(imageI, imageJ).getGreen();
          currSumBlue += filter[y][x] * image.getColor(imageI, imageJ).getBlue();
        }

      }

    }

    return new ColorImpl(Math.round(currSumRed),
                         Math.round(currSumGreen),
                         Math.round(currSumBlue));
  }

  protected abstract float[][] provideKernel();

  protected abstract int[] provideKernelCenterCoord();

  private boolean checkValidCoord(int i, int j, ReadOnlyImage image) {

    int minX = 0;
    int minY = 0;
    int maxX = image.getWidth() - 1;
    int maxY = image.getHeight() - 1;

    if ((j > maxX) || (j < minX)) {

      return false;
    }

    return (i <= maxY) && (i >= minY);
  }

}
