package com.prosvirnin.artem.hotdogornothotdog

import android.os.Bundle
import android.app.Activity
import android.view.View
import android.content.Intent
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLConnection
import android.os.Build

class MainActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0)
            return;

        val bmp = data.getExtras().get("data") as Bitmap;
        imageView.setImageBitmap(bmp);
        SendBmpRequest().execute(bmp);
    }

    fun takeAPhoto(view: View) {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    internal inner class SendBmpRequest : AsyncTask<Bitmap, Void, String>() {
         override fun doInBackground(vararg arg: Bitmap): String {
             val stream = ByteArrayOutputStream()
             arg[0].compress(Bitmap.CompressFormat.JPEG, 100, stream)

            return sendData(stream.toByteArray());
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            if (s == "hotdog")
                resultTxt.setText("Hotdog")
            else
                resultTxt.setText("Not a hotdog. Most likely: " + s)
        }

        private fun sendData(bytes: ByteArray): String {
            val url = URL(getHost() + "/predict")
            val conn = url.openConnection()

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "image/jpeg");

            conn.connect()

            val outputStream = conn.getOutputStream()
            outputStream.write(bytes)
            outputStream.close()

            return readResult(conn)
        }

        private fun readResult(conn: URLConnection): String {
            val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
            val stringBuilder = StringBuilder()

            var line: String? = null

            while (true) {
                line = reader.readLine()
                if (line == null)
                    break;
                stringBuilder.append(line + "\n")
            }

            return stringBuilder.toString();
        }

        private fun getHost(): String {
            if (isEmulator())
                return "http://10.0.2.2:5000";
            return "http://192.168.88.255:5000"
        }

        private fun isEmulator(): Boolean {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || "google_sdk".equals(Build.PRODUCT);
        }
    }
}