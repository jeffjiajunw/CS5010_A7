import ime.controller.MockImage;
import ime.controller.command.BrightenCommand;
import ime.controller.command.CombineCommand;
import ime.controller.command.Command;
import ime.controller.command.MapCommand;
import ime.controller.io.BufferedImageReader;
import ime.controller.io.BufferedImageWriter;
import ime.controller.io.ImageReader;
import ime.controller.io.ImageWriter;
import ime.controller.io.PpmImageReader;
import ime.controller.io.PpmImageWriter;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.BrightenDarken;
import ime.model.operations.RGBCombine;
import ime.model.session.Session;
import ime.model.session.SessionImpl;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import static ime.util.Colors.blueFrom;
import static ime.util.Colors.fromRgb;
import static ime.util.Colors.greenFrom;
import static ime.util.Colors.redFrom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests some of the commands from the controller part of the
 * MVC architecture.
 */
public class ControllerTests {

  private ImageReader reader;
  private ByteArrayOutputStream output;
  private ByteArrayOutputStream ppmWriteOutput;
  private ImageWriter writer;
  private ImageWriter ppmWriter;
  private ReadOnlyImage red;
  private ReadOnlyImage green;
  private ReadOnlyImage blue;
  private Session session;
  private Command command;
  private ImageReader ppmReader;


  @Before
  public void setUp() {

    reader = new BufferedImageReader();
    ppmReader = new PpmImageReader();
    output = new ByteArrayOutputStream();
    ppmWriteOutput = new ByteArrayOutputStream();
    writer = new BufferedImageWriter("png");
    ppmWriter = new PpmImageWriter();

    session = new SessionImpl();

    try {
      session.getImage("src");
      session.getImage("dest");
      fail("should not be able to get src and dest images when they don't exist");
    } catch (IllegalArgumentException e) {
      // we should expect this result
    }

    red =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(1, 1)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .build();
    green =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(1, 1)
                    .setPixel(0, 0, new ColorImpl(4, 5, 6))
                    .build();
    blue =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(1, 1)
                    .setPixel(0, 0, new ColorImpl(7, 8, 9))
                    .build();

    session.insertOrReplaceImage("red", red);
    session.insertOrReplaceImage("green", green);
    session.insertOrReplaceImage("blue", blue);

    command = new MapCommand("src", "dest", image -> image);

  }

  @Test
  public void testBrightenCommand() {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(100, 50, 25))
                    .setPixel(0, 1, new ColorImpl(30, 50, 90))
                    .setPixel(1, 0, new ColorImpl(100, 12, 25))
                    .setPixel(1, 1, new ColorImpl(3, 50, 44))
                    .build();
    Session session = new SessionImpl();
    session.insertOrReplaceImage("src", image);

    Command command = new BrightenCommand("src", "dest", 5);
    command.execute(session);

