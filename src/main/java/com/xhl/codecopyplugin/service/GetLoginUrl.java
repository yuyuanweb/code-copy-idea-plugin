package com.xhl.codecopyplugin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;


/**
 * 获取二维码、场景值
 *
 * @author daiyifei
 */
@Data
public class GetLoginUrl {

    private HttpResponse httpResponse = null;

    /**
     * 获取登录二维码
     *
     * @return
     */
    public String getLoginQrCodeUrl() {
        // 构造请求URL
        String url = "https://www.codecopy.cn/api/user/login/wx_mp/get_scene";
        // 构造请求
        HttpResponse response = HttpRequest.post(url)
                .header("accept", "*/*")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("Referer", "https://www.codecopy.cn/user/login?redirect=https%3A%2F%2Fwww.codecopy.cn%2Fsearch")
                .header("Referrer-Policy", "strict-origin-when-cross-origin")
                .execute();
        if (response.isOk()) {
            // 打印响应的内容
            httpResponse = response;
            System.out.println(response.body());
        } else {
            System.out.println("请求失败，状态码：" + response.getStatus());
        }
        String result = response.body();
        // JSON 解析
        // 取出 JSON 中 data 的值
        JSONObject jsonObject = JSONUtil.parseObj(result);
//        String data = jsonObject.get("data").toString();
//        JSONObject dataJson = JSONUtil.parseObj(data);
//        String qrCode = dataJson.get("qrCode").toString();
        // 从JSON对象中获取qrCode的值
        String qrCode = jsonObject.getByPath("data.qrCode", String.class);

        return qrCode;
    }

    /**
     * 获取场景值
     *
     * @return
     */

    public String getLoginScene() {

        String result = httpResponse.body();
        // JSON 解析
        // 取出 JSON 中 data 的值
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String scene = jsonObject.getByPath("data.scene", String.class);
        return scene;
    }

}
