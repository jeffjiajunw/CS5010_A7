package ime.view.listener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Listen and act when the user decides to submit a piece/pieces
 * of information by entering data into the text fields and then
 * clicking on the submit button. This listener is used for operations
 * that require inputs in order to execute.
 */
public abstract class SubmitNumberListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {

    int[] valuesInputted;
    try {

      valuesInputted = getValuesInputted();
      checkValidValues(valuesInputted);
    } catch (Exception exception) {

      createDialog();
      return;
    }

    executeCommand(valuesInputted);

  }

  /**
   * Execute the operation associated with the SubmitNumberListener
   * implementation. Use the integers in valuesInputted as arguments
   * for the operation.
   *
   * @param valuesInputted The array of integer(s) that are necessary
   *                       arguments for the operation.
   */
  public abstract void executeCommand(int[] valuesInputted);

  /**
   * Get the current frame that facilitates user input.
   *
   * @return JFrame         The current frame that has the text fields
   *                        and submission button.
   */
  public abstract JFrame getFrame();

  /**
   * Get the ith text field in the frame.
   *
   * @param i The index of the text field in the array.
   * @return JTextField     The text field at the ith index.
   */
  public abstract JTextField getTypeNumber(int i);

  /**
   * Get the length of the typeNumbers array which contains
   * text fields.
   *
   * @return length         The length of the typeNumbers array.
   */
  public abstract int getTypeNumbersLength();

  /**
   * Get the label that appears when the user inputs an erroneous
   * value into one of the text fields and tries to submit it/them.
   *
   * @return errorLabel         The string that represents why the value(s)
   *                            inputted is/are incorrect.
   */
  public abstract String getErrorLabel();

  /**
   * Check if the values inputted are correct, and if they are not, throw
   * an IllegalArgumentException.
   *
   * @param valuesInputted The array of integer(s) that are necessary
   *                       arguments for the operation.
   * @throws IllegalArgumentException The arguments inputted are invalid if
   *                                  this is thrown.
   */
  public abstract void checkValidValues(int[] valuesInputted) throws IllegalArgumentException;

  private int[] getValuesInputted() {

    int numValues = getTypeNumbersLength();
    int[] valuesInputted = new int[numValues];
    for (int i = 0; i < numValues; i += 1) {
      valuesInputted[i] = Integer.parseInt(getTypeNumber(i).getText());
    }

    return valuesInputted;
  }

  private void createDialog() {

    JDialog dialog = new JDialog(getFrame(), "Invalid Number Error", true);
    dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
    dialog.add(new JLabel(getErrorLabel()));
    dialog.setSize(500, 80);
    dialog.setVisible(true);
  }

}
