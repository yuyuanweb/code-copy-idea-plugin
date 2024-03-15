package com.xhl.codecopyplugin.service;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.xhl.codecopyplugin.ui.LoginDialog;

import java.net.MalformedURLException;

/**
 * 让用户扫码登录
 * @author daiyifei
 */
public class UserLogin {

    public void userLogin(Project project){
        GetLoginUrl getLoginUrl = new GetLoginUrl();
        String qrCodeImageUrl = getLoginUrl.getLoginQrCodeUrl();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.showLoginDialog(qrCodeImageUrl);
        // 轮询用户是否扫码
        String scene = getLoginUrl.getLoginScene();
        LoginPollingService loginPollingService = new LoginPollingService(scene, project, loginDialog);
        loginPollingService.startPolling();

    }
}
