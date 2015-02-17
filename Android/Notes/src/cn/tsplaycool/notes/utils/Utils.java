package cn.tsplaycool.notes.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 一些Android笔记
 * 
 * @author Tans
 */
public class Utils {

	/**
	 * 判断系统中某个service是否存在
	 * 
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager mgr = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> serviceList = (ArrayList<RunningServiceInfo>) mgr
				.getRunningServices(100);
		for (int i = 0; i < serviceList.size(); i++) {
			// "cn.kuwo.service.remote.uninstall.UninstallMonitorService"
			if (serviceName.equals(serviceList.get(i).service.getClassName()
					.toString())) {
				Log.d("tanshuai", "JAVA Service 正在Running! ");
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
		}
		return "0.0.0.0";
	}

	/**
	 * 获取手机号码运营商
	 * 
	 * @param context
	 * @return
	 */
	public static SimOperator getSimOperator(Context context) {
		SimOperator simOperator;
		String subscriberId = getSubscriberId(context);
		if (TextUtils.isEmpty(subscriberId)) {
			simOperator = SimOperator.UNKNOW;
		} else {
			// IMSI号前面3位460是国家，紧接着后面2位00 00,02,07是中国移动，<br>
			// 01,06是中国联通，03, 05是中国电信,20铁通
			if (subscriberId.startsWith("46001")
					|| subscriberId.startsWith("46006")) {
				simOperator = SimOperator.UNICOM;
			} else if (subscriberId.startsWith("46000")
					|| subscriberId.startsWith("46002")
					|| subscriberId.startsWith("46007")) {
				simOperator = SimOperator.MOBILE;
			} else if (subscriberId.startsWith("46003")
					|| subscriberId.startsWith("46005")) {
				simOperator = SimOperator.TELECOM;
			} else if (subscriberId.startsWith("46020")) {
				simOperator = SimOperator.TIETONG;
			} else {
				simOperator = SimOperator.OTHER;
			}
		}
		return simOperator;
	}

	// 不同手机号枚举
	public enum SimOperator {
		// 联通，移动，电线，铁通，其他，未知
		UNICOM, MOBILE, TELECOM, TIETONG, OTHER, UNKNOW
	}

	// 获取sim卡的subscriber id
	private static String getSubscriberId(Context context) {
		String subscriberId = null;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			subscriberId = telephonyManager.getSubscriberId();
		} catch (Exception e) {
		}
		return subscriberId;
	}

	/**
	 * 判断APP是否当日首次启动
	 * 
	 * @param context
	 * @return
	 */
	public boolean isTodayFstLaunch(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Notes",
				Context.MODE_PRIVATE);
		String savedDate = sp.getString("key_today_date", "00000000");
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd",
				Locale.SIMPLIFIED_CHINESE);
		String currentDate = formate.format(date);
		if (!currentDate.equals(savedDate)) {
			Editor editor = sp.edit();
			editor.putString("key_today_data", currentDate);
			editor.commit();
			return true;
		} else {
			return false;
		}
	}

}
