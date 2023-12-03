package ime.view;

import ime.controller.ViewController;

/** This interface outlines the operations of a graphical view. */
public interface View {
  /** Display the most up-to-date image to the user. */
  void display();

  /**
   * Set this view's controller. The view controller is responsible for handling commands, and
   * telling the view which images to display.
   *
   * @param controller the controller.
   */
  void setController(ViewController controller);
}
