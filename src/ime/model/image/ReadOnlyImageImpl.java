package ime.model.image;

import java.util.Arrays;
import java.util.Objects;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.util.Colors;

/**
 * ReadOnlyImageImpl has a height, width, and color matrix
 * that are initialized at runtime and cannot be modified.
 * The height and width can be retrieved to determine
 * an image's size. Furthermore, every ReadOnlyImageImpl
 * object will have a Color[][] matrix that represents each
 * RGB value for each pixel in the image. This color matrix's
 * pixels can be set at any point, and its colors can only
 * be retrieved via getColor.
 */
public class ReadOnlyImageImpl implements ReadOnlyImage {

  private final int height;
  private final int width;
  private final Color[][] imagePixels;

  /**
   * Initialize the image's height, width,
   * and color matrix.
   *
   * @param height      The first length parameter of the image.
   * @param width       The second length parameter of the image.
   * @param imagePixels The matrix of all pixels and their colors.
   */
  private ReadOnlyImageImpl(int height, int width, Color[][] imagePixels) {

    this.height = height;
    this.width = width;
    this.imagePixels = imagePixels;
  }

  @Override
  public int getHeight() {

    return height;
  }

  @Override
  public int getWidth() {

    return width;
  }

  @Override
  public Color getColor(int i, int j) {

    Color currColor = imagePixels[i][j];

    int r = currColor.getRed();
    int g = currColor.getGreen();
    int b = currColor.getBlue();

    Color colorCopy = new ColorImpl(r, g, b);

    return colorCopy;
  }

  @Override
  public int getPixel(int i, int j) {

    Color currColor = imagePixels[i][j];

    int r = currColor.getRed();
    int g = currColor.getGreen();
    int b = currColor.getBlue();

    return Colors.fromRgb(r, g, b);
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (!(o instanceof ReadOnlyImageImpl)) {
      return false;
    }

    ReadOnlyImageImpl that = (ReadOnlyImageImpl) o;

    if ((that.getHeight() != height) || (that.getWidth() != width)) {
      return false;
    }

    Color thisPixel;
    Color otherPixel;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        thisPixel = this.getColor(i, j);
        otherPixel = that.getColor(i, j);

        if (!thisPixel.equals(otherPixel)) {
          return false;
        }

      }

    }

    return true;
  }

  @Override
  public int hashCode() {

    return Objects.hash(
            height,
            width,
            Arrays.deepHashCode(getImagePixelsForHash())
    );
  }

  private int[][][] getImagePixelsForHash() {

    int[][][] imagePixelRGBArray = new int[height][width][3];

    int r;
    int g;
    int b;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = getColor(i, j).getRed();
        g = getColor(i, j).getGreen();
        b = getColor(i, j).getBlue();

        imagePixelRGBArray[i][j] = new int[]{r, g, b};

      }
    }

    return imagePixelRGBArray;
  }

  /**
   * Builds the ReadOnlyImageImpl by constructing the Color matrix, and initializing
   * the height and width. This builder returns an immutable version of the
   * ReadOnlyImageImpl.
   */
  public static class ReadOnlyImageBuilder implements IReadOnlyImageBuilder {

    private final int height;
    private final int width;
    private int setPixelCount;
    private final Color[][] imagePixels;

    /**
     * Initialize the height and width so that the Color
     * matrix can be initialized. Set the pixel count to
     * 0 to keep track of whether all of the pixels in the
     * image have been set.
     *
     * @param height          The height of the image.
     * @param width           The width of the image.
     */
    public ReadOnlyImageBuilder(int height, int width) {

      this.height = height;
      this.width = width;
      imagePixels = new Color[height][width];
      setPixelCount = 0;
    }

    @Override
    public IReadOnlyImageBuilder setPixel(int i, int j, Color color) {

      if (imagePixels[i][j] == null) {

        setPixelCount += 1;
      }

      imagePixels[i][j] = color;
      return this;
    }

    @Override
    public ReadOnlyImage build() {

      if (height <= 0) {

        throw new IllegalStateException("Height has to be greater than 0!");
      }

      if (width <= 0) {

        throw new IllegalStateException("Width has to be greater than 0!");
      }

      if (setPixelCount != (height * width)) {

        throw new IllegalStateException("All pixels have to be set before building!");
      }

      return new ReadOnlyImageImpl(height, width, imagePixels);
    }

  }

}
