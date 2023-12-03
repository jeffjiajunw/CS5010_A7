package ime.controller;

import ime.controller.command.Command;
import ime.model.color.Color;
import ime.model.image.ReadOnlyImage;

/** This class represents a mock image used to test the {@link Command} interface. */
public class MockImage implements ReadOnlyImage {
  private final String name;

  /**
   * Construct a new mock image with the given name.
   *
   * @param name the name.
   */
  public MockImage(String name) {
    this.name = name;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public Color getColor(int i, int j) throws IllegalArgumentException {
    return null;
  }

  @Override
  public int getPixel(int i, int j) {
    return 0;
  }

  @Override
  public String toString() {
    return name;
  }
}
