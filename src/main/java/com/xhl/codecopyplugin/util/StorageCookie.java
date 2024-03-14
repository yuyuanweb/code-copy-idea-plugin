package com.xhl.codecopyplugin.util;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @author daiyifei
 */
@Data
public class StorageCookie {

    private final Project project; // 每个StorageCookie实例关联一个特定的Project

    // 在构造函数中传入Project实例
    public StorageCookie(Project project) {
        this.project = project;
    }

    // 保存cookie到这个Project的存储中
    public void saveCookie(String cookie) {
        PropertiesComponent.getInstance(project).setValue("LOGIN_COOKIE", cookie);
    }

    // 从这个Project的存储中获取cookie
    public String getSavedCookie() {
        return PropertiesComponent.getInstance(project).getValue("LOGIN_COOKIE", "");
    }

    public void removeSavedCookie() {
        PropertiesComponent.getInstance(project).setValue("LOGIN_COOKIE", null);
    }

}
