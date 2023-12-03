package ime.controller.io;

import ime.model.image.ReadOnlyImage;

import java.io.InputStream;

/** This class represents readers that can accept input streams and produce images. */
public interface ImageReader {
  /**
   * Read the input stream.
   *
   * @param input the input stream.
   * @return the image.
   * @throws IllegalArgumentException if the input stream could not be read.
   */
  ReadOnlyImage read(InputStream input) throws IllegalArgumentException;
}
