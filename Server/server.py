from flask import Flask, request
from io import BytesIO
import predictor as p

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def hello_world():
    return 'Hello, World!'

@app.route('/predict', methods=['POST'])
def predict():
    bytes = BytesIO(request.data)
    predicted = p.predict(bytes)

    return predicted[0][1]

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)