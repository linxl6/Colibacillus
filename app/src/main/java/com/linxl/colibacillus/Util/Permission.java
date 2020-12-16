package com.linxl.colibacillus.Util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.linxl.colibacillus.BuildConfig;

public class Permission {
//    /**
//     * 跳转到miui的权限管理页面
//     */
//    private void gotoMiuiPermission(Context context) {
//        try { // MIUI 8
//            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
//            localIntent.putExtra("extra_pkgname", context.getPackageName());
//            context.startActivity(localIntent);
//        } catch (Exception e) {
//            try { // MIUI 5/6/7
//                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
//                localIntent.putExtra("extra_pkgname", context.getPackageName());
//                context.startActivity(localIntent);
//            } catch (Exception e1) { // 否则跳转到应用详情
//                context.startActivity(getAppDetailSettingIntent());
//            }
//        }
//    }
//
//    /**
//     * 跳转到魅族的权限管理系统
//     */
//    private void gotoMeizuPermission(Context context) {
//        try {
//            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            context.startActivity(getAppDetailSettingIntent());
//        }
//    }
//
//    /**
//     * 华为的权限管理页面
//     */
//    private void gotoHuaweiPermission(Context context) {
//        try {
//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//            intent.setComponent(comp);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            context.startActivity(getAppDetailSettingIntent());
//        }
//
//    }
//
//    /**
//     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
//     *
//     * @return
//     */
//    private Intent getAppDetailSettingIntent() {
//        Intent localIntent = new Intent();
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= 9) {
//            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
//        } else if (Build.VERSION.SDK_INT <= 8) {
//            localIntent.setAction(Intent.ACTION_VIEW);
//            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
//            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
//        }
//        return localIntent;
//    }


}
