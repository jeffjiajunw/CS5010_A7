package ime.controller.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import ime.controller.io.ImageWriter;
import ime.model.image.ReadOnlyImage;
import ime.model.session.Session;

/** This command saves images to output streams. */
public class SaveCommand implements Command {
  private final String imagePath;
  private final String imageName;
  private final ImageWriter writer;
  private final OutputStream output;

  /**
   * Construct a new save command.
   *
   * @param imagePath the path of the image to save.
   * @param imageName the name of the image to save.
   * @param writer the writer to used to write the image file.
   * @param output (optionally) the output stream.
   */
  public SaveCommand(String imagePath, String imageName, ImageWriter writer, OutputStream output)
      throws IllegalArgumentException {
    this.imagePath = Objects.requireNonNull(imagePath);
    this.imageName = Objects.requireNonNull(imageName);
    this.writer = Objects.requireNonNull(writer);
    this.output = Objects.requireNonNull(output);
  }

  /**
   * Construct a new save command.
   *
   * @param imagePath the path of the image to save.
   * @param imageName the name of the image to save.
   * @param writer the writer to used to write the image file.
   */
  public SaveCommand(String imagePath, String imageName, ImageWriter writer)
      throws IllegalArgumentException {
    this(imagePath, imageName, writer, getFileOutputStream(imagePath));
  }

  /**
   * Create a new file output stream.
   *
   * @param imagePath the image file path.
   * @return the file stream.
   * @throws IllegalArgumentException if the file could not be created.
   */
  public static OutputStream getFileOutputStream(String imagePath) throws IllegalArgumentException {
    // try to create an output stream from the file path
    try {
      // this snippet to recursively create directories and their parent directories was
      // taken from this StackOverflow post: https://stackoverflow.com/a/4040667
      File file = new File(imagePath);
      File parent = file.getParentFile();
      if (parent != null && !parent.exists() && !parent.mkdirs()) {
        throw new IllegalArgumentException("couldn't create directories");
      }

      return new FileOutputStream(file);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Read the image from the session, and write it to the output stream.
   *
   * @param session the session.
   * @throws IllegalArgumentException if the image does not exist in the session.
   */
  @Override
  public void execute(Session session) throws IllegalArgumentException {
    Objects.requireNonNull(session);

    try {
      ReadOnlyImage image = session.getImage(imageName);
      writer.write(output, image);
    } catch (IllegalArgumentException e) {
      try {
        output.close();
        Files.deleteIfExists(Path.of(imagePath));
      } catch (IOException ex) {
        throw new IllegalStateException(ex);
      }
    }
  }

  @Override
  public String toString() {
    return String.format(
        "<SaveCommand imagePath=\"%s\" imageName=\"%s\" writer=\"%s\">",
        imagePath, imageName, writer);
  }
}
