package ime.model.operations;

import ime.model.image.ReadOnlyImage;

/**
 * ThreeImageOperation allows for implementations
 * to utilize three images to create the resulting
 * image.
 */
public interface ThreeImageOperation {

  /**
   * Take in three images and process them to
   * create a single image.
   *
   * @param firstImage  The first image to be processed.
   * @param secondImage The second image to be processed.
   * @param thirdImage  The third image to be processed.
   * @return resImage       The resulting image that is created
   *                        after processing.
   */
  public ReadOnlyImage apply(ReadOnlyImage firstImage,
                             ReadOnlyImage secondImage,
                             ReadOnlyImage thirdImage);
}
