package ime.controller;

import ime.controller.command.ExtendedRunCommand;
import ime.controller.command.MapCommand;
import ime.controller.supplier.CommandSupplier;
import ime.controller.supplier.CompressCommandSupplier;
import ime.controller.supplier.LevelsCommandSupplier;
import ime.controller.supplier.PreviewSplitMapCommandSupplier;
import ime.model.operations.Blur;
import ime.model.operations.Sharpen;
import ime.model.operations.ColorCorrect;
import ime.model.operations.GenerateHistogram;
import ime.model.operations.ExtractIntensityComponent;
import ime.model.operations.ExtractLumaComponent;
import ime.model.operations.ExtractValueComponent;
import ime.model.operations.SepiaTone;

import java.util.Map;

/** This class extends the existing controller to add more features. */
public class ExtendedTextController extends TextController {
  /**
   * Construct a new extended text controller. Suitable for testing.
   *
   * @param input the input stream.
   * @param output the output log stream.
   * @param commands the map of supported commands
   */
  public ExtendedTextController(
      Readable input, Appendable output, Map<String, CommandSupplier> commands) {
    super(input, output, commands);
  }

  /**
   * Construct a new extended text controller. Suitable for production.
   *
   * @param input the input stream.
   */
  public ExtendedTextController(Readable input) {
    super(input);
  }

  @Override
  protected Map<String, CommandSupplier> getDefaultCommands() {
    Map<String, CommandSupplier> commands = super.getDefaultCommands();
    commands.put("compress", new CompressCommandSupplier());
    commands.put("histogram", new MapCommand.MapCommandSupplier(GenerateHistogram::new));
    commands.put("color-correct", new PreviewSplitMapCommandSupplier(ColorCorrect::new));
    commands.put("levels", new LevelsCommandSupplier());

    // replace some old commands with new and improved split preview options
    commands.put("run", new ExtendedRunCommand.Supplier());
    commands.put("blur", new PreviewSplitMapCommandSupplier(Blur::new));
    commands.put("sharpen", new PreviewSplitMapCommandSupplier(Sharpen::new));
    commands.put("sepia", new PreviewSplitMapCommandSupplier(SepiaTone::new));
    commands.put("luma-component", new PreviewSplitMapCommandSupplier(ExtractLumaComponent::new));
    commands.put("value-component", new PreviewSplitMapCommandSupplier(ExtractValueComponent::new));
    commands.put(
        "intensity-component", new PreviewSplitMapCommandSupplier(ExtractIntensityComponent::new));

    return commands;
  }
}
