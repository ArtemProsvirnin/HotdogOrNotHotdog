import os
import requests

def post_image(img_file):
    with open(img_file, 'rb') as file:
        img = file.read()

    content_type = 'image/jpeg'
    headers = { 'content-type': content_type }

    response = requests.post('http://0.0.0.0:5000/predict', data=img, headers=headers)

    print(response.text)

post_image('./images/4.jpg')
post_image('./images/8.jpg')
post_image('./images/cat.jpg')