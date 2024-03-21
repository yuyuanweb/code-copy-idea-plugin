package com.xhl.codecopyplugin.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * @author daiyifei
 * 登录 UI
 */
public class LoginDialog {

    private final JFrame frame;

    public LoginDialog() {
        frame = new JFrame("代码小抄登录");
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        // 确保关闭窗口时释放资源
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 窗口居中
        frame.setLocationRelativeTo(null);
    }

    public void showLoginDialog(String qrCodeImageUrl) {
        // 使用SwingWorker在后台线程中加载图片
        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                // 在后台线程中从URL加载图片
                try {
                    URL url = new URL(qrCodeImageUrl);
                    return ImageIO.read(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                // 加载完成后在EDT中更新UI
                try {
                    BufferedImage image = get();
                    if (image != null) {
                        // 创建包含文本和图片的面板
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                        panel.add(Box.createVerticalStrut(20)); // 调整这个值以改变间隙大小

                        // 添加文本标签到面板
                        JLabel textLabel = new JLabel("请扫描二维码登录您的代码小抄账号");
                        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 文本居中
                        panel.add(textLabel);


                        // 添加间隙
                        panel.add(Box.createVerticalStrut(30)); // 调整这个值以改变间隙大小

                        // 添加图片标签到面板
                        ImageIcon icon = new ImageIcon(image);
                        JLabel label = new JLabel(icon);
                        label.setAlignmentX(Component.CENTER_ALIGNMENT); // 图片居中
                        panel.add(label);

                        // 将面板添加到frame中，并确保它在中心位置显示
                        frame.add(panel, BorderLayout.CENTER);

                        frame.validate(); // 更新frame的布局
                        frame.repaint(); // 重新绘制frame
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    // 处理加载图片时的错误
                }
            }
        }.execute();

        // 显示对话框
        frame.setVisible(true);
    }

    public void closeDialog() {
        frame.dispose(); // 关闭对话框
    }

}
