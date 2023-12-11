package ime.model.operations;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;

/**
 * Dither the image using intensity value.
 */

public class Dither extends ExtractIntensityComponent implements ImageOperation {
  int height;
  int width;

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {
    height = image.getHeight();
    width = image.getWidth();

    Color color;

    ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    for (int i = 0; i < height; i += 1) {
      for (int j = 0; j < width; j += 1) {
        int intensity = findRed(i, j, image);
        color = new ColorImpl(intensity, intensity, intensity);
        resImageBuilder.setPixel(i, j, color);
      }
    }

    for (int i = 0; i < height; i += 1) {
      for (int j = 0; j < width; j += 1) {
        int old_color = resImageBuilder.getPixel(i, j);
        int new_color = 0;
        if (old_color >= 128) {
          new_color = 255;
        }
        double error = old_color - new_color;
        color = new ColorImpl(new_color, new_color, new_color);
        resImageBuilder.setPixel(i, j, color);

        updateColor(resImageBuilder, i, j + 1, error * 7 / 16);
        updateColor(resImageBuilder, i + 1, j - 1, error * 3 / 16);
        updateColor(resImageBuilder, i + 1, j, error * 5 / 16);
        updateColor(resImageBuilder, i + 1, j + 1, error * 1 / 16);
      }
    }

    return resImageBuilder.build();
  }

  private void updateColor(ReadOnlyImageImpl.ReadOnlyImageBuilder resImageBuilder,
                           int i, int j, double update) {
    if (i < 0 || i >= height || j < 0 || j >= width) {
      return;
    }
    int value = resImageBuilder.getPixel(i, j) + (int) Math.round(update);
    Color color = new ColorImpl(value, value, value);
    resImageBuilder.setPixel(i, j, color);
  }

}
