package com.xhl.codecopyplugin.action;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.xhl.codecopyplugin.constant.LanguageMapper;
import com.xhl.codecopyplugin.service.GetLoginUrl;
import com.xhl.codecopyplugin.service.GetShareLink;
import com.xhl.codecopyplugin.service.LoginPollingService;
import com.xhl.codecopyplugin.ui.LoginDialog;

import java.awt.datatransfer.StringSelection;
import java.net.MalformedURLException;

public class MyCustomAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();


        // 登录逻辑
        // 获取登录二维码、scene
        if (true) {
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
            LoginPollingService loginPollingService = new LoginPollingService(scene, project);
            loginPollingService.startPolling();

            System.out.println("登录逻辑结束");
//            loginDialog.closeDialog();
            // 登录成功后获取登录用户的cookie
//            System.out.println(savedCookie);
            return; // 登录窗口显示后返回，不继续执行后面的代码
        }


        // 获取用户选中的代码
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String selectedText = null;
        // 如果没有选中就不显示插件
//        update(e);
        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            selectedText = selectionModel.getSelectedText();
            // 现在，selectedText中包含了用户选中的代码
        }

        if (selectedText == null) {
            // 通知用户没有选中代码
            Messages.showMessageDialog("没有选中代码", "提示", Messages.getInformationIcon());
            return;
        }

        // 获取当前文件的后缀
        String extension = getExtension(e);

        // 将后缀转成对应语言需要传入的参数
        String language = LanguageMapper.getLanguage(extension);

        // 调用系统接口，获取分享链接
        GetShareLink getShareLink = new GetShareLink();

        String result = getShareLink.callCodeCopySystemApi(selectedText, language);

        // 将链接存储到剪贴板
        CopyPasteManager.getInstance().setContents(new StringSelection(result));

        // 给用户一个弹窗提示
//        Messages.showMessageDialog(result, "分享链接(已复制)", Messages.getInformationIcon());
        // 给用户一个非模态的通知

        // 发布通知，它会显示在IDE的右下角
        Notification notification = NOTIFICATION_GROUP.createNotification("分享链接（已复制）", result, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);


    }


    /**
     * 更新动作
     *
     * @param e
     */
//
//    @Override
//    public void update(AnActionEvent e) {
//        Editor editor = e.getData(PlatformDataKeys.EDITOR);
//        boolean isVisible = hasSelectedText(editor);
//        e.getPresentation().setEnabledAndVisible(isVisible);
//    }
//
//    private boolean hasSelectedText(Editor editor) {
//        if (editor == null) {
//            return false;
//        }
//        SelectionModel selectionModel = editor.getSelectionModel();
//        return selectionModel.hasSelection();
//    }


    /**
     * 获取当前文件的后缀
     *
     * @param e
     * @return
     */

    public String getExtension(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformCoreDataKeys.VIRTUAL_FILE);
        String fileExtension = vFile != null ? vFile.getExtension() : null;
        return fileExtension;
    }

    /**
     * 创建一个通知组（右下角）
     */

    private static final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup("Custom Notification Group",
                    NotificationDisplayType.BALLOON, true);


}
