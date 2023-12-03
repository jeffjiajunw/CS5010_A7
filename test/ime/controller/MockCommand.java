package ime.controller;

import ime.controller.command.Command;
import ime.model.session.Session;

/** This class represents a mock command used to test the {@link TextController} class. */
public class MockCommand implements Command {
  private final String name;
  private final StringBuilder log;

  /**
   * Construct a new mock command with the given name and log.
   *
   * @param name the name.
   * @param log the log.
   */
  MockCommand(String name, StringBuilder log) {
    this.name = name;
    this.log = log;
  }

  @Override
  public void execute(Session session) {
    log.append(String.format("command \"%s\" consumes session \"%s\"\n", name, session.toString()));
  }

  @Override
  public String toString() {
    return name;
  }
}
