package com.blackswan.fake.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import com.blackswan.fake.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

/**
 * 版本检测，自动更新
 * 
 * @author shenyj-ydrh 1.通过Url检测更新 2.下载并安装更新 3.删除临时路径
 * 
 */
@SuppressLint("DefaultLocale")
public class UpdateService {
        // 调用更新的Activity
        public Activity activity = null;
        // 当前版本号
        public int versionCode = 0;
        // 当前版本名称
        public String versionName = "";
        //服务器现有版本
        public int serverVersion = 0; 
        // 控制台信息标识
        private static final String TAG = "AutoUpdate";
        // 文件当前路径
        private String currentFilePath = "";
        // 安装包文件临时路径
        private String currentTempFilePath = "";
        // 获得文件扩展名字符串
        private String fileEx = "";
        // 获得文件名字符串
        private String fileNa = "";
        // 服务器地址
        private String strURL = "http://127.0.0.1:8080/ApiDemos.apk";
        private ProgressDialog dialog;
        /**
         * 构造方法，获得当前版本信息
         * 
         * @param activity
         */
        public UpdateService(Activity activity) {
                this.activity = activity;
                // 获得当前版本
                getCurrentVersion();
        }
        
        /*** 
         * 在这里写一个方法用于请求获取服务器端的serverVersion. 
         */
        
        
        /**
         * 检测更新
         */
        public void check() {
                // 检测网络
                if (isNetworkAvailable(this.activity) == false) {  
                        return;
                }
                // 如果网络可用，检测到新版本
                if (versionCode < serverVersion) {
                        // 弹出对话框，选择是否需要更新版本
                        showUpdateDialog();
                }
        }

        /**
         * 检测是否有可用网络
         * 
         * @param context
         * @return 网络连接状态
         */
        public static boolean isNetworkAvailable(Context context) {
                try {
                        ConnectivityManager cm = (ConnectivityManager) context
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                        // 获取网络信息
                        NetworkInfo info = cm.getActiveNetworkInfo();
                        // 返回检测的网络状态
                        return (info != null && info.isConnected());
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        /**
         * 弹出对话框，选择是否需要更新版本
         */
        public void showUpdateDialog() {
                @SuppressWarnings("unused")
                AlertDialog alert = new AlertDialog.Builder(this.activity)
                                .setTitle("新版本").setIcon(R.drawable.ic_launcher)
                                .setMessage("是否更新？")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                // 通过地址下载文件
                                                downloadTheFile(strURL);
                                                // 显示更新状态，进度条
                                                showWaitDialog();
                                        }
                                })
                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                        }
                                }).show();
        }

        /**
         * 显示更新状态，进度条
         */
        public void showWaitDialog() {
                dialog = new ProgressDialog(activity);
                dialog.setMessage("正在更新，请稍候...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.show();
        }

        /**
         * 获得当前版本信息
         */
        public void getCurrentVersion() {
                try {
                        // 获取应用包信息
                        PackageInfo info = activity.getPackageManager().getPackageInfo(
                                        activity.getPackageName(), 0);
                        this.versionCode = info.versionCode;
                        this.versionName = info.versionName;
                } catch (NameNotFoundException e) {
                        e.printStackTrace();
                }
        }

        /**
         * 截取文件名称并执行下载
         * 
         * @param strPath
         */
        private void downloadTheFile(final String strPath) {
                // 获得文件文件扩展名字符串
                fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
                                .toLowerCase();
                // 获得文件文件名字符串
                fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
                                strURL.lastIndexOf("."));
                try {
                        if (strPath.equals(currentFilePath)) {
                                doDownloadTheFile(strPath);
                        }
                        currentFilePath = strPath;
                        new Thread(new Runnable() {

                                @Override
                                public void run() {
                                        // TODO Auto-generated method stub
                                        try {
                                                // 执行下载
                                                doDownloadTheFile(strPath);
                                        } catch (Exception e) {
                                                Log.e(TAG, e.getMessage(), e);
                                        }
                                }
                        }).start();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * 执行新版本进行下载，并安装
         * 
         * @param strPath
         * @throws Exception
         */
        @SuppressWarnings("resource")
		private void doDownloadTheFile(String strPath) throws Exception {
                Log.i(TAG, "getDataSource()");
                // 判断strPath是否为网络地址
                if (!URLUtil.isNetworkUrl(strPath)) {
                        Log.i(TAG, "服务器地址错误！");
                } else {
                        URL myURL = new URL(strPath);
                        URLConnection conn = myURL.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        if (is == null) {
                                throw new RuntimeException("stream is null");
                        }
                        //生成一个临时文件 
                        File myTempFile = File.createTempFile(fileNa, "." + fileEx);
                        // 安装包文件临时路径
                        currentTempFilePath = myTempFile.getAbsolutePath();
                        FileOutputStream fos = new FileOutputStream(myTempFile);
                        byte buf[] = new byte[128];
                        do {
                                int numread = is.read(buf);
                                if (numread <= 0) {
                                        break;
                                }
                                fos.write(buf, 0, numread);
                        } while (true);
                        Log.i(TAG, "getDataSource() Download  ok...");
                        dialog.cancel();
                        dialog.dismiss();
                        // 打开文件
                        openFile(myTempFile);
                        try {
                                is.close();
                        } catch (Exception ex) {
                                Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
                        }
                }
        }

        /**
         * 打开文件进行安装
         * 
         * @param f
         */
        private void openFile(File f) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                // 获得下载好的文件类型
                String type = getMIMEType(f);
                // 打开各种类型文件
                intent.setDataAndType(Uri.fromFile(f), type);
                // 安装
                activity.startActivity(intent);
        }

        /**
         * 删除临时路径里的安装包
         */
        public void delFile() {
                Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");
                File myFile = new File(currentTempFilePath);
                if (myFile.exists()) {
                        myFile.delete();
                }
        }

        /**
         * 获得下载文件的类型
         * 
         * @param f
         *            文件名称
         * @return 文件类型
         */
        private String getMIMEType(File f) {
                String type = "";
                // 获得文件名称
                String fName = f.getName();
                // 获得文件扩展名
                String end = fName
                                .substring(fName.lastIndexOf(".") + 1, fName.length())
                                .toLowerCase();
                if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
                        type = "audio";
                } else if (end.equals("3gp") || end.equals("mp4")) {
                        type = "video";
                } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                                || end.equals("jpeg") || end.equals("bmp")) {
                        type = "image";
                } else if (end.equals("apk")) {
                        type = "application/vnd.android.package-archive";
                } else {
                        type = "*";
                }
                if (end.equals("apk")) {
                } else {
                        type += "/*";
                }
                return type;
        }
}

