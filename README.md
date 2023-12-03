# CS5010 PDP – IME

> Authors: Agasti Mhatre and Kevin Zyskowski

### Design Overview

For this project we adhere to the MVC design pattern.

Our `model` package contains all code related to image manipulation.

The following interfaces and classes are used for the following purposes:

- ImageOperation.java: Every operation within the model that takes in a single image as an argument should implement the
  image operation. Function objects that implement this image should also return a new image as output.
- ThreeImageOperation.java: Every operation within the model that takes in three images as arguments should implement
  the three image operation. Function objects that implement this image should also return a new image as output.

- Color.java: This interface defines what all Color implementations should have. Every color should be able to retrieve
  its red, green, and blue channels and return them to the user.
- ColorImpl.java: The ColorImpl represents a color with RGB channels. It's RGB values cannot be changed once
  initialized. It also is responsible for clamping values.

- ReadOnlyImage.java: This interface defines an immutable image. An immutable image should be able to return its width,
  height, and pixels to the client.
- ReadOnlyImageImpl.java: An immutable image whose height, width, and pixel colors cannot be changed after
  initialization. Pixel values can be returned and set per pixel.
- IReadOnlyImageBuilder.java: Defines the set of methods needed for a builder for a ReadOnlyImage object. Every builder
  must be able to set pixel values, but it also must be able to build and return a ReadOnlyImage object.

- HaarWaveletCompression.java: Compresses the rows and columns of the 2D Color matrix of a ReadOnlyImage.

- AbstractFilter.java: This abstract class applies a filter to an image by running a kernel through every pixel of the
  image. The filter is provided by the implementation.
- Blur.java: Provides the 3x3 blur kernel to the apply method.
- Sharpen.java: Provides the 5x5 sharpen kernel to the apply method.

- HorizontalFlip.java: Uses pointers on the left and right sides of the image to swap pixels horizontally.
- VerticalFlip.java: Uses pointers on the top and bottom sides of the image to swap pixels vertically.

- Histogram.java: Abstract histogram class that helps its subclasses generate hash maps and increment them. The hash
  maps store the color count of each RGB value.
- GenerateHistogram.java: Takes in a ReadOnlyImage and creates a histogram for its red, green, and blue values.
- ColorCorrect.java: Calculates the count of each RGB value and adjusts the image coloring by adjusting the peaks of
  each channel.

- AbstractLinearTransformation.java: Finds the individual red, green, and blue components of each pixel. Because each
  implementation that extends this class is a linear operation, each implementation can provide the individual red,
  blue, and green components for each pixel.
- BrightenDarken.java: Adds a constant factor to each channel of each pixel in an image.
- ExtractBlueComponent.java: Return only the current blue channel of the pixel. Set other channels to 0.
- ExtractRedComponent.java: Return only the current red channel of the pixel. Set other channels to 0.
- ExtractGreenComponent.java: Return only the current green channel of the pixel. Set other channels to 0.
- ExtractIntensityComponent.java: Set every channel value of the new pixel to the average value of the three channels
  for the current pixel.
- ExtractLumaComponent.java: Set every channel value of the new pixel to the weighted sum of the three channels for the
  current pixel.
- ExtractValueComponent.java: Set every channel value of the new pixel to the maximum value of the three channels for
  the current pixel.

- MatrixMultiplication.java: Multiply a matrix by the RGB values of each pixel of the image. The new resulting matrix
  represents the new RGB values.
- SepiaTone.java: Provides the 3x3 Sepia Tone matrix that produces a sepia styled image.

- PreviewSplitOperation.java: Split the image into a left side and a right side: the left being the original image and
  the right being the new image after a given operation is applied to it.

- RGBCombine.java: Takes three images as input. The first image has its first channel of each pixel combined with the
  second channel of the second image and the third channel of the third image. The new image is therefore a combination
  of the three images and their channels.

Our `controller` package contains all code related to handling inputs and outputs, and communicates with the `model`
package. The main `controller.Controller` interface runs the program, and the text-based input/output functionality is
supported by the `controller.TextController` class. This class takes in an input stream, and output stream, model
object, and optionally a list of supported commands. It then runs the program by parsing the input stream, processing
the input and interacting with the model, and then outputting messages to the output stream.

The controller is implemented using a hybrid command/builder pattern, which is implemented by the
`controller.command.Command` and `controller.command.CommandBuilder` interfaces, respectively. The `Command` interface
operates as a consumer on `Session` objects, and works through side effects.

Most implementations of the `Command` interface accept the session as input, and then retrieve images, pass them through
a configuration of `ImageOperation` objects, and store them back into the session. There are slight exceptions in the
case of `RunCommand`, `SaveCommand`, and `LoadCommand`, where IO is involved.

The `SaveCommand` and `LoadCommand` objects abstract delegate the reading/writing of images to
`controller.io.ImageReader` and `controller.io.ImageWriter` objects, respectively, so they can be extended to load or
save images from many kinds of input/output streams, and not just files. Currently, the program supports JPEG, JPG, PNG,
and PPM images through the `PpmImageReader`, `PpmImageWriter`, `BufferedImageReader`, and `BufferedImageWriter`
classes.

