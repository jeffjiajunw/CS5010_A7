package ime.controller;

import ime.model.image.ReadOnlyImage;
import ime.model.session.Session;

/** This class represents a mock session used to test the {@link TextController} class. */
public class MockSession implements Session {
  private final String name;

  /**
   * Create a new mock session with the given name.
   *
   * @param name the session name.
   */
  MockSession(String name) {
    this.name = name;
  }

  @Override
  public ReadOnlyImage getImage(String name) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void insertOrReplaceImage(String name, ReadOnlyImage image) {
    // do nothing
  }

  @Override
  public String toString() {
    return name;
  }
}
