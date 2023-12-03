package ime.controller.supplier;

import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.model.operations.ImageOperation;
import ime.model.operations.HaarWaveletCompression;

import java.util.Objects;

/** This class supplies compress commands. */
public class CompressCommandSupplier implements CommandSupplier {
  @Override
  public Command get(String... args) throws IllegalArgumentException {
    if (args.length != 3) {
      throw new IllegalArgumentException("invalid number of arguments");
    }
    Objects.requireNonNull(args[0]);
    int percentage = Integer.parseInt(args[0]);
    String inputName = args[1];
    String outputName = args[2];
    ImageOperation operation = new HaarWaveletCompression(percentage);
    return new MapCommand(inputName, outputName, operation);
  }
}
