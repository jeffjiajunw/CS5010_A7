package ime.model.session;

import ime.model.image.ReadOnlyImage;

/**
 * This interface represents a working session in the IME program, where users can insert,
 * manipulate, and read images.
 */
public interface Session {
  /**
   * Gets the image associated with a given name in this session.
   *
   * @param name the image name.
   * @return the image.
   */
  ReadOnlyImage getImage(String name) throws IllegalArgumentException;

  /**
   * Inserts a new image into this working session, or replaces an existing image if one already
   * exists with the same name.
   *
   * @param name the image name.
   * @param image the image.
   */
  void insertOrReplaceImage(String name, ReadOnlyImage image);
}
