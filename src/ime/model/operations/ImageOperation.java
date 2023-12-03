package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * Every operation that can be performed on an
 * image should be a function object that,
 * when apply is called, applies its transformation
 * to the image's pixels. It should then return a
 * new image that has been transformed.
 */
public interface ImageOperation {

  /**
   * Apply the function object's operation
   * to the current image. Return a new
   * image that is a result of the
   * transformation.
   *
   * @param image The current image that is going
   *              to be transformed.
   * @return resImage     The new image that is a result of
   *                      the transformation.
   */
  public ReadOnlyImage apply(ReadOnlyImage image);
}
