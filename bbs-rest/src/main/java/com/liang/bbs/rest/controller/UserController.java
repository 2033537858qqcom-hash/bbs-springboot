package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.bbs.rest.client.ArticleArticleClient;
import com.liang.bbs.rest.client.UserServiceClient;
import com.liang.bbs.rest.client.UserLevelClient;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.client.UserFollowClient;
import com.liang.bbs.rest.client.UserLikeClient;
import com.liang.bbs.rest.client.UserLikeCommentClient;
import com.liang.bbs.rest.utils.FileLengthUtils;
import com.liang.bbs.user.facade.dto.*;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserEmailDTO;
import com.liang.manage.auth.facade.dto.user.UserPasswordDTO;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserRightsDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.utils.CommonUtils;
import com.liang.nansheng.common.web.basic.ResponseResult;
import com.liang.nansheng.common.web.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/user/")
@Tag(name = "API")
public class UserController {
    @Autowired
    private UserLevelClient userLevelClient;

    @Autowired
    private UserFollowClient userFollowClient;

    @Autowired
    private UserLikeClient userLikeClient;

    @Autowired
    private UserLikeCommentClient userLikeCommentClient;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private ArticleArticleClient articleArticleClient;

    @Autowired
    private FileLengthUtils fileLengthUtils;

