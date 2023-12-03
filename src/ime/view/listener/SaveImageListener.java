package ime.view.listener;

import ime.controller.ViewController;
import ime.controller.command.Command;
import ime.controller.io.Writers;
import ime.controller.supplier.io.SaveCommandSupplier;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This listener is activated when the save button in the
 * GraphicalView is clicked. The purpose of this button is
 * to save the image currently in the frame to a specified
 * location that the user chooses.
 */
public class SaveImageListener implements ActionListener {
  private final JFrame parentFrame;
  private final ViewController controller;

  public SaveImageListener(JFrame parentFrame, ViewController controller) {
    this.parentFrame = Objects.requireNonNull(parentFrame);
    this.controller = Objects.requireNonNull(controller);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (controller.getCurrentImage().isEmpty()) {
      JOptionPane.showConfirmDialog(
          parentFrame,
          "There is no image to be saved!",
          "Saving empty image",
          JOptionPane.DEFAULT_OPTION);
      return;
    }
    showFilePicker();
  }

  private void showFilePicker() {
    JFileChooser picker = new JFileChooser();
    if (picker.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
      String filePath = picker.getSelectedFile().getAbsolutePath();
      SaveCommandSupplier supplier =
          new SaveCommandSupplier(Writers.get("ppm", "jpeg", "jpg", "png"));
      Command command = supplier.get(filePath, "image");
      controller.execute(command);
    }
  }
}
