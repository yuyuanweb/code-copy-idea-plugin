package com.xhl.codecopyplugin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.xhl.codecopyplugin.pojo.dto.CodeSegmentsDTO;
import com.xhl.codecopyplugin.pojo.dto.PostAddRequest;
import com.xhl.codecopyplugin.util.StorageCookie;

import java.util.Collections;
import java.util.Objects;

/**
 * 请求接口获取到分享链接
 *
 * @author daiyifei
 */
public class GetShareLink {
    public String callCodeCopySystemApi(String selectedText, String language, Project project) {
        // 实现调用系统接口的逻辑，返回获取的链接
        // 可以使用 Java 的网络请求库
        // 接口地址
        String url = "https://www.codecopy.cn/api/post/add";

        // 封装成对象去请求
        PostAddRequest postAddRequest = new PostAddRequest();
        postAddRequest.setTheme("vs-dark");
        CodeSegmentsDTO codeSegmentsDTO = new CodeSegmentsDTO();
        CodeSegmentsDTO.CodesDTO codesDTO = new CodeSegmentsDTO.CodesDTO();
        codesDTO.setCode(selectedText);
        codesDTO.setLanguage(language);
        codesDTO.setName("");
        codeSegmentsDTO.setCodes(Collections.singletonList(codesDTO));

        postAddRequest.setCodeSegments(codeSegmentsDTO);
        postAddRequest.setShareType(0);
        postAddRequest.setCodeLanguage(language);

        String body = JSONUtil.toJsonStr(postAddRequest);
        System.out.println("请求体" + body);

        StorageCookie cookie = new StorageCookie(project);
        String savedCookie = cookie.getSavedCookie();
        System.out.println("保存的cookie：" + savedCookie);
        String sendCookie = "SESSION=" + savedCookie;

        // 发送 POST 请求 带上 cookie 识别用户
        HttpResponse response = HttpRequest.post(url)
                .header("accept", "application/json")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("content-type", "application/json")
                .header("sec-ch-ua", "\"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("cookie", sendCookie)
                .body(body)
                .execute();


        // 每次发布前调用一下获取登录态的接口，判断是否要重新登录
        boolean b = checkIfUserNeedsToLogin(sendCookie);
        System.out.println(b);
        if (b) {
            System.out.println("Cookie 已过期，请重新登录。");
            UserLogin user = new UserLogin();
            user.userLogin(project);
            return null;
        }

        String result = response.body();

        // JSON 解析
        // 取出 JSON 中 data 的值
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String link = jsonObject.get("data").toString();
        System.out.println(link);

        return "https://codecopy.cn/post/" + link;

    }


    /**
     * 判断 cookie 是否过期
     * @param cookie
     * @return
     */

    public boolean checkIfUserNeedsToLogin(String cookie) {
        String url = "https://www.codecopy.cn/api/user/get/login";
        HttpResponse response = HttpRequest.get(url)
                .header("accept", "*/*")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("cookie", cookie)
                .header("Referer", "https://www.codecopy.cn/post/58igr3")
                .header("Referrer-Policy", "strict-origin-when-cross-origin")
                .execute();

        if (response.isOk()) {
            // 解析响应体来判断登录状态，逻辑取决于API响应的结构
            JSONObject jsonResponse = JSONUtil.parseObj(response.body());
            System.out.println(jsonResponse.get("code").toString());

            if (Objects.equals(jsonResponse.get("code").toString(), "40100")) {
                return true;
            }
            return false;

        } else {
            // 请求失败，可能也需要重新登录或者检查网络连接
            return true;
        }
    }
}
