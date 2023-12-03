package ime.model.operations;

import java.util.List;
import java.util.Map;

import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;

/**
 * This class adjusts the color values of each pixel by aligning
 * the peaks of each color with each other. This is accomplished
 * by first finding the maximum values of each channel and then
 * averaging them. Then, each value of each pixel is corrected
 * according to this average.
 */
public class ColorCorrect extends Histogram implements ImageOperation {

  private final int imageSize;
  private Map<Integer, Integer> redMap;
  private Map<Integer, Integer> greenMap;
  private Map<Integer, Integer> blueMap;
  private int[] redPeak;
  private int[] greenPeak;
  private int[] bluePeak;

  /**
   * Initialize the imageSize to construct the
   * HashMaps.
   */
  public ColorCorrect() {

    imageSize = 256;
  }

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {

    List<Map<Integer, Integer>> mapList = mapSetUp(imageSize);
    this.redMap = mapList.get(0);
    this.greenMap = mapList.get(1);
    this.blueMap = mapList.get(2);

    findColorCount(image);
    return correctColor(image);

  }

  @Override
  public void findColorCount(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    int[] r = new int[2];
    int[] g = new int[2];
    int[] b = new int[2];

    redPeak = new int[]{-1, -1};
    greenPeak = new int[]{-1, -1};
    bluePeak = new int[]{-1, -1};

    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r[0] = image.getColor(i, j).getRed();
        g[0] = image.getColor(i, j).getGreen();
        b[0] = image.getColor(i, j).getBlue();

        putRedInMap(r[0]);
        putGreenInMap(g[0]);
        putBlueInMap(b[0]);

        r[1] = redMap.get(r[0]);
        g[1] = greenMap.get(g[0]);
        b[1] = blueMap.get(b[0]);

        findRedPeak(r);
        findGreenPeak(g);
        findBluePeak(b);

      }

    }

  }

  private void findRedPeak(int[] r) {

    if (validPeak(r, redPeak) || (redPeak[1] == -1)) {

      redPeak = new int[]{r[0], r[1]};
    }

  }

  private void findGreenPeak(int[] g) {

    if (validPeak(g, greenPeak) || (greenPeak[1] == -1)) {

      greenPeak = new int[]{g[0], g[1]};
    }

  }

  private void findBluePeak(int[] b) {

    if (validPeak(b, bluePeak) || (bluePeak[1] == -1)) {

      bluePeak = new int[]{b[0], b[1]};
    }

  }

  private boolean validPeak(int[] newColor, int[] oldColor) {

    if (newColor[1] <= oldColor[1]) {

      return false;
    }

    return (newColor[0] > 10) && (newColor[0] < 245);
  }

  private ReadOnlyImage correctColor(ReadOnlyImage image) {

    int height = image.getHeight();
    int width = image.getWidth();

    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    int average = Math.round(
            (redPeak[0] + greenPeak[0] + bluePeak[0]) / 3f
    );

    int redDifference = average - redPeak[0];
    int greenDifference = average - greenPeak[0];
    int blueDifference = average - bluePeak[0];

    int r;
    int g;
    int b;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = image.getColor(i, j).getRed() + redDifference;
        g = image.getColor(i, j).getGreen() + greenDifference;
        b = image.getColor(i, j).getBlue() + blueDifference;

        resImageBuilder.setPixel(i, j, new ColorImpl(r, g, b));

      }

    }

    return resImageBuilder.build();
  }

}