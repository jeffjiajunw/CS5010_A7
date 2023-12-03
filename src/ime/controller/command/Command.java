package ime.controller.command;

import ime.model.session.Session;

/**
 * This interface represents a command that interacts with a session. Commands interact with
 * sessions through side effects.
 */
public interface Command {
  /**
   * Execute this command against the given session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the command could not be executed.
   */
  void execute(Session session) throws IllegalArgumentException;
}
