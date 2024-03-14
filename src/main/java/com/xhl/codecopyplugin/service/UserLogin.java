package com.xhl.codecopyplugin.service;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.xhl.codecopyplugin.ui.LoginDialog;

import java.net.MalformedURLException;

/**
 * 让用户扫码登录
 */
public class UserLogin {

    public void userLogin(Project project){
        GetLoginUrl getLoginUrl = new GetLoginUrl();
        String qrCodeImageUrl = getLoginUrl.getLoginQrCodeUrl();
        LoginDialog loginDialog = new LoginDialog();
        try {
            loginDialog.showLoginDialog(qrCodeImageUrl);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        // 轮询用户是否扫码
        String scene = getLoginUrl.getLoginScene();
        System.out.println(scene);
        LoginPollingService loginPollingService = new LoginPollingService(scene, project, loginDialog);
        loginPollingService.startPolling();

    }

}
