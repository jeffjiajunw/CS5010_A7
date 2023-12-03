package ime.controller;

import ime.model.image.ReadOnlyImage;
import ime.model.operations.ImageOperation;

import java.util.Objects;

/** This class mocks the {@link ImageOperation} interface. */
public class MockImageOperation implements ImageOperation {
  private final String name;

  /**
   * Construct a new mock.
   *
   * @param name the name of the image.
   */
  public MockImageOperation(String name) {
    this.name = Objects.requireNonNull(name);
  }

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {
    return image;
  }

  @Override
  public String toString() {
    return name;
  }
}
