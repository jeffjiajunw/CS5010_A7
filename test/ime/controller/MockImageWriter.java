package ime.controller;

import ime.controller.command.SaveCommand;
import ime.controller.io.ImageWriter;
import ime.model.image.ReadOnlyImage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** This class represents a mock image writer used to test the {@link SaveCommand} class. */
public class MockImageWriter implements ImageWriter {
  @Override
  public void write(OutputStream output, ReadOnlyImage image) {
    try {
      output.write((image.toString()).getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("could not write to output stream for some reason");
    }
  }

  @Override
  public String toString() {
    return "mock writer";
  }
}
