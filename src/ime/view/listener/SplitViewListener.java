package ime.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ime.controller.ViewController;
import ime.controller.command.MapCommand;
import ime.model.operations.ImageOperation;
import ime.model.operations.PreviewSplitOperation;
import ime.util.Images;

/**
 * Adds the split preview functionality to every operation button
 * in the main JFrame. Utilizes a slider to change the percent of
 * the image that is currently under the new operation. The user can
 * also commit the changes once they are done previewing the operations'
 * effects.
 */
public class SplitViewListener implements ActionListener, ChangeListener {

  private JFrame splitViewFrame;
  private JPanel imagePanel;
  private JLabel label;
  private final ViewController controller;
  private final ImageOperation imageOperation;

  /**
   * Initialize the controller to be able to execute the SplitView operation.
   * Each SplitView operation also takes in an imageOperation which represents
   * the model operation to be applied to the image.
   *
   * @param controller          The controller executes the PreviewSplitView operation.
   * @param imageOperation      The imageOperation is the actual image modification to
   *                            be applied to the current image.
   */
  public SplitViewListener(ViewController controller, ImageOperation imageOperation) {

    this.controller = Objects.requireNonNull(controller);
    this.imageOperation = Objects.requireNonNull(imageOperation);
    this.imagePanel = new JPanel();
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    JSlider percentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    percentSlider.setMajorTickSpacing(10);
    percentSlider.setMinorTickSpacing(1);
    percentSlider.setPaintTicks(true);
    percentSlider.setPaintLabels(true);
    percentSlider.addChangeListener(this);

    JButton confirmChange = new JButton("Confirm");
    confirmChange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.execute(new MapCommand(
                "image",
                "image",
                imageOperation));
      }
    });

    imagePanel.add(percentSlider);
    imagePanel.add(confirmChange);

    label = getImageLabel(50);
    imagePanel.add(label);

    splitViewFrame = new JFrame();
    splitViewFrame.add(imagePanel);

    splitViewFrame.pack();
    splitViewFrame.setVisible(true);
  }

  @Override
  public void stateChanged(ChangeEvent e) {

    imagePanel.remove(label);

    JSlider slider = (JSlider) e.getSource();

    JLabel pictureLabel = getImageLabel(slider.getValue());
    label = pictureLabel;
    imagePanel.add(label);

    splitViewFrame.validate();
  }

  private JLabel getImageLabel(int percentage) {
    return new JLabel(
            new ImageIcon(
                    Images.fromReadOnlyImage(
                            new PreviewSplitOperation(imageOperation, percentage).apply(
                                    controller.getCurrentImage().get()
                            )
                    )
            )
    );
  }

}
