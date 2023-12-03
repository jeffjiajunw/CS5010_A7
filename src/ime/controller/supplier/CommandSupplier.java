package ime.controller.supplier;

import ime.controller.command.Command;

/** This interface represents a command supplier that accepts variable string inputs. */
public interface CommandSupplier {
  /**
   * Construct a new command. The arguments are given as an array of strings, and must be parsed by
   * the supplier implementation to coerce data into proper format for its command.
   *
   * @param args the raw string arguments.
   * @return the command object.
   * @throws IllegalArgumentException if the arguments are malformed.
   */
  Command get(String... args) throws IllegalArgumentException;
}
