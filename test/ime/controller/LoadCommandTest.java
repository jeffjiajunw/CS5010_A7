package ime.controller;

import ime.controller.command.Command;
import ime.controller.command.LoadCommand;
import ime.model.session.Session;
import ime.model.session.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/** This class defines tests for the {@link LoadCommand} class. */
public class LoadCommandTest {
  private Session session;

  @Before
  public void setUp() throws IOException {
    session = new SessionImpl();
    Files.createFile(Path.of("temp-image.txt"));
    Files.write(Path.of("temp-image.txt"), "image contents".getBytes(StandardCharsets.UTF_8));
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(Path.of("temp-image.txt"));
  }

  @Test
  public void loadCommandWorks() {
    StringBuilder log = new StringBuilder();
    Command command =
        new LoadCommand("temp-image.txt", "image", new MockImageReader("mock-image", log));
    command.execute(session);
    assertEquals("mock-image", session.getImage("image").toString());
    assertEquals("image contents", log.toString());
  }

}
