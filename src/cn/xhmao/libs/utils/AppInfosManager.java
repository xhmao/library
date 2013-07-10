package cn.xhmao.libs.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppInfosManager {
	static class ApplicationManagerHolder {
		static AppInfosManager sInstance = new AppInfosManager();
	}
	
	public static AppInfosManager instance() {
		return ApplicationManagerHolder.sInstance;
	}

	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static boolean queryExistApp(Context context, String pkg, String cls) {
		if (context == null || 
				pkg == null || pkg.equals("") || 
				cls == null || cls.equals("")) {
			return false;
		}
		
		List<ResolveInfo> existApps;
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent();
		ComponentName name = new ComponentName(pkg, cls);
		
		intent.setComponent(name);
		
		existApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY); 
		if (existApps == null || existApps.size() == 0) {
			return false;
		}
		
		return true;
	}
	
	public static String searchDefaultHomeApplication(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ComponentName> prefActList = new ArrayList<ComponentName>();
		List<IntentFilter> intentList = new ArrayList<IntentFilter>(); 
		pm.getPreferredActivities(intentList,  prefActList, null); 
		
		
		/* ACTION_MAIN + CATEGORY_HOME + CATEGORY_DEFAULT = home app */
		for(int i = 0; i < prefActList.size(); ++i) {
			ComponentName cn = prefActList.get(i);
			IntentFilter ifl = intentList.get(i);
			for(int j = 0; j < ifl.countActions(); ++j){
				if(ifl.getAction(j).equals(Intent.ACTION_MAIN)){
					Boolean isCategoryHome = false;
					Boolean isCategoryDefault= false;
					for(int k = 0; k < ifl.countCategories(); k++){
						if(ifl.getCategory(k).equals(Intent.CATEGORY_HOME)) 
							isCategoryHome = true;
						if(ifl.getCategory(k).equals(Intent.CATEGORY_DEFAULT)) 
							isCategoryDefault = true;
					}
					if(isCategoryHome && isCategoryDefault){
						//LogUtil.d("Switcher", "default home app pkg name is " + cn.getPackageName());
						//LogUtil.d("Switcher", "default home app class name is " + cn.getClassName());
						
						return cn.getPackageName();
					}
				}
			}
		}
		
		return "";
	}
	
	public static List<ResolveInfo> searchHomeApps(Context context, boolean sort){
		List<ResolveInfo> homeApps;
		PackageManager manager = context.getPackageManager();
		
		/* ACTION_MAIN + CATEGORY_HOME + CATEGORY_DEFAULT = Home app */
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_HOME);
		mainIntent.addCategory(Intent.CATEGORY_DEFAULT);
		homeApps = manager.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY); 
		
        if (sort) {
		    Collections.sort(homeApps, new ResolveInfo.DisplayNameComparator(manager));
        }
		
		return homeApps;
	}

	public static List<ResolveInfo> searchHomeApps(Context context) {
        return searchHomeApps(context, true);
    }

	public static List<ResolveInfo> enumAllApps(Context context, boolean sort){
		List<ResolveInfo> apps;
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		apps = manager.queryIntentActivities(mainIntent, 0); 
		
        if (sort) {
		    Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        }
		
		return apps;
	}
	
	public static List<android.content.pm.ApplicationInfo> enumInstalledApps(Context context){
		PackageManager pm = context.getPackageManager();
        List<android.content.pm.ApplicationInfo> installedAppList = pm.getInstalledApplications(
                PackageManager.GET_UNINSTALLED_PACKAGES);
        if (installedAppList == null) {
            return new ArrayList<android.content.pm.ApplicationInfo> ();
        } 
		
        /*if (sort) {
		    Collections.sort(installedAppList, new ResolveInfo.DisplayNameComparator(pm));
        }*/
		
		return installedAppList;
	}
	
	public static List<ResolveInfo> enumAllApps(Context context) {
        return enumAllApps(context, true);
    }

    public static int getAppVersionCode(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();  
        int versionCode = 0;
        try {  
            PackageInfo info = pm.getPackageInfo(pkgName, 0);  
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        return versionCode;
    }

    public static String getAppVersionName(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();  
        String versionCode = "";
        try {  
            PackageInfo info = pm.getPackageInfo(pkgName, 0);  
            versionCode = info.versionName;
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        return versionCode;
    }
    
    /* Get version code */
    public static int getVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /* Get version name */
    public static String getVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }
    
    @SuppressWarnings("deprecation")
	public static void killProcess(Context context, String packageName) {
        ActivityManager am = (ActivityManager)context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        // In Android 2.2 or higher platform, restartPackage just a wrapper of ActivityManager.killBackgroundProcesse
    	am.restartPackage(packageName);
    }
}
