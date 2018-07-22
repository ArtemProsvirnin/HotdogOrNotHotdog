from flask import Flask, request
from io import BytesIO
import predictor as p

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/predict', methods=['POST'])
def predict():
    bytes = BytesIO(request.data)
    return '{ "result": "' + p.predict(bytes) + '" }'

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)