# CS5010 IME: Working with other code

### Code overview
- Dither an image implementation: Y
- Script command to dither an image: Y
- Dither from GUI: Y

### Design Overview

For this project we implemented image dithering to be in harmony with the design given.

- Added the dither method class in the operations that extends ExtractIntensityComponent since we need to use intensity of the 
  image. Using the ReadOnlyImageBuilder and to set pixel and update the pixel. 
- Added a new method called "getPixel" to ReadOnlyImageImpl in order to get the existing pixel value and update it.
- Added a new dither command using commands.put in ExtendedTextController.
- Added a Jbutton called "dither" to GraphicalView and set its buttonlistener to controller so that all things are similar with
  what is provided.


### Citations

Author: Jovana Askrabic (https://unsplash.com/@jovana0909)

Source: https://unsplash.com/photos/black-white-and-brown-bernese-mountain-dog-lying-on-green-grass-field-during-daytime-4QQQTTXn3NA

License or Usage Terms: https://unsplash.com/license
