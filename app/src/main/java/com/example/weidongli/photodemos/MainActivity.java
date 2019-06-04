package com.example.weidongli.photodemos;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="MainActivity" ;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private final int IMAGE_RESULT_CODE = 2;//相册
    private final int PICK = 1;//拍照

    private PopupWindow pop;
    private List<String> list;

    private View bottomView;
    private Bitmap bitmap;
    private int flag;
    private  String photoPath;

    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File file;
    private Bitmap bitmap1;
    private FileOutputStream fileOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
    }

    private void initView() {
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image1:
                showPop();
                flag=1;
                break;
            case R.id.image2:
                showPop();
                flag=2;
                break;
            case R.id.image3:
                showPop();
                flag=3;
                break;

            case R.id.image4:
                showPop();
                flag=4;
                break;


        }
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }

    private void showPop() {
        bottomView = View.inflate(MainActivity.this, R.layout.layout_bottom_dialog, null);
        Button mAlbum = bottomView.findViewById(R.id.tv_album);
        Button mCamera = bottomView.findViewById(R.id.tv_camera);
        Button mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        getPermission();
                        goPhotoAlbum();

                        break;
                    case R.id.tv_camera:
                        getPermission();
                        goCamera();
                        break;

                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    //激活相机操作
    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MainActivity.this, "com.example.weidongli.photodemos.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        MainActivity.this.startActivityForResult(intent, PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


//    //成功打开权限
//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//
//        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
//    }
//    //用户未同意权限
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
//    }


    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照
            case PICK:

//
//                String sdStatus = Environment.getExternalStorageState();
//
//                if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
//                    System.out.println(" ------------- sd card is not avaiable ---------------");
//                    return;
//                }
//
//
//                String name = "photo.jpg";
//
//                Bundle bundle = data.getExtras();
//                 bitmap = (Bitmap) bundle.get("data");
//
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
//                file.mkdirs(); //创建文件夹
//
//                String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name;
//
//                FileOutputStream b =null;
//
//                try {
//                    b=new FileOutputStream(fileName);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }finally {
//                    try {
//                        b.flush();
//                        b.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                if(flag==1)
//                {
//                    imageView1.setImageBitmap(bitmap);
//
//                }
//                else if(flag==2)
//                {
//                    imageView2.setImageBitmap(bitmap);
//
//                }
//                else if(flag==3)
//                {
//                    imageView3.setImageBitmap(bitmap);
//
//                }
//                else if(flag==4)
//                { imageView4.setImageBitmap(bitmap);
//                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoPath = String.valueOf(cameraSavePath);
                } else {
                    photoPath = uri.getEncodedPath();
                }
                Log.d("拍照返回图片路径:", photoPath);
                file = new File(photoPath);
                Log.e(TAG, "initView: "+photoPath );
                if(flag==1)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView1);
                }
                else if(flag==2)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView2);
                }
                else if(flag==3)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView3);
                }
                else if(flag==4)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView4);
                }





                break;

            //相册
            case IMAGE_RESULT_CODE:

//                Uri selectedImage = data.getData();
//                String[] filePathColumns = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePathColumns[0]);
//                String imagePath = c.getString(columnIndex);
//                bitmap = BitmapFactory.decodeFile(imagePath);
//                if(flag==1)
//                {
//                    imageView1.setImageBitmap(bitmap);
//
//                }
//                else if(flag==2)
//                {
//                    imageView2.setImageBitmap(bitmap);
//
//                }
//                else if(flag==3)
//                {
//                    imageView3.setImageBitmap(bitmap);
//
//                }
//                else if(flag==4)
//                { imageView4.setImageBitmap(bitmap);
//                }
                photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                file = new File(photoPath);
                if(flag==1)
                {
                   //Glide.with(MainActivity.this).load(photoPath).into(imageView1);
                    bitmap = BitmapFactory.decodeFile(photoPath);
                    Toast.makeText(MainActivity.this,"图片的大小为"+bitmap.getByteCount()/1024+"M",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "SSSS: "+bitmap.getByteCount()/1024+photoPath );
//                    try {
//                        fileOutputStream = new FileOutputStream(file.getPath());
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    bitmap.compress(Bitmap.CompressFormat.PNG,30,fileOutputStream);
//                 //   Log.e(TAG, "onActivityResult: "+Base64Util.bitmapToBase64(bitmap) );
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                    imageView1.setImageBitmap(scaledBitmap);

                    Log.e(TAG, "onActivityResult: "+scaledBitmap.getByteCount()/1024 );
                    //创建路径
                    String path = Environment.getExternalStorageDirectory()
                            .getPath() ;
                    //获取外部储存目录
                    file = new File(path);
                    //创建新目录, 创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。
                    file.mkdirs();
                    //以当前时间重新命名文件
                    long i = System.currentTimeMillis();
                    //生成新的文件
                    file = new File(file.toString() + "/" + i + ".png");
                    Log.e("fileNew", path);
                    Toast.makeText(MainActivity.this,"图片的大小为"+scaledBitmap.getByteCount()/1024+"KB",Toast.LENGTH_LONG).show();

                }
                else if(flag==2)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(new Target<Drawable>() {
                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void getSize(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(@Nullable Request request) {

                        }

                        @Nullable
                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
                }
                else if(flag==3)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView3);
                }
                else if(flag==4)
                {
                    Glide.with(MainActivity.this).load(photoPath).into(imageView4);
                }

                break;
        }
    }
}
