package ime.controller;

import ime.controller.supplier.CommandSupplier;
import ime.model.session.Session;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the {@link TextController} implementation of the {@link Controller} interface.
 */
public class TextControllerTest {
  private StringBuilder output;
  private StringBuilder log;
  private Session session;

  /**
   * Create and run a controller with the given input string. Output/logs are collected.
   *
   * @param input the input stream.
   */
  private void run(String input) {
    output = new StringBuilder();
    log = new StringBuilder();
    session = new MockSession("mock session");
    Map<String, CommandSupplier> commands = new HashMap<>();
    commands.put("command1", new MockCommandSupplier(log, "command1", false));
    commands.put("command2", new MockCommandSupplier(log, "command2", true));
    Controller controller = new TextController(new StringReader(input), output, commands);
    controller.run(session);
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    new TextController(null, output, new HashMap<>());
  }

  @Test(expected = NullPointerException.class)
  public void testNullSession() {
    new TextController(new StringReader("q"), System.out, new HashMap<>()).run(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullCommands() {
    new TextController(new StringReader("q"), System.out, null);
  }

  @Test
  public void testQuitsImmediately() {
    run("quit");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());
  }

  @Test
  public void testQuitsDuringCommand() {
    run("load Koala.ppm quit");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());
  }

  @Test
  public void testDifferentQuitKeywords() {
    run("quit");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());

    run("q");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());

    run("QUIT");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());

    run("Q");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());

    run("QuIt");
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());
  }

  @Test
  public void testNoInput() {
    run("");
    assertEquals("", output.toString());
    assertEquals("", log.toString());
  }

  @Test
  public void testExecuteOneCommand() {
    run("command1 arg1 arg2 arg3\nquit");
    assertEquals(
        "command parsed: command1\n" + "command executed: command1\n" + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command1\" get() called with args: [arg1, arg2, arg3]\n"
            + "command \"command1\" consumes session \"mock session\"\n",
        log.toString());
  }

  @Test
  public void testExecuteTwoCommands() {
    run("command1 arg1 arg2 arg3\ncommand1 arg4 arg5 arg6\nq");
    assertEquals(
        "command parsed: command1\n"
            + "command executed: command1\n"
            + "command parsed: command1\n"
            + "command executed: command1\n"
            + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command1\" get() called with args: [arg1, arg2, arg3]\n"
            + "command \"command1\" consumes session \"mock session\"\n"
            + "supplier \"command1\" get() called with args: [arg4, arg5, arg6]\n"
            + "command \"command1\" consumes session \"mock session\"\n",
        log.toString());
  }

  @Test
  public void testGarbageCommand() {
    run("garbage-command arg1 arg2 arg3");
    assertEquals("", output.toString());
    assertEquals("", log.toString());
  }

  @Test
  public void testBlankLinesInserted() {
    run("command1 arg1 arg2 arg3\n\n\n\n\n\n\ncommand1 arg4 arg5 arg6\nq");
    assertEquals(
        "command parsed: command1\n"
            + "command executed: command1\n"
            + "command parsed: command1\n"
            + "command executed: command1\n"
            + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command1\" get() called with args: [arg1, arg2, arg3]\n"
            + "command \"command1\" consumes session \"mock session\"\n"
            + "supplier \"command1\" get() called with args: [arg4, arg5, arg6]\n"
            + "command \"command1\" consumes session \"mock session\"\n",
        log.toString());
  }

  @Test
  public void testCommandFails2nd() {
    run("command1 arg1 arg2\ncommand2 arg3 arg4 arg5\nq");
    assertEquals(
        "command parsed: command1\n" + "command executed: command1\n" + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command1\" get() called with args: [arg1, arg2]\n"
            + "command \"command1\" consumes session \"mock session\"\n"
            + "supplier \"command2\" get() called with args: [arg3, arg4, arg5]\n",
        log.toString());
  }

  @Test
  public void testCommandFails1st() {
    run("command2 arg1 arg2\ncommand1 arg3 arg4 arg5\nq");
    assertEquals(
        "command parsed: command1\n" + "command executed: command1\n" + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command2\" get() called with args: [arg1, arg2]\n"
            + "supplier \"command1\" get() called with args: [arg3, arg4, arg5]\n"
            + "command \"command1\" consumes session \"mock session\"\n",
        log.toString());
  }

  @Test
  public void testMultipleSpaces() {
    run("command1 arg3 arg4      arg5\n          quit");
    assertEquals(
        "command parsed: command1\n" + "command executed: command1\n" + "quit\n",
        output.toString());
    assertEquals(
        "supplier \"command1\" get() called with args: [arg3, arg4, arg5]\n"
            + "command \"command1\" consumes session \"mock session\"\n",
        log.toString());
  }

  @Test
  public void testCommandsCantBeModifiedAfterConstruction() {
    output = new StringBuilder();
    log = new StringBuilder();
    session = new MockSession("mock session");
    Map<String, CommandSupplier> commands = new HashMap<>();
    commands.put("command1", new MockCommandSupplier(log, "command1", false));
    commands.put("command2", new MockCommandSupplier(log, "command2", true));
    Controller controller =
        new TextController(new StringReader("command3 arg1 arg2\nq"), output, commands);

    // this line should not add command3 to the controller since it's already been constructed
    commands.put("command3", new MockCommandSupplier(log, "command3", true));

    controller.run(session);
    assertEquals("quit\n", output.toString());
    assertEquals("", log.toString());
  }
}
