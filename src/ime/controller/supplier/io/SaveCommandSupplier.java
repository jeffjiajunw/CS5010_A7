package ime.controller.supplier.io;

import ime.controller.command.Command;
import ime.controller.command.SaveCommand;
import ime.controller.io.ImageWriter;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/** This class supplies save commands. */
public class SaveCommandSupplier extends IOCommandSupplier {
  private final Map<String, Supplier<ImageWriter>> writers;

  /**
   * Construct a new save command supplier.
   *
   * @param writers the map of writers this supplier's save commands should support.
   */
  public SaveCommandSupplier(Map<String, Supplier<ImageWriter>> writers) {
    this.writers = Objects.requireNonNull(writers);
  }

  @Override
  protected Command build(String imagePath, String imageName) throws IllegalArgumentException {
    // create an image writer for the output stream
    String formatType = getFormatType(imagePath).toLowerCase();
    if (!writers.containsKey(formatType)) {
      throw new IllegalArgumentException("file not supported");
    } else {
      ImageWriter writer = writers.get(formatType).get();
      return new SaveCommand(imagePath, imageName, writer);
    }
  }
}
