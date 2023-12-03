package ime.view.listener;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ime.controller.ViewController;
import ime.model.operations.HaarWaveletCompression;

/**
 * Execute the Compress function after the user
 * has submitted the necessary input.
 */
public class CompressListener
        extends SubmitNumberListener
        implements ActionListener {

  private ViewController controller;
  private JFrame frame;
  private JTextField[] typeNumbers;

  /**
   * Use the controller to execute the operation,
   * remove the frame from the user's view, and
   * retrieve the information from the typeNumbers
   * text fields for the operation.
   *
   * @param controller          The controller is needed to execute
   *                            the operation.
   * @param frame               The frame will be removed after
   *                            submission.
   * @param typeNumbers         The text field values will be retrieved
   *                            in order to use for the operation.
   */
  public CompressListener(
          ViewController controller,
          JFrame frame,
          JTextField[] typeNumbers) {

    this.controller = controller;
    this.frame = frame;
    this.typeNumbers = typeNumbers;
  }

  @Override
  public void executeCommand(int[] valuesInputted) {

    frame.dispose();

    new SplitViewListener(
            controller,
            new HaarWaveletCompression(valuesInputted[0]))
            .actionPerformed(null);
  }

  @Override
  public JFrame getFrame() {
    return frame;
  }

  @Override
  public JTextField getTypeNumber(int i) {
    return typeNumbers[i];
  }

  @Override
  public int getTypeNumbersLength() {
    return typeNumbers.length;
  }

  @Override
  public String getErrorLabel() {
    return "Please enter a valid integer greater than or equal to 0!";
  }

  @Override
  public void checkValidValues(int[] valuesInputted) throws IllegalArgumentException {

    if (valuesInputted[0] < 0) {
      String error = "Compression factor must be greater than or equal to 0!";
      throw new IllegalArgumentException(error);
    }
  }

}