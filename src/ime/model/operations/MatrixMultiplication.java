package ime.model.operations;

import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;

/**
 * Takes in a matrix and multiplies it by the channels of each pixel
 * of the image. The new resultant matrix represents the new values
 * of the image pixel's colors.
 */
public abstract class MatrixMultiplication implements ImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();
    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    int r;
    int g;
    int b;
    int[] newColors;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = image.getColor(i, j).getRed();
        g = image.getColor(i, j).getGreen();
        b = image.getColor(i, j).getBlue();

        newColors = multiplyMatrices(r, g, b);

        r = newColors[0];
        g = newColors[1];
        b = newColors[2];

        resImageBuilder.setPixel(i, j, new ColorImpl(r, g, b));

      }

    }

    return resImageBuilder.build();
  }

  /**
   * Take in a 3x3 matrix and multiply it by the RGB values
   * of the current pixel. The new resultant matrix represents
   * the new RGB values.
   *
   * @param r           The red value of the current pixel.
   * @param g           The green value of the current pixel.
   * @param b           The blue value of the current pixel.
   * @return resMatrix  The 1x3 matrix that represents the new
   *                    RGB values.
   */
  public int[] multiplyMatrices(int r, int g, int b) {

    float[][] transformMatrix = getTransformationMatrix();
    int[] resMatrix = new int[3];

    int height = transformMatrix.length;

    for (int i = 0; i < height; i += 1) {

      resMatrix[i] = Math.round(
              (transformMatrix[i][0] * r)
                      + (transformMatrix[i][1] * g)
                      + (transformMatrix[i][2] * b));

    }

    return resMatrix;
  }

  protected abstract float[][] getTransformationMatrix();

}
