package ime.model.operations;

import java.util.List;
import java.util.Map;

import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;

/**
 * Create a histogram for any given image. Creating a histogram
 * involves starting with a completely white image and then
 * filling in pixel values for the red, green, and blue curves according
 * to the scaled frequency for each channel.
 */
public class GenerateHistogram extends Histogram implements ImageOperation {

  private final int imageSize;
  private Map<Integer, Integer> redMap;
  private Map<Integer, Integer> greenMap;
  private Map<Integer, Integer> blueMap;
  private int maxRed;
  private int maxGreen;
  private int maxBlue;

  /**
   * Initialize the imageSize of the image that
   * will be a result of creating the histogram
   * of the image being operated on. The imageSize
   * will be 256x256, and the maximum values for
   * the three channels all start at 0 since the
   * counting has not begun yet.
   */
  public GenerateHistogram() {

    imageSize = 256;
    maxRed = 0;
    maxGreen = 0;
    maxBlue = 0;

  }

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    List<Map<Integer, Integer>> mapList = mapSetUp(imageSize);
    this.redMap = mapList.get(0);
    this.greenMap = mapList.get(1);
    this.blueMap = mapList.get(2);

    findColorCount(image);
    return generateHistogram(image);
  }

  @Override
  public void findColorCount(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    int r;
    int g;
    int b;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = image.getColor(i, j).getRed();
        g = image.getColor(i, j).getGreen();
        b = image.getColor(i, j).getBlue();

        putRedInMap(r);
        putGreenInMap(g);
        putBlueInMap(b);

        maxRed = Math.max(redMap.get(r), maxRed);
        maxGreen = Math.max(greenMap.get(g), maxGreen);
        maxBlue = Math.max(blueMap.get(b), maxBlue);

      }

    }

  }

  /**
   * Create a new image according to the imageSize
   * and fill it with white space to initialize it.
   * Then, retrieve the count values of each channel
   * value for each pixel and calculate the i by scaling
   * it by the max count for each color.
   *
   * @param image The image that is being processed.
   * @return histImage    The new image which represents a
   *                      histogram of the color frequencies
   *                      of the image being processed.
   */
  public ReadOnlyImage generateHistogram(ReadOnlyImage image) {

    ReadOnlyImageImpl.ReadOnlyImageBuilder histImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(imageSize, imageSize);

    int brightValue = 255;
    for (int i = 0; i < imageSize; i += 1) {

      for (int j = 0; j < imageSize; j += 1) {

        histImageBuilder.setPixel(i, j, new ColorImpl(brightValue, brightValue, brightValue));
      }
    }

    int rI;
    int gI;
    int bI;

    for (int j = 0; j < imageSize; j += 1) {

      rI = getScaledI(redMap.get(j), maxRed);
      gI = getScaledI(greenMap.get(j), maxGreen);
      bI = getScaledI(blueMap.get(j), maxBlue);

      histImageBuilder.setPixel(rI, j, new ColorImpl(brightValue, 0, 0));
      histImageBuilder.setPixel(gI, j, new ColorImpl(0, brightValue, 0));
      histImageBuilder.setPixel(bI, j, new ColorImpl(0, 0, brightValue));

    }

    return histImageBuilder.build();
  }

  private int getScaledI(float colorCount, float maxColor) {

    int maxGridVal = imageSize - 1;
    float fraction = maxGridVal / maxColor;

    return maxGridVal - Math.round(colorCount * fraction);
  }

}