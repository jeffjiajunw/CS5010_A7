package ime.view.listener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ime.controller.ViewController;

/**
 * Factory that creates the specific type of
 * SubmitNumberListener object based on the
 * SubmitNumberListenerType passed as an argument.
 */
public class SubmitNumberListenerFactory {

  /**
   * Returns a SubmitNumberListener depending
   * on the type passed. Each SubmitNumberListener type
   * needs a controller, frame, and array of JTextFields
   * that allow for the user to enter data in one or more
   * text boxes.
   *
   * @param type        The type of SubmitNumberListener to
   *                    be created.
   * @param controller  The controller is needed to execute
   *                    a specific operation.
   * @param frame       The newly created frame that has the
   *                    necessary buttons/text fields that will
   *                    facilitate user input.
   * @param typeNumbers The text fields from which data needs to
   *                    be collected.
   * @return SubmitNumberListener This collects the data from the user input
   *                              and executes the necessary operation.
   */
  public static SubmitNumberListener
      createSubmitNumberListener(SubmitNumberListenerType type,
                             ViewController controller,
                             JFrame frame,
                             JTextField[] typeNumbers) {

    switch (type) {

      case COMPRESS:
        return new CompressListener(
                controller,
                frame,
                typeNumbers
        );


      case LEVELS_ADJUST:
        return new LevelsAdjustListener(
                controller,
                frame,
                typeNumbers

        );

      default:
        return null;
    }

  }

}
