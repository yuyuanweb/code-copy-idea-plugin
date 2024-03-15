package com.xhl.codecopyplugin.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author daiyifei
 */
@NoArgsConstructor
@Data
public class CodeSegmentsDTO implements Serializable {
    private List<CodesDTO> codes;

    @NoArgsConstructor
    @Data
    public static class CodesDTO {
        private String code;
        private String language;
        private String name;
    }


    private static final long serialVersionUID = 1L;
}