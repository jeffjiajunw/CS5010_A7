package ime.controller;

import ime.model.session.Session;

/** The main controller interface for our program. */
public interface Controller {
  /**
   * Run this controller, using the given session as its model.
   *
   * @param session the session model.
   */
  void run(Session session);
}
