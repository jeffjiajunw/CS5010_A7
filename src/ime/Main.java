package ime;

import ime.controller.Controller;
import ime.controller.ExtendedTextController;
import ime.controller.ViewControllerImpl;
import ime.model.session.Session;
import ime.model.session.SessionImpl;
import ime.view.GraphicalView;
import ime.view.View;

import java.io.InputStreamReader;
import java.io.StringReader;

/** This class represents the main entry-point for the IME program. */
public final class Main {
  /**
   * The main function for our program. Responsible for instantiating MVC objects and running them.
   *
   * <p>Can optionally provide the "-file [scriptPath]" command line argument. If entered, the
   * script at the specified path will be run then the program will exit. Otherwise, the program
   * runs in an interactive mode until the user quits.
   *
   * <p>Can optionally provide the "-text" command line argument. If entered, the script program
   * will execute in text mode. By default, the program executes in GUI mode.
   *
   * @param args optional command line arguments.
   */
  public static void main(String[] args) {
    Controller controller;
    if (args.length == 0) {
      // no arguments, run in graphics mode
      View view = new GraphicalView("IME");
      controller = new ViewControllerImpl(view);
    } else if (args.length == 1 && args[0].equals("-text")) {
      // run in interactive text mode
      controller = new ExtendedTextController(new InputStreamReader(System.in));
    } else if (args.length == 2 && args[0].equals("-file")) {
      // run from a script file
      String scriptPath = args[1];
      controller =
          new ExtendedTextController(new StringReader(String.format("run %s", scriptPath)));
    } else {
      throw new IllegalArgumentException("Program arguments not recognized.");
    }

    Session session = new SessionImpl();
    controller.run(session);
  }
}
