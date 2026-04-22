package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.*;
import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.bbs.rest.client.ArticleArticleClient;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.utils.FileLengthUtils;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.nansheng.common.auth.RoleSsoDTO;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.enums.RoleGradeEnum;
import com.liang.nansheng.common.utils.CommonUtils;
import com.liang.nansheng.common.web.basic.ResponseResult;
import com.liang.nansheng.common.web.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/article/")
@Tag(name = "API")
public class ArticleController {
    @Autowired
    private ArticleArticleClient articleArticleClient;

    @Autowired
    private FileLengthUtils fileLengthUtils;

    @NoNeedLogin
    @GetMapping("getList")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ArticleDTO>> getList(ArticleSearchDTO articleSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleListRequest request = new InternalArticleListRequest();
        request.setArticleSearchDTO(articleSearchDTO);
        request.setCurrentUser(currentUser);
        request.setArticleStateEnum(ArticleStateEnum.enable);
        return ResponseResult.success(articleArticleClient.getList(request));
    }

    @NoNeedLogin
    @GetMapping("getPersonalArticles")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ArticleDTO>> getPersonalArticles(ArticleSearchDTO articleSearchDTO, @RequestParam(required = false) ArticleStateEnum articleStateEnum) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        if (currentUser != null && currentUser.getUserId().equals(articleSearchDTO.getCreateUser())) {
            articleStateEnum= null;
        }
        InternalArticleListRequest request = new InternalArticleListRequest();
        request.setArticleSearchDTO(articleSearchDTO);
        request.setCurrentUser(currentUser);
        request.setArticleStateEnum(articleStateEnum);
        return ResponseResult.success(articleArticleClient.getList(request));
    }

    @GetMapping("getPendingReviewArticles")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ArticleDTO>> getPendingReviewArticles(ArticleSearchDTO articleSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleListRequest request = new InternalArticleListRequest();
        request.setArticleSearchDTO(articleSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.getPendingReviewArticles(request));
    }

    @GetMapping("getDisabledArticles")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ArticleDTO>> getDisabledArticles(ArticleSearchDTO articleSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleListRequest request = new InternalArticleListRequest();
        request.setArticleSearchDTO(articleSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.getDisabledArticles(request));
    }

    @PostMapping("/updateState")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updateState(@RequestBody ArticleDTO articleDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleStateRequest request = new InternalArticleStateRequest();
        request.setArticleDTO(articleDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.updateState(request));
    }

    @NoNeedLogin
    @GetMapping("getLikesArticle")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ArticleDTO>> getLikesArticle(LikeSearchDTO likeSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalLikeSearchRequest request = new InternalLikeSearchRequest();
        request.setLikeSearchDTO(likeSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.getLikesArticle(request));
    }

    @NoNeedLogin
    @GetMapping("getArticleCommentVisitTotal")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<TotalDTO> getArticleCommentVisitTotal() {
        return ResponseResult.success(articleArticleClient.getArticleCommentVisitTotal());
    }

    @NoNeedLogin
    @GetMapping("getById")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<ArticleDTO> getById(@RequestParam Integer id, @RequestParam(required = false) Boolean isPv) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleIdsRequest request = new InternalArticleIdsRequest();
        request.setIds(Collections.singletonList(id));
        request.setIsPv(isPv);
        request.setCurrentUser(currentUser);
        List<ArticleDTO> articleDTOS = articleArticleClient.getByIds(request);
        if (CollectionUtils.isNotEmpty(articleDTOS)) {
            ArticleDTO articleDTO = articleDTOS.get(0);
            // 閻ц缍?鏉╁洦鎶ら棃鐐衡偓姘崇箖鐎光剝鐗抽惃鍕瀮缁旂媴绱欏▽锟犫偓姘崇箖鐎光剝鐗抽惃鍕瀮缁旂姳绗夐崗浣筋啅閸掝偂姹夐弻銉ф箙-闂勩倓绨＄搾鍛吀閸滃本婀版禍鐚寸礆
            if (currentUser != null && !ArticleStateEnum.enable.getCode().equals(articleDTO.getState())) {
                // 瑜版挸澧犻悽銊﹀煕閹碘偓閺堝娈戠憴鎺曞缁涘楠?
                List<String> grades = currentUser.getRoles().stream().map(RoleSsoDTO::getGrade).distinct().collect(Collectors.toList());
                // 娑撳秵妲哥搾鍛獓缁狅紕鎮婇崨妯款潡閼圭瀽nd娑撳秵妲搁張顑挎眽
                if (!grades.contains(RoleGradeEnum.NS_SUPER_ADMIN_ROLE.name()) && !articleDTO.getCreateUser().equals(currentUser.getUserId())) {
                    return ResponseResult.build(ResponseCode.NOT_EXISTS, null);
                }
            }
            // 閺堫亞娅ヨぐ?鏉╁洦鎶ら棃鐐衡偓姘崇箖鐎光剝鐗抽惃鍕瀮缁旂媴绱檃ll閿?
            if (currentUser == null && !ArticleStateEnum.enable.getCode().equals(articleDTO.getState())) {
                return ResponseResult.build(ResponseCode.NOT_EXISTS, null);
            }
        } else {
            return ResponseResult.build(ResponseCode.NOT_EXISTS, null);
        }
        return ResponseResult.success(articleDTOS.get(0));
    }

    @PostMapping("/create")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> create(@RequestParam(value = "file", required = false) MultipartFile picture,
                                          ArticleDTO articleDTO, @RequestParam List<Integer> labelIds) throws IOException {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleCreateUpdateRequest request = new InternalArticleCreateUpdateRequest();
        request.setArticleDTO(articleDTO);
        request.setLabelIds(labelIds);
        request.setCurrentUser(currentUser);
        // 閺冪娀鍘ら崶?
        if (picture == null) {
            return ResponseResult.success(articleArticleClient.create(request));
        }

        if (fileLengthUtils.isFileNotTooBig(picture.getBytes())) {
            request.setBytes(picture.getBytes());
            request.setSourceFileName(picture.getOriginalFilename());
            return ResponseResult.success(articleArticleClient.create(request));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    @PostMapping("/update")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> update(@RequestParam(value = "file", required = false) MultipartFile picture,
                                          ArticleDTO articleDTO, @RequestParam List<Integer> labelIds) throws IOException {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleCreateUpdateRequest request = new InternalArticleCreateUpdateRequest();
        request.setArticleDTO(articleDTO);
        request.setLabelIds(labelIds);
        request.setCurrentUser(currentUser);
        // 閺冪娀鍘ら崶?
        if (picture == null) {
            return ResponseResult.success(articleArticleClient.update(request));
        }

        if (fileLengthUtils.isFileNotTooBig(picture.getBytes())) {
            request.setBytes(picture.getBytes());
            request.setSourceFileName(picture.getOriginalFilename());
            return ResponseResult.success(articleArticleClient.update(request));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    /**
     * 娑撳﹣绱堕崶鍓у閿涘牅绔村鐙呯礆- mavonEditor
     *
     * @param picture
     * @return
     */
    @PostMapping("/uploadPicture")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<String> uploadPicture(@RequestParam(value = "picture") MultipartFile picture) throws IOException {
        if (fileLengthUtils.isFileNotTooBig(picture.getBytes())) {
            InternalBinaryUploadRequest request = new InternalBinaryUploadRequest();
            request.setBytes(picture.getBytes());
            request.setSourceFileName(picture.getOriginalFilename());
            return ResponseResult.success(articleArticleClient.uploadPicture(request));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    @NoNeedLogin
    @GetMapping("getCountById")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<ArticleCountDTO> getCountById(@RequestParam Integer id) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleCountRequest request = new InternalArticleCountRequest();
        request.setId(id);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.getCountById(request));
    }

    @GetMapping("articleTop")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> articleTop(@RequestParam Integer id, @RequestParam Boolean top) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleTopRequest request = new InternalArticleTopRequest();
        request.setId(id);
        request.setTop(top);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.articleTop(request));
    }

    @PostMapping("delete/{id}")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> delete(@PathVariable Integer id) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalArticleDeleteRequest request = new InternalArticleDeleteRequest();
        request.setId(id);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleArticleClient.delete(request));
    }

    @GetMapping("getArticleCheckCount")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<ArticleCheckCountDTO> getArticleCheckCount(@RequestParam(required = false) String title) {
        return ResponseResult.success(articleArticleClient.getArticleCheckCount(title));
    }

}


