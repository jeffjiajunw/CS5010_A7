package ime.controller.supplier.io;

import ime.controller.command.Command;
import ime.controller.command.LoadCommand;
import ime.controller.io.ImageReader;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/** This class supplies load commands. */
public class LoadCommandSupplier extends IOCommandSupplier {
  private final Map<String, Supplier<ImageReader>> readers;

  /**
   * Construct a new load command supplier.
   *
   * @param readers the map of readers this supplier's load commands should support.
   */
  public LoadCommandSupplier(Map<String, Supplier<ImageReader>> readers) {
    this.readers = Objects.requireNonNull(readers);
  }

  @Override
  protected Command build(String imagePath, String imageName) {
    String formatType = getFormatType(imagePath).toLowerCase();
    if (!readers.containsKey(formatType)) {
      throw new IllegalArgumentException("file type not supported");
    } else {
      ImageReader reader = readers.get(formatType).get();
      return new LoadCommand(imagePath, imageName, reader);
    }
  }
}
