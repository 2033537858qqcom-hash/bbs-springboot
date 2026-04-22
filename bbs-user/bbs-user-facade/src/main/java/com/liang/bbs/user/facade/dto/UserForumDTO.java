package com.liang.bbs.user.facade.dto;

import com.liang.manage.auth.facade.dto.user.UserListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserForumDTO extends UserListDTO implements Serializable {
    /**
     * 鑾峰緱鐨勭偣璧炴暟
     */
    private Long likeCount;

    /**
     * 鑾峰緱鐨勯槄璇婚噺
     */
    private Long readCount;

    /**
     * 绉垎
     */
    private Integer points;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    /**
     * 鏄惁鍏虫敞
     */
    private Boolean isFollow;

    private static final long serialVersionUID = 1L;
}