    @NoNeedLogin
    @GetMapping("getCurrentUserRights")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<UserRightsDTO> getCurrentUserRights() {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userSsoDTOtoUserRightsDTO(currentUser));
    }

    @NoNeedLogin
    @GetMapping("getFollowUsers")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<FollowDTO>> getFollowUsers(FollowSearchDTO followSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalFollowQueryRequest request = new InternalFollowQueryRequest();
        request.setFollowSearchDTO(followSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(userFollowClient.getFollowUsers(request));
    }

    @GetMapping("updateFollowState")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updateFollowState(@RequestParam Long toUser) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalFollowStateRequest request = new InternalFollowStateRequest();
        request.setFromUser(currentUser.getUserId());
        request.setToUser(toUser);
        return ResponseResult.success(userFollowClient.updateFollowState(request));
    }

    @GetMapping("updateLikeState")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updateLikeState(@RequestParam Integer articleId) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalLikeOperateRequest request = new InternalLikeOperateRequest();
        request.setArticleId(articleId);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(userLikeClient.updateLikeState(request));
    }

    @GetMapping("updateLikeCommentState")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updateLikeCommentState(@RequestParam Integer commentId) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalLikeCommentOperateRequest request = new InternalLikeCommentOperateRequest();
        request.setCommentId(commentId);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(userLikeCommentClient.updateLikeCommentState(request));
    }

    @NoNeedLogin
    @GetMapping("getHotAuthorsList")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<UserForumDTO>> getHotAuthorsList(UserSearchDTO userSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalUserLevelHotAuthorsRequest request = new InternalUserLevelHotAuthorsRequest();
        request.setUserSearchDTO(userSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(userLevelClient.getHotAuthorsList(request));
    }

    @NoNeedLogin
    @GetMapping("getUserInfo")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<UserForumDTO> getUserInfo(@RequestParam Long userId) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalUserLevelUserInfoRequest request = new InternalUserLevelUserInfoRequest();
        request.setUserId(userId);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(userLevelClient.getUserInfo(request));
    }

    @NoNeedLogin
    @GetMapping("getFollowCount")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<FollowCountDTO> getFollowCount(@RequestParam Long userId) {
        return ResponseResult.success(userFollowClient.getFollowCount(userId));
    }

    @PostMapping("uploadUserPicture")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> uploadUserPicture(@RequestParam("picture") MultipartFile picture) throws IOException {
        if (fileLengthUtils.isFileNotTooBig(picture.getBytes())) {
            UserSsoDTO currentUser = UserContextUtils.currentUser();
            return ResponseResult.success(userService.uploadUserPicture(picture.getBytes(), picture.getOriginalFilename(), currentUser));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    @PostMapping("updateUserBasicInfo")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updateUserBasicInfo(@RequestBody UserDTO userDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.updateUserBasicInfo(userDTO, currentUser));
    }

    @NoNeedLogin
    @GetMapping("sendEmailVerifyCode")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> sendEmailVerifyCode(@RequestParam String email) {
        UserSsoDTO currentUser = new UserSsoDTO();
        if (UserContextUtils.currentUser() == null) {
            // 閸忕厧顔愰幍瀣簚闁插秶鐤嗙€靛棛鐖?
            currentUser.setUserId(userService.getByEmail(email).getId());
        } else {
            currentUser = UserContextUtils.currentUser();
        }
        return ResponseResult.success(userService.sendEmailVerifyCode(email, currentUser));
    }

    @NoNeedLogin
    @GetMapping("sendSmsVerifyCode")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> sendSmsVerifyCode(@RequestParam String phone) {
        UserSsoDTO currentUser = new UserSsoDTO();
        if (UserContextUtils.currentUser() == null) {
            // 閸忕厧顔愰柇顔绢唸闁插秶鐤嗙€靛棛鐖?
            currentUser.setUserId(userService.getByPhone(phone).getId());
        } else {
            currentUser = UserContextUtils.currentUser();
        }
        return ResponseResult.success(userService.sendSmsVerifyCode(phone, currentUser));
    }

    @PostMapping("bindEmail")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> bindEmail(@RequestBody UserEmailDTO userEmailDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.bindEmail(userEmailDTO, currentUser));
    }

    @PostMapping("bindPhone")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> bindPhone(@RequestBody UserEmailDTO userEmailDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.bindPhone(userEmailDTO, currentUser));
    }

    @PostMapping("untieEmail")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> untieEmail() {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.untieEmail(currentUser));
    }

    @PostMapping("untiePhone")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> untiePhone() {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.untiePhone(currentUser));
    }

    @PostMapping("updatePassword")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> updatePassword(@RequestBody UserPasswordDTO passwordDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.updatePassword(passwordDTO, currentUser));
    }

    @GetMapping("isValidEmail")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> isValidEmail(@RequestParam String email) {
        return ResponseResult.success(userService.isValidEmail(email));
    }

    @GetMapping("isValidPhone")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> isValidPhone(@RequestParam String phone) {
        return ResponseResult.success(userService.isValidPhone(phone));
    }

    @NoNeedLogin
    @GetMapping("isValidUser")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> isValidUser(@RequestParam String username) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(userService.isValidUser(username, currentUser));
    }

    @NoNeedLogin
    @PostMapping("isPhoneExist/{phone}")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> isPhoneExist(@PathVariable String phone) {
        return ResponseResult.success(userService.isPhoneExist(phone));
    }

    @NoNeedLogin
    @PostMapping("isEmailExist/{email}")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> isEmailExist(@PathVariable String email) {
        return ResponseResult.success(userService.isEmailExist(email));
    }

    @NoNeedLogin
    @PostMapping("phoneResetPassword")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> phoneResetPassword(@RequestBody UserEmailDTO userEmailDTO) {
        return ResponseResult.success(userService.phoneResetPassword(userEmailDTO));
    }

    @NoNeedLogin
    @PostMapping("emailResetPassword")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> emailResetPassword(@RequestBody UserEmailDTO userEmailDTO) {
        return ResponseResult.success(userService.emailResetPassword(userEmailDTO));
    }

    @NoNeedLogin
    @GetMapping("getUserOperateCount")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<UserOperateCountDTO> getUserOperateCount(@RequestParam Long userId,
                                                                   @RequestParam(required = false) ArticleStateEnum articleStateEnum) {
        UserOperateCountDTO userOperateCountDTO = new UserOperateCountDTO();

        // 閼惧嘲褰囬悽銊﹀煕閺傚洨鐝烽弫浼村櫤
        userOperateCountDTO.setArticleCount(articleArticleClient.getUserArticleCount(
                userId,
                articleStateEnum == null ? "null" : articleStateEnum.name()
        ));

        // 閼惧嘲褰囬崗铏暈/缁绗ｉ弫浼村櫤
        FollowCountDTO followCount = userFollowClient.getFollowCount(userId);
        userOperateCountDTO.setFanCount(followCount.getFanCount());
        userOperateCountDTO.setFollowCount(followCount.getFollowCount());

        // 闁俺绻冮悽銊﹀煕id閼惧嘲褰囬悙纭呯閻ㄥ嫭鏋冪粩鐘虫殶闁?
        userOperateCountDTO.setLikeCount(userLikeClient.getUserTheLikeCount(userId));

        return ResponseResult.success(userOperateCountDTO);
    }

    /**
     * userSsoDTO鏉炵悢serRightsDTO
     *
     * @param currentUser
     * @return
     */
    private UserRightsDTO userSsoDTOtoUserRightsDTO(UserSsoDTO currentUser) {
        UserRightsDTO userRightsDTO = new UserRightsDTO();
        if (currentUser == null) {
            return userRightsDTO;
        }
        userRightsDTO.setUserId(currentUser.getUserId());
        userRightsDTO.setUserName(currentUser.getUserName());
        userRightsDTO.setRoles(currentUser.getRoles());

        return userRightsDTO;
    }

}


