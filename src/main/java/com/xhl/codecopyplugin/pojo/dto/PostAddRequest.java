package com.xhl.codecopyplugin.pojo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author daiyifei
 */
@NoArgsConstructor
@Data
public class PostAddRequest implements Serializable {

    private String theme;
    private CodeSegmentsDTO codeSegments;
    private Integer shareType;
    private String codeLanguage;


    @Serial
    private static final long serialVersionUID = 1L;


}
