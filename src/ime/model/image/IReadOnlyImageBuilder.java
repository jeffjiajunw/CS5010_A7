package ime.model.image;

import ime.model.color.Color;

/**
 * This interface helps initialize the ReadOnlyImage by providing
 * the methods needed to set the pixel values of a ReadOnlyImage.
 * Before the image is either copied to another builder, passed to
 * the controller, etc., it needs to have all of its pixels set and
 * then built. This way, all of the pixels in the image can be set
 * before it becomes immutable.
 */
public interface IReadOnlyImageBuilder {

  /**
   * Set this builder's pixel to the specified value.
   *
   * @param i     the ith row of the pixel.
   * @param j     the jth column of the pixel.
   * @param color the new color.
   * @throws IllegalArgumentException if either the value or position are invalid for this builder.
   */
  IReadOnlyImageBuilder setPixel(int i, int j, Color color) throws IllegalArgumentException;

  /**
   * Get this builder's pixel of specified value.
   *
   * @param i     the ith row of the pixel.
   * @param j     the jth column of the pixel.
   * @throws IllegalArgumentException if position are invalid for this builder.
   */
  int getPixel(int i, int j) throws IllegalArgumentException;

  /**
   * Complete building the ReadOnlyImage object.
   *
   * @return ReadOnlyImage This is the new immutable Image object.
   */
  ReadOnlyImage build();
}
