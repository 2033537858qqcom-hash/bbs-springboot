package com.liang.bbs.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.*;
import com.liang.bbs.article.facade.server.ArticleLabelService;
import com.liang.bbs.article.facade.server.ArticleService;
import com.liang.bbs.article.facade.server.CommentService;
import com.liang.bbs.article.facade.server.LabelService;
import com.liang.bbs.article.persistence.entity.ArticlePo;
import com.liang.bbs.article.persistence.entity.ArticlePoExample;
import com.liang.bbs.article.persistence.mapper.ArticlePoExMapper;
import com.liang.bbs.article.persistence.mapper.ArticlePoMapper;
import com.liang.bbs.article.service.client.FileServiceClient;
import com.liang.bbs.article.service.client.UserFollowClient;
import com.liang.bbs.article.service.client.UserLikeClient;
import com.liang.bbs.article.service.client.UserServiceClient;
import com.liang.bbs.article.service.client.UserLevelClient;
import com.liang.bbs.article.service.client.VisitServiceClient;
import com.liang.bbs.article.service.mapstruct.ArticleMS;
import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.bbs.user.facade.dto.UserLevelDTO;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ImageTypeEnum;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.utils.CommonUtils;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticlePoMapper articlePoMapper;

    @Autowired
    private ArticlePoExMapper articlePoExMapper;

    @Autowired
    private ArticleLabelService articleLabelService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private UserLikeClient userLikeClient;

    @Autowired
    private VisitServiceClient visitService;

    @Autowired
    private UserFollowClient userFollowClient;

    @Autowired
    private UserLevelClient userLevelClient;

    @Autowired
    private FileServiceClient fileService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Integer contentMax = 200;

    /**
     * 閼惧嘲褰囬幍鈧張澶婎吀閺嶆悂鈧俺绻冮惃鍕瀮缁?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<ArticleDTO> getPassAll(LocalDateTime startTime, LocalDateTime endTime) {
        ArticlePoExample example = new ArticlePoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andCreateTimeBetween(startTime, endTime)
                .andStateEqualTo(ArticleStateEnum.enable.getCode());
        return ArticleMS.INSTANCE.toDTO(articlePoMapper.selectByExample(example));
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿
     *
     * @param articleSearchDTO
     * @param currentUser
     * @param articleStateEnum
     * @return
     */
    @Override
    public PageInfo<ArticleDTO> getList(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser, ArticleStateEnum articleStateEnum) {
        List<Integer> articleIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(articleSearchDTO.getLabelIds())) {
            // 閺嶈宓侀弽鍥╊劮id闂嗗棗鎮庨懢宄板絿閺傚洨鐝烽弽鍥╊劮娣団剝浼?
            List<ArticleLabelDTO> articleLabelDTOS = articleLabelService.getByLabelIds(articleSearchDTO.getLabelIds());
            if (CollectionUtils.isNotEmpty(articleLabelDTOS)) {
                articleIds = articleLabelDTOS.stream().map(ArticleLabelDTO::getArticleId).collect(Collectors.toList());
            } else {
                // 鐠囥儲鐖ｇ粵鍙ョ瑓濞屸剝婀侀弬鍥╃彿
                return new PageInfo<>(new ArrayList<>());
            }
        }

        ArticlePoExample example = new ArticlePoExample();
        ArticlePoExample.Criteria criteria = example.createCriteria()
                .andIsDeletedEqualTo(false);
        if (articleStateEnum != null) {
            criteria.andStateEqualTo(articleStateEnum.getCode());
        }
        if (articleSearchDTO.getId() != null) {
            criteria.andIdEqualTo(articleSearchDTO.getId());
        }
        if (StringUtils.isNotBlank(articleSearchDTO.getTitle())) {
            criteria.andTitleLike("%" + articleSearchDTO.getTitle() + "%");
        }
        if (CollectionUtils.isNotEmpty(articleIds)) {
            criteria.andIdIn(articleIds);
        }
        if (articleSearchDTO.getCreateUser() != null) {
            criteria.andCreateUserEqualTo(articleSearchDTO.getCreateUser());
        }
        example.setOrderByClause("top desc, create_time desc, `id` desc");

        PageHelper.startPage(articleSearchDTO.getCurrentPage(), articleSearchDTO.getPageSize());
        List<ArticlePo> articlePos = articlePoMapper.selectByExample(example);
        PageInfo<ArticleDTO> pageInfo = ArticleMS.INSTANCE.toPage(new PageInfo<>(articlePos));
        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            return pageInfo;
        }

        // 閺嬪嫬缂撻弬鍥╃彿娣団剝浼?
        buildArticleInfo(pageInfo.getList(), currentUser);

        return pageInfo;
    }

    @Override
    public Long getUserArticleCount(Long createUser, ArticleStateEnum articleStateEnum) {
        ArticlePoExample example = new ArticlePoExample();
        ArticlePoExample.Criteria criteria = example.createCriteria()
                .andIsDeletedEqualTo(false)
                .andCreateUserEqualTo(createUser);
        if (articleStateEnum != null) {
            criteria.andStateEqualTo(articleStateEnum.getCode());
        }

        return articlePoMapper.countByExample(example);
    }

    /**
     * 閼惧嘲褰囧鍛吀閺嶅摜娈戦弬鍥╃彿
     *
     * @param articleSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public PageInfo<ArticleDTO> getPendingReviewArticles(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser) {
        return this.getList(articleSearchDTO, currentUser, ArticleStateEnum.pendingReview);
    }

    /**
     * 閼惧嘲褰囩粋浣烘暏閻ㄥ嫭鏋冪粩?
     *
     * @param articleSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public PageInfo<ArticleDTO> getDisabledArticles(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser) {
        return this.getList(articleSearchDTO, currentUser, ArticleStateEnum.disabled);
    }

    /**
     * 娣囶喗鏁奸弬鍥╃彿鐎光剝澹掗悩鑸碘偓?
     *
     * @param articleDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean updateState(ArticleDTO articleDTO, UserSsoDTO currentUser) {
        ArticlePo articlePo = new ArticlePo();
        articlePo.setId(articleDTO.getId());
        articlePo.setUpdateTime(LocalDateTime.now());
        if (ArticleStateEnum.pendingReview.getCode().equals(articleDTO.getState())) {
            articlePo.setState(ArticleStateEnum.pendingReview.getCode());
        }
        if (ArticleStateEnum.disabled.getCode().equals(articleDTO.getState())) {
            articlePo.setState(ArticleStateEnum.disabled.getCode());
        }
        if (ArticleStateEnum.enable.getCode().equals(articleDTO.getState())) {
            articlePo.setState(ArticleStateEnum.enable.getCode());
        }

        if (articlePoMapper.updateByPrimaryKeySelective(articlePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新文章状态失败");
        }

        return true;
    }

    /**
     * 閼惧嘲褰囬悙纭呯鏉╁洨娈戦弬鍥╃彿
     *
     * @param likeSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public PageInfo<ArticleDTO> getLikesArticle(LikeSearchDTO likeSearchDTO, UserSsoDTO currentUser) {
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>();
        PageInfo<LikeDTO> likeDTOPageInfo = userLikeClient.getArticleByUserId(likeSearchDTO);
        BeanUtils.copyProperties(likeDTOPageInfo, pageInfo);
        if (CollectionUtils.isNotEmpty(likeDTOPageInfo.getList())) {
            List<Integer> articleIds = likeDTOPageInfo.getList().stream().distinct().map(LikeDTO::getArticleId).collect(Collectors.toList());
            List<ArticleDTO> articleDTOS = getBaseByIds(articleIds, ArticleStateEnum.enable);

            // 閺嬪嫬缂撻弬鍥╃彿娣団剝浼?
            buildArticleInfo(articleDTOS, currentUser);
            pageInfo.setList(articleDTOS);
        }

        return pageInfo;
    }

    /**
     * 闁俺绻冮弬鍥╃彿id闂嗗棗鎮庨懢宄板絿閺傚洨鐝锋穱鈩冧紖
     *
     * @param ids
     * @param isPv        閺勵垰鎯佹晶鐐插閺傚洨鐝峰ù蹇氼潔閺佷即鍣?
     * @param currentUser
     * @return
     */
    @Override
    public List<ArticleDTO> getByIds(List<Integer> ids, Boolean isPv, UserSsoDTO currentUser) {
        List<ArticleDTO> articleDTOS = getBaseByIds(ids, null);
        if (CollectionUtils.isEmpty(articleDTOS)) {
            return articleDTOS;
        }

        // 閺嬪嫬缂撻弬鍥╃彿娣団剝浼?
        buildArticleInfo(articleDTOS, currentUser);

        // 閼惧嘲褰囬弬鍥╃彿閸愬懎顔?
        List<ArticleMarkdownInfo> articleMarkdownInfos = getMarkdownByArticleIds(ids);
        Map<Integer, List<ArticleMarkdownInfo>> articleId2List = articleMarkdownInfos.stream().collect(Collectors.groupingBy(ArticleMarkdownInfo::getArticleId));
        articleDTOS.forEach(articleDTO -> {
            if (articleId2List.containsKey(articleDTO.getId())) {
                ArticleMarkdownInfo articleMarkdownInfo = articleId2List.get(articleDTO.getId()).get(0);
                articleDTO.setMarkdown(articleMarkdownInfo.getArticleMarkdown());
                articleDTO.setHtml(articleMarkdownInfo.getArticleHtml());
            }
        });

        // 婢х偛濮為弬鍥╃彿濞村繗顫嶉弫浼村櫤
        if (isPv != null && isPv) {
            this.updatePv(articleDTOS.get(0));
        }

        return articleDTOS;
    }

    /**
     * 闁俺绻冮弬鍥╃彿id闂嗗棗鎮庨懢宄板絿閺傚洨鐝锋穱鈩冧紖(閺堚偓閸╄櫣顢呴惃鍕繆閹?
     *
     * @param ids
     * @return
     */
    @Override
    public List<ArticleDTO> getBaseByIds(List<Integer> ids, ArticleStateEnum articleStateEnum) {
        ArticlePoExample example = new ArticlePoExample();
        ArticlePoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        if (articleStateEnum != null) {
            criteria.andStateEqualTo(articleStateEnum.getCode());
        }
        // 閹稿”n閻ㄥ嫬寮弫浼淬€庢惔蹇斿笓鎼?
        String stringIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        example.setOrderByClause("field(id, " + stringIds + ")");

        return ArticleMS.INSTANCE.toDTO(articlePoMapper.selectByExample(example));
    }

    /**
     * 閹炬澘鍟撻弬鍥╃彿閿涘牊妫ら柊宥呮禈閿?
     *
     * @param articleDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(articleDTO.getTitle()) || StringUtils.isBlank(articleDTO.getHtml())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "标题或内容不能为空");
        }
        articleDTO.setIsDeleted(false);
        String content = CommonUtils.html2Text(articleDTO.getHtml());
        articleDTO.setContent(content.length() < contentMax ? content : content.substring(0, contentMax));
        articleDTO.setCreateUser(currentUser.getUserId());
        articleDTO.setUpdateUser(currentUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        articleDTO.setCreateTime(now);
        articleDTO.setUpdateTime(now);
        // 闁俺绻冪€光剝鐗抽惃鍕瀮缁旂姵澧犳导姘儙閻㈩煉绱欓崡绛圭窗姒涙顓诲鍛吀閺嶉潻绱?
        articleDTO.setState(ArticleStateEnum.pendingReview.getCode());
        ArticlePo articlePo = ArticleMS.INSTANCE.toPo(articleDTO);
        if (articlePoMapper.insertSelective(articlePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增文章失败");
        }
        // 閺傛澘顤冮弬鍥︽閺嶅洨顒烽崗宕囬兇娣団剝浼?
        articleLabelService.create(labelIds, articlePo.getId(), currentUser);

        // 閹绘帒鍙嗛弬鍥╃彿閸愬懎顔愰敍鍧ngo閿?
        insertArticleContent(articlePo.getId(), articleDTO.getMarkdown(), articleDTO.getHtml(), currentUser.getUserId(), now);

        return true;
    }

    /**
     * 閺囧瓨鏌婇弬鍥╃彿閿涘牊妫ら柊宥呮禈閿?
     *
     * @param articleDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean update(ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(articleDTO.getTitle()) || StringUtils.isBlank(articleDTO.getHtml())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "标题或内容不能为空");
        }
        ArticlePo oldArticlePo = articlePoMapper.selectByPrimaryKey(articleDTO.getId());
        // 閸欘亣鍏橀弴瀛樻煀閼奉亜绻侀惃鍕瀮缁?
        if (!currentUser.getUserId().equals(oldArticlePo.getCreateUser())) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "只能修改自己发布的文章");
        }

        String content = CommonUtils.html2Text(articleDTO.getHtml());
        articleDTO.setContent(content.length() < contentMax ? content : content.substring(0, contentMax));
        LocalDateTime now = LocalDateTime.now();
        articleDTO.setUpdateTime(now);
        articleDTO.setUpdateUser(currentUser.getUserId());
        articleDTO.setState(ArticleStateEnum.pendingReview.getCode());
        ArticlePo articlePo = ArticleMS.INSTANCE.toPo(articleDTO);
        if (articlePoMapper.updateByPrimaryKeySelective(articlePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新文章失败");
        }
        // 閺囧瓨鏌婇弬鍥︽閺嶅洨顒烽崗宕囬兇娣団剝浼?
        articleLabelService.update(labelIds, articlePo.getId(), currentUser);

        // 閺囧瓨鏌婇弬鍥╃彿閸愬懎顔愰敍鍧ngo閿?
        updateArticleContent(articlePo.getId(), articleDTO.getMarkdown(), articleDTO.getHtml(), currentUser.getUserId(), now);

        return true;
    }

    /**
     * 閹炬澘鍟撻弬鍥╃彿
     *
     * @param bytes
     * @param sourceFileName
     * @param articleDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(byte[] bytes, String sourceFileName, ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser) {
        try {
            // 閺傚洣娆㈡稉濠佺炊閿涘牊瀵滃В鏂剧伐閸樺缂夐敍?
            String picture = fileService.fileScaleUpload(bytes, sourceFileName, ImageTypeEnum.articleTitleMap.name());
            articleDTO.setTitleMap(picture);
            create(articleDTO, labelIds, currentUser);
        } catch (Exception e) {
            log.error("上传文章标题图失败", e);
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "上传文章标题图失败");
        }

        return null;
    }

    /**
     * 閺囧瓨鏌婇弬鍥╃彿
     *
     * @param bytes
     * @param sourceFileName
     * @param articleDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean update(byte[] bytes, String sourceFileName, ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser) {
        try {
            // 閺傚洣娆㈡稉濠佺炊閿涘牊瀵滃В鏂剧伐閸樺缂夐敍?
            String picture = fileService.fileScaleUpload(bytes, sourceFileName, ImageTypeEnum.articleTitleMap.name());
            articleDTO.setTitleMap(picture);
            update(articleDTO, labelIds, currentUser);
        } catch (Exception e) {
            log.error("上传文章标题图失败", e);
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "上传文章标题图失败");
        }

        return null;
    }

    /**
     * 娑撳﹣绱堕崶鍓у閿涘牅绔村鐙呯礆- mavonEditor
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    @Override
    public String uploadPicture(byte[] bytes, String sourceFileName) {
        // 閺傚洣娆㈡稉濠佺炊閿涘牊瀵滃В鏂剧伐閸樺缂夐敍?
        return fileService.fileScaleUpload(bytes, sourceFileName, ImageTypeEnum.articlePicture.name());
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿鐠囧嫯顔戠拋鍧楁６閹粯鏆?
     *
     * @return
     */
    @Override
    public TotalDTO getArticleCommentVisitTotal() {
        TotalDTO totalDTO = new TotalDTO();
        totalDTO.setArticleCount(getTotal());
        totalDTO.setCommentCount(commentService.getTotal());
        totalDTO.setVisitCount(visitService.getTotal());
        return totalDTO;
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿閺佷即鍣?
     *
     * @return
     */
    @Override
    public Long getTotal() {
        ArticlePoExample example = new ArticlePoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(ArticleStateEnum.enable.getCode());
        return articlePoMapper.countByExample(example);
    }

    /**
     * 閼惧嘲褰囬悽銊﹀煕闂冨懓顕伴弫浼村櫤
     *
     * @param userIds
     * @return
     */
    @Override
    public List<ArticleReadDTO> getUserReadCount(List<Long> userIds) {
        return articlePoExMapper.selectUserReadCount(userIds);
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿娑撯偓娴滄稓绮虹拋鈩冩殶閹?
     *
     * @param id
     * @param currentUser
     * @return
     */
    @Override
    public ArticleCountDTO getCountById(Integer id, UserSsoDTO currentUser) {
        // 閼惧嘲褰囬弬鍥╃彿娣団剝浼?
        ArticlePo articlePo = articlePoMapper.selectByPrimaryKey(id);
        ArticleCountDTO articleCountDTO = new ArticleCountDTO();
        // 閼惧嘲褰囬弬鍥╃彿閻愮绂愰弫浼村櫤
        articleCountDTO.setLikeCount(userLikeClient.getLikeCountArticle(Collections.singletonList(id)));
        // 閺勵垰鎯佸鑼病閻愮绂愰妴渚€鈧俺绻僨romUser閸滃oUser閼惧嘲褰囬崗铏暈娣団剝浼?
        if (currentUser != null) {
            articleCountDTO.setIsLike(userLikeClient.isLike(id, currentUser.getUserId()));
            FollowDTO followDTO = userFollowClient.getByFromToUser(currentUser.getUserId(), articlePo.getCreateUser(), false);
            if (followDTO != null) {
                articleCountDTO.setIsFollow(true);
            }
        }
        // 閼惧嘲褰囬弬鍥╃彿鐠囧嫯顔戦弫浼村櫤
        articleCountDTO.setCommentCount(commentService.getCommentCountByArticle(id));
        // 閼惧嘲褰囬悽銊﹀煕缁涘楠?
        List<UserLevelDTO> userLevelDTOS = userLevelClient.getByUserId(articlePo.getCreateUser());
        if (CollectionUtils.isNotEmpty(userLevelDTOS)) {
            articleCountDTO.setLevel(userLevelDTOS.get(0).getLevel());
        }

        return articleCountDTO;
    }

    /**
     * 婢х偛濮為弬鍥╃彿濞村繗顫嶉弫浼村櫤
     *
     * @param articleDTO
     * @return
     */
    @Override
    public Boolean updatePv(ArticleDTO articleDTO) {
        ArticlePo articlePo = ArticleMS.INSTANCE.toPo(articleDTO);
        articlePo.setPv(articlePo.getPv() + 1);
        if (articlePoMapper.updateByPrimaryKeySelective(articlePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新文章浏览量失败");
        }
        return true;
    }

    /**
     * 闁俺绻冮悽銊﹀煕閼惧嘲褰囬弬鍥╃彿娣団剝浼?
     *
     * @param userId
     * @return
     */
    @Override
    public List<ArticleDTO> getByUserId(Long userId) {
        ArticlePoExample example = new ArticlePoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(ArticleStateEnum.enable.getCode())
                .andCreateUserEqualTo(userId);

        return ArticleMS.INSTANCE.toDTO(articlePoMapper.selectByExample(example));
    }

    @Override
    public Boolean articleTop(Integer id, Boolean top, UserSsoDTO currentUser) {
        ArticlePo articlePo = articlePoMapper.selectByPrimaryKey(id);
        articlePo.setUpdateTime(LocalDateTime.now());
        articlePo.setUpdateUser(currentUser.getUserId());
        // 缂冾噣銆?
        if (top) {
            int maxTop = Objects.isNull(this.getMaxTop()) ? 0 : this.getMaxTop();
            articlePo.setTop(maxTop + 1);
            if (articlePoMapper.updateByPrimaryKey(articlePo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閺傚洨鐝风純顕€銆婃径杈Е");
            }
        } else {
            // 閸欐牗绉风純顕€銆?
            articlePo.setTop(null);
            if (articlePoMapper.updateByPrimaryKey(articlePo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閺傚洨鐝烽崣鏍ㄧХ缂冾噣銆婃径杈Е");
            }
        }

        return true;
    }

    @Override
    public Integer getMaxTop() {
        ArticlePoExample example = new ArticlePoExample();
        example.setOrderByClause("top desc limit 1");
        List<ArticlePo> articlePos = articlePoMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(articlePos)) {
            return articlePos.get(0).getTop();
        }

        return null;
    }

    @Override
    public Boolean delete(Integer id, UserSsoDTO currentUser) {
        ArticlePo articlePo = articlePoMapper.selectByPrimaryKey(id);
        // 閸欘亣鍏橀崚鐘绘珟閼奉亜绻侀惃鍕瀮缁?
        if (!currentUser.getUserId().equals(articlePo.getCreateUser())) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "只能删除自己发布的文章");
        }
        articlePo.setIsDeleted(true);
        articlePo.setUpdateTime(LocalDateTime.now());
        articlePo.setUpdateUser(currentUser.getUserId());
        if (articlePoMapper.updateByPrimaryKeySelective(articlePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "删除文章失败");
        }

        return true;
    }

    @Override
    public ArticleCheckCountDTO getArticleCheckCount(String title) {
        ArticleCheckCountDTO articleCheckCountDTO = new ArticleCheckCountDTO();
        List<Map<String, Object>> list = articlePoExMapper.selectArticleCheckCount(title);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(stringLongMap -> {
                Integer state = Integer.valueOf(stringLongMap.get("state").toString());
                Long num = Long.valueOf(stringLongMap.get("num").toString());
                if (ArticleStateEnum.enable.getCode().equals(state)) {
                    articleCheckCountDTO.setEnableCount(num);
                }
                if (ArticleStateEnum.disabled.getCode().equals(state)) {
                    articleCheckCountDTO.setDisabledCount(num);
                }
                if (ArticleStateEnum.pendingReview.getCode().equals(state)) {
                    articleCheckCountDTO.setPendingReviewCount(num);
                }
            });
        }

        return articleCheckCountDTO;
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿id閸掔増鐖ｇ粵鍙ヤ繆閹垳娈戦弰鐘茬殸
     *
     * @param articleIds
     * @return
     */
    private Map<Integer, List<LabelDTO>> getArticleIdToLabels(List<Integer> articleIds) {
        // 閺傚洨鐝穒d閸掔増鐖ｇ粵鍙ヤ繆閹垳娈戦弰鐘茬殸閿涘潣ey:閺傚洨鐝穒d, value:閺嶅洨顒锋穱鈩冧紖閿?
        Map<Integer, List<LabelDTO>> articleToLabelMap = new HashMap<>();

        List<ArticleLabelDTO> articleLabelDTOS = articleLabelService.getByArticleIds(articleIds);
        if (CollectionUtils.isNotEmpty(articleLabelDTOS)) {
            // 閼惧嘲褰囬幍鈧張澶屾畱閺嶅洨顒穒d闂嗗棗鎮?
            List<Integer> labelIds = articleLabelDTOS.stream().distinct().map(ArticleLabelDTO::getLabelId).collect(Collectors.toList());
            Map<Integer, List<ArticleLabelDTO>> articleIdKeyMap = articleLabelDTOS.stream().collect(Collectors.groupingBy(ArticleLabelDTO::getArticleId));
            List<LabelDTO> labelDTOS = labelService.getByIds(labelIds);
            if (CollectionUtils.isNotEmpty(labelDTOS)) {
                Map<Integer, List<LabelDTO>> labelMap = labelDTOS.stream().collect(Collectors.groupingBy(LabelDTO::getId));
                articleIdKeyMap.forEach((k, v) -> {
                    List<LabelDTO> result = new ArrayList<>();
                    v.forEach(articleLabelDTO -> {
                        List<LabelDTO> labelDTOList = labelMap.get(articleLabelDTO.getLabelId());
                        result.addAll(CollectionUtils.isNotEmpty(labelDTOList) ? labelDTOList : new ArrayList<>());
                    });
                    articleToLabelMap.put(k, result);
                });
            }
        }

        return articleToLabelMap;
    }

    /**
     * 閺嬪嫬缂撻弬鍥╃彿娣団剝浼?
     *
     * @param articleDTOS
     * @param currentUser
     */
    private void buildArticleInfo(List<ArticleDTO> articleDTOS, UserSsoDTO currentUser) {
        // 閼惧嘲褰囬弬鍥╃彿id閸掔増鐖ｇ粵鍙ヤ繆閹垳娈戦弰鐘茬殸
        List<Integer> articleIds = articleDTOS.stream().map(ArticleDTO::getId).collect(Collectors.toList());
        Map<Integer, List<LabelDTO>> articleToLabelMap = getArticleIdToLabels(articleIds);

        // 闁俺绻冮悽銊﹀煕id闂嗗棗鎮庨懢宄板絿閻劍鍩涙穱鈩冧紖
        List<Long> userIds = articleDTOS.stream().map(ArticleDTO::getCreateUser).collect(Collectors.toList());
        // 閻劍鍩涢崺铏诡攨娣団剝浼?
        Map<Long, List<UserDTO>> idUsers = userService.getByIds(userIds).stream().collect(Collectors.groupingBy(UserDTO::getId));
        // 閻劍鍩涚粵澶岄獓娣団剝浼?
        Map<Long, List<UserLevelDTO>> userIdToUserLevel = userLevelClient.getByUserIds(userIds).stream().collect(Collectors.groupingBy(UserLevelDTO::getUserId));
        // 闁俺绻僫d閼惧嘲褰噉ame
        articleDTOS.forEach(articleDTO -> {
            if (idUsers.containsKey(articleDTO.getCreateUser())) {
                articleDTO.setCreateUserName(idUsers.get(articleDTO.getCreateUser()).get(0).getName());
                articleDTO.setPicture(idUsers.get(articleDTO.getCreateUser()).get(0).getPicture());
            }
            if (userIdToUserLevel.containsKey(articleDTO.getCreateUser())) {
                articleDTO.setLevel(userIdToUserLevel.get(articleDTO.getCreateUser()).get(0).getLevel());
            }
            // 閺佺増宓佹惔鎾诲櫡闂堛垹骞撻幏?
            if (!articleToLabelMap.isEmpty()) {
                articleDTO.setLabelDTOS(articleToLabelMap.get(articleDTO.getId()));
            }
            articleDTO.setArticleCountDTO(this.getCountById(articleDTO.getId(), currentUser));
        });
    }

    /**
     * 閹绘帒鍙嗛弬鍥╃彿閸愬懎顔愰敍鍧ngo閿?
     *
     * @param articleId
     * @param markdown
     * @param html
     * @param userId
     * @param now
     */
    private void insertArticleContent(Integer articleId, String markdown, String html, Long userId, LocalDateTime now) {
        ArticleMarkdownInfo articleMarkdownInfo = new ArticleMarkdownInfo();
        articleMarkdownInfo.setArticleId(articleId);
        articleMarkdownInfo.setArticleMarkdown(markdown);
        articleMarkdownInfo.setArticleHtml(html);
        articleMarkdownInfo.setUserId(userId);
        articleMarkdownInfo.setTime(now);
        mongoTemplate.insert(Collections.singletonList(articleMarkdownInfo), ArticleMarkdownInfo.class);
    }

    /**
     * 閺囧瓨鏌婇弬鍥╃彿閸愬懎顔愰敍鍧ngo閿?
     *
     * @param articleId
     * @param markdown
     * @param html
     * @param userId
     * @param now
     */
    private void updateArticleContent(Integer articleId, String markdown, String html, Long userId, LocalDateTime now) {
        Query query = new Query(Criteria.where("articleId").is(articleId));
        Update update = new Update();
        update.set("articleMarkdown", markdown);
        update.set("articleHtml", html);
        update.set("userId", userId);
        update.set("time", now);
        mongoTemplate.updateMulti(query, update, ArticleMarkdownInfo.class);
    }

    /**
     * 闁俺绻冮弬鍥╃彿id闂嗗棗鎮庨懢宄板絿閺傚洨鐝烽崘鍛啇
     *
     * @param articleIds
     * @return
     */
    private List<ArticleMarkdownInfo> getMarkdownByArticleIds(List<Integer> articleIds) {
        Query query = new Query(Criteria.where("articleId").in(articleIds));
        return mongoTemplate.find(query, ArticleMarkdownInfo.class);
    }

}


