package ime.model.session;

import java.util.HashMap;
import java.util.Map;

import ime.model.image.ReadOnlyImage;

/**
 * Store and save images by their name so that
 * they can be accessed later on in the program.
 */
public class SessionImpl implements Session {

  private Map<String, ReadOnlyImage> imageMap;

  /**
   * Instantiate the HashMap in order to be able
   * to store images throughout the course of the
   * program.
   */
  public SessionImpl() {

    imageMap = new HashMap<String, ReadOnlyImage>();
  }

  @Override
  public ReadOnlyImage getImage(String name) throws IllegalArgumentException {

    if (!imageMap.containsKey(name)) {
      throw new IllegalArgumentException("Image has not been loaded into the session!");
    }

    return imageMap.get(name);
  }

  @Override
  public void insertOrReplaceImage(String name, ReadOnlyImage image) {

    imageMap.put(name, image);
  }

}
