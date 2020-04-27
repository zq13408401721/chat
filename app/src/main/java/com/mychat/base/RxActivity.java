package com.mychat.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mychat.R;


/**
 * 动态权限设置基类
 */
public class RxActivity extends AppCompatActivity {


    private SparseArray<OnPermissionResultListener> listenerMap = new SparseArray<>();
    protected AlertDialog.Builder builder;

    public interface OnPermissionResultListener{
        //设置权限被允许
        void onAllow();

        //设置权限被拒绝
        void onReject();
    }


    protected void checkPermissions(final String[] permissions,OnPermissionResultListener listener){
        if(Build.VERSION.SDK_INT < 23 || permissions.length == 0){
            if(listener != null){
                listener.onAllow();
            }
        }else{
            int size = listenerMap.size();
            if(listener != null){
                listenerMap.put(size,listener);
            }
            ActivityCompat.requestPermissions(this,permissions,size);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionResultListener onPermissionResultListener = listenerMap.get(requestCode);
        if(onPermissionResultListener != null){
            listenerMap.remove(requestCode);
            // 需要通过循环判断是否所有条件都被允许了
            for(int i=0; i<grantResults.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){  //用户拒绝权限
                    //1 用户拒绝了权限，没有勾选"不在提醒"，此方法降返回true
                    //2 用户拒绝了该权限，勾选了"不在提醒'，此方法放回false
                    //3 如果用户同意了权限，次方法返回false
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])){
                        showDialog();
                    }else{
                        onPermissionResultListener.onReject();
                    }
                    return;
                }
            }
            onPermissionResultListener.onAllow();
        }
    }

    private void showDialog(){
        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("设置权限")
                .setMessage("动态权限设置").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toAppDetailSetting();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 跳转到设置的详情页
     */
    private void toAppDetailSetting(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.setting.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package",getPackageName(),null));
        startActivity(intent);
    }
}

