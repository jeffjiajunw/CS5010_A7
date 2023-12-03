# CS5010 PDP – IME

> Authors: Agasti Mhatre and Kevin Zyskowski

### Jar Overview

To execute each mode of our jar (in this zip) execute the following sample CLI commands:

GUI mode

```
java -jar res/ime.jar
```

Text mode

```
java -jar res/ime.jar -text
```

Script file mode

```
java -jar res/ime.jar -file res/sample.txt
```

### GUI Overview

The GUI is operated through a toolbar, with buttons that expose each feature of our application.

- The load button allows users to load images from anywhere on their computer. Only one image can be loaded and worked
  on at a time.
- The save button allows users to save images to anywhere on their computer.
- The red, green, and blue buttons extract the individual red, green, and blue components of an image, respectively.
  These buttons provide split previews for users to visualize what changes will be made to their image before they make
  it.
- The sepia button applies a sepia filter to the image.
- The greyscale button applies a greyscale filter to the image.
- The horizontal and vertical flip buttons perform flips to the image.
- The blur and sharpen buttons perform blur and sharpening operations to the image.
- The compress button allows users to compress their images. When clicking this button, a popup appears that prompts the
  user for the compression factor. The user can enter the factor, and then preview what the compression would look like
  with a split preview window, before confirming the operation.
- The levels adjust button allows users to edit the levels curve of their image. When clicking this button, a popup
  appears that prompts the user for three individual x-coordinates for the blacks, mid-tones, and highlights. Then the
  user can also preview the level adjustment before confirming it.

### Commands Overview

- Load an image from an ASCII PPM, JPG or PNG file.
    - `load image-path image-name`
- Create images that visualize the red, green, blue components, value, intensity or luma of an image.
    - `red-component image-name dest-image-name`
    - `green-component image-name dest-image-name`
    - `blue-component image-name dest-image-name`
    - `value-component image-name dest-image-name`
    - `luma-component image-name dest-image-name`
    - `intensity-component image-name dest-image-name`
- Optionally preview split the luma operation.
    - `luma-component image-name dest-image-name split [0,100]`
- Flip an image horizontally or vertically.
    - `horizontal-flip image-name dest-image-name`
    - `vertical-flip image-name dest-image-name`
- Brighten or darken an image (positive increment brightens, negative increment darkens).
    - `brighten increment image-name dest-image-name`
- Split a single image into 3 images representing each of the three channels.
    - `rgb-split image-name dest-image-name-red dest-image-name-green dest-image-name-blue`
- Combine three greyscale image into a single color image whose red, green, blue values come from the three images.
    - `rgb-combine image-name red-image green-image blue-image`
- Blur or sharpen an image.
    - `blur image-name dest-image-name`
    - `sharpen image-name dest-image-name`
- Optionally preview split the blur or sharpen operation.
    - `blur image-name dest-image-name split [0,100]`
    - `sharpen image-name dest-image-name split [0,100]`
- Convert an image into sepia.
    - `sepia image-name dest-image-name`
- Optionally preview split the sepia operation.
    - `sepia image-name dest-image-name split [0,100]`
- Compress images.
    - `compress image-name dest-image-name`
- Generate image histograms.
    - `histogram image-name dest-image-name`
- Color correct images.
    - `color-correct image-name dest-image-name`
    - `color-correct image-name dest-image-name split [0, 100]`
- Levels adjust an image.
    - `levels image-name dest-image-name b m w`
    - `levels image-name dest-image-name b m w split [0, 100]`
- Save an image to an ASCII PPM, JPG or PNG file.
    - `save image-path image-name`
- Interact with the IME program to using text-based scripting.
    - `run script-file`
- Write single-line comments in script files.
    - `#load koala.ppm and call it 'koala'`
