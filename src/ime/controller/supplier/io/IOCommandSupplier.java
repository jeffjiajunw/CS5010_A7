package ime.controller.supplier.io;

import ime.controller.command.Command;
import ime.controller.supplier.CommandSupplier;

/** This class supplies abstract IO commands. */
public abstract class IOCommandSupplier implements CommandSupplier {
  /**
   * Get the image file's format type.
   *
   * @param imagePath the image path.
   * @return the file format type.
   */
  protected static String getFormatType(String imagePath) {
    // this snippet of code is adapted from StackOverflow:
    // https://stackoverflow.com/a/942587
    int dotIndex = imagePath.lastIndexOf('.');
    if (dotIndex > 0) {
      return imagePath.substring(dotIndex + 1).toLowerCase();
    } else {
      return "";
    }
  }

  @Override
  public Command get(String... args) throws IllegalArgumentException {
    if (args.length != 2) {
      throw new IllegalArgumentException("invalid number of arguments");
    }
    String imagePath = args[0];
    String imageName = args[1];
    return build(imagePath, imageName);
  }

  /**
   * Construct a new IO command given the image path, and image name.
   *
   * @param imagePath the name of the image.
   * @param imageName the path of the image.
   * @return the command.
   */
  protected abstract Command build(String imagePath, String imageName);
}
