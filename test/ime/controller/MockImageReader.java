package ime.controller;

import ime.controller.command.LoadCommand;
import ime.controller.io.ImageReader;
import ime.model.image.ReadOnlyImage;

import java.io.InputStream;
import java.util.Scanner;

/** This class represents a mock image reader used to test the {@link LoadCommand} class. */
public class MockImageReader implements ImageReader {
  private final String imageName;
  private final StringBuilder log;

  /**
   * Construct a new reader that constructs mock images with a given name.
   *
   * @param imageName the image name.
   * @param log the log.
   */
  public MockImageReader(String imageName, StringBuilder log) {
    this.imageName = imageName;
    this.log = log;
  }

  @Override
  public ReadOnlyImage read(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
    log.append(scanner.hasNext() ? scanner.next() : "");
    return new MockImage(imageName);
  }

  @Override
  public String toString() {
    return "mock reader";
  }
}
