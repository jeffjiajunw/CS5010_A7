package ime.controller;

import ime.controller.command.Command;
import ime.controller.command.SaveCommand;
import ime.model.session.Session;
import ime.model.session.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/** This class defines tests for the {@link SaveCommand} class. */
public class SaveCommandTest {
  private Session session;

  @Before
  public void setUp() throws IOException {
    session = new SessionImpl();
    Files.writeString(Path.of("temp-image.txt"), "mock image contents");
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(Path.of("temp-image.txt"));
  }

  @Test
  public void saveCommandWorks() throws IOException {
    session.insertOrReplaceImage("image", new MockImage("mock image"));
    OutputStream log = new ByteArrayOutputStream();
    Command command = new SaveCommand("temp-image.txt", "image", new MockImageWriter(), log);

    command.execute(session);
    assertEquals("mock image", log.toString());
    assertEquals("mock image contents", Files.readString(Path.of("temp-image.txt")));
  }
}
