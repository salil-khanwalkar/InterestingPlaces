/**
 * 
 */
package atos.net.interestingplaces.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetworkUtil {

	/**
	 * Get the network info
	 * 
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * Check if there is any connectivity
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(final Context context) {
		NetworkInfo info = NetworkUtil.getNetworkInfo(context);
		if ((null != info)
				&& ((info.getType() == ConnectivityManager.TYPE_WIFI) || (info.getType() == ConnectivityManager.TYPE_MOBILE))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if there is any connectivity to a Wifi network
	 * 
	 * @param context
	 * @param type
	 * @return
	 */
	public static boolean isConnectedWifi(final Context context) {
		NetworkInfo info = NetworkUtil.getNetworkInfo(context);
		return ((info != null) && info.isConnected() && (info.getType() == ConnectivityManager.TYPE_WIFI));
	}

	/**
	 * Check if there is any connectivity to a mobile network
	 * 
	 * @param context
	 * @param type
	 * @return
	 */
	public static boolean isConnectedMobile(final Context context) {
		NetworkInfo info = NetworkUtil.getNetworkInfo(context);
		return ((info != null) && info.isConnected() && (info.getType() == ConnectivityManager.TYPE_MOBILE));
	}

	/**
	 * Check if there is fast connectivity
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectedFast(final Context context) {
		NetworkInfo info = NetworkUtil.getNetworkInfo(context);
		return ((info != null) && info.isConnected() && NetworkUtil.isConnectionFast(info.getType(), info.getSubtype()));
	}

	/**
	 * Check if the connection is fast
	 * 
	 * @param type
	 * @param subType
	 * @return
	 */
	public static String getConnectionType(final int type, final int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return "WiFi";
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return "1xRTT"; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return "CDMA"; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return "EDGE"; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return "EVDO 0"; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return "EVDO A"; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return "GPRS"; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return "HSDPA"; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return "HSPA"; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return "HSUPA"; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return "UMTS"; // ~ 400-7000 kbps
					/*
					 * Above API level 7, make sure to set
					 * android:targetSdkVersion
					 * to appropriate level to use these
					 */
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					return "EPHRD"; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					return "EVDO B"; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					return "HSPAP"; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					return "IDEN"; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					return "LTE"; // ~ 10+ Mbps
					// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					return "UNKNOWN";
			}
		}
		return "NETWORK TYPE UNKNOWN";
	}

	/**
	 * Check if the connection is fast
	 * 
	 * @param type
	 * @param subType
	 * @return
	 */
	public static boolean isConnectionFast(final int type, final int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return true; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return true; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return false; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return true; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return true; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return true; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return true; // ~ 400-7000 kbps
					/*
					 * Above API level 7, make sure to set
					 * android:targetSdkVersion
					 * to appropriate level to use these
					 */
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					return true; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					return true; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					return true; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					return false; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					return true; // ~ 10+ Mbps
					// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					return false;
			}
		} else {
			return false;
		}
	}
}
