package ime.model.color;

import java.util.Objects;

/**
 * ColorImpl has values for its three channels which
 * cannot be changed after initialization. The channel
 * values are automatically clamped to stay within the
 * valid range of 0 to 255.
 */
public class ColorImpl implements Color {

  private final int red;
  private final int green;
  private final int blue;


  /**
   * Initialize the three channels upon construction
   * of the ColorImpl. Leverage the clamp function to
   * keep the channel values within the range.
   *
   * @param red   The red component of the RGB channel.
   * @param green The green component of the RGB channel.
   * @param blue  The blue component of the RGB channel.
   */
  public ColorImpl(int red, int green, int blue) {

    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  @Override
  public int getRed() {

    return red;
  }

  @Override
  public int getGreen() {

    return green;
  }

  @Override
  public int getBlue() {

    return blue;
  }

  private int clamp(float res) {

    if (res > 255) {
      return 255;
    }

    if (res < 0) {
      return 0;
    }

    return Math.round(res);
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (!(o instanceof ColorImpl)) {
      return false;
    }

    ColorImpl that = (ColorImpl) o;

    return (this.getRed() == that.getRed())
            && (this.getGreen() == that.getGreen())
            && (this.getBlue() == that.getBlue());
  }

  @Override
  public int hashCode() {

    return Objects.hash(red, green, blue);
  }

}
