# Swifty-Draw
Swifty Draw is a simple ML(Machine Learning) experiment/game running on Android and uses Tensorflow from all processing. 
This is completely based on Doodling where the drawn doodles are fed to Neural Network which then recognizes what is drawn.

This example uses only 19 images data for Demonstrating. 
A task will be provided to draw and it needs to be completed within given time. You will have one chance.

<img src="https://raw.githubusercontent.com/MidsizeMango/Swifty-Draw/master/image_fs.png" /> 

## Building

Building this is pretty easy. Simply import the files in Android Studio and you are good to go. 

* Training the dataset : The retrained_graph_swiftydraw.pb and retrained_labels_swiftydraw.txt have been provided in this source.
But if you more data or images, then you will have to train your own data and create neural network yourself to get these files out. 
Checkout [Training and Processing](https://github.com/MidsizeMango/Swifty-Draw/tree/master/Training-Processing) for details about processing and training.

## Credits:
Aniruddh Chandatre

## License

This is under the MIT License : 

```
Copyright 2018 Prasad Shirvandkar

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
