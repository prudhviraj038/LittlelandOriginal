package com.leadinfosoft.littleland.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by radhe on 1/27/2018.
 */
public class CircleTransform implements Transformation {

    ImageView imageView;

    public CircleTransform(ImageView iv_image) {

        this.imageView = iv_image;

    }

    @Override
    public Bitmap transform(Bitmap source) {
        //Create a new and slightly higher bitmap where 2 images will be drawn
        Bitmap combinedBitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight() / 3 + source.getHeight(), source.getConfig());

        //Canvas to draw the original bitmap to the top part
        Canvas comboImage = new Canvas(combinedBitmap);
        comboImage.drawBitmap(source, 0f, 0f, null);

        //Matrix to rotate, mirror and reposition the bitmap underneath the original bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        matrix.preScale(-1, 1);
        matrix.postTranslate(0, source.getHeight() * 2);

        //Do a blur transformation using the library
        BlurTransformation blurTransformation = new BlurTransformation(imageView.getContext(), 10, 1);

        //Get the bitmap object from the transformation
        Bitmap bottom = blurTransformation.transform(source);

        //Set the matrix to the canvas to apply the previous modifications
        comboImage.setMatrix(matrix);
        //Draw the second bitmap with transformation, rotation, mirroring and translation
        comboImage.drawBitmap(bottom, 0f, 0f, null);
//        combinedBitmap;
        return combinedBitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}