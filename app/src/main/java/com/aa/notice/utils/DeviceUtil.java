package com.aa.notice.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.aa.notice.CustomApplcation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.SENSOR_SERVICE;

public class DeviceUtil {

	public static String getDeviceId(){
		// phone imei
        String deviceId = "";
        try {
            TelephonyManager telephonyManager =
				(TelephonyManager) CustomApplcation.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(deviceId)){
                deviceId = Settings.Secure.getString(CustomApplcation.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            if (TextUtils.isEmpty(deviceId)){
                deviceId = Build.SERIAL;
            }

        } catch (SecurityException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
		return deviceId;
	}

    public static String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID();
        }

        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    @SuppressLint("MissingPermission")
    private static String getUUID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

	public static String getDeviceName(){
	    return Build.MODEL;
    }
	public static String getWifiIp(){
            URL infoUrl = null;
            InputStream inStream = null;
            try {
                // http://iframe.ip138.com/ic.asp
                // infoUrl = new URL("http://city.ip138.com/city0.asp");
                infoUrl = new URL("http://ip38.com");
                URLConnection connection = infoUrl.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inStream, "utf-8"));
                    StringBuilder strber = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                        strber.append(line + "\n");
                    inStream.close();
                    // 从反馈的结果中提取出IP地址
                    // int start = strber.indexOf("[");
                    // Log.d("zph", "" + start);
                    // int end = strber.indexOf("]", start + 1);
                    // Log.d("zph", "" + end);
                    line = strber.substring(378, 395);
                    line.replaceAll(" ", "");
                    return line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }
	public static String getImei2(){
	    String mimei="";
        TelephonyManager telephonyManager = (TelephonyManager) CustomApplcation.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = telephonyManager.getClass();
        try {
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
            mimei = (String) getImei.invoke(telephonyManager, 1);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return mimei;
    }

    public static boolean isRoot(){
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        if (new File(xBinPath).exists() && isExecutable(xBinPath))
            return true;
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     * @return true 为模拟器
     */
    public static Boolean isVirtual(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        if (null == sensor) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDeviceLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        String language = locale.getLanguage() + "-" + locale.getCountry();
        return language;
    }
}


