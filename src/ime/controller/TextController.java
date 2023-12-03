package ime.controller;

import ime.controller.command.BrightenCommand;
import ime.controller.command.CombineCommand;
import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.controller.command.RunCommand;
import ime.controller.command.SplitCommand;
import ime.controller.io.BufferedImageReader;
import ime.controller.io.BufferedImageWriter;
import ime.controller.io.ImageReader;
import ime.controller.io.ImageWriter;
import ime.controller.io.PpmImageReader;
import ime.controller.io.PpmImageWriter;
import ime.controller.supplier.CommandSupplier;
import ime.controller.supplier.io.LoadCommandSupplier;
import ime.controller.supplier.io.SaveCommandSupplier;
import ime.model.operations.Blur;
import ime.model.operations.Sharpen;
import ime.model.operations.HorizontalFlip;
import ime.model.operations.VerticalFlip;
import ime.model.operations.ExtractBlueComponent;
import ime.model.operations.ExtractGreenComponent;
import ime.model.operations.ExtractIntensityComponent;
import ime.model.operations.ExtractLumaComponent;
import ime.model.operations.ExtractRedComponent;
import ime.model.operations.ExtractValueComponent;
import ime.model.operations.SepiaTone;
import ime.model.session.Session;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Supplier;

/** This class implements a text-based {@link Controller} object. */
public class TextController implements Controller {
  private final Readable input;
  private final Appendable output;
  protected Map<String, CommandSupplier> commands;

  /**
   * Construct a new text controller. Suitable for testing.
   *
   * @param input the input stream.
   * @param output the output log stream.
   * @param commands the map of supported commands
   */
  public TextController(Readable input, Appendable output, Map<String, CommandSupplier> commands) {
    this.input = Objects.requireNonNull(input);
    this.output = output;
    this.commands = new HashMap<>(Objects.requireNonNull(commands));
  }

  /**
   * Construct a new text controller with no output. Suitable for production.
   *
   * @param input the input stream.
   */
  public TextController(Readable input) {
    this.input = Objects.requireNonNull(input);
    this.output = null;
    this.commands = getDefaultCommands();
  }

  /**
   * Check if the given string is "q" or "quit", ignoring case.
   *
   * @param str the string.
   * @return true if the string equals "q" or "quit", false otherwise.
   */
  private static boolean isQuit(String str) {
    return str.equalsIgnoreCase("q") || str.equalsIgnoreCase("quit");
  }

  /**
   * Get the map of default commands supported by this controller. Can be overridden to add more
   * commands.
   *
   * @return the map of default commands.
   */
  protected Map<String, CommandSupplier> getDefaultCommands() {
    Map<String, CommandSupplier> commands = new HashMap<>();
    commands.put("run", new RunCommand.Supplier());
    commands.put("rgb-combine", new CombineCommand.Supplier());
    commands.put("rgb-split", new SplitCommand.Supplier());
    commands.put("save", new SaveCommandSupplier(getDefaultImageWriters()));
    commands.put("load", new LoadCommandSupplier(getDefaultImageReaders()));
    commands.put("brighten", new BrightenCommand.Supplier());
    commands.put("blur", new MapCommand.MapCommandSupplier(Blur::new));
    commands.put("sharpen", new MapCommand.MapCommandSupplier(Sharpen::new));
    commands.put("sepia", new MapCommand.MapCommandSupplier(SepiaTone::new));
    commands.put("horizontal-flip", new MapCommand.MapCommandSupplier(HorizontalFlip::new));
    commands.put("vertical-flip", new MapCommand.MapCommandSupplier(VerticalFlip::new));
    commands.put("red-component", new MapCommand.MapCommandSupplier(ExtractRedComponent::new));
    commands.put("green-component", new MapCommand.MapCommandSupplier(ExtractGreenComponent::new));
    commands.put("blue-component", new MapCommand.MapCommandSupplier(ExtractBlueComponent::new));
    commands.put("value-component", new MapCommand.MapCommandSupplier(ExtractValueComponent::new));
    commands.put("luma-component", new MapCommand.MapCommandSupplier(ExtractLumaComponent::new));
    commands.put(
        "intensity-component", new MapCommand.MapCommandSupplier(ExtractIntensityComponent::new));
    return commands;
  }

  /**
   * Get the map of default image readers supported by this controller. Can be overridden to add
   * more readers.
   *
   * @return the map of default image readers.
   */
  protected Map<String, Supplier<ImageReader>> getDefaultImageReaders() {
    Map<String, Supplier<ImageReader>> readers = new HashMap<>();
    readers.put("ppm", PpmImageReader::new);
    readers.put("jpeg", BufferedImageReader::new);
    readers.put("jpg", BufferedImageReader::new);
    readers.put("png", BufferedImageReader::new);
    return readers;
  }

  /**
   * Get the map of default image writers supported by this controller. Can be overridden to add
   * more readers.
   *
   * @return the map of default image writers.
   */
  protected Map<String, Supplier<ImageWriter>> getDefaultImageWriters() {
    Map<String, Supplier<ImageWriter>> writers = new HashMap<>();
    writers.put("ppm", PpmImageWriter::new);
    writers.put("jpeg", () -> new BufferedImageWriter("jpeg"));
    writers.put("jpg", () -> new BufferedImageWriter("jpg"));
    writers.put("png", () -> new BufferedImageWriter("png"));
    return writers;
  }

  /**
   * Parse the array of strings for a command and construct it.
   *
   * @param arguments potential { commandName, arguments... }.
   * @return the command.
   * @throws IllegalArgumentException if the command is not supported or the arguments are
   *     malformed.
   */
  private Command parse(String[] arguments) throws IllegalArgumentException {
    String commandName = arguments[0];
    if (!commands.containsKey(commandName)) {
      throw new IllegalArgumentException("command not supported");
    }
    CommandSupplier supplier = commands.get(commandName);
    return supplier.get(Arrays.copyOfRange(arguments, 1, arguments.length));
  }

  /**
   * Print a log message to the output stream if one exists for this controller.
   *
   * @param msg the message.
   * @throws IllegalStateException if the output exists and could not be written to.
   */
  private void print(String msg) throws IllegalStateException {
    if (output != null) {
      try {
        output.append(msg);
      } catch (IOException e) {
        throw new IllegalStateException("cannot write to output");
      }
    }
  }

  @Override
  public void run(Session session) {
    Objects.requireNonNull(session);

    Scanner scanner = new Scanner(input);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      // skip blank lines or comment lines
      if (line.isBlank() || line.startsWith("#")) {
        continue;
      }

      String[] arguments = line.split("\\s+");
      // break loop if any argument is the quit command
      if (Arrays.stream(arguments).anyMatch(TextController::isQuit)) {
        print("quit\n");
        break;
      }

      Command command;
      try {
        command = parse(arguments);
        print(String.format("command parsed: %s\n", command));
        command.execute(session);
        print(String.format("command executed: %s\n", command));
      } catch (IllegalArgumentException e) {
        // failure to either parse or execute command, ignore it and try again
        // with a new command
      }
    }
    scanner.close();
  }
}
