package ime.controller;

import ime.controller.command.Command;
import ime.controller.command.SplitCommand;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import org.junit.Before;
import org.junit.Test;

import ime.model.color.ColorImpl;
import ime.model.operations.ExtractBlueComponent;
import ime.model.operations.ExtractGreenComponent;
import ime.model.operations.ExtractRedComponent;
import ime.model.session.Session;
import ime.model.session.SessionImpl;

import static org.junit.Assert.assertEquals;

/** This class tests the split command. */
public class SplitCommandTest {
  private ReadOnlyImage image;
  private Session session;

  @Before
  public void setUp() {
    session = new SessionImpl();
    image =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(1, 1)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .build();
    session.insertOrReplaceImage("src", image);
  }

  @Test
  public void testCommand() {
    Command command = new SplitCommand("src", "red", "green", "blue");
    command.execute(session);
    assertEquals(new ExtractRedComponent().apply(image), session.getImage("red"));
    assertEquals(new ExtractGreenComponent().apply(image), session.getImage("green"));
    assertEquals(new ExtractBlueComponent().apply(image), session.getImage("blue"));
  }
}
