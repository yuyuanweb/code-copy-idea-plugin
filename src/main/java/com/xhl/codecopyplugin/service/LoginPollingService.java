package com.xhl.codecopyplugin.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.xhl.codecopyplugin.constant.RequestConstant;
import com.xhl.codecopyplugin.ui.LoginDialog;
import com.xhl.codecopyplugin.util.StorageCookie;
import lombok.Data;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 轮询登录接口
 *
 * @author daiyifei
 */
@Data
public class LoginPollingService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String scene;

    private Project project;

    private LoginDialog loginDialog;

    private volatile boolean isPolling = false;

    public LoginPollingService(String scene, Project project, LoginDialog loginDialog) {
        this.scene = scene;
        this.project = project;
        this.loginDialog = loginDialog;
    }

    public void startPolling() {
        isPolling = true;
        final Runnable poller = () -> {
            if (!isPolling) {
                return;
            }
            System.out.println("轮询----------------");
            try {
                // 检查登录状态
                boolean loginSuccess = checkLoginStatus(scene);
                if (loginSuccess) {
                    // 登录成功，更新UI
                    onLoginSuccess();
                    // 停止轮询
                    stopPolling();
                }
            } catch (Exception e) {
                // 处理异常，可能需要根据情况重新开始轮询或停止轮询
                stopPolling();
            }
        };
        // 每2秒执行一次
        scheduler.scheduleAtFixedRate(poller, 0, 2, TimeUnit.SECONDS);

    }

    public void stopPolling() {
        isPolling = false;
        scheduler.shutdown();
    }

    /**
     * @param scene 检查用户的登录状态
     * @return
     */
    private boolean checkLoginStatus(String scene) {

        System.out.println("Checking---------------");

        // 实现检查登录状态的逻辑，返回登录是否成功
        String url = RequestConstant.HOST + "user/login/wx_mp";
        String requestJson = JSONUtil.createObj().set("scene", scene)
                .toString();
        try {
            HttpResponse response = HttpRequest.post(url)
                    .header("accept", "application/json")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("content-type", "application/json")
                    .header("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"macOS\"")
                    .header("sec-fetch-dest", "empty")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-site", "same-origin")
//                    .header("cookie",)
                    .header("Referer", "https://www.codecopy.cn/user/login?redirect=https%3A%2F%2Fwww.codecopy.cn%2Fsearch")
                    .header("Referrer-Policy", "strict-origin-when-cross-origin")
                    .body(requestJson)
                    .execute();


            if (response.isOk()) {
                List<HttpCookie> cookies = response.getCookies();
                System.out.println("cookies:" + cookies.get(0).getValue());
                // 取出后端返回的 cookie
                String cookie = cookies.get(0).getValue();

                // 解析响应的 JSON
                JSONObject jsonResponse = JSONUtil.parseObj(response.body());
                // 检查data字段是否非 null
                Object dataField = jsonResponse.get("data");
                System.out.println("dataField:" + dataField);
                if (!ObjectUtil.isNull(dataField)) {
                    // 登录成功，保存cookie
                    if (cookie != null) {
                        // 保存 cookie 以供后续使用
                        StorageCookie storageCookie = new StorageCookie(project);
                        storageCookie.saveCookie(cookie);

                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    /**
     * 登录成功时的操作
     */
    private void onLoginSuccess() {
        // 登录成功时的操作
        System.out.println("成功登录");
        loginDialog.closeDialog();
        Notification notification = NOTIFICATION_GROUP.createNotification("成功登录，快一键分享吧！", NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }


    private static final NotificationGroup NOTIFICATION_GROUP =
            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group");
}
