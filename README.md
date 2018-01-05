# Swifty-Draw
Swifty Draw is a simple ML(Machine Learning) experiment/game running on Android and uses Tensorflow from all processing. 
This is completely based on Doodling where the drawn doodles are fed to Neural Network which then recognizes what is drawn.
This example uses only 19 images data for Demonstrating. 
A task is given to draw and it needs to be completed within given time. You will have one chance.

## Building

Building this is pretty easy. Simply import the files in Android Studio and you are good to go. 

But, you have to get your own images data and train it.

* Training the dataset : The retrained_graph_swiftydraw.pb and retrained_labels_swiftydraw.txt have not been provided in this source. 
So you'll have to train the neural network yourself to get these files out. 
Checkout [Training&Processing](https://github.com/MidsizeMango/Swifty-Draw/tree/master/Training-Processing) for details about processing and training.
