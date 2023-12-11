import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.Blur;
import ime.model.operations.BrightenDarken;
import ime.model.operations.ColorCorrect;
import ime.model.operations.Dither;
import ime.model.operations.ExtractBlueComponent;
import ime.model.operations.ExtractGreenComponent;
import ime.model.operations.ExtractIntensityComponent;
import ime.model.operations.ExtractLumaComponent;
import ime.model.operations.ExtractRedComponent;
import ime.model.operations.ExtractValueComponent;
import ime.model.operations.GenerateHistogram;
import ime.model.operations.Histogram;
import ime.model.operations.HorizontalFlip;
import ime.model.operations.ImageOperation;
import ime.model.operations.LevelsAdjustment;
import ime.model.operations.PreviewSplitOperation;
import ime.model.operations.RGBCombine;
import ime.model.operations.SepiaTone;
import ime.model.operations.Sharpen;
import ime.model.operations.VerticalFlip;
import ime.util.Colors;

import static ime.util.Colors.fromRgb;
import static org.junit.Assert.assertEquals;

/**
 * Test whether the model part of the MVC architecture
 * works correctly. Check whether the color and image
 * implementations work correctly, and whether the
 * filter, flip, linear transformation, and
 * matrix transformation operations also work
 * correctly.
 */
public class ModelTest {

  private ReadOnlyImage imageOddWidth;
  private ReadOnlyImage imageEvenWidth;
  // private Image imageZeroWidth;

  @Before
  public void setUp() {
    imageOddWidth =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 3)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .build();

