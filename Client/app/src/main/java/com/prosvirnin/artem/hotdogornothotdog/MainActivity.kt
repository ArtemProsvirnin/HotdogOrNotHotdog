package com.prosvirnin.artem.hotdogornothotdog

import android.os.Bundle
import android.app.Activity
import android.view.View
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView

class MainActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);
        val bmp = data.getExtras().get("data") as Bitmap;
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(bmp);
    }

    fun btnOnClick(view: View) {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    fun postData(bmp: Bitmap) {
        //TODO send data to server
    }
}