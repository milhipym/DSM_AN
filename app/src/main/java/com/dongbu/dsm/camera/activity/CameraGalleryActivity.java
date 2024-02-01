package com.dongbu.dsm.camera.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.view.ActionMode;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by LandonJung on 2017-09-11.
 */

public class CameraGalleryActivity extends BaseActivity {
    public static boolean bResize = false;
    public static int RESIZE_WIDTH = 400;
    public static int RESIZE_HEIGHT = 400;
    public static int CAMERA_GALLERY_TYPE_A = 1;    // Camera
    public static int CAMERA_GALLERY_TYPE_B = 2;    // Gallery
    public static int CAMERA_GALLERY_TYPE_C = 3;    // Popup ( Camera + Gallery )

    ImageView img_logo;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    int mType = CAMERA_GALLERY_TYPE_C;
    String cameraImagePath, tmpCameraImagePath;

    String selectedImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null);
        DSMUtil.PopupAnimationNone(this);

        if(getIntent() != null) {
            mType = getIntent().getIntExtra(CommonData.JSON_CAMERA_GALLERY_TYPE, CAMERA_GALLERY_TYPE_C);
        }

        if(mType == CAMERA_GALLERY_TYPE_A) {
            doCamera();
        } else if(mType == CAMERA_GALLERY_TYPE_B) {
            doGallery();
        } else if(mType == CAMERA_GALLERY_TYPE_C) {
            startDialog();
        }
    }

    private void startDialog() {

        mDialog = new CustomAlertDialog(CameraGalleryActivity.this, CustomAlertDialog.TYPE_B);
        mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
        mDialog.setContent(getString(R.string.popup_dialog_select_camera_gallery));
        mDialog.setPositiveButton(getString(R.string.popup_dialog_camera), new CustomAlertDialogInterface.OnClickListener() {

            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                dialog.dismiss();

        doCamera();
    }
});
        mDialog.setNegativeButton(getString(R.string.popup_dialog_gallery), new CustomAlertDialogInterface.OnClickListener() {

@Override
public void onClick(CustomAlertDialog dialog, Button button) {
        dialog.dismiss();

        doGallery();
        }
        });
        mDialog.setBackButtonClickListener(new CustomAlertDialogInterface.OnClickListener() {

@Override
public void onClick(CustomAlertDialog dialog, Button button) {
        onBackPressed();
        }
        });

        mDialog.show();

        }

@Override
public void onBackPressed() {
//        if(mDialog.isShowing()) {
//            mDialog.dismiss();
//        }

        cloaseProc(RESULT_CANCELED);
        super.onBackPressed();
    }

    public void doCamera() {

        String storageState = Environment.getExternalStorageState();
        if(storageState.equals(Environment.MEDIA_MOUNTED)) {

            tmpCameraImagePath = CommonData.SAVE_IMAGE_PATH_ROOT + "camera" + File.separatorChar + DSMUtil.getRandomStr() + ".jpg";
            File _photoFile = new File(tmpCameraImagePath);
            try {
                if(_photoFile.exists() == false) {
                    _photoFile.getParentFile().mkdirs();
                    _photoFile.createNewFile();
                }

            } catch (IOException e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
                Log.e("Could not create file.");
            }
            Log.i("tmpCameraImagePath = " + tmpCameraImagePath);

            Uri _fileUri = Uri.fromFile(_photoFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
//            intent.putExtra( MediaStore.EXTRA_OUTPUT, _fileUri);
//            startActivityForResult(intent, CAMERA_REQUEST);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra( MediaStore.EXTRA_OUTPUT, _fileUri);
            } else {
                File file = new File(_fileUri.getPath());
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST);
            }


        }   else {

            mDialog = new CustomAlertDialog(CameraGalleryActivity.this, CustomAlertDialog.TYPE_B);
            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
            mDialog.setContent(getString(R.string.popup_dialog_no_sdcard));
            mDialog.setPositiveButton(getString(R.string.popup_dialog_camera), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();

                    cloaseProc(RESULT_CANCELED);
                }
            });

            mDialog.show();

        }
        //finish();
    }

    public void doGallery() {
        Intent pictureActionIntent = null;

        pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(
                pictureActionIntent,
                GALLERY_PICTURE);

       // finish();
    }

    private class SaveImageTask extends AsyncTask<String, Void, Bitmap> {

        public SaveImageTask() {

        }

        @Override
        protected void onPreExecute() {

            showProgress();

            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {

            Bitmap bmp = null;

            do {

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                }

                bmp = BitmapFactory.decodeFile(tmpCameraImagePath);

            } while (bmp == null);

            bmp.recycle();
            bmp = null;

            return null;
        }

        protected void onPostExecute(Bitmap result) {

            cameraImagePath = tmpCameraImagePath;

            File f = new File(cameraImagePath);
            if (!f.exists()) {
                Toast.makeText(getBaseContext(),
                        "Error while capturing image", Toast.LENGTH_LONG)
                        .show();
                cloaseProc(RESULT_CANCELED);
                return;
            }

            selectedImagePath = cameraImagePath;

            try {

                if(bResize) {
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    bitmap = Bitmap.createScaledBitmap(bitmap, RESIZE_WIDTH, RESIZE_HEIGHT, true);

                    int rotate = 0;
                    try {
                        ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                        int orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 270;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotate = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotate = 90;
                                break;
                        }
                    } catch (Exception e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);

                    //img_logo.setImageBitmap(bitmap);
                    storeImageTosdCard(bitmap);
                }

                cloaseProc(RESULT_OK);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if(Config.DISPLAY_LOG) e.printStackTrace();
                cloaseProc(RESULT_CANCELED);
            }

            hideProgress();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            new SaveImageTask().execute();

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {
                    Log.d("selectedImagePath = " + selectedImagePath);
                }

//                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
//                // preview image
//                if(bResize) {
//                    bitmap = Bitmap.createScaledBitmap(bitmap, RESIZE_WIDTH, RESIZE_HEIGHT, false);
//                }

                //img_logo.setImageBitmap(bitmap);
                cloaseProc(RESULT_OK);
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
                cloaseProc(RESULT_CANCELED);
            }
        } else {
            cloaseProc(RESULT_CANCELED);
        }
    }

    private void cloaseProc(int resultCode) {
        Intent data = new Intent();
        data.putExtra(CommonData.JSON_CAMERA_GALLERY_IMAGE_PATH, selectedImagePath);
        setResult(resultCode, data);
        finish();
    }

    private void storeImageTosdCard(Bitmap processedBitmap) {
        try {
            // TODO Auto-generated method stub
            Log.d("/////////////////////// 사진 저장 /////////////////////////");
            OutputStream output;
            // Find the SD Card path
            File filepath = Environment.getExternalStorageDirectory();
            // Create a new folder in SD Card
            //File dir = new File(filepath.getAbsolutePath() + "/dsm/");
            File dir = new File(Utils.getContext().getCacheDir().getPath() + " /camera/");
            dir.mkdirs();

            String imge_name = "dsm_" + System.currentTimeMillis() + ".jpg";

            Log.d("저장위치 : " + dir + imge_name);

            // Create a name for the saved image
            File file = new File(dir, imge_name);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }

            try {

                output = new FileOutputStream(file);

                // Compress into png format image from 0% - 100%
                processedBitmap
                        .compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();

                int file_size = Integer
                        .parseInt(String.valueOf(file.length() / 1024));
                Log.d("size ===>>> " + file_size);
                Log.d("file.length() ===>>> " + file.length());

                selectedImagePath = file.getAbsolutePath();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

    }

    @Override
    public void finish() {

        DSMUtil.PopupAnimationNone(this);

        super.finish();
    }
}
