import os
import numpy as np
import sys
from PIL import Image
import PIL.ImageOps

no_images = 1000; 
npy_dir = 'H:/npyfiles/'
out_dir = 'H:/Output/'

npy_files = []
for x in os.listdir(npy_dir):
	if(os.path.isfile(os.path.join(npy_dir, x))):
		npy_files.append(x)
print(npy_files)
#print(x)
categories = []
for i in npy_files:
	#print('NpyFiles:', i)
	category_split = i.split('.')
	#print('Category Split:', category_split)
	category = category_split[0].title()
	#print('Category:', category + '\n')
	categories.append(category)
print(categories)

for j in categories:
	if not os.path.exists(os.path.join(out_dir, j)):
		os.makedirs(os.path.join(out_dir, j))

index = 0
for k in npy_files:
	print('Processing File:', k)
	#file_name_split = k.split('_')
	images = np.load(os.path.join(npy_dir, k))
	print('Saving in', categories[index])
	no_imgs = range(0, no_images, 1)
	for a in no_imgs:
		print('Processing Image:', a+1);
		file_name = '%s.jpg'%(a+1)
		file_path = os.path.join(out_dir, categories[index], file_name)
		img = images[a].reshape(28, 28)
		f_img = Image.fromarray(img);
		inv_image = PIL.ImageOps.invert(f_img)
		inv_image.save(file_path, 'JPEG')
	index+=1