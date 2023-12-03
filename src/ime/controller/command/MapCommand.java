package ime.controller.command;

import ime.controller.supplier.CommandSupplier;
import ime.model.image.ReadOnlyImage;
import ime.model.operations.ImageOperation;
import ime.model.session.Session;

import java.util.Objects;
import java.util.function.Supplier;

/** This command applies generic operations to images. */
public class MapCommand implements Command {
  private final String inputName;
  private final String outputName;
  private final ImageOperation operation;

  /**
   * Construct a new map command.
   *
   * @param inputName the name of the input image.
   * @param outputName the name of the output image.
   * @param operation the operation to apply.
   */
  public MapCommand(String inputName, String outputName, ImageOperation operation) {
    this.inputName = Objects.requireNonNull(inputName);
    this.outputName = Objects.requireNonNull(outputName);
    this.operation = Objects.requireNonNull(operation);
  }

  /**
   * Read the input image from the given session, apply the image operation, and save the output
   * image back into the session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the image does not exist or the operation could not be
   *     executed.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    ReadOnlyImage input = session.getImage(inputName);
    ReadOnlyImage output = operation.apply(input);
    session.insertOrReplaceImage(outputName, output);
  }

  @Override
  public String toString() {
    return String.format(
        "<MapCommand inputName=\"%s\" outputName=\"%s\" operation=\"%s\">",
        inputName, outputName, operation);
  }

  /** This class supplies map commands. */
  public static class MapCommandSupplier implements CommandSupplier {
    private final ImageOperation operation;

    /**
     * Construct a new map command supplier.
     *
     * @param operation the operation to outfit new map commands with.
     */
    public MapCommandSupplier(Supplier<ImageOperation> operation) {
      this.operation = Objects.requireNonNull(operation.get());
    }

    @Override
    public Command get(String... args) throws IllegalArgumentException {
      if (args.length != 2) {
        throw new IllegalArgumentException("invalid number of arguments");
      }
      String input = args[0];
      String output = args[1];
      return new MapCommand(input, output, operation);
    }
  }
}
