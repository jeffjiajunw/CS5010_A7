package ime.controller.command;

import ime.controller.Controller;
import ime.controller.TextController;
import ime.controller.supplier.CommandSupplier;
import ime.model.session.Session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Objects;

/** This command iteratively runs a series of commands from a script file. */
public class RunCommand implements Command {
  private final String scriptPath;

  /**
   * Construct a new run command.
   *
   * @param scriptPath the path of the script file.
   */
  public RunCommand(String scriptPath) {
    this.scriptPath = Objects.requireNonNull(scriptPath);
  }

  /**
   * Get the controller that will run the script file.
   *
   * @param scriptPath the path of the script.
   * @return the controller.
   * @throws FileNotFoundException if the script file is not found.
   */
  protected Controller getScriptController(String scriptPath) throws FileNotFoundException {
    return new TextController(new InputStreamReader(new FileInputStream(scriptPath)));
  }

  /**
   * Parse the script file, construct a new controller to accept the script as input, and run it
   * against the given session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the script file could not be read.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    try {
      Controller controller = getScriptController(scriptPath);
      controller.run(session);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public String toString() {
    return String.format("<RunCommand src=\"%s\">", scriptPath);
  }

  /** This class supplies run commands. */
  public static class Supplier implements CommandSupplier {
    /**
     * Get the run command, given the script path.
     *
     * @param scriptPath the script path.
     * @return the command.
     */
    protected Command getRunCommand(String scriptPath) {
      return new RunCommand(scriptPath);
    }

    @Override
    public Command get(String... args) throws IllegalArgumentException {
      if (args.length != 1) {
        throw new IllegalArgumentException("invalid number of arguments");
      }
      String scriptPath = args[0];
      return getRunCommand(scriptPath);
    }
  }
}
