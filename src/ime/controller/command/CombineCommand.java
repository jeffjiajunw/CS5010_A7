package ime.controller.command;

import ime.controller.supplier.CommandSupplier;
import ime.model.image.ReadOnlyImage;
import ime.model.operations.RGBCombine;
import ime.model.session.Session;

import java.util.Objects;

/** This command combines three red, green, and blue images into one color image. */
public class CombineCommand implements Command {
  private final String srcImageNameRed;
  private final String srcImageNameGreen;
  private final String srcImageNameBlue;
  private final String destImageName;

  /**
   * Construct a new combine command.
   *
   * @param srcImageNameRed the name of the source image for the red channel.
   * @param srcImageNameGreen the name of the source image for the green channel.
   * @param srcImageNameBlue the name of the source image for the blue channel.
   * @param destImageName the name of the destination image to create.
   */
  public CombineCommand(
      String srcImageNameRed,
      String srcImageNameGreen,
      String srcImageNameBlue,
      String destImageName) {
    this.srcImageNameRed = Objects.requireNonNull(srcImageNameRed);
    this.srcImageNameBlue = Objects.requireNonNull(srcImageNameBlue);
    this.srcImageNameGreen = Objects.requireNonNull(srcImageNameGreen);
    this.destImageName = Objects.requireNonNull(destImageName);
  }

  /**
   * Read the red, green, and blue images from the given session, combine them into one color image,
   * and write the color image back into the session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the red, green, or blue images don't exist in the session.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    ReadOnlyImage imageRed = session.getImage(srcImageNameRed);
    ReadOnlyImage imageGreen = session.getImage(srcImageNameGreen);
    ReadOnlyImage imageBlue = session.getImage(srcImageNameBlue);
    ReadOnlyImage destImage = new RGBCombine().apply(imageRed, imageGreen, imageBlue);
    session.insertOrReplaceImage(destImageName, destImage);
  }

  @Override
  public String toString() {
    return String.format(
        "<CombineCommand srcRed=\"%s\" srcGreen=\"%s\" srcBlue=\"%s\" dest=\"%s\">",
        srcImageNameRed, srcImageNameGreen, srcImageNameBlue, destImageName);
  }

  /** This class supplies combine commands. */
  public static class Supplier implements CommandSupplier {
    @Override
    public Command get(String... args) throws IllegalArgumentException {
      if (args.length != 4) {
        throw new IllegalArgumentException("invalid number of arguments");
      }
      String destImageName = args[0];
      String imageNameRed = args[1];
      String imageNameGreen = args[2];
      String imageNameBlue = args[3];
      return new CombineCommand(imageNameRed, imageNameGreen, imageNameBlue, destImageName);
    }
  }
}
