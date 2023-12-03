package ime.model.operations;

/**
 * Blur provides a 3x3 matrix to AbstractFilter in order to carry
 * out the blurring process. The matrix is initialized during
 * construction and cannot be changed. The kernel center of
 * (1, 1) is also provided by this class.
 */
public class Blur extends AbstractFilter implements ImageOperation {

  private final float[][] filter;

  /**
   * Instantiate the 3x3 blur filter.
   */
  public Blur() {

    filter = new float[][]{
            {1 / 16f, 1 / 8f, 1 / 16f},
            {1 / 8f, 1 / 4f, 1 / 8f},
            {1 / 16f, 1 / 8f, 1 / 16f}
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
