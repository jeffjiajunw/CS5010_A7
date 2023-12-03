package ime.controller.command;

import ime.controller.supplier.CommandSupplier;
import ime.model.operations.BrightenDarken;

import java.util.Objects;

/** This command brightens or darkens images. */
public class BrightenCommand extends MapCommand {
  /**
   * Construct a new brighten-darken command.
   *
   * @param inputName the name of the input image.
   * @param outputName the name of the output image.
   * @param increment the brightening/darkening increment.
   */
  public BrightenCommand(String inputName, String outputName, int increment) {
    super(inputName, outputName, new BrightenDarken(increment));
  }

  /** This class supplies brighten-darken commands. */
  public static class Supplier implements CommandSupplier {
    @Override
    public Command get(String... args) throws IllegalArgumentException {
      if (args.length != 3) {
        throw new IllegalArgumentException("invalid number of arguments");
      }
      Objects.requireNonNull(args[0]);
      int increment = Integer.parseInt(args[0]);
      String inputName = args[1];
      String outputName = args[2];
      return new BrightenCommand(inputName, outputName, increment);
    }
  }
}
