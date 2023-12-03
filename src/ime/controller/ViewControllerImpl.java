package ime.controller;

import ime.controller.command.Command;
import ime.model.image.ReadOnlyImage;
import ime.model.operations.GenerateHistogram;
import ime.model.session.Session;
import ime.view.View;

import java.util.Objects;
import java.util.Optional;

/**
 * This class implements the view controller interface, and acts as the main GUI controller
 * implementation.
 */
public class ViewControllerImpl implements ViewController {
  private final View view;
  private Session session;

  /**
   * Construct a new view controller object.
   *
   * @param view the view to hook into.
   */
  public ViewControllerImpl(View view) {
    this.view = Objects.requireNonNull(view);
    view.setController(this);
  }

  @Override
  public void run(Session session) {
    if (this.session != null) {
      throw new IllegalStateException("Controller is already running.");
    }
    this.session = Objects.requireNonNull(session);
    view.display();
  }

  @Override
  public void execute(Command command) throws IllegalStateException {
    if (session == null) {
      throw new IllegalStateException("Controller is not running.");
    }
    try {
      Objects.requireNonNull(command).execute(session);
    } catch (IllegalArgumentException ex) {
      // figure out what to do here
    }
    // refresh the view
    view.display();
  }

  @Override
  public Optional<ReadOnlyImage> getCurrentImage() throws IllegalStateException {
    if (session == null) {
      throw new IllegalStateException("Controller is not running.");
    }
    try {
      return Optional.of(session.getImage("image"));
    } catch (IllegalArgumentException e) {
      // image is not in the session
      return Optional.empty();
    }
  }

  @Override
  public ReadOnlyImage getCurrentHistogram() throws IllegalStateException {
    if (session == null) {
      throw new IllegalStateException("Controller is not running.");
    }
    try {
      ReadOnlyImage image = session.getImage("image");
      return new GenerateHistogram().apply(image);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Controller is not working on an image.");
    }
  }
}
