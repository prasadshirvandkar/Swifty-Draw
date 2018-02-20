# Dataset | Processing and training

You can easily prepocess and train the [quickdraw dataset](https://github.com/googlecreativelab/quickdraw-dataset) to make it work for this application. Since I won't be uploading the dataset myself, you will have to process the dataset on your own for further usage.

## Downloading the data :
 
 Google has open sourced the preprocessed quickdraw dataset and it is available in 4 major types. We will be using the numpy bitmaps (npy files) to generate images which can be trained on our image classifier. 
 
### Numpy bitmaps (.npy) :

All the simplified drawings have been rendered into a 28x28 grayscale bitmap in numpy .npy format. The files can be loaded with np.load(). These images were generated from the simplified data, but are aligned to the center of the drawing's bounding box rather than the top-left corner.

Download the dataset from : [Numpy image bitmaps](https://console.cloud.google.com/storage/browser/quickdraw_dataset/full/numpy_bitmap)

NOTE: Only download the categories you need. Total size of all the npy files collectively is more than 35gb.


## Training and Processing of Data

## Processing :
The npy files are provided as input to convert_npy_to_jpg.py python script which will generate about 1000 images (can be edited) in each category processed.

NOTE: Before running the file, check or change the input and output location according to your directory location in the script.

*  Run the processing script : 

```
python3 convert_npy_to_jpg.py
```
The images are the input to the neural network training.

## Training :
The retrain script can retrain either Inception V3 model or a MobileNet. In this exercise, we will use a MobileNet. The principal difference is that Inception V3 is optimized for accuracy, while the MobileNets are optimized to be small and efficient, at the cost of some accuracy.

Inception V3 has a first-choice accuracy of 78% on ImageNet, but is the model is 85MB, and requires many times more processing than even the largest MobileNet configuration, which achieves 70.5% accuracy, with just a 19MB download.

To start the training, just enter:
```
mkdir TrainingResult
```
Then

```
python3 -m retrain \
  --bottleneck_dir=TrainingResult/bottlenecks \
  --how_many_training_steps=7000 \
  --model_dir=TrainingResult/models/ \
  --summaries_dir=TrainingResult/training_summaries/"mobilenet_1.0_224" \
  --output_graph=TrainingResult/retrained_graph.pb \
  --output_labels=TrainingResult/retrained_labels.txt \
  --architecture="mobilenet_1.0_224" \
  --image_dir=Output

```
NOTE: Image Dir Output to be replaced by your Images Directory.

The Training will take some time depending upon the dataset and the processing power of your machine.

When this finishes, we will have two files : 
 
 * ```retrained_graph.pb``` : The newly generated protobuf graph of our neural network which will be used in Android App for recognizing images.
 * ```retrained_labels.txt``` : The newly generated labels file which contains names for images trained.




