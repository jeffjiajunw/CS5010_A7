package ime.controller.supplier;

import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.model.operations.ImageOperation;
import ime.model.operations.LevelsAdjustment;
import ime.model.operations.PreviewSplitOperation;

/** This class supplies level commands. */
public class LevelsCommandSupplier implements CommandSupplier {
  @Override
  public Command get(String... args) throws IllegalArgumentException {
    if (args.length == 5) {
      int b = Integer.parseInt(args[2]);
      int m = Integer.parseInt(args[3]);
      int w = Integer.parseInt(args[4]);
      ImageOperation operation = new LevelsAdjustment(b, m, w);
      return new MapCommand(args[0], args[1], operation);
    } else if (args.length == 7) {
      int b = Integer.parseInt(args[2]);
      int m = Integer.parseInt(args[3]);
      int w = Integer.parseInt(args[4]);
      int percentage = Integer.parseInt(args[6]);
      ImageOperation operation =
          new PreviewSplitOperation(new LevelsAdjustment(b, m, w), percentage);
      return new MapCommand(args[0], args[1], operation);
    } else {
      throw new IllegalArgumentException("invalid number of arguments");
    }
  }
}
