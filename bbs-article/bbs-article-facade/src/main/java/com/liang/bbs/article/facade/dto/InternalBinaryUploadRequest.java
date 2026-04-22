package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalBinaryUploadRequest implements Serializable {

    private byte[] bytes;

    private String sourceFileName;

    private static final long serialVersionUID = 1L;
}
