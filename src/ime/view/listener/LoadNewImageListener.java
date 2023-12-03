package ime.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ime.controller.ViewController;
import ime.controller.command.Command;
import ime.controller.io.Readers;
import ime.controller.supplier.io.LoadCommandSupplier;

/**
 * This listener is activated when the load button in the
 * GraphicalView is clicked. The purpose of this button is
 * to load a new image into the GraphicalView from a chosen
 * file path by the user.
 */
public class LoadNewImageListener implements ActionListener {
  private final JFrame parentFrame;
  private final ViewController controller;

  /**
   * Initialize the parentFrame and the controller in order
   * to first add the image to the parentFrame, and then execute
   * the load command in the given controller.
   *
   * @param parentFrame         The frame into which a new image will
   *                            be loaded.
   * @param controller          The controller will execute the load command
   *                            for the image.
   */
  public LoadNewImageListener(JFrame parentFrame, ViewController controller) {
    this.parentFrame = Objects.requireNonNull(parentFrame);
    this.controller = Objects.requireNonNull(controller);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (controller.getCurrentImage().isPresent()) {
      // we have an image loaded, double check user wants to save first
      int answer = showConfirmDialog();
      if (answer == 0) {
        // perform save operation
        new SaveImageListener(parentFrame, controller).actionPerformed(null);
      }
    }
    showFilePicker();
  }

  private void showFilePicker() {
    JFileChooser picker = new JFileChooser();
    if (picker.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
      String filePath = picker.getSelectedFile().getAbsolutePath();
      LoadCommandSupplier supplier =
          new LoadCommandSupplier(Readers.get("ppm", "jpeg", "jpg", "png"));
      Command command = supplier.get(filePath, "image");
      controller.execute(command);
    }
  }

  /**
   * Prompts the user to save the current image if the
   * user tries loading in a new image but has not saved
   * the previous image.
   *
   * @return answer         The answer is zero if the user agrees to
   *                        save the image.
   */
  public int showConfirmDialog() {
    JFrame frame = new JFrame();
    int answer =
        JOptionPane.showConfirmDialog(
            frame,
            "The current image may not have been saved. " +
                    "Would you like to save it before loading a new image?",
            "Save current image",
            JOptionPane.YES_NO_OPTION);
    frame.dispose();
    return answer;
  }
}
