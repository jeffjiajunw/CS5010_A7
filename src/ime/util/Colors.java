package ime.util;

/**
 * This class holds utility functions for converting between (red, green, blue) tuples and 24-bit
 * integer representations of colors.
 */
public class Colors {
  /**
   * Convert a (red, green, blue) tuple into a 24-bit number. Inputs are truncated to bytes.
   *
   * @param r red byte.
   * @param g green byte.
   * @param b blue byte.
   * @return 24-bit pixel value.
   */
  public static int fromRgb(int r, int g, int b) {
    return ((r & 0xFF) << 16) + ((g & 0xFF) << 8) + (b & 0xFF);
  }

  /**
   * Get the red byte from a 24-bit color value.
   *
   * @param b pixel value.
   * @return red byte.
   */
  public static int redFrom(int b) {
    return (b >> 16) & 0xFF;
  }

  /**
   * Get the green byte from a 24-bit color value.
   *
   * @param b pixel value.
   * @return green byte.
   */
  public static int greenFrom(int b) {
    return (b >> 8) & 0xFF;
  }

  /**
   * Get the blue byte from a 24-bit color value.
   *
   * @param b pixel value.
   * @return blue byte.
   */
  public static int blueFrom(int b) {
    return (b) & 0xFF;
  }
}
