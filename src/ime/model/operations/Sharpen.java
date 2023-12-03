package ime.model.operations;

/**
 * Sharpen provides a 5x5 matrix to AbstractFilter in order to carry
 * out the sharpening process. The matrix is initialized during
 * construction and cannot be changed. The kernel center of
 * (2, 2) is also provided by this class.
 */
public class Sharpen extends AbstractFilter implements ImageOperation {

  private final float[][] filter;

  /**
   * Instantiate the 5x5 Sharpen filter.
   */
  public Sharpen() {

    filter = new float[][]{

            {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1, 1 / 4f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
            {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}

    };

  }

  @Override
  protected float[][] provideKernel() {

    return filter;
  }

  @Override
  protected int[] provideKernelCenterCoord() {

    int[] centerCoord = new int[]{filter.length / 2, filter[0].length / 2};
    return centerCoord;
  }
}