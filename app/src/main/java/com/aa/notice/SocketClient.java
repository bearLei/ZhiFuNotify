package com.aa.notice;

import android.content.Context;
import android.text.TextUtils;

import com.aa.notice.utils.PayHelperUtils;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import static com.aa.notice.MainActivity.sendmsg;


public class SocketClient extends WebSocketClient {
    SocketListener socketListener;
    static Vector<String> payListOrder = new Vector<String>();

    private int retryCount;
    public void setSocketListener(SocketListener socketListener) {
        this.socketListener = socketListener;
    }

    public static boolean isValidLong(String str) {
        try {
//            String a="132.456";
            float b = Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException {
//        WebSocketClient client = new SocketClient(new URI("ws://58.82.250.200:9092/"));
//        client.connect();

//        String str2 = "可用余额 1元";
//        float d = getDoubleValue(str2);
//        System.out.println(">>"+d);
        String test = "123456";
        System.out.println(test.substring(5, 6));

    }

    Context context;

    public SocketClient(Context context, URI serverUri) {
        super(serverUri);
        this.context = context;
        System.out.println("初始化通道" + CustomApplcation.getSocketUrl());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("打开通道");
        if (socketListener != null) {
            socketListener.onOpen();
        }
    }

    public static long nowTime = 0;

    //    @Override
//    public void send(String message) {
//       AES A=NEW
//        super.send(A);
//    }
    @Override
    public void onMessage(String message) {

        try {
            System.out.println("接受消息" + JsonHelper.isJson(message) + "   " + message);
            //解析指令，然后请求http
            if (JsonHelper.isJson(message)) {
                //解析type
                JSONObject jsonObj = new JSONObject(message);
                JSONObject dataObject = jsonObj.optJSONObject("data");
                int code = jsonObj.optInt("code");
                String mstype = jsonObj.optString("mstype");
                String msg = jsonObj.optString("msg");
                if (code == 1000) {
                    retryCount = 0;
                    if (!TextUtils.isEmpty(mstype) && mstype.equals("login") && socketListener != null) {
                            socketListener.onMessageIp(CustomApplcation.socketLoginIp);
                            socketListener.onLoginSuccess();
                    }else if (!TextUtils.isEmpty(mstype) && mstype.equals("notify") && socketListener != null){
                        socketListener.onNotify(message);
                    }
                }else {
                    retryCount++;
                    if (retryCount > 20 && socketListener != null){
                        socketListener.closed();
                        return;
                    }
                    if (mstype.equals("notify")){
                        if (socketListener != null){
                            socketListener.onNotify(message);
                            return;
                        }
                    }
                    if (socketListener != null){
                        socketListener.onError(msg);
                    }
                }
                //回调
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (socketListener != null) {
            socketListener.closed();
        }
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
        System.out.println("picher_log" + "通道关闭");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        if (socketListener != null) {
            socketListener.onError(ex.getMessage());
        }

        System.out.println("连接错误");
        PayHelperUtils.sendmsg(context, ex.getMessage() + " \n " + CustomApplcation.getSocketUrl());

    }


    /**
     * 解析字符串获得双精度型数值，
     *
     * @param str
     * @return
     */
    public static float getDoubleValue(String str) {
        float d = 0;

        if (str != null && str.length() != 0) {
            StringBuffer bf = new StringBuffer();

            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (c >= '0' && c <= '9') {
                    bf.append(c);
                } else if (c == '.') {
                    if (bf.length() == 0) {
                        continue;
                    } else if (bf.indexOf(".") != -1) {
                        break;
                    } else {
                        bf.append(c);
                    }
                } else {
                    if (bf.length() != 0) {
                        break;
                    }
                }
            }
            try {
                d = Float.parseFloat(bf.toString());
            } catch (Exception e) {
            }
        }

        return d;
    }


}
