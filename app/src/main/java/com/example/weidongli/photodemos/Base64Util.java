package com.example.weidongli.photodemos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by guolg on 2019/4/17.
 */

public class Base64Util {


    static final String imageFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/CameraInit/my_photo.jpg";
    //图片转化成base64字符串
//    public static String GetImageStr()
//    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//        String imgFile = imageFile;//待处理的图片
//        InputStream in = null;
//        byte[] data = null;
//        //读取图片字节数组
//        try
//        {
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);//返回Base64编码过的字节数组字符串
////        return null;
//    }

    //Bitmap—》Base64
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

//
//    public static boolean GenerateImage(String imgStr)
//    {   //对字节数组字符串进行Base64解码并生成图片
//        if (imgStr == null) //图像数据为空
//            return false;
//        BASE64Decoder decoder = new BASE64Decoder();
//        try
//        {
//            //Base64解码
//            byte[] b = decoder.decodeBuffer(imgStr);
//            for(int i=0;i<b.length;++i)
//            {
//                if(b[i]<0)
//                {//调整异常数据
//                    b[i]+=256;
//                }
//            }
//            //生成jpeg图片
//            String imgFilePath =imageFile+"/22234.jpg";//新生成的图片
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(b);
//            out.flush();
//            out.close();
//            return true;
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


    }

//    // 将图片文件转化为字节数组字符串，并对其转化为--->Base64编码
// public  String GetImageStr(String imgFilePath) {
//byte[] data = null;
//// 读取图片字节数组
// try {
//InputStream in = new FileInputStream(imgFilePath);
// data = new byte[in.available()];
// in.read(data);
// in.close();
//} catch (Exception e) {
// e.printStackTrace();
//}
//
// // 对字节数组Base64编码
//BASE64Encoder encoder = new BASE64Encoder();
// return encoder.encode(data);// 返回Base64编码过的字节数组字符串
//}




    // 把Bitmap转换成Base64
    public static String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, 0);
    }

    // 把Base64转换成Bitmap
    public static Bitmap getBitmapFromBase64(String iconBase64) {
        byte[] bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 将bitmap转换成为Base64
     * @param bitmap
     * @return
     */
    public  static String bitmaptoString(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 将Base64转换成为Bitmap
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
