package ime.model.operations;

/**
 * Provide the SepiaTone matrix to the MatrixMultiplication
 * class in order to transform the image into a sepia filtered
 * image.
 */
public class SepiaTone extends MatrixMultiplication implements ImageOperation {

  @Override
  protected float[][] getTransformationMatrix() {
    return new float[][]{
            {0.393f, 0.769f, 0.189f},
            {0.349f, 0.686f, 0.168f},
            {0.272f, 0.534f, 0.131f}

    };
  }

}
