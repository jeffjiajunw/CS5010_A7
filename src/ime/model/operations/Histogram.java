package ime.model.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ime.model.image.ReadOnlyImage;
import ime.model.operations.ImageOperation;

/**
 * This class allows its implementations to share the
 * creation of the RGB maps in order to effectively
 * count the number of instances of each channel value
 * for frequency calculations.
 */
public abstract class Histogram implements ImageOperation {

  private Map<Integer, Integer> redMap;
  private Map<Integer, Integer> greenMap;
  private Map<Integer, Integer> blueMap;

  /**
   * Initialize the red, green, and blue hashmaps to
   * store color counts.
   */
  public Histogram() {

    redMap = new HashMap<Integer, Integer>();
    greenMap = new HashMap<Integer, Integer>();
    blueMap = new HashMap<Integer, Integer>();

  }

  /**
   * Find the count of each red, green, and blue channel values.
   *
   * @param image The image to be processed into a histogram.
   */
  public abstract void findColorCount(ReadOnlyImage image);

  protected List<Map<Integer, Integer>> mapSetUp(int imageSize) {

    for (int j = 0; j < imageSize; j += 1) {

      redMap.put(j, 0);
      greenMap.put(j, 0);
      blueMap.put(j, 0);

    }

    List<Map<Integer, Integer>> mapList = new ArrayList<Map<Integer, Integer>>();

    mapList.add(redMap);
    mapList.add(greenMap);
    mapList.add(blueMap);

    return mapList;
  }

  protected void putRedInMap(int color) {

    int currColorCount = redMap.get(color);
    currColorCount += 1;

    redMap.put(color, currColorCount);
  }

  protected void putGreenInMap(int color) {

    int currColorCount = greenMap.get(color);
    currColorCount += 1;

    greenMap.put(color, currColorCount);
  }

  protected void putBlueInMap(int color) {

    int currColorCount = blueMap.get(color);
    currColorCount += 1;

    blueMap.put(color, currColorCount);
  }

}