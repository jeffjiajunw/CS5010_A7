package ime.controller.supplier;

import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.model.operations.ImageOperation;
import ime.model.operations.PreviewSplitOperation;

import java.util.Objects;
import java.util.function.Supplier;

/** This object supplies map commands that optionally support a split preview. */
public class PreviewSplitMapCommandSupplier implements CommandSupplier {
  private final Supplier<ImageOperation> operationSupplier;
  private final CommandSupplier commandSupplier;

  /**
   * Construct a new split preview map command supplier, with the underlying operation.
   *
   * @param operationSupplier the operation supplier.
   */
  public PreviewSplitMapCommandSupplier(Supplier<ImageOperation> operationSupplier) {
    this.operationSupplier = Objects.requireNonNull(operationSupplier);
    this.commandSupplier = new MapCommand.MapCommandSupplier(operationSupplier);
  }

  @Override
  public Command get(String... args) throws IllegalArgumentException {
    if (args.length == 4 && args[2] != null && args[2].equals("split") && args[3] != null) {
      int percentage = Integer.parseInt(args[3]);
      String input = args[0];
      String output = args[1];
      return new MapCommand(
          input, output, new PreviewSplitOperation(operationSupplier.get(), percentage));
    } else {
      return commandSupplier.get(args);
    }
  }
}
