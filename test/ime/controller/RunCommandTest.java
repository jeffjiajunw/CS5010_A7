package ime.controller;

import ime.controller.command.Command;
import ime.controller.command.RunCommand;
import ime.model.session.Session;
import ime.model.session.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/** This class defines tests for the {@link RunCommand} class. */
public class RunCommandTest {
  private Session runCommandSession;

  @Before
  public void setUp() throws IOException {
    runCommandSession = new SessionImpl();
    Files.createFile(Path.of("script.txt"));
    Files.createFile(Path.of("temp-image.ppm"));
    Files.write(
        Path.of("temp-image.ppm"), "P3\n2 2 255\n1 2 3\n4 5 6\n7 8 9\n10 11 12\n".getBytes());
    Files.write(
        Path.of("script.txt"),
        ("load temp-image.ppm image\n"
                + "# ignore comment lines\n"
                + "sepia image sepia-image\n"
                + "tuaneudu umauwdma\n"
                + "save sepia-image.ppm sepia-image\n")
            .getBytes());
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(Path.of("script.txt"));
    Files.deleteIfExists(Path.of("temp-image.ppm"));
    Files.deleteIfExists(Path.of("sepia-image.ppm"));
  }

  @Test
  public void testCommand() throws IOException {
    Command command = new RunCommand("script.txt");
    command.execute(runCommandSession);

    assertEquals(
        "P3\n"
            + "# Made by IME\n"
            + "2\n"
            + "2\n"
            + "255\n"
            + "2\n"
            + "2\n"
            + "2\n"
            + "7\n"
            + "6\n"
            + "5\n"
            + "11\n"
            + "9\n"
            + "7\n"
            + "15\n"
            + "13\n"
            + "10\n",
        Files.readString(Path.of("sepia-image.ppm")));
  }
}
