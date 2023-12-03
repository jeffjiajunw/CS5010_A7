package ime.controller.command;

import ime.controller.Controller;
import ime.controller.ExtendedTextController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/** This run command enables script files to utilize the extended list of commands. */
public class ExtendedRunCommand extends RunCommand {
  /**
   * Construct a new run command.
   *
   * @param scriptPath the path of the script file.
   */
  public ExtendedRunCommand(String scriptPath) {
    super(scriptPath);
  }

  @Override
  protected Controller getScriptController(String scriptPath) throws FileNotFoundException {
    return new ExtendedTextController(new InputStreamReader(new FileInputStream(scriptPath)));
  }

  /** This class supplies run the extended commands. */
  public static class Supplier extends RunCommand.Supplier {
    /**
     * Get the run command, given the script path.
     *
     * @param scriptPath the script path.
     * @return the command.
     */
    @Override
    protected Command getRunCommand(String scriptPath) {
      return new ExtendedRunCommand(scriptPath);
    }
  }
}
