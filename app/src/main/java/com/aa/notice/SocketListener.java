package com.aa.notice;

public interface SocketListener {

    public void onOpen();

    public void closed();

    public void onError(String s);

    public void tostToMain(String message);

    public void onMessageIp(String msg);//ip信息

    public void onMessageIpError(String msg);//ip错误，弹框提示

    void onLoginSuccess();

    void onHeat();

    void onNotifyError(String s);

    void onNotify(String s);
}
