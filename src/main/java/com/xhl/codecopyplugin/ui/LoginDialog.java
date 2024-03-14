package com.xhl.codecopyplugin.ui;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author daiyifei
 */
public class LoginDialog {


    private JFrame frame; // 成员变量

    public LoginDialog() {
        frame = new JFrame("代码小抄登录");
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
    }

    public void showLoginDialog(String qrCodeImageUrl) throws MalformedURLException {
        // 创建一个标签来展示二维码图片
        JLabel label = new JLabel(new ImageIcon(new URL(qrCodeImageUrl)));
        frame.add(label, BorderLayout.CENTER);

        // 显示对话框
        frame.setVisible(true);
    }

    public void closeDialog() {
        frame.dispose(); // 关闭对话框
    }



}
