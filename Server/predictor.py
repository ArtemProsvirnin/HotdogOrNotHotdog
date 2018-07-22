import numpy as np
import  tensorflow as tf

from PIL import Image
from keras.applications.resnet50 import preprocess_input
from keras.applications import ResNet50
from keras.preprocessing.image import load_img, img_to_array
from keras.applications.densenet import decode_predictions

model = ResNet50()
graph = tf.get_default_graph()

def read_and_prep_image(bytes):
    im = Image.open(bytes)
    im = im.resize((224, 224))
    img_array = np.array([img_to_array(im)])
    return preprocess_input(img_array)

def predict(bytes):
    image_data = read_and_prep_image(bytes)

    with graph.as_default():
        preds = model.predict(image_data)
        most_likely_label = decode_predictions(preds, top=1)
        return 'hotdog' if most_likely_label[0][0][1] == 'hotdog' else 'not a hotdog'