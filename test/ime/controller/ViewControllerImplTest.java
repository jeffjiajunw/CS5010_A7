package ime.controller;

import ime.controller.command.BrightenCommand;
import ime.controller.command.Command;
import ime.model.session.Session;
import ime.model.session.SessionImpl;
import ime.view.View;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** This class tests the view controller implementation. */
public class ViewControllerImplTest {
  private StringBuilder log;
  private ViewController controller;

  @Before
  public void setUp() {
    log = new StringBuilder();
    View view = new MockView(log);
    controller = new ViewControllerImpl(view);
  }

  @Test(expected = NullPointerException.class)
  public void testNullViewPassedToConstructor() {
    new ViewControllerImpl(null);
  }

  @Test
  public void testViewHookedCorrectly() {
    String controllerString = controller.toString();
    assertEquals("controller: " + controllerString + "\n", log.toString());
  }

  @Test(expected = NullPointerException.class)
  public void testRunNullSession() {
    controller.run(null);
  }

  @Test
  public void testRunDisplaysView() {
    controller.run(new SessionImpl());
    String controllerString = controller.toString();
    assertEquals("controller: " + controllerString + "\ndisplay\n", log.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testExecuteBeforeRun() {
    controller.execute(new BrightenCommand("image", "image", 5));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentImageBeforeRun() {
    controller.getCurrentImage();
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentHistogramBeforeRun() {
    controller.getCurrentHistogram();
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentHistogramBeforeImageLoaded() {
    controller.run(new SessionImpl());
    controller.getCurrentHistogram();
  }

  @Test
  public void testCommandExecutionRefreshesView() {
    String expected = "controller: " + controller.toString() + "\n";
    assertEquals(expected, log.toString());

    controller.run(new SessionImpl());
    expected += "display\n";
    assertEquals(expected, log.toString());

    controller.execute(new MockLoadCommand());
    expected += "display\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testRun() {
    controller.run(new SessionImpl());
    controller.execute(new MockLoadCommand());
    assertTrue(controller.getCurrentImage().isPresent());
  }

  @Test
  public void testRunNoImage() {
    controller.run(new SessionImpl());
    assertTrue(controller.getCurrentImage().isEmpty());
  }

  /**
   * This class mocks a load command, so we can insert mock images into the session for testing the
   * view controller.
   */
  public static final class MockLoadCommand implements Command {
    @Override
    public void execute(Session session) throws IllegalArgumentException {
      Objects.requireNonNull(session).insertOrReplaceImage("image", new MockImage("image"));
    }
  }

  /** This class mocks the view interface, so we can test the view controller. */
  public static final class MockView implements View {
    private final StringBuilder log;

    /**
     * Construct a new mock view.
     *
     * @param log the string builder log used in testing.
     */
    public MockView(StringBuilder log) {
      this.log = Objects.requireNonNull(log);
    }

    @Override
    public void display() {
      log.append("display\n");
    }

    @Override
    public void setController(ViewController controller) {
      log.append(String.format("controller: %s\n", controller.toString()));
    }
  }
}
