package ime.model.image;

import ime.model.color.Color;

/**
 * This interface represents a ReadOnlyImage, i.e. a two-dimensional
 * sequence of pixels. Each pixel is a sequence of bits stored in an
 * integer, but the number/structure of bits stored in an image may differ
 * between various implementations to fit specific needs.
 */
public interface ReadOnlyImage {
  /**
   * Get the width of this image, in pixels.
   *
   * @return the width.
   */
  int getWidth();

  /**
   * Get the height of this image, in pixels.
   *
   * @return the height.
   */
  int getHeight();

  /**
   * Get this image's color data at the specified (x, y) position.
   *
   * @param i the ith row of the pixel.
   * @param j the jth column of the pixel.
   * @return the pixel.
   * @throws IllegalArgumentException if the position is invalid for this image.
   */
  Color getColor(int i, int j) throws IllegalArgumentException;

  /**
   * Get this image's pixel data as an integer at the specified (x, y) position.
   *
   * @param i the ith row of the pixel.
   * @param j the jth column of the pixel.
   * @return the pixel.
   * @throws IllegalArgumentException if the position is invalid for this image.
   */
  int getPixel(int i, int j) throws IllegalArgumentException;
}
