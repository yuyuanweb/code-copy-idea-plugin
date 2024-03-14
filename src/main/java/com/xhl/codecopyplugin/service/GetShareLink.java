package com.xhl.codecopyplugin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xhl.codecopyplugin.pojo.dto.CodeSegmentsDTO;
import com.xhl.codecopyplugin.pojo.dto.PostAddRequest;

import java.util.Collections;

/**
 * 请求接口获取到分享链接
 *
 * @author daiyifei
 */
public class GetShareLink {
    public String callCodeCopySystemApi(String selectedText, String language) {
        // 实现调用系统接口的逻辑，返回获取的链接
        // 可以使用 Java 的网络请求库
        // 接口地址
//        System.out.println("-------------"+selectedText);
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

        // 发送 POST 请求
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
                .body(body)
                .execute();

        String result = response.body();
        System.out.println("返回结果" + result);


        // JSON 解析
        // 取出 JSON 中 data 的值
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String link = jsonObject.get("data").toString();
        System.out.println(link);

        return "https://codecopy.cn/post/" + link;  // 替换为实际获取的链接

    }

    // 测试能否正常请求
    public static void main(String[] args) {
        String url = "https://www.codecopy.cn/api/post/add";
        String body = "{\"theme\":\"vs-dark\",\"codeSegments\":{\"codes\":[{\"code\":\"l\",\"language\":\"plain_text\",\"name\":\"\"}]},\"shareType\":0,\"codeLanguage\":\"plain_text\"}";

        // 发送 POST 请求
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
                .body(JSONUtil.toJsonStr(body))
                .execute();

        // 输出响应结果
        System.out.println(response.body());

    }
}
