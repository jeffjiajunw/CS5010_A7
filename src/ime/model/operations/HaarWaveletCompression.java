package ime.model.operations;

import ime.model.color.ColorImpl;
import ime.model.image.IReadOnlyImageBuilder;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;
import ime.util.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/** This class implements Haar wavelet compression on images. */
public class HaarWaveletCompression implements ImageOperation {
  private final double percentage;

  /**
   * Construct a new compression operation with the given compression percentage. A percentage of 0
   * indicates no compression, and a percentage of 100 indicates complete loss of data and results
   * in a black image.
   *
   * @param percentage the compression percentage. Must be within the range [0, 100].
   * @throws IllegalArgumentException if the percentage is not valid.
   */
  public HaarWaveletCompression(int percentage) throws IllegalArgumentException {
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("percentage must be between 0 and 100");
    }
    this.percentage = ((double) percentage) / 100.0;
  }

  /**
   * Get the minimum transform square matrix size that is a power of two.
   *
   * @param width the width of the image.
   * @param height the height of the image.
   * @return the size of the 2D sequence data to transform.
   */
  static int getTransformDimension(int width, int height) {
    // get the largest of width or height
    int size = Math.max(width, height);

    // figure out the power of two that gets us to this size
    int pow = (int) (Math.log(size) / Math.log(2));

    // if the size is not a power of 2 (but a little greater),
    // then return the next largest power of two
    if (Math.pow(2, pow) < size) {
      return (int) Math.pow(2, pow + 1);
    }

    // otherwise, the size is a power of two so return it
    return size;
  }

  /**
   * Extract channel data from an image into a 2D matrix.
   *
   * @param image the image
   * @param size the size of the output matrix. Should be a power of 2.
   * @param extractor the channel extractor.
   * @return the zero-rightbottom-padded channel matrix.
   */
  static double[][] extractChannelData(
      ReadOnlyImage image, int size, Function<Integer, Integer> extractor) {
    double[][] out = new double[size][size];
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (x < size && y < size) {
          out[y][x] = extractor.apply(image.getPixel(y, x));
        } else {
          out[y][x] = 0;
        }
      }
    }
    return out;
  }

  /**
   * Combines the RGB channels back into one image, and removes any zero padding.
   *
   * @param red the red channel.
   * @param green the green channel.
   * @param blue the blue channel.
   * @param width the width of the final image.
   * @param height the height of the final image.
   * @return the image object.
   */
  static ReadOnlyImage combineChannelData(
      double[][] red, double[][] green, double[][] blue, int width, int height) {
    IReadOnlyImageBuilder builder = new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        builder.setPixel(
            y,
            x,
            new ColorImpl(
                (int) Math.round(red[y][x]),
                (int) Math.round(green[y][x]),
                (int) Math.round(blue[y][x])));
      }
    }
    return builder.build();
  }

  /**
   * Transforms the given channel data into averages and differences recursively until the matrix is
   * mostly differences.
   *
   * @param channels the array of channels.
   */
  static void transform(double[][]... channels) {
    for (double[][] channel : channels) {
      int c = channel.length;
      while (c > 1) {
        for (int i = 0; i < c; i++) {
          double[] transformedRow = transformStepRow(channel, i, c);
          for (int x = 0; x < c; x++) {
            channel[i][x] = transformedRow[x];
          }
        }
        for (int i = 0; i < c; i++) {
          double[] transformedCol = transformStepCol(channel, i, c);
          for (int y = 0; y < c; y++) {
            channel[y][i] = transformedCol[y];
          }
        }

        c /= 2;
      }
    }
  }

  /**
   * Transform one row of the input matrix into averages and differences.
   *
   * @param input the input matrix.
   * @param row the row to transform.
   * @param c the length of the row to transform.
   * @return the transformed row.
   */
  private static double[] transformStepRow(double[][] input, int row, int c) {
    int half = c / 2;
    double[] output = new double[c];
    for (int i = 0; i < c; i += 2) {
      double avg = (input[row][i] + input[row][i + 1]) / Math.sqrt(2);
      double diff = (input[row][i] - input[row][i + 1]) / Math.sqrt(2);
      output[i / 2] = avg;
      output[i / 2 + half] = diff;
    }
    return output;
  }

  /**
   * Transform one column of the input matrix into averages and differences.
   *
   * @param input the input matrix.
   * @param col the column to transform.
   * @param c the length of the column to transform.
   * @return the transformed column.
   */
  private static double[] transformStepCol(double[][] input, int col, int c) {
    int half = c / 2;
    double[] output = new double[c];
    for (int i = 0; i < c; i += 2) {
      double avg = (input[i][col] + input[i + 1][col]) / Math.sqrt(2);
      double diff = (input[i][col] - input[i + 1][col]) / Math.sqrt(2);
      output[i / 2] = avg;
      output[i / 2 + half] = diff;
    }
    return output;
  }

  /**
   * Inverse the transform of averages/differences back into its original data.
   *
   * @param channels the array of channels.
   */
  static void inverse(double[][]... channels) {
    for (double[][] channel : channels) {
      int c = 2;
      while (c <= channel.length) {
        for (int i = 0; i < c; i++) {
          double[] inversedRow = inverseStepRow(channel, i, c);
          for (int x = 0; x < c; x++) {
            channel[i][x] = inversedRow[x];
          }
        }
        for (int i = 0; i < c; i++) {
          double[] inversedCol = inverseStepCol(channel, i, c);
          for (int y = 0; y < c; y++) {
            channel[y][i] = inversedCol[y];
          }
        }

        c *= 2;
      }
    }
  }

  /**
   * Inverse transform one row of the input matrix.
   *
   * @param input the input matrix.
   * @param row the row to invert.
   * @param c the length of the row to invert.
   * @return the inverted row.
   */
  private static double[] inverseStepRow(double[][] input, int row, int c) {
    int half = c / 2;
    double[] avgs = new double[half];
    double[] diffs = new double[half];

    for (int i = 0; i < half; i++) {
      double a = input[row][i];
      double b = input[row][i + half];

      double avg = (a + b) / Math.sqrt(2);
      double diff = (a - b) / Math.sqrt(2);
      avgs[i] = avg;
      diffs[i] = diff;
    }

    return interleave(avgs, diffs);
  }

  /**
   * Inverse transform one column of the input matrix.
   *
   * @param input the input matrix.
   * @param col the column to invert.
   * @param c the length of the column to invert.
   * @return the inverted column.
   */
  private static double[] inverseStepCol(double[][] input, int col, int c) {
    int half = c / 2;
    double[] avgs = new double[half];
    double[] diffs = new double[half];

    for (int i = 0; i < half; i++) {
      double a = input[i][col];
      double b = input[i + half][col];

      double avg = (a + b) / Math.sqrt(2);
      double diff = (a - b) / Math.sqrt(2);
      avgs[i] = avg;
      diffs[i] = diff;
    }

    return interleave(avgs, diffs);
  }

  /**
   * Interleave two arrays of equal length together.
   *
   * @param avgs the averages.
   * @param diffs the differences.
   * @return the interleaved array.
   */
  static double[] interleave(double[] avgs, double[] diffs) {
    double[] output = new double[avgs.length * 2];
    int i = 0;
    for (int j = 0; j < avgs.length; j++) {
      output[i] = avgs[j];
      i += 1;
      output[i] = diffs[j];
      i += 1;
    }
    return output;
  }

  /**
   * Flatten the matrix into a list, row-by-row.
   *
   * @param data the matrix.
   * @return the flattened list.
   */
  static List<Double> flatten(double[][] data) {
    int size = data.length;
    List<Double> output = new ArrayList<>();
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        if (data[y][x] != 0) {
          output.add(Math.abs(data[y][x]));
        }
      }
    }
    return output;
  }

  /**
   * Count the number of non-zero values in the given matrix.
   *
   * @param data the matrix.
   * @return the number of non-zero values.
   */
  static int countNonZero(double[][] data) {
    int size = data.length;
    int numNonZero = 0;
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        if (data[y][x] != 0) {
          numNonZero += 1;
        }
      }
    }
    return numNonZero;
  }

  /**
   * Apply the given threshold to the channels, and set the appropriate lowest values to zero.
   *
   * @param threshold the threshold at which to set values to zero.
   * @param channels the array of channels.
   */
  static void threshold(double threshold, double[][]... channels) {
    for (double[][] channel : channels) {
      int size = channel.length;
      for (int y = 0; y < size; y++) {
        for (int x = 0; x < size; x++) {
          if (Math.abs(channel[y][x]) <= threshold) {
            channel[y][x] = 0;
          }
        }
      }
    }
  }

  /**
   * Calculate the threshold value at which to prune pixel values to zero in each channel.
   *
   * @param channels the array of channels.
   * @return the threshold value.
   */
  double calculateThresholdValue(double[][]... channels) {
    // get the number of non-zero values to remove
    int numNonZeroInitial = 0;
    for (double[][] channel : channels) {
      numNonZeroInitial += countNonZero(channel);
    }
    int numNonZeroFinal = numNonZeroInitial - (int) Math.round(percentage * numNonZeroInitial);
    int numNonZeroPixelsToRemove = numNonZeroInitial - numNonZeroFinal;

    // remove NO non-zero values
    if (numNonZeroPixelsToRemove == 0) {
      return Double.NEGATIVE_INFINITY;
    }

    // remove ALL non-zero values
    if (numNonZeroFinal == 0) {
      return Double.POSITIVE_INFINITY;
    }

    // get a sorted list of all values in each channel
    List<Double> flattenedValues = new ArrayList<>();
    for (double[][] channel : channels) {
      List<Double> values =
          flatten(channel).stream()
              .map(Math::abs)
              .filter(value -> value > 0)
              .collect(Collectors.toList());
      flattenedValues.addAll(values);
    }
    flattenedValues.sort(Double::compare);

    return flattenedValues.get(numNonZeroPixelsToRemove);
  }

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int size = getTransformDimension(width, height);

    double[][] red = extractChannelData(image, size, Colors::redFrom);
    double[][] green = extractChannelData(image, size, Colors::greenFrom);
    double[][] blue = extractChannelData(image, size, Colors::blueFrom);
    transform(red, green, blue);
    double thresholdValue = calculateThresholdValue(red, green, blue);
    threshold(thresholdValue, red, green, blue);
    inverse(red, green, blue);

    return combineChannelData(red, green, blue, image.getWidth(), image.getHeight());
  }
}