    ReadOnlyImage actual = session.getImage("dest");
    assertEquals(new BrightenDarken(5).apply(image), actual);
  }

  @Test
  public void testDarkenCommand() {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(100, 50, 25))
                    .setPixel(0, 1, new ColorImpl(30, 50, 90))
                    .setPixel(1, 0, new ColorImpl(100, 12, 25))
                    .setPixel(1, 1, new ColorImpl(3, 50, 44))
                    .build();
    Session session = new SessionImpl();
    session.insertOrReplaceImage("src", image);

    Command command = new BrightenCommand("src", "dest", -4);
    command.execute(session);

    ReadOnlyImage actual = session.getImage("dest");
    assertEquals(new BrightenDarken(-4).apply(image), actual);
  }

  @Test(expected = NullPointerException.class)
  public void testReadNullImage() {
    reader.read(null);
  }

  @Test
  public void testReader() throws IOException {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
    image.setRGB(0, 0, fromRgb(1, 2, 3));
    image.setRGB(1, 0, fromRgb(4, 5, 6));
    image.setRGB(0, 1, fromRgb(7, 8, 9));
    image.setRGB(1, 1, fromRgb(10, 11, 12));
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", byteStream);
    InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

    ReadOnlyImage expected =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();
    assertEquals(expected, reader.read(inputStream));
  }

  @Test(expected = NullPointerException.class)
  public void testWriteNullImage() {
    writer.write(output, null);
  }

  @Test(expected = NullPointerException.class)
  public void testWriteNullOutput() {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();
    writer.write(null, image);
  }

  @Test
  public void testWriter() throws IOException {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();

    writer.write(output, image);
    byte[] bytes = output.toByteArray();
    BufferedImage buffer = ImageIO.read(new ByteArrayInputStream(bytes));
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int r = redFrom(image.getPixel(y, x));
        int g = greenFrom(image.getPixel(y, x));
        int b = blueFrom(image.getPixel(y, x));
        // need to add 0xFF 00 00 00 for transparency layer
        assertEquals(buffer.getRGB(x, y), 0xFF000000 + fromRgb(r, g, b));
      }
    }
  }

  @Test
  public void testCommand() {
    Command command = new CombineCommand("red", "green", "blue", "dest");
    command.execute(session);

    ReadOnlyImage expected = new RGBCombine().apply(red, green, blue);
    ReadOnlyImage actual = session.getImage("dest");
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBadRed() {
    Command command = new CombineCommand("badred", "green", "blue", "dest");
    command.execute(session);
    session.getImage("dest");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBadGreen() {
    Command command = new CombineCommand("red", "badgreen", "blue", "dest");
    command.execute(session);
    session.getImage("dest");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBadBlue() {
    Command command = new CombineCommand("red", "green", "badblue", "dest");
    command.execute(session);
    session.getImage("dest");
  }

  @Test
  public void testMapCommand() {
    session.insertOrReplaceImage("src", new MockImage("src"));
    command.execute(session);
    assertEquals("src", session.getImage("dest").toString());
  }

  @Test
  public void testCommandOverridesExistingImage() {
    session.insertOrReplaceImage("src", new MockImage("src"));
    session.insertOrReplaceImage("dest", new MockImage("dest"));
    assertEquals("dest", session.getImage("dest").toString());
    command.execute(session);
    assertEquals("src", session.getImage("dest").toString());
  }

  @Test
  public void testMutatingCommand() {
    session.insertOrReplaceImage("src", new MockImage("src"));
    Command command = new MapCommand("src", "src", image -> new MockImage("new-src"));
    command.execute(session);
    assertEquals("new-src", session.getImage("src").toString());
  }

  @Test
  public void testPPMReader() {
    InputStream inputStream =
            new ByteArrayInputStream(
                    "P3\n2 2 255\n1 2 3\n4 5 6\n7 8 9\n10 11 12\n"
                            .getBytes(StandardCharsets.UTF_8));
    ReadOnlyImage expected = new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .setPixel(0, 1, new ColorImpl(4, 5, 6))
            .setPixel(1, 0, new ColorImpl(7, 8, 9))
            .setPixel(1, 1, new ColorImpl(10, 11, 12))
            .build();
    assertEquals(expected, ppmReader.read(inputStream));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReaderNotP3() {
    InputStream inputStream =
            new ByteArrayInputStream(
                    "P4\n2 2 255\n1 2 3\n4 5 6\n7 8 9\n10 11 12\n"
                            .getBytes(StandardCharsets.UTF_8));
    ppmReader.read(inputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReaderMissingValue() {
    InputStream inputStream =
            new ByteArrayInputStream(
                    "P4\n2 2 255\n1 2 3\n4 5 6\n7 8 9\n10 11\n"
                            .getBytes(StandardCharsets.UTF_8));
    ppmReader.read(inputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReaderBadValue() {
    InputStream inputStream =
            new ByteArrayInputStream(
                    "P4\n2 2 255\n1 2 3\n4 5 6\n7 jydyrdhg 9\n10 11 a\n"
                            .getBytes(StandardCharsets.UTF_8));
    ppmReader.read(inputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReaderMissingValues() {
    InputStream inputStream =
            new ByteArrayInputStream("P4\n2 2 255\n".getBytes(StandardCharsets.UTF_8));
    ppmReader.read(inputStream);
  }

  @Test(expected = NullPointerException.class)
  public void testWriteNullImagePPMWriter() {
    ppmWriter.write(ppmWriteOutput, null);
  }

  @Test(expected = NullPointerException.class)
  public void testWriteNullOutputPPMWriter() {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();
    ppmWriter.write(null, image);
  }

  @Test
  public void testPPMWriter() {
    ReadOnlyImage image =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();
    ppmWriter.write(ppmWriteOutput, image);
    assertEquals(
            "P3\n# Made by IME\n2\n2\n255\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n",
            ppmWriteOutput.toString(StandardCharsets.UTF_8));
  }


}