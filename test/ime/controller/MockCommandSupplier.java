package ime.controller;

import ime.controller.command.Command;
import ime.controller.supplier.CommandSupplier;

import java.util.Arrays;

/** This class mocks the {@link CommandSupplier} interface. */
public class MockCommandSupplier implements CommandSupplier {
  private final StringBuilder log;
  private final String name;
  private final boolean fails;

  /**
   * Create a new mock.
   *
   * @param log the output log.
   * @param name the name of the builder.
   * @param fails whether this builder will fail on get().
   */
  public MockCommandSupplier(StringBuilder log, String name, boolean fails) {
    this.log = log;
    this.name = name;
    this.fails = fails;
  }

  @Override
  public Command get(String... args) throws IllegalArgumentException {
    log.append(
        String.format("supplier \"%s\" get() called with args: %s\n", name, Arrays.toString(args)));
    if (fails) {
      throw new IllegalArgumentException("mock command supplier fails");
    }

    return new MockCommand(name, log);
  }
}
