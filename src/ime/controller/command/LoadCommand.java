package ime.controller.command;

import ime.controller.io.ImageReader;
import ime.model.image.ReadOnlyImage;
import ime.model.session.Session;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/** This command loads images from files. */
public class LoadCommand implements Command {
  private final String imagePath;
  private final String imageName;
  private final ImageReader reader;
  private final InputStream input;

  /**
   * Construct a new load command.
   *
   * @param imagePath the path of the image to load.
   * @param imageName the name of the loaded image.
   * @param reader the reader used to parse the image file.
   * @param input (optionally) the input stream.
   */
  public LoadCommand(String imagePath, String imageName, ImageReader reader, InputStream input) {
    this.imagePath = Objects.requireNonNull(imagePath);
    this.imageName = Objects.requireNonNull(imageName);
    this.reader = Objects.requireNonNull(reader);
    this.input = Objects.requireNonNull(input);
  }

  /**
   * Construct a new load command.
   *
   * @param imagePath the path of the image to load.
   * @param imageName the name of the loaded image.
   * @param reader the reader used to parse the image file.
   */
  public LoadCommand(String imagePath, String imageName, ImageReader reader) {
    this(imagePath, imageName, reader, getFileInputStream(imagePath));
  }

  /**
   * Create a new file input stream.
   *
   * @param imagePath the image file path.
   * @return the file stream.
   * @throws IllegalArgumentException if the file could not be read.
   */
  public static InputStream getFileInputStream(String imagePath) throws IllegalArgumentException {
    try {
      return new FileInputStream(imagePath);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Parse the file at the image path with the image reader, and write the image into the given
   * session.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the file could not be read.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    ReadOnlyImage image = reader.read(input);
    session.insertOrReplaceImage(imageName, image);
  }

  @Override
  public String toString() {
    return String.format(
        "<LoadCommand imagePath=\"%s\" imageName=\"%s\" reader=\"%s\">",
        imagePath, imageName, reader);
  }
}
