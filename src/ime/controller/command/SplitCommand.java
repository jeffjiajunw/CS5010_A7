package ime.controller.command;

import ime.controller.supplier.CommandSupplier;
import ime.model.image.ReadOnlyImage;
import ime.model.operations.ExtractBlueComponent;
import ime.model.operations.ExtractGreenComponent;
import ime.model.operations.ExtractRedComponent;
import ime.model.session.Session;

import java.util.Objects;

/** This command splits a color image into three red, green, and blue images. */
public class SplitCommand implements Command {
  private final String srcImageName;
  private final String destImageNameRed;
  private final String destImageNameGreen;
  private final String destImageNameBlue;

  /**
   * Construct a new split command.
   *
   * @param srcImageName the name of the source color image.
   * @param destImageNameRed the name of the destination image for the red channel.
   * @param destImageNameGreen the name of the destination image for the green channel.
   * @param destImageNameBlue the name of the destination image for the blue channel.
   */
  public SplitCommand(
      String srcImageName,
      String destImageNameRed,
      String destImageNameGreen,
      String destImageNameBlue) {
    this.srcImageName = Objects.requireNonNull(srcImageName);
    this.destImageNameRed = Objects.requireNonNull(destImageNameRed);
    this.destImageNameGreen = Objects.requireNonNull(destImageNameGreen);
    this.destImageNameBlue = Objects.requireNonNull(destImageNameBlue);
  }

  /**
   * Read the color image from the session, split it into three channels, and save each channel back
   * into the session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the color image does not exist in the session.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    ReadOnlyImage image = session.getImage(srcImageName);

    ReadOnlyImage redImage = new ExtractRedComponent().apply(image);
    ReadOnlyImage greenImage = new ExtractGreenComponent().apply(image);
    ReadOnlyImage blueImage = new ExtractBlueComponent().apply(image);

    session.insertOrReplaceImage(destImageNameRed, redImage);
    session.insertOrReplaceImage(destImageNameGreen, greenImage);
    session.insertOrReplaceImage(destImageNameBlue, blueImage);
  }

  @Override
  public String toString() {
    return String.format(
        "<SplitCommand src=\"%s\" red=\"%s\" green=\"%s\" blue=\"%s\">",
        srcImageName, destImageNameRed, destImageNameGreen, destImageNameBlue);
  }

  /** This class supplies split commands. */
  public static class Supplier implements CommandSupplier {
    @Override
    public Command get(String... args) throws IllegalArgumentException {
      if (args.length != 4) {
        throw new IllegalArgumentException("invalid number of arguments");
      }
      String imageName = args[0];
      String destImageNameRed = args[1];
      String destImageNameGreen = args[2];
      String destImageNameBlue = args[3];
      return new SplitCommand(imageName, destImageNameRed, destImageNameGreen, destImageNameBlue);
    }
  }
}
