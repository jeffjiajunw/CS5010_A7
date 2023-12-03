package ime.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ime.controller.ViewController;
import ime.view.GraphicalView;

/**
 * The EnterNumberListener is an ActionListener that spawns
 * a new JFrame with a JButton and JTextField(s). This is
 * to allow the user to provide necessary user input for
 * the model to be able to run certain operations.
 */
public class EnterNumberListener implements ActionListener {

  private ViewController controller;
  private final GraphicalView graphicalView;
  private final String label;
  private final SubmitNumberListenerType type;
  private JFrame newFrame;
  private JPanel newPanel;

  /**
   * Instantiate the controller, graphicalView, label, and type
   * in order to create the specific SubmitNumberListener type
   * and pass down some of these necessary objects to the
   * SubmitNumberListener that will be created.
   *
   * @param controller    The controller is needed in order to execute the
   *                      operation on the image.
   * @param graphicalView Add the new frame with the text fields and button to the
   *                      existing graphicalView as a popup.
   * @param label         Set the label of the frame that will pop up.
   * @param type          The type of SubmitNumberListener that must be created.
   */
  public EnterNumberListener(ViewController controller,
                             GraphicalView graphicalView,
                             String label,
                             SubmitNumberListenerType type) {

    this.controller = controller;
    this.graphicalView = graphicalView;
    this.label = label;
    this.type = type;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    createPanel();
    createFrame();

    int numTextFields = getNumTextFields();

    JTextField[] typeNumbers = new JTextField[numTextFields];

    for (int i = 0; i < numTextFields; i += 1) {
      typeNumbers[i] = (new JTextField(10));
      newPanel.add(typeNumbers[i]);
    }

    JButton enterNumber = new JButton(label);
    ActionListener submitNumberListener =
            SubmitNumberListenerFactory.createSubmitNumberListener(
                    type,
                    controller,
                    newFrame,
                    typeNumbers
            );

    enterNumber.addActionListener(submitNumberListener);

    newPanel.add(enterNumber);

    for (int i = 0; i < numTextFields; i += 1) {
      typeNumbers[i].setVisible(true);
    }

    enterNumber.setVisible(true);
    newFrame.setVisible(true);
    graphicalView.pack();
  }

  private int getNumTextFields() {

    if (type == SubmitNumberListenerType.LEVELS_ADJUST) {
      return 3;
    }

    return 1;
  }

  private void createPanel() {

    newPanel = new JPanel();
    newPanel.setSize(500, 300);
  }

  private void createFrame() {

    newFrame = new JFrame(label);
    newFrame.add(newPanel);
    newFrame.setSize(500, 300);
  }

}