    imageEvenWidth =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 4)
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(0, 3, new ColorImpl(10, 11, 12))
                    .setPixel(1, 0, new ColorImpl(13, 14, 15))
                    .setPixel(1, 1, new ColorImpl(16, 17, 18))
                    .setPixel(1, 2, new ColorImpl(19, 20, 21))
                    .setPixel(1, 3, new ColorImpl(22, 23, 24))
                    .build();
  }

  @Test
  public void testColorImpl() {

    Color color = new ColorImpl(1, 2, 3);

    assertEquals(1, color.getRed());
    assertEquals(2, color.getGreen());
    assertEquals(3, color.getBlue());
  }

  @Test
  public void testColorImplClamp() {

    Color color = new ColorImpl(257, 300, 1000);
    assertEquals(255, color.getRed());
    assertEquals(255, color.getGreen());
    assertEquals(255, color.getBlue());


    color = new ColorImpl(-257, -300, -1000);
    assertEquals(0, color.getRed());
    assertEquals(0, color.getGreen());
    assertEquals(0, color.getBlue());
  }

  @Test
  public void testColorImplEquals() {

    Color firstColor = new ColorImpl(1, 2, 3);
    Color secondColor = new ColorImpl(1, 2, 3);
    assertEquals(true, firstColor.equals(secondColor));
    assertEquals(true, secondColor.equals(firstColor));

    secondColor = new ColorImpl(4, 5, 6);
    assertEquals(false, firstColor.equals(secondColor));
    assertEquals(false, secondColor.equals(firstColor));

  }

  @Test
  public void testColorImplHashCode() {

    Color firstColor = new ColorImpl(1, 2, 3);
    Color secondColor = new ColorImpl(1, 2, 3);
    assertEquals(true,
            firstColor.hashCode() == secondColor.hashCode());

    secondColor = new ColorImpl(1, 2, 4);
    assertEquals(false,
            firstColor.hashCode() == secondColor.hashCode());

  }

  @Test
  public void testImageImpl() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder imageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1);

    int r;
    int g;
    int b;

    imageBuilder.setPixel(0, 0, new ColorImpl(1, 2, 3));
    imageBuilder.setPixel(1, 0, new ColorImpl(4, 5, 6));

    ReadOnlyImage image = imageBuilder.build();

    assertEquals(2, image.getHeight());
    assertEquals(1, image.getWidth());
    r = image.getColor(0, 0).getRed();
    g = image.getColor(0, 0).getGreen();
    b = image.getColor(0, 0).getBlue();
    assertEquals(1, r);
    assertEquals(2, g);
    assertEquals(3, b);

    r = image.getColor(1, 0).getRed();
    g = image.getColor(1, 0).getGreen();
    b = image.getColor(1, 0).getBlue();
    assertEquals(4, r);
    assertEquals(5, g);
    assertEquals(6, b);
  }

  @Test
  public void testImageImplEquals() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder firstImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);
    ReadOnlyImageImpl.ReadOnlyImageBuilder secondImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);

    int count = 1;
    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        firstImageBuilder.setPixel(i, j, new ColorImpl(count, count, count));
        secondImageBuilder.setPixel(i, j, new ColorImpl(count, count, count));

        count += 1;
      }

    }

    ReadOnlyImage firstImage = firstImageBuilder.build();
    ReadOnlyImage secondImage = secondImageBuilder.build();

    assertEquals(true, firstImage.equals(secondImage));
    assertEquals(true, secondImage.equals(firstImage));

    secondImageBuilder.setPixel(2, 2, new ColorImpl(100, 100, 100));
    secondImage = secondImageBuilder.build();

    assertEquals(false, firstImage.equals(secondImage));
    assertEquals(false, secondImage.equals(firstImage));

  }

  @Test
  public void testImageImplHashCode() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder firstImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);
    ReadOnlyImageImpl.ReadOnlyImageBuilder secondImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);

    int count = 1;
    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        firstImageBuilder.setPixel(i, j, new ColorImpl(count, count, count));
        secondImageBuilder.setPixel(i, j, new ColorImpl(count, count, count));

        count += 1;
      }

    }

    ReadOnlyImage firstImage = firstImageBuilder.build();
    ReadOnlyImage secondImage = secondImageBuilder.build();

    assertEquals(true,
            firstImage.hashCode() == secondImage.hashCode());

    secondImageBuilder.setPixel(2, 2, new ColorImpl(100, 100, 100));

    secondImage = secondImageBuilder.build();
    assertEquals(false,
            firstImage.hashCode() == secondImage.hashCode());
  }

  @Test
  public void testBlurLarge() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(4, 4))
                    .setPixel(0, 0, new ColorImpl(1, 1, 1))
                    .setPixel(0, 1, new ColorImpl(4, 4, 4))
                    .setPixel(0, 2, new ColorImpl(7, 7, 7))
                    .setPixel(0, 3, new ColorImpl(10, 10, 10))
                    .setPixel(1, 0, new ColorImpl(13, 13, 13))
                    .setPixel(1, 1, new ColorImpl(16, 16, 16))
                    .setPixel(1, 2, new ColorImpl(19, 19, 19))
                    .setPixel(1, 3, new ColorImpl(22, 22, 22))
                    .setPixel(2, 0, new ColorImpl(25, 25, 25))
                    .setPixel(2, 1, new ColorImpl(28, 28, 28))
                    .setPixel(2, 2, new ColorImpl(31, 31, 31))
                    .setPixel(2, 3, new ColorImpl(34, 34, 34))
                    .setPixel(3, 0, new ColorImpl(37, 37, 37))
                    .setPixel(3, 1, new ColorImpl(40, 40, 40))
                    .setPixel(3, 2, new ColorImpl(43, 43, 43))
                    .setPixel(3, 3, new ColorImpl(46, 46, 46)).build();

    ReadOnlyImage resImage = (new Blur()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(4, 4))
                    .setPixel(0, 0, new ColorImpl(3, 3, 3))
                    .setPixel(0, 1, new ColorImpl(6, 6, 6))
                    .setPixel(0, 2, new ColorImpl(8, 8, 8))
                    .setPixel(0, 3, new ColorImpl(7, 7, 7))
                    .setPixel(1, 0, new ColorImpl(11, 11, 11))
                    .setPixel(1, 1, new ColorImpl(16, 16, 16))
                    .setPixel(1, 2, new ColorImpl(19, 19, 19))
                    .setPixel(1, 3, new ColorImpl(16, 16, 16))
                    .setPixel(2, 0, new ColorImpl(20, 20, 20))
                    .setPixel(2, 1, new ColorImpl(28, 28, 28))
                    .setPixel(2, 2, new ColorImpl(31, 31, 31))
                    .setPixel(2, 3, new ColorImpl(25, 25, 25))
                    .setPixel(3, 0, new ColorImpl(19, 19, 19))
                    .setPixel(3, 1, new ColorImpl(27, 27, 27))
                    .setPixel(3, 2, new ColorImpl(29, 29, 29))
                    .setPixel(3, 3, new ColorImpl(23, 23, 23))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testBlurSmall() {

    ReadOnlyImage image = (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
            .setPixel(0, 0, new ColorImpl(5, 5, 5))
            .setPixel(1, 0, new ColorImpl(8, 8, 8))
            .build();

    ReadOnlyImage resImage = (new Blur()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(2, 2, 2))
                    .setPixel(1, 0, new ColorImpl(3, 3, 3))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testBlurDiffChannelValues() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(5, 2, 1))
                    .setPixel(1, 0, new ColorImpl(4, 7, 3))
                    .build();

    ReadOnlyImage resImage = (new Blur()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(2, 1, 1))
                    .setPixel(1, 0, new ColorImpl(2, 2, 1))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testSharpenLarge() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder imageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(6, 3);

    int count = 1;
    for (int i = 0; i < 6; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        imageBuilder.setPixel(i, j, new ColorImpl(count, count, count));
        count += 1;
      }
    }

    ReadOnlyImage image = imageBuilder.build();

    ReadOnlyImage resImage = (new Sharpen()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(6, 3))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(4, 4, 4))
                    .setPixel(0, 2, new ColorImpl(3, 3, 3))
                    .setPixel(1, 0, new ColorImpl(3, 3, 3))
                    .setPixel(1, 1, new ColorImpl(11, 11, 11))
                    .setPixel(1, 2, new ColorImpl(7, 7, 7))
                    .setPixel(2, 0, new ColorImpl(7, 7, 7))
                    .setPixel(2, 1, new ColorImpl(18, 18, 18))
                    .setPixel(2, 2, new ColorImpl(11, 11, 11))
                    .setPixel(3, 0, new ColorImpl(11, 11, 11))
                    .setPixel(3, 1, new ColorImpl(25, 25, 25))
                    .setPixel(3, 2, new ColorImpl(14, 14, 14))
                    .setPixel(4, 0, new ColorImpl(21, 21, 21))
                    .setPixel(4, 1, new ColorImpl(39, 39, 39))
                    .setPixel(4, 2, new ColorImpl(25, 25, 25))
                    .setPixel(5, 0, new ColorImpl(19, 19, 19))
                    .setPixel(5, 1, new ColorImpl(32, 32, 32))
                    .setPixel(5, 2, new ColorImpl(22, 22, 22))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testSharpenSmall() {

    ReadOnlyImage image = (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
            .setPixel(0, 0, new ColorImpl(2, 2, 2))
            .setPixel(0, 1, new ColorImpl(5, 5, 5))
            .setPixel(1, 0, new ColorImpl(1, 1, 1))
            .setPixel(1, 1, new ColorImpl(19, 19, 19))
            .build();

    ReadOnlyImage resImage = (new Sharpen()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(8, 8, 8))
                    .setPixel(0, 1, new ColorImpl(11, 11, 11))
                    .setPixel(1, 0, new ColorImpl(8, 8, 8))
                    .setPixel(1, 1, new ColorImpl(21, 21, 21))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testSharpenDiffChannelValues() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(5, 2, 1))
                    .setPixel(1, 0, new ColorImpl(4, 7, 3))
                    .build();

    ReadOnlyImage resImage = (new Sharpen()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(6, 4, 2))
                    .setPixel(1, 0, new ColorImpl(5, 8, 3))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testHorizontalFlipEven() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();

    ReadOnlyImage resImage = (new HorizontalFlip()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(4, 5, 6))
                    .setPixel(0, 1, new ColorImpl(1, 2, 3))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(7, 8, 9))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testHorizontalFlipOdd() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new HorizontalFlip()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(7, 8, 9))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(1, 2, 3))
                    .setPixel(1, 0, new ColorImpl(16, 17, 18))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(10, 11, 12))
                    .setPixel(2, 0, new ColorImpl(25, 26, 27))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(19, 20, 21))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testVerticalFlipEven() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(1, 0, new ColorImpl(7, 8, 9))
                    .setPixel(1, 1, new ColorImpl(10, 11, 12))
                    .build();

    ReadOnlyImage resImage = (new VerticalFlip()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(7, 8, 9))
                    .setPixel(0, 1, new ColorImpl(10, 11, 12))
                    .setPixel(1, 0, new ColorImpl(1, 2, 3))
                    .setPixel(1, 1, new ColorImpl(4, 5, 6))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testVerticalFlipOdd() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new VerticalFlip()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(19, 20, 21))
                    .setPixel(0, 1, new ColorImpl(22, 23, 24))
                    .setPixel(0, 2, new ColorImpl(25, 26, 27))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(1, 2, 3))
                    .setPixel(2, 1, new ColorImpl(4, 5, 6))
                    .setPixel(2, 2, new ColorImpl(7, 8, 9))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testBrighten() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new BrightenDarken(1)).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(2, 3, 4))
                    .setPixel(0, 1, new ColorImpl(5, 6, 7))
                    .setPixel(0, 2, new ColorImpl(8, 9, 10))
                    .setPixel(1, 0, new ColorImpl(11, 12, 13))
                    .setPixel(1, 1, new ColorImpl(14, 15, 16))
                    .setPixel(1, 2, new ColorImpl(17, 18, 19))
                    .setPixel(2, 0, new ColorImpl(20, 21, 22))
                    .setPixel(2, 1, new ColorImpl(23, 24, 25))
                    .setPixel(2, 2, new ColorImpl(26, 27, 28))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testDarken() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new BrightenDarken(-1)).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(0, 1, 2))
                    .setPixel(0, 1, new ColorImpl(3, 4, 5))
                    .setPixel(0, 2, new ColorImpl(6, 7, 8))
                    .setPixel(1, 0, new ColorImpl(9, 10, 11))
                    .setPixel(1, 1, new ColorImpl(12, 13, 14))
                    .setPixel(1, 2, new ColorImpl(15, 16, 17))
                    .setPixel(2, 0, new ColorImpl(18, 19, 20))
                    .setPixel(2, 1, new ColorImpl(21, 22, 23))
                    .setPixel(2, 2, new ColorImpl(24, 25, 26))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractBlueComponent() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new ExtractBlueComponent()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(0, 0, 3))
                    .setPixel(0, 1, new ColorImpl(0, 0, 6))
                    .setPixel(0, 2, new ColorImpl(0, 0, 9))
                    .setPixel(1, 0, new ColorImpl(0, 0, 12))
                    .setPixel(1, 1, new ColorImpl(0, 0, 15))
                    .setPixel(1, 2, new ColorImpl(0, 0, 18))
                    .setPixel(2, 0, new ColorImpl(0, 0, 21))
                    .setPixel(2, 1, new ColorImpl(0, 0, 24))
                    .setPixel(2, 2, new ColorImpl(0, 0, 27))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractGreenComponent() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new ExtractGreenComponent()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(0, 2, 0))
                    .setPixel(0, 1, new ColorImpl(0, 5, 0))
                    .setPixel(0, 2, new ColorImpl(0, 8, 0))
                    .setPixel(1, 0, new ColorImpl(0, 11, 0))
                    .setPixel(1, 1, new ColorImpl(0, 14, 0))
                    .setPixel(1, 2, new ColorImpl(0, 17, 0))
                    .setPixel(2, 0, new ColorImpl(0, 20, 0))
                    .setPixel(2, 1, new ColorImpl(0, 23, 0))
                    .setPixel(2, 2, new ColorImpl(0, 26, 0))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractRedComponent() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new ExtractRedComponent()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 0, 0))
                    .setPixel(0, 1, new ColorImpl(4, 0, 0))
                    .setPixel(0, 2, new ColorImpl(7, 0, 0))
                    .setPixel(1, 0, new ColorImpl(10, 0, 0))
                    .setPixel(1, 1, new ColorImpl(13, 0, 0))
                    .setPixel(1, 2, new ColorImpl(16, 0, 0))
                    .setPixel(2, 0, new ColorImpl(19, 0, 0))
                    .setPixel(2, 1, new ColorImpl(22, 0, 0))
                    .setPixel(2, 2, new ColorImpl(25, 0, 0))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractLumaComponent() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new ExtractLumaComponent()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(2, 2, 2))
                    .setPixel(0, 1, new ColorImpl(5, 5, 5))
                    .setPixel(0, 2, new ColorImpl(8, 8, 8))
                    .setPixel(1, 0, new ColorImpl(11, 11, 11))
                    .setPixel(1, 1, new ColorImpl(14, 14, 14))
                    .setPixel(1, 2, new ColorImpl(17, 17, 17))
                    .setPixel(2, 0, new ColorImpl(20, 20, 20))
                    .setPixel(2, 1, new ColorImpl(23, 23, 23))
                    .setPixel(2, 2, new ColorImpl(26, 26, 26))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractValueComponent() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder imageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);

    int r;
    int g;
    int b;
    Random randInt = new Random();
    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        r = randInt.nextInt(256);
        g = randInt.nextInt(256);
        b = randInt.nextInt(256);

        imageBuilder.setPixel(i, j, new ColorImpl(r, g, b));
      }

    }

    ReadOnlyImage image = imageBuilder.build();

    ReadOnlyImage resImage = (new ExtractValueComponent()).apply(image);
    int maxColorVal;

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        maxColorVal = Math.max(Math.max(
                        image.getColor(i, j).getRed(),
                        image.getColor(i, j).getGreen()
                ), image.getColor(i, j).getBlue()
        );

        assertEquals(
                maxColorVal,
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                maxColorVal,
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                maxColorVal,
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testExtractIntensityComponent() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder imageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3);

    int r;
    int g;
    int b;
    Random randInt = new Random();
    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        r = randInt.nextInt(256);
        g = randInt.nextInt(256);
        b = randInt.nextInt(256);

        imageBuilder.setPixel(i, j, new ColorImpl(r, g, b));
      }

    }

    ReadOnlyImage image = imageBuilder.build();
    ReadOnlyImage resImage = (new ExtractIntensityComponent()).apply(image);
    int avgColorVal;

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        avgColorVal = Math.round((
                image.getColor(i, j).getRed() +
                        image.getColor(i, j).getGreen() +
                        image.getColor(i, j).getBlue())
                / 3f);

        assertEquals(
                avgColorVal,
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                avgColorVal,
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                avgColorVal,
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testSepiaTone() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(1, 2, 3))
                    .setPixel(0, 1, new ColorImpl(4, 5, 6))
                    .setPixel(0, 2, new ColorImpl(7, 8, 9))
                    .setPixel(1, 0, new ColorImpl(10, 11, 12))
                    .setPixel(1, 1, new ColorImpl(13, 14, 15))
                    .setPixel(1, 2, new ColorImpl(16, 17, 18))
                    .setPixel(2, 0, new ColorImpl(19, 20, 21))
                    .setPixel(2, 1, new ColorImpl(22, 23, 24))
                    .setPixel(2, 2, new ColorImpl(25, 26, 27))
                    .build();

    ReadOnlyImage resImage = (new SepiaTone()).apply(image);
    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(2, 2, 2))
                    .setPixel(0, 1, new ColorImpl(7, 6, 5))
                    .setPixel(0, 2, new ColorImpl(11, 9, 7))
                    .setPixel(1, 0, new ColorImpl(15, 13, 10))
                    .setPixel(1, 1, new ColorImpl(19, 17, 13))
                    .setPixel(1, 2, new ColorImpl(23, 20, 16))
                    .setPixel(2, 0, new ColorImpl(27, 24, 19))
                    .setPixel(2, 1, new ColorImpl(31, 27, 21))
                    .setPixel(2, 2, new ColorImpl(35, 31, 24))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineIllegalWidth() {

    ReadOnlyImage firstImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 1))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .build();
    ReadOnlyImage secondImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();
    ReadOnlyImage thirdImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();

    ReadOnlyImage resImage = (new RGBCombine()).apply(
            firstImage,
            secondImage,
            thirdImage
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineIllegalHeight() {

    ReadOnlyImage firstImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();
    ReadOnlyImage secondImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(4, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(2, 0, new ColorImpl(0, 0, 0))
                    .setPixel(3, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .setPixel(2, 1, new ColorImpl(0, 0, 0))
                    .setPixel(3, 1, new ColorImpl(0, 0, 0))
                    .build();
    ReadOnlyImage thirdImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();

    ReadOnlyImage resImage = (new RGBCombine()).apply(
            firstImage,
            secondImage,
            thirdImage
    );
  }

  @Test
  public void testRGBCombine() {

    ReadOnlyImageImpl.ReadOnlyImageBuilder firstImageBuilder =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3));
    ReadOnlyImageImpl.ReadOnlyImageBuilder secondImageBuilder =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3));
    ReadOnlyImageImpl.ReadOnlyImageBuilder thirdImageBuilder =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3));

    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        firstImageBuilder.setPixel(i, j, new ColorImpl(120, 1, 1));
        secondImageBuilder.setPixel(i, j, new ColorImpl(1, 120, 1));
        thirdImageBuilder.setPixel(i, j, new ColorImpl(1, 1, 120));
      }

    }

    ReadOnlyImage combinedImage = (new RGBCombine()).apply(
            firstImageBuilder.build(),
            secondImageBuilder.build(),
            thirdImageBuilder.build()
    );

    for (int i = 0; i < combinedImage.getHeight(); i += 1) {

      for (int j = 0; j < combinedImage.getWidth(); j += 1) {

        assertEquals(120, combinedImage.getColor(i, j).getRed());
        assertEquals(120, combinedImage.getColor(i, j).getGreen());
        assertEquals(120, combinedImage.getColor(i, j).getBlue());

      }

    }

  }

  @Test
  public void testGenerateHistogram() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(20, 20, 20))
                    .setPixel(0, 1, new ColorImpl(1, 1, 1))
                    .setPixel(0, 2, new ColorImpl(20, 20, 20))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(20, 20, 20))
                    .setPixel(1, 2, new ColorImpl(20, 20, 20))
                    .setPixel(2, 0, new ColorImpl(0, 0, 0))
                    .setPixel(2, 1, new ColorImpl(20, 20, 20))
                    .setPixel(2, 2, new ColorImpl(0, 0, 0))
                    .build();

    ReadOnlyImage resImage = (new GenerateHistogram()).apply(image);
    ReadOnlyImageImpl.ReadOnlyImageBuilder expectedImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(256, 256);

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        expectedImageBuilder.setPixel(i, j, new ColorImpl(255, 255, 255));
      }
    }

    expectedImageBuilder.setPixel(102, 0, new ColorImpl(0, 0, 255))
            .setPixel(204, 1, new ColorImpl(0, 0, 255))
            .setPixel(0, 20, new ColorImpl(0, 0, 255));

    for (int j = 0; j < 256; j += 1) {

      if (j == 0 || j == 1 || j == 20) {
        continue;
      }

      expectedImageBuilder.setPixel(255, j, new ColorImpl(255, 0, 0))
              .setPixel(255, j, new ColorImpl(0, 255, 0))
              .setPixel(255, j, new ColorImpl(0, 0, 255));

    }

    ReadOnlyImage expectedImage = expectedImageBuilder.build();

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testGenerateBlueHistogram() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(20, 20, 20))
                    .setPixel(0, 1, new ColorImpl(1, 1, 1))
                    .setPixel(0, 2, new ColorImpl(20, 20, 20))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(20, 20, 20))
                    .setPixel(1, 2, new ColorImpl(20, 20, 20))
                    .setPixel(2, 0, new ColorImpl(0, 0, 0))
                    .setPixel(2, 1, new ColorImpl(20, 20, 20))
                    .setPixel(2, 2, new ColorImpl(0, 0, 0))
                    .build();

    ReadOnlyImage resImage = (new GenerateHistogram()).apply(
            (new ExtractBlueComponent()).apply(image)
    );

    ReadOnlyImageImpl.ReadOnlyImageBuilder expectedImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(256, 256);

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        expectedImageBuilder.setPixel(i, j, new ColorImpl(255, 255, 255));
      }
    }

    expectedImageBuilder.setPixel(102, 0, new ColorImpl(0, 0, 255))
            .setPixel(204, 1, new ColorImpl(0, 0, 255))
            .setPixel(0, 20, new ColorImpl(0, 0, 255))
            .setPixel(0, 0, new ColorImpl(0, 255, 0))
            .setPixel(255, 1, new ColorImpl(0, 255, 0))
            .setPixel(255, 20, new ColorImpl(0, 255, 0));

    List<Integer> jZero = new ArrayList<Integer>();
    jZero.add(0);
    jZero.add(1);
    jZero.add(20);
    for (int j = 0; j < 256; j += 1) {

      if (jZero.contains(j)) {
        continue;
      }

      expectedImageBuilder.setPixel(255, j, new ColorImpl(255, 0, 0))
              .setPixel(255, j, new ColorImpl(0, 255, 0))
              .setPixel(255, j, new ColorImpl(0, 0, 255));

    }

    ReadOnlyImage expectedImage = expectedImageBuilder.build();

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testGenerateHistogramDarkImage() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();

    ReadOnlyImage resImage = (new GenerateHistogram()).apply(image);
    ReadOnlyImageImpl.ReadOnlyImageBuilder expectedImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(256, 256);

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        expectedImageBuilder.setPixel(i, j, new ColorImpl(255, 255, 255));
      }
    }

    expectedImageBuilder.setPixel(0, 0, new ColorImpl(0, 0, 255));

    for (int j = 0; j < 256; j += 1) {

      if (j == 0) {
        continue;
      }

      expectedImageBuilder.setPixel(255, j, new ColorImpl(255, 0, 0))
              .setPixel(255, j, new ColorImpl(0, 255, 0))
              .setPixel(255, j, new ColorImpl(0, 0, 255));

    }

    ReadOnlyImage expectedImage = expectedImageBuilder.build();

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }

  @Test
  public void testColorCorrect() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(11, 40, 72))
                    .setPixel(0, 1, new ColorImpl(21, 50, 82))
                    .setPixel(0, 2, new ColorImpl(31, 60, 92))
                    .setPixel(1, 0, new ColorImpl(40, 70, 100))
                    .setPixel(1, 1, new ColorImpl(40, 70, 100))
                    .setPixel(1, 2, new ColorImpl(40, 70, 100))
                    .setPixel(2, 0, new ColorImpl(51, 80, 112))
                    .setPixel(2, 1, new ColorImpl(61, 90, 122))
                    .setPixel(2, 2, new ColorImpl(71, 100, 132))
                    .build();

    Histogram colorCorrect = new ColorCorrect();

    ReadOnlyImage resImage = colorCorrect.apply(image);
    ReadOnlyImage expectedImage =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3)
                    .setPixel(0, 0, new ColorImpl(41, 40, 42))
                    .setPixel(0, 1, new ColorImpl(51, 50, 52))
                    .setPixel(0, 2, new ColorImpl(61, 60, 62))
                    .setPixel(1, 0, new ColorImpl(70, 70, 70))
                    .setPixel(1, 1, new ColorImpl(70, 70, 70))
                    .setPixel(1, 2, new ColorImpl(70, 70, 70))
                    .setPixel(2, 0, new ColorImpl(81, 80, 82))
                    .setPixel(2, 1, new ColorImpl(91, 90, 92))
                    .setPixel(2, 2, new ColorImpl(101, 100, 102))
                    .build();

    for (int i = 0; i < 3; i += 1) {
      for (int j = 0; j < 3; j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }
    }

    ReadOnlyImage resHistogram = (new GenerateHistogram()).apply(resImage);
    ReadOnlyImageImpl.ReadOnlyImageBuilder expectedHistogramBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(256, 256);

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        expectedHistogramBuilder.setPixel(i, j, new ColorImpl(255, 255, 255));
      }
    }

    //red
    expectedHistogramBuilder
            .setPixel(170, 41, new ColorImpl(255, 0, 0))
            .setPixel(170, 51, new ColorImpl(255, 0, 0))
            .setPixel(170, 61, new ColorImpl(255, 0, 0))
            .setPixel(0, 70, new ColorImpl(255, 0, 0))
            .setPixel(170, 81, new ColorImpl(255, 0, 0))
            .setPixel(170, 91, new ColorImpl(255, 0, 0))
            .setPixel(170, 101, new ColorImpl(255, 0, 0));

    List<Integer> jZero = new ArrayList<Integer>();
    jZero.add(41);
    jZero.add(51);
    jZero.add(61);
    jZero.add(70);
    jZero.add(81);
    jZero.add(91);
    jZero.add(101);
    for (int j = 0; j < 256; j += 1) {

      if (jZero.contains(j)) {
        continue;
      }

      expectedHistogramBuilder.setPixel(255, j, new ColorImpl(255, 0, 0));
    }

    //green
    expectedHistogramBuilder
            .setPixel(170, 40, new ColorImpl(0, 255, 0))
            .setPixel(170, 50, new ColorImpl(0, 255, 0))
            .setPixel(170, 60, new ColorImpl(0, 255, 0))
            .setPixel(0, 70, new ColorImpl(0, 255, 0))
            .setPixel(170, 80, new ColorImpl(0, 255, 0))
            .setPixel(170, 90, new ColorImpl(0, 255, 0))
            .setPixel(170, 100, new ColorImpl(0, 255, 0));

    jZero = new ArrayList<Integer>();
    jZero.add(40);
    jZero.add(50);
    jZero.add(60);
    jZero.add(70);
    jZero.add(80);
    jZero.add(90);
    jZero.add(100);
    for (int j = 0; j < 256; j += 1) {

      if (jZero.contains(j)) {
        continue;
      }

      expectedHistogramBuilder.setPixel(255, j, new ColorImpl(0, 255, 0));
    }

    //blue
    expectedHistogramBuilder
            .setPixel(170, 42, new ColorImpl(0, 0, 255))
            .setPixel(170, 52, new ColorImpl(0, 0, 255))
            .setPixel(170, 62, new ColorImpl(0, 0, 255))
            .setPixel(0, 70, new ColorImpl(0, 0, 255))
            .setPixel(170, 82, new ColorImpl(0, 0, 255))
            .setPixel(170, 92, new ColorImpl(0, 0, 255))
            .setPixel(170, 102, new ColorImpl(0, 0, 255));

    jZero = new ArrayList<Integer>();
    jZero.add(42);
    jZero.add(52);
    jZero.add(62);
    jZero.add(70);
    jZero.add(82);
    jZero.add(92);
    jZero.add(102);
    for (int j = 0; j < 256; j += 1) {

      if (jZero.contains(j)) {
        continue;
      }

      expectedHistogramBuilder.setPixel(255, j, new ColorImpl(0, 0, 255));
    }

    ReadOnlyImage expectedHistogram = expectedHistogramBuilder.build();
    for (int i = 0; i < 256; i += 1) {
      for (int j = 0; j < 256; j += 1) {

        assertEquals(
                expectedHistogram.getColor(i, j).getRed(),
                resHistogram.getColor(i, j).getRed()
        );

        assertEquals(
                expectedHistogram.getColor(i, j).getGreen(),
                resHistogram.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedHistogram.getColor(i, j).getBlue(),
                resHistogram.getColor(i, j).getBlue()
        );

      }
    }
  }

  @Test
  public void testLevelsAdjustment() {

    ReadOnlyImage image =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3))
                    .setPixel(0, 0, new ColorImpl(11, 11, 11))
                    .setPixel(0, 1, new ColorImpl(12, 12, 12))
                    .setPixel(0, 2, new ColorImpl(13, 13, 13))
                    .setPixel(1, 0, new ColorImpl(14, 14, 14))
                    .setPixel(1, 1, new ColorImpl(15, 15, 15))
                    .setPixel(1, 2, new ColorImpl(16, 16, 16))
                    .setPixel(2, 0, new ColorImpl(17, 17, 17))
                    .setPixel(2, 1, new ColorImpl(18, 18, 18))
                    .setPixel(2, 2, new ColorImpl(19, 19, 19))
                    .build();

    ReadOnlyImage resImage = (new LevelsAdjustment(10, 20, 30)).apply(image);
    ReadOnlyImage expectedImage =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(3, 3)
                    .setPixel(0, 0, new ColorImpl(13, 13, 13))
                    .setPixel(0, 1, new ColorImpl(26, 26, 26))
                    .setPixel(0, 2, new ColorImpl(39, 39, 39))
                    .setPixel(1, 0, new ColorImpl(51, 51, 51))
                    .setPixel(1, 1, new ColorImpl(64, 64, 64))
                    .setPixel(1, 2, new ColorImpl(77, 77, 77))
                    .setPixel(2, 0, new ColorImpl(90, 90, 90))
                    .setPixel(2, 1, new ColorImpl(102, 102, 102))
                    .setPixel(2, 2, new ColorImpl(115, 115, 115))
                    .build();


    for (int i = 0; i < 3; i += 1) {

      for (int j = 0; j < 3; j += 1) {

        assertEquals(
                resImage.getColor(i, j).getRed(),
                expectedImage.getColor(i, j).getRed()
        );

        assertEquals(
                resImage.getColor(i, j).getGreen(),
                expectedImage.getColor(i, j).getGreen()
        );

        assertEquals(
                resImage.getColor(i, j).getBlue(),
                expectedImage.getColor(i, j).getBlue()
        );

      }
    }

    ReadOnlyImage resHistogram = (new GenerateHistogram()).apply(resImage);
    ReadOnlyImageImpl.ReadOnlyImageBuilder expectedHistogramBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(256, 256);

    for (int i = 0; i < 256; i += 1) {

      for (int j = 0; j < 256; j += 1) {

        expectedHistogramBuilder.setPixel(i, j, new ColorImpl(255, 255, 255));
      }
    }

    List<Integer> jVals = new ArrayList<>();
    jVals.add(13);
    jVals.add(26);
    jVals.add(39);
    jVals.add(51);
    jVals.add(64);
    jVals.add(77);
    jVals.add(90);
    jVals.add(102);
    jVals.add(115);

    for (int j : jVals) {
      expectedHistogramBuilder.setPixel(0, j, new ColorImpl(0, 0, 255));
    }

    for (int j = 0; j < 256; j++) {

      if (jVals.contains(j)) {
        continue;
      }

      expectedHistogramBuilder.setPixel(255, j, new ColorImpl(0, 0, 255));
    }

    ReadOnlyImage expectedHistogram = expectedHistogramBuilder.build();
    for (int i = 0; i < 256; i += 1) {
      for (int j = 0; j < 256; j += 1) {

        assertEquals(
                expectedHistogram.getColor(i, j).getRed(),
                resHistogram.getColor(i, j).getRed()
        );

        assertEquals(
                expectedHistogram.getColor(i, j).getGreen(),
                resHistogram.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedHistogram.getColor(i, j).getBlue(),
                resHistogram.getColor(i, j).getBlue()
        );

      }
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativePercentage() {
    new PreviewSplitOperation(new Blur(), -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPercentageOver100() {
    new PreviewSplitOperation(new Blur(), 101);
  }

  @Test
  public void testZeroPercentage() {
    ImageOperation operation = new PreviewSplitOperation(new ExtractBlueComponent(), 0);

    assertEquals(imageEvenWidth, operation.apply(imageEvenWidth));
    assertEquals(imageOddWidth, operation.apply(imageOddWidth));
  }

  @Test
  public void testFullPercentage() {
    ImageOperation inner = new ExtractBlueComponent();
    ImageOperation operation = new PreviewSplitOperation(inner, 100);

    assertEquals(inner.apply(imageEvenWidth), operation.apply(imageEvenWidth));
    assertEquals(inner.apply(imageOddWidth), operation.apply(imageOddWidth));
  }

  @Test
  public void testHalfPercentage() {
    ImageOperation inner = new ExtractBlueComponent();
    ImageOperation operation = new PreviewSplitOperation(inner, 50);

    ReadOnlyImage actual = operation.apply(imageOddWidth);
    assertEquals(3, actual.getPixel(0, 0));
    assertEquals(6, actual.getPixel(0, 1));
    assertEquals(fromRgb(7, 8, 9), actual.getPixel(0, 2));
    assertEquals(12, actual.getPixel(1, 0));
    assertEquals(15, actual.getPixel(1, 1));
    assertEquals(fromRgb(16, 17, 18), actual.getPixel(1, 2));

    actual = operation.apply(imageEvenWidth);
    assertEquals(3, actual.getPixel(0, 0));
    assertEquals(6, actual.getPixel(0, 1));
    assertEquals(fromRgb(7, 8, 9), actual.getPixel(0, 2));
    assertEquals(fromRgb(10, 11, 12), actual.getPixel(0, 3));
    assertEquals(15, actual.getPixel(1, 0));
    assertEquals(18, actual.getPixel(1, 1));
    assertEquals(fromRgb(19, 20, 21), actual.getPixel(1, 2));
    assertEquals(fromRgb(22, 23, 24), actual.getPixel(1, 3));
  }

  @Test
  public void testRgb() {
    assertEquals(0x123456, Colors.fromRgb(0x12, 0x34, 0x56));
  }

  @Test
  public void testRed() {
    assertEquals(0x12, Colors.redFrom(0x123456));
  }

  @Test
  public void testGreen() {
    assertEquals(0x34, Colors.greenFrom(0x123456));
  }

  @Test
  public void testBlue() {
    assertEquals(0x56, Colors.blueFrom(0x123456));
  }

  @Test
  public void testOverflow() {
    // RGB values should be truncated to their first 8 bits
    assertEquals(0xFF, Colors.redFrom(Colors.fromRgb(0xFFF, 0xFF, 0xFF)));
  }

  @Test
  public void testDither() {

    ReadOnlyImage image = (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
            .setPixel(0, 0, new ColorImpl(2, 2, 2))
            .setPixel(0, 1, new ColorImpl(5, 5, 5))
            .setPixel(1, 0, new ColorImpl(1, 1, 1))
            .setPixel(1, 1, new ColorImpl(19, 19, 19))
            .build();

    ReadOnlyImage resImage = (new Dither()).apply(image);

    ReadOnlyImage expectedImage =
            (new ReadOnlyImageImpl.ReadOnlyImageBuilder(2, 2))
                    .setPixel(0, 0, new ColorImpl(0, 0, 0))
                    .setPixel(0, 1, new ColorImpl(0, 0, 0))
                    .setPixel(1, 0, new ColorImpl(0, 0, 0))
                    .setPixel(1, 1, new ColorImpl(0, 0, 0))
                    .build();

    for (int i = 0; i < image.getHeight(); i += 1) {

      for (int j = 0; j < image.getWidth(); j += 1) {

        assertEquals(
                expectedImage.getColor(i, j).getRed(),
                resImage.getColor(i, j).getRed()
        );

        assertEquals(
                expectedImage.getColor(i, j).getGreen(),
                resImage.getColor(i, j).getGreen()
        );

        assertEquals(
                expectedImage.getColor(i, j).getBlue(),
                resImage.getColor(i, j).getBlue()
        );

      }

    }

  }
}