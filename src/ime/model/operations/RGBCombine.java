package ime.model.operations;

import ime.model.color.Color;
import ime.model.color.ColorImpl;
import ime.model.image.ReadOnlyImage;
import ime.model.image.ReadOnlyImageImpl;
import ime.model.operations.ThreeImageOperation;

/**
 * Combine the first channel of the first image,
 * the second channel of the second image, and the
 * third channel of the third image to create the
 * new image. The three images that are being
 * combined have to be of the same height and width.
 */
public class RGBCombine implements ThreeImageOperation {

  @Override
  public ReadOnlyImage apply(ReadOnlyImage firstImage,
                             ReadOnlyImage secondImage,
                             ReadOnlyImage thirdImage) {

    if (!checkValidSize(firstImage, secondImage, thirdImage)) {

      throw new IllegalArgumentException("Images are not of the same size!");
    }

    int height = firstImage.getHeight();
    int width = firstImage.getWidth();

    ReadOnlyImageImpl.ReadOnlyImageBuilder combinedImageBuilder =
            new ReadOnlyImageImpl.ReadOnlyImageBuilder(height, width);

    Color tempColor;
    int r;
    int g;
    int b;
    for (int i = 0; i < height; i += 1) {

      for (int j = 0; j < width; j += 1) {

        r = firstImage.getColor(i, j).getRed();
        g = secondImage.getColor(i, j).getGreen();
        b = thirdImage.getColor(i, j).getBlue();

        tempColor = new ColorImpl(r, g, b);
        combinedImageBuilder.setPixel(i, j, tempColor);
      }

    }

    return combinedImageBuilder.build();
  }

  private boolean checkValidSize(ReadOnlyImage firstImage,
                                 ReadOnlyImage secondImage,
                                 ReadOnlyImage thirdImage) {

    int firstHeight = firstImage.getHeight();
    int secondHeight = secondImage.getHeight();
    int thirdHeight = thirdImage.getHeight();

    int firstWidth = firstImage.getWidth();
    int secondWidth = secondImage.getWidth();
    int thirdWidth = thirdImage.getWidth();


    if (!checkValidHeight(firstHeight, secondHeight, thirdHeight)) {
      return false;
    }

    return checkValidWidth(firstWidth, secondWidth, thirdWidth);
  }

  private boolean checkValidHeight(int firstHeight,
                                   int secondHeight,
                                   int thirdHeight) {

    if (firstHeight != secondHeight) {
      return false;
    }

    if (firstHeight != thirdHeight) {
      return false;
    }

    return secondHeight == thirdHeight;
  }

  private boolean checkValidWidth(int firstWidth,
                                  int secondWidth,
                                  int thirdWidth) {

    if (firstWidth != secondWidth) {
      return false;
    }

    if (firstWidth != thirdWidth) {
      return false;
    }

    return secondWidth == thirdWidth;
  }
}
