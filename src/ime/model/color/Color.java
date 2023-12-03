package ime.model.color;

/**
 * Colors in images are represented by RGB values. Each pixel
 * has an RGB value which is made up of the colors red, green,
 * and blue. These three colors can have values that range from
 * 0 (inclusive) to 255 (inclusive).
 */
public interface Color {

  /**
   * Return the red component of the current
   * color object.
   *
   * @return red          Value ranges from 0-255.
   */
  public int getRed();

  /**
   * Return the green component of the current
   * color object.
   *
   * @return green          Value ranges from 0-255.
   */
  public int getGreen();

  /**
   * Return the blue component of the current
   * color object.
   *
   * @return blue          Value ranges from 0-255.
   */
  public int getBlue();

}
