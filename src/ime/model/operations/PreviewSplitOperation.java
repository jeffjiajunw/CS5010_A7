package ime.model.operations;

import java.util.Objects;

import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ImageOperation;

/**
 * This operation allows image operations to be "previewed" with a percentage slider across the
 * horizontal axis.
 */
public class PreviewSplitOperation implements ImageOperation {
  private final ImageOperation operation;
  private final double percentage;

  /**
   * Construct a new split operation.
   *
   * @param operation  the operation to preview.
   * @param percentage the percentage of the image from the left to preview the operation.
   * @throws IllegalArgumentException if the percentage is not in the range [0, 100].
   */
  public PreviewSplitOperation(ImageOperation operation, int percentage)
          throws IllegalArgumentException {
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("percentage must be between 0 and 100");
    }
    this.operation = Objects.requireNonNull(operation);
    this.percentage = ((double) percentage) / 100.0;
  }

  @Override
  public ReadOnlyImage apply(ReadOnlyImage image) {
    Objects.requireNonNull(image); // make sure image ain't null

    ReadOnlyImage previewImage = operation.apply(image);
    ReadOnlyImageImpl.ReadOnlyImageBuilder outputImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(image.getHeight(), image.getWidth());

    int boundaryX = (int) Math.round(image.getWidth() * percentage);
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (x < boundaryX) {
          outputImageBuilder.setPixel(y, x, previewImage.getColor(y, x));
        } else {
          outputImageBuilder.setPixel(y, x, image.getColor(y, x));
        }
      }
    }

    return outputImageBuilder.build();
  }

  @Override
  public String toString() {
    return String.format("<PreviewSplit op=\"%s\" percentage=\"%.2f\">", operation, percentage);
  }
}
