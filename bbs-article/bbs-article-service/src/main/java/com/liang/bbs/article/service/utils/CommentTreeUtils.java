package com.liang.bbs.article.service.utils;

import com.liang.bbs.article.facade.dto.CommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鐢ㄤ簬鏍戣浆闆嗗悎锛岄泦鍚堣浆鏍戝満鏅?
 *
 */
@Slf4j
public class CommentTreeUtils {
    /**
     * 闆嗗悎杞爲
     *
     * @param commentDTOS
     * @return
     */
    public static List<CommentDTO> toTree(List<CommentDTO> commentDTOS) {
        Map<Integer, CommentDTO> commentDTOMap = commentDTOS.stream().collect(Collectors.toMap(CommentDTO::getId, e -> e));
        List<CommentDTO> root = new ArrayList<>();
        for (CommentDTO dto : commentDTOS) {
            Integer preId = dto.getPreId();
            // 鏄牴璇勮
            if (preId == 0) {
                // 璁剧疆璇勮娣卞害
                dto.setDepth(0);
                root.add(dto);
            } else {
                CommentDTO parent = commentDTOMap.get(preId);
                // 璺宠繃瀛愮骇鏃犵埗绾х殑璇勮
                if (parent == null) {
                    continue;
                }
                List<CommentDTO> children = CollectionUtils.isEmpty(parent.getChild()) ? new ArrayList<>() : parent.getChild();
                // 璁剧疆璇勮娣卞害
                dto.setDepth(parent.getDepth() + 1);
                children.add(dto);
                parent.setChild(children);
            }
        }
        return root;
    }


}
