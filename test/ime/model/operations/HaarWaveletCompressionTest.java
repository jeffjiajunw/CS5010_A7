package ime.model.operations;

import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/** This class tests the {@link HaarWaveletCompression} image operation. */
public class HaarWaveletCompressionTest {
  @Test
  public void testCompression() {
    ReadOnlyImage image =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .setPixel(0, 1, new ColorImpl(4, 5, 6))
            .setPixel(1, 0, new ColorImpl(7, 8, 9))
            .setPixel(1, 1, new ColorImpl(8, 9, 10))
            .build();

    ReadOnlyImage expected =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(5, 3, 7))
            .setPixel(0, 1, new ColorImpl(5, 3, 7))
            .setPixel(1, 0, new ColorImpl(5, 8, 7))
            .setPixel(1, 1, new ColorImpl(5, 8, 7))
            .build();

    ReadOnlyImage actual = new HaarWaveletCompression(50).apply(image);
    assertEquals(expected, actual);
  }

  @Test
  public void testNoCompression() {
    ReadOnlyImage image =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .setPixel(0, 1, new ColorImpl(4, 5, 6))
            .setPixel(1, 0, new ColorImpl(7, 8, 9))
            .setPixel(1, 1, new ColorImpl(8, 9, 10))
            .build();

    ReadOnlyImage expected =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .setPixel(0, 1, new ColorImpl(4, 5, 6))
            .setPixel(1, 0, new ColorImpl(7, 8, 9))
            .setPixel(1, 1, new ColorImpl(8, 9, 10))
            .build();

    ReadOnlyImage actual = new HaarWaveletCompression(0).apply(image);
    assertEquals(expected, actual);
  }

  @Test
  public void testFullCompression() {
    ReadOnlyImage image =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(1, 2, 3))
            .setPixel(0, 1, new ColorImpl(4, 5, 6))
            .setPixel(1, 0, new ColorImpl(7, 8, 9))
            .setPixel(1, 1, new ColorImpl(8, 9, 10))
            .build();

    ReadOnlyImage expected =
        new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2)
            .setPixel(0, 0, new ColorImpl(0, 0, 0))
            .setPixel(0, 1, new ColorImpl(0, 0, 0))
            .setPixel(1, 0, new ColorImpl(0, 0, 0))
            .setPixel(1, 1, new ColorImpl(0, 0, 0))
            .build();

    ReadOnlyImage actual = new HaarWaveletCompression(100).apply(image);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCompression() {
    new HaarWaveletCompression(-10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooLargeCompression() {
    new HaarWaveletCompression(104);
  }

  @Test
  public void testGetTransformDimension() {
    assertEquals(16, HaarWaveletCompression.getTransformDimension(10, 12));
    assertEquals(16, HaarWaveletCompression.getTransformDimension(12, 8));
    assertEquals(32, HaarWaveletCompression.getTransformDimension(32, 12));
    assertEquals(32, HaarWaveletCompression.getTransformDimension(10, 17));
    assertEquals(16, HaarWaveletCompression.getTransformDimension(16, 16));
    assertEquals(4, HaarWaveletCompression.getTransformDimension(4, 2));
    assertEquals(8, HaarWaveletCompression.getTransformDimension(2, 8));
  }

  @Test(expected = IllegalStateException.class)
  public void testCombineChannelDataZeroSize() {
    double[][] red = new double[0][0];
    double[][] green = new double[0][0];
    double[][] blue = new double[0][0];

    ReadOnlyImage image = HaarWaveletCompression.combineChannelData(red, green, blue, 0, 0);
    assertEquals(0, image.getWidth());
    assertEquals(0, image.getHeight());
  }

  @Test
  public void testInterleave() {
    double[] avgs = new double[] {1, 2, 3};
    double[] diffs = new double[] {0.1, 0.2, 0.3};
    double[] expected = new double[] {1, 0.1, 2, 0.2, 3, 0.3};
    assertArrayEquals(expected, HaarWaveletCompression.interleave(avgs, diffs), 0.01);
  }

  @Test
  public void testCountNonZero() {
    assertEquals(0, HaarWaveletCompression.countNonZero(new double[0][0]));

    double[][] data = new double[3][3];
    data[0][1] = 2;
    data[1][2] = -3;
    data[2][2] = 4;
    assertEquals(3, HaarWaveletCompression.countNonZero(data));

    data = new double[1][1];
    data[0][0] = 2;
    assertEquals(1, HaarWaveletCompression.countNonZero(data));
  }
}
