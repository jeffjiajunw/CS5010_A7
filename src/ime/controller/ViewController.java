package ime.controller;

import ime.controller.command.Command;
import ime.model.image.ReadOnlyImage;

import java.util.Optional;

/** An extended controller interface for our program that interacts with graphical views. */
public interface ViewController extends Controller {
  /**
   * Execute a given command. This method is exposed to allow view callbacks to communicate with the
   * controller, which in turn manipulates the model.
   *
   * @param command the command to execute.
   * @throws IllegalStateException if the controller is not running when this method is called.
   */
  void execute(Command command) throws IllegalStateException;

  /**
   * Get the current image that is being worked on by this view controller, to display in a view. In
   * the event that no image is being worked on (such as when the program first starts up), an empty
   * optional is returned.
   *
   * @return the image, if one exists.
   * @throws IllegalStateException if the controller is not running when this method is called.
   */
  Optional<ReadOnlyImage> getCurrentImage() throws IllegalStateException;

  /**
   * Get the histogram of the current image being worked on by this view controller, to display in a
   * view. In case no image is being worked on, an empty optional is returned.
   *
   * @return the histogram, if it exists.
   * @throws IllegalStateException if the controller is not running when this method is called, or
   *     if there is no image being worked on.
   */
  ReadOnlyImage getCurrentHistogram() throws IllegalStateException;
}