The `RunCommand` object parses a file from the given script path and then creates a new `TextController` to run on that
file input stream. This way, the script file is treated exactly the same way as the original input stream, so the same
commands can be run, and scripts can even recursively call more scripts (except in cases where scripts are self-looping;
it is expected that users do not create scripts with this behavior).

The `MapCommand` is the most common command utilized, as it simply retrieves an image from the session, applies a
one-to-one `ImageOperation` function object on it, and stores the result back into the session. All commands that
extract image components, or blur/sharpen/sepia/brighten, are implemented or extended using this class.

Because it is not necessarily known at construction time what the arguments of a `Command` object will be, we offer
the `CommandSupplier` interface that offers a `get(String... args)` method to parse inputs, validate types/formats, and
create the correct `Command` objects. In the future this builder might be refactored to more closely mimic the design
found in the book _Effective Java_ by Joshua Bloch.

Three new command suppliers were added for assignment 5: the CompressCommandSupplier, the LevelsCommandSupplier, and the
PreviewSplitMapCommandSupplier. The first supplier was added for the compress operation, but utilizes the existing
MapCommand class. The second was added for the levels operation and also utilizes the existing map command. The third is
a generic class built for any map command that optionally offers preview splits. This supplier is utilized for all the
new/updated commands that require preview splits.

Our `view` package contains all code related to viewing the image in our editor and taking user input from a GUI.

The following interfaces and classes are used in the view:

- CompressListener.java: Works with the compress button in order to compress an image by a certain factor.
- EnterNumberListener.java: Creates a new frame with a button and text fields in order to get input from
  the user. Currently, this listener is only added to the compress and levels adjustment buttons.
- LevelsAdjustListener.java: Works with the levels adjust button in order to adjust an image by 
  the b, m, and w values inputted by the user.
- LoadNewImageListener.java: Works with the load button to load a new image into the GraphicalView frame.
- SaveImageListener.java: Works with the save button to save the image that is currently being worked on to
  a destination chosen by the user.
- SplitViewListener.java: Allows each button associated with an operation to be previewed before the user decides
  to commit the change.
- SubmitNumberListener.java: Abstract listener that helps the EnterNumberListener understand what to do after 
  the user clicks a submit button after entering values in a text field.
- SubmitNumberListenerFactory.java: Factory that returns the SubmitNumberListener implementation depending on
  the type passed. Currently supports CompressListener and LevelsAdjustListener.
- SubmitNumberListenerType.java: Enum that supports Compress and Levels Adjustment implementations.
- GraphicalView.java: The main frame that the user sees when the user starts up our program. This view
  contains everything needed to load, save, and edit the image.
- View.java: Outlines the display() and setController() methods for the view so that the controller of the
  view can be set (the controller executes the operations) and the view can be displayed from the Main function.

### UML Diagram

Below is the UML diagram of our program's design hierarchy:

![UML Diagram of the IME program](ime.png)

### Changes Made Since Assignment 4

- Added the "histogram" package with the following new classes: Histogram, GenerateHistogram, and ColorCorrect. These
  new function objects extend from the ImageOperation interface in order to easily be incorporated into the controller.
- Refactored the ReadOnlyImage class to include two new functions: getColor and getPixel. getColor returns a color
  object for the current RGB value, whereas getPixel returns the integer version of the three-channel color value.
- Added LevelsAdjustment to the lineartransformation package because each channel of each pixel is found using the
  uniform quadratic formula provided during initialization of the function object.
- Implemented ReadOnlyImage in place of Image in order to return an immutable image that cannot be mutated after being
  built by the builder.
- Created ReadOnlyImageBuilder within ReadOnlyImageImpl and created IReadOnlyImageBuilder interface; ReadOnlyImages are
  created through the builder and cannot be changed after building.
- Force all function objects to use ReadOnly image.
- The ReadOnlyImageImpl cannot be built from the builder if the height/width are less than or equal to 0, or if all the
  pixels in the color matrix are not set.
- Separated the command builders from the command classes, and renamed them suppliers. This change was made because the
  command building logic was specific to text-based inputs, whereas the actual command logic can be used across
  different types of views/input schemas.
- Added HaarWaveletCompression in the model layer to act as our compression function object.
- Added a new PreviewSplitOperation class in the model layer to add split percentage previewing on any ImageOperation.
- Added a new PreviewSplitMapCommandSupplier class to allow users to specify optional splits and their percentages for
  existing map commands.
- Moved the default commands and reader/writer classes back into the TextController class, so that they can be
  overridden in future extensions.
- Extended the TextController class (ExtendedTextController) to override the getDefaultCommands() to introduce the newly
  created commands, as well as update the existing commands with preview splitting.
- Created new command suppliers to utilize the new ImageOperation function objects.

### Changes Made Since Assignment 5
- View package was added with all of its files and packages
- Main class was changed to accomodate the new ViewController

### Citations

The `bambi.png` image packaged with this code was created by Kevin Zyskowski and was authorized by the owner to be used
for this project.
