package ime.controller.io;

import ime.model.image.ReadOnlyImage;

import java.io.OutputStream;

/** This class represents writers that can accept images. */
public interface ImageWriter {
  /**
   * Write the image.
   *
   * @param output the output stream.
   * @param image the image
   * @throws IllegalArgumentException if the image could not be written to the output stream.
   */
  void write(OutputStream output, ReadOnlyImage image) throws IllegalArgumentException;
}
