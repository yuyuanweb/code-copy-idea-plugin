package com.xhl.codecopyplugin.constant;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * @author daiyifei
 */
public class LanguageMapper {
    private static HashMap<String, String> map;

    static {
        String url = "https://www.codecopy.cn/api/post/language/mapper";

        // 发送 POST 请求
        String s = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseObj(s);

        map = jsonObject.getJSONObject("data").toBean(HashMap.class);
        // 输出响应结果
        System.out.println(map);
    }


    public static String getLanguage(String key) {
        return map.getOrDefault(key, "plain_text");
    }



}
