package ime.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import ime.controller.ViewController;
import ime.model.image.ReadOnlyImage;
import ime.model.operations.Blur;
import ime.model.operations.ColorCorrect;
import ime.model.operations.ExtractBlueComponent;
import ime.model.operations.ExtractGreenComponent;
import ime.model.operations.ExtractLumaComponent;
import ime.model.operations.ExtractRedComponent;
import ime.model.operations.HorizontalFlip;
import ime.model.operations.SepiaTone;
import ime.model.operations.Sharpen;
import ime.model.operations.VerticalFlip;
import ime.util.Images;
import ime.view.listener.EnterNumberListener;
import ime.view.listener.LoadNewImageListener;
import ime.view.listener.SaveImageListener;
import ime.view.listener.SplitViewListener;
import ime.view.listener.SubmitNumberListenerType;

/**
 * This class implements the view interface using the Java Swing library.
 */
public class GraphicalView extends JFrame implements View {
  // Component panels
  private final JPanel editor;
  private final JPanel histogram;

  // Buttons
  private final JButton loadButton;
  private final JButton saveButton;
  private final JButton redButton;
  private final JButton greenButton;
  private final JButton blueButton;
  private final JButton blurButton;
  private final JButton sharpenButton;
  private final JButton greyscaleButton;
  private final JButton sepiaButton;
  private final JButton horizontalFlipButton;
  private final JButton verticalFlipButton;
  private final JButton compressButton;
  private final JButton colorCorrectButton;
  private final JButton levelsAdjustButton;

  // Controller logic fields
  private ViewController controller;

  /**
   * Initialize the necessary buttons with listeners and create
   * the main frame with its toolbar. Users will be able to choose
   * which operations they want to execute on images, and they will
   * be able to load/save images one at a time.
   *
   * @param title         The title of the current GraphicalView.
   */
  public GraphicalView(String title) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());
    add(panel);

    // add the center image editor
    editor = new JPanel();
    editor.setPreferredSize(new Dimension(512, 512));
    panel.add(
            new JScrollPane(
                    editor,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
            BorderLayout.CENTER);

    // add the histogram
    histogram = new JPanel();
    editor.setPreferredSize(new Dimension(256, 256));
    panel.add(histogram, BorderLayout.EAST);

    // make our buttons
    loadButton = new JButton("load");
    saveButton = new JButton("save");
    redButton = new JButton("red");
    greenButton = new JButton("green");
    blueButton = new JButton("blue");
    blurButton = new JButton("blur");
    sharpenButton = new JButton("sharpen");
    greyscaleButton = new JButton("greyscale");
    sepiaButton = new JButton("sepia");
    horizontalFlipButton = new JButton("horizontal flip");
    verticalFlipButton = new JButton("vertical flip");
    compressButton = new JButton("compress");
    colorCorrectButton = new JButton("color correct");
    levelsAdjustButton = new JButton("levels adjust");

    JButton[] buttons =
            new JButton[]{
              loadButton, saveButton, redButton, greenButton,
              blueButton, sepiaButton, greyscaleButton,
              horizontalFlipButton, verticalFlipButton,
              blurButton, sharpenButton, compressButton,
              colorCorrectButton, levelsAdjustButton,
            };

    JToolBar toolBar = addToToolBar(buttons);

    panel.add(toolBar, BorderLayout.PAGE_START);
  }

  private static void setButtonListener(JButton button, ActionListener listener) {
    for (ActionListener l : button.getActionListeners()) {
      button.removeActionListener(l);
    }
    button.addActionListener(listener);
  }

  @Override
  public void display() {
    if (controller == null) {
      throw new IllegalStateException("Controller not configured.");
    }

    editor.removeAll();
    histogram.removeAll();

    Optional<ReadOnlyImage> maybeImage = controller.getCurrentImage();
    if (maybeImage.isEmpty()) {

      JLabel uploadImageLabel = new JLabel("Please load a new image");
      editor.add(uploadImageLabel);

    } else {
      ReadOnlyImage image = maybeImage.get();
      JLabel pictureLabel = new JLabel(new ImageIcon(Images.fromReadOnlyImage(image)));
      editor.add(pictureLabel);
      editor.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

      ReadOnlyImage histogramImage = controller.getCurrentHistogram();
      JLabel histogramLabel = new JLabel(new ImageIcon(Images.fromReadOnlyImage(histogramImage)));
      histogram.add(histogramLabel);
      histogram.setPreferredSize(new Dimension(256, 256));
    }

    pack();
    requestFocus();
    setVisible(true);
  }

  @Override
  public void setController(ViewController controller) {
    this.controller = Objects.requireNonNull(controller);

    setButtonListener(loadButton,
            new LoadNewImageListener(this, controller));
    setButtonListener(saveButton,
            new SaveImageListener(this, controller));
    setButtonListener(redButton,
            new SplitViewListener(controller, new ExtractRedComponent()));
    setButtonListener(greenButton,
            new SplitViewListener(controller, new ExtractGreenComponent()));
    setButtonListener(blueButton,
            new SplitViewListener(controller, new ExtractBlueComponent()));
    setButtonListener(blurButton,
            new SplitViewListener(controller, new Blur()));
    setButtonListener(sharpenButton,
            new SplitViewListener(controller, new Sharpen()));
    setButtonListener(greyscaleButton,
            new SplitViewListener(controller, new ExtractLumaComponent()));
    setButtonListener(sepiaButton,
            new SplitViewListener(controller, new SepiaTone()));
    setButtonListener(horizontalFlipButton,
            new SplitViewListener(controller, new HorizontalFlip()));
    setButtonListener(verticalFlipButton,
            new SplitViewListener(controller, new VerticalFlip()));
    setButtonListener(colorCorrectButton,
            new SplitViewListener(controller, new ColorCorrect()));
    setButtonListener(compressButton, new EnterNumberListener(
            controller,
            this,
            "Enter compression factor",
            SubmitNumberListenerType.COMPRESS));
    setButtonListener(levelsAdjustButton, new EnterNumberListener(
            controller,
            this,
            "Enter b, m, and w",
            SubmitNumberListenerType.LEVELS_ADJUST));
  }

  private JToolBar addToToolBar(JButton[] buttons) {

    JToolBar toolBar = new JToolBar();

    for (int i = 0; i < buttons.length; i += 1) {
      toolBar.add(buttons[i]);
    }

    return toolBar;
  }
}