package ime.controller;

import ime.controller.command.BrightenCommand;
import ime.controller.command.CombineCommand;
import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.controller.command.RunCommand;
import ime.controller.command.SplitCommand;
import ime.controller.supplier.CommandSupplier;
import ime.controller.supplier.LevelsCommandSupplier;
import ime.controller.supplier.PreviewSplitMapCommandSupplier;
import ime.controller.supplier.io.LoadCommandSupplier;
import ime.controller.supplier.io.SaveCommandSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/** This class defines common tests for command suppliers. */
public abstract class AbstractSupplierTest {
  private final int nargs;

  /**
   * Construct a new test object.
   *
   * @param nargs the number of arguments the command supplier expects.
   */
  public AbstractSupplierTest(int nargs) {
    this.nargs = nargs;
  }

  /**
   * Construct a new command supplier object.
   *
   * @return the supplier.
   */
  protected abstract CommandSupplier supplier();

  @Test(expected = IllegalArgumentException.class)
  public void testNoArguments() {
    supplier().get();
  }

  @Test(expected = NullPointerException.class)
  public void testNullArguments() {
    supplier().get(new String[nargs]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooManyArguments() {
    String[] args = new String[nargs + 1];
    for (int i = 0; i < args.length; i++) {
      args[i] = Integer.toString(i);
    }
    supplier().get(args);
  }

  /** This class tests the brighten-darken command supplier. */
  public static class BrightenCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public BrightenCommandSupplierTest() {
      super(3);
    }

    @Override
    protected CommandSupplier supplier() {
      return new BrightenCommand.Supplier();
    }

    @Test(expected = NumberFormatException.class)
    public void testIncrementNotInteger() {
      new BrightenCommand.Supplier().get("one", "image-src", "image-dest");
    }

    @Test
    public void testSupplier() {
      Command command = supplier().get("1", "image-src", "image-dest");
      assertEquals(
          "<MapCommand inputName=\"image-src\" outputName=\"image-dest\" "
              + "operation=\"<Brighten factor=1>\">",
          command.toString());
    }
  }

  /** This class tests the combine command supplier. */
  public static class CombineCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public CombineCommandSupplierTest() {
      super(4);
    }

    @Override
    protected CommandSupplier supplier() {
      return new CombineCommand.Supplier();
    }

    @Test
    public void testSupplier() {
      Command command = new CombineCommand.Supplier().get("red", "green", "blue", "dest");
      assertEquals(
          "<CombineCommand srcRed=\"green\" srcGreen=\"blue\" srcBlue=\"dest\" dest=\"red\">",
          command.toString());
    }
  }

  /** This class tests the levels command supplier. */
  public static class LevelsCommandSupplierTest {
    @Test
    public void testSupplierWithoutSplit() {
      Command command = new LevelsCommandSupplier().get("src", "dest", "20", "100", "255");
      assertEquals(
          "<MapCommand inputName=\"src\" outputName=\"dest\" "
              + "operation=\"<Levels a=\"-0.003\" b=\"1.999\" c=\"-38.644\">\">",
          command.toString());
    }

    @Test
    public void testSupplierWithSplit() {
      Command command =
          new LevelsCommandSupplier().get("src", "dest", "20", "100", "255", "split", "50");
      assertEquals(
          "<MapCommand inputName=\"src\" outputName=\"dest\" "
              + "operation=\"<PreviewSplit op=\"<Levels a=\"-0.003\" "
              + "b=\"1.999\" c=\"-38.644\">\" percentage=\"0.50\">\">",
          command.toString());
    }
  }

  /** This class tests the load command supplier. */
  public static class LoadCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public LoadCommandSupplierTest() {
      super(2);
    }

    @Override
    protected CommandSupplier supplier() {
      return new LoadCommandSupplier(new HashMap<>());
    }

    @Before
    public void setUp() throws IOException {
      Files.writeString(Path.of("temp-image.txt"), "image contents test");
    }

    @After
    public void tearDown() throws IOException {
      Files.deleteIfExists(Path.of("temp-image.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileFormat() {
      CommandSupplier supplier = new LoadCommandSupplier(new HashMap<>());
      supplier.get("temp-image.txt", "image");
    }

    @Test
    public void testSupplier() {
      StringBuilder log = new StringBuilder();
      CommandSupplier supplier =
          new LoadCommandSupplier(Map.of("txt", () -> new MockImageReader("hello", log)));

      assertEquals(
          "<LoadCommand imagePath=\"temp-image.txt\" imageName=\"image\" reader=\"mock reader\">",
          supplier.get("temp-image.txt", "image").toString());
    }
  }

  /** This class tests the map command supplier. */
  public static class MapCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public MapCommandSupplierTest() {
      super(2);
    }

    @Override
    protected CommandSupplier supplier() {
      return new MapCommand.MapCommandSupplier(() -> image -> image);
    }

    @Test
    public void testSupplier() {
      CommandSupplier supplier =
          new MapCommand.MapCommandSupplier(() -> new MockImageOperation("mock operation"));
      assertEquals(
          "<MapCommand inputName=\"src\" outputName=\"dest\" operation=\"mock operation\">",
          supplier.get("src", "dest").toString());
    }
  }

  /** This class defines tests for the split preview command supplier. */
  public static class PreviewSplitMapCommandSupplierTest {
    @Test(expected = NullPointerException.class)
    public void testNullOperationSupplier() {
      new PreviewSplitMapCommandSupplier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoArguments() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation")).get();
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentsNoSplit() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation")).get(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooFewArguments() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
          .get("src", "dest", "split");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyArguments() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
          .get("src", "dest", "split", "50", "extra");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPercentageNotANumber() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
          .get("src", "dest", "split", "fifty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPercentageTooLow() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
          .get("src", "dest", "split", "-10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPercentageTooHigh() {
      new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
          .get("src", "dest", "split", "101");
    }

    @Test
    public void testSupplierWithoutSplit() {
      Command command =
          new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("operation"))
              .get("src", "dest");
      assertEquals(
          "<MapCommand inputName=\"src\" outputName=\"dest\" operation=\"operation\">",
          command.toString());
    }

    @Test
    public void testSupplierWithSplit() {
      Command command =
          new PreviewSplitMapCommandSupplier(() -> new MockImageOperation("mock operation"))
              .get("src", "dest", "split", "50");
      assertEquals(
          "<MapCommand inputName=\"src\" outputName=\"dest\""
              + " operation=\"<PreviewSplit op=\"mock operation\" percentage=\"0.50\">\">",
          command.toString());
    }
  }

  /** This class defines tests for the run command supplier. */
  public static class RunCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public RunCommandSupplierTest() {
      super(1);
    }

    @Override
    protected CommandSupplier supplier() {
      return new RunCommand.Supplier();
    }

    @Test
    public void testSupplier() {
      Command command = supplier().get("script-path.txt");
      assertEquals("<RunCommand src=\"script-path.txt\">", command.toString());
    }
  }

  /** This class tests the save command supplier. */
  public static class SaveCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public SaveCommandSupplierTest() {
      super(2);
    }

    @Override
    protected CommandSupplier supplier() {
      return new SaveCommandSupplier(new HashMap<>());
    }

    @After
    public void tearDown() throws IOException {
      Files.deleteIfExists(Path.of("temp-image.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileFormat() {
      CommandSupplier supplier = new SaveCommandSupplier(new HashMap<>());
      supplier.get("temp-image.txt", "image");
    }

    @Test
    public void testSupplier() {
      StringBuilder log = new StringBuilder();
      CommandSupplier supplier = new SaveCommandSupplier(Map.of("txt", MockImageWriter::new));

      assertEquals(
          "<SaveCommand imagePath=\"temp-image.txt\" imageName=\"image\" writer=\"mock writer\">",
          supplier.get("temp-image.txt", "image").toString());
    }
  }

  /** This class tests the RGB split command supplier. */
  public static class SplitCommandSupplierTest extends AbstractSupplierTest {
    /** Construct a new test object. */
    public SplitCommandSupplierTest() {
      super(4);
    }

    @Override
    protected CommandSupplier supplier() {
      return new SplitCommand.Supplier();
    }

    @Test
    public void testSupplier() {
      Command command = new SplitCommand.Supplier().get("src", "red", "green", "blue");
      assertEquals(
          "<SplitCommand src=\"src\" red=\"red\" green=\"green\" blue=\"blue\">",
          command.toString());
    }
  }
}
