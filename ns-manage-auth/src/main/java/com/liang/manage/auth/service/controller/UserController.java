package com.liang.manage.auth.service.controller;

import com.liang.manage.auth.facade.dto.user.*;
import com.liang.manage.auth.service.store.InMemoryManageAuthStore;
import com.liang.nansheng.common.auth.UserSsoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String DEFAULT_VERIFY_CODE = "123456";
    private static final String DEFAULT_BASE_URL = "http://127.0.0.1:7014";

    private final InMemoryManageAuthStore store;
    private final String baseUrl;

    public UserController(InMemoryManageAuthStore store,
                          @Value("${local.manage-auth.base-url:" + DEFAULT_BASE_URL + "}") String baseUrl) {
        this.store = store;
        this.baseUrl = StringUtils.removeEnd(baseUrl, "/");
    }

    @GetMapping("/inner-login-url")
    public String innerLoginUrl(@RequestParam("referer") String referer) {
        return baseUrl + "/login?redirect=" + URLEncoder.encode(referer, StandardCharsets.UTF_8);
    }

    @GetMapping("/verify-token")
    public Boolean verifyToken(@RequestParam("token") String token) {
        return store.tokenExists(token);
    }

    @GetMapping("/is-expired")
    public Boolean isExpired(@RequestParam("token") String token) {
        return store.tokenExpired(token);
    }

    @GetMapping("/token-user")
    public UserSsoDTO getUserSsoDTOByToken(@RequestParam("token") String token) {
        return store.tokenUser(token);
    }

    @GetMapping("/by-id")
    public UserDTO getById(@RequestParam("id") Long id) {
        return store.findUserById(id);
    }

    @GetMapping("/by-email")
    public UserDTO getByEmail(@RequestParam("email") String email) {
        return store.findUserByEmail(email);
    }

    @GetMapping("/by-phone")
    public UserDTO getByPhone(@RequestParam("phone") String phone) {
        return store.findUserByPhone(phone);
    }

    @GetMapping("/all-list")
    public List<UserListDTO> getAllList() {
        return store.allUsers();
    }

    @GetMapping("/by-ids")
    public List<UserDTO> getByIds(@RequestParam("userIds") List<Long> userIds) {
        return store.findUsersByIds(userIds);
    }

    @PostMapping("/upload-user-picture")
    public Boolean uploadUserPicture(@RequestParam("bytes") byte[] bytes,
                                     @RequestParam("sourceFileName") String sourceFileName,
                                     UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null || bytes == null || bytes.length == 0) {
            return false;
        }
        String ext = sourceFileName != null && sourceFileName.contains(".")
                ? sourceFileName.substring(sourceFileName.lastIndexOf("."))
                : ".png";
        String url = baseUrl + "/mock-user-picture/" + userId + "/" + System.currentTimeMillis() + ext;
        return store.updatePicture(userId, url);
    }

    @PostMapping("/update-user-basic-info")
    public Boolean updateUserBasicInfo(@RequestBody UserDTO userDTO, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        return userId != null && store.updateUserBasicInfo(userDTO, userId);
    }

    @GetMapping("/send-email-verify-code")
    public Boolean sendEmailVerifyCode(@RequestParam("email") String email, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        store.saveVerifyCode("email:" + email, DEFAULT_VERIFY_CODE);
        return true;
    }

    @GetMapping("/send-sms-verify-code")
    public Boolean sendSmsVerifyCode(@RequestParam("phone") String phone, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        store.saveVerifyCode("phone:" + phone, DEFAULT_VERIFY_CODE);
        return true;
    }

    @PostMapping("/bind-email")
    public Boolean bindEmail(@RequestBody UserEmailDTO userEmailDTO, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null || userEmailDTO == null || StringUtils.isBlank(userEmailDTO.getEmail())) {
            return false;
        }
        if (!store.verifyCode("email:" + userEmailDTO.getEmail(), userEmailDTO.getCode())) {
            return false;
        }
        return store.bindEmail(userId, userEmailDTO.getEmail());
    }

    @PostMapping("/bind-phone")
    public Boolean bindPhone(@RequestBody UserEmailDTO userEmailDTO, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null || userEmailDTO == null || StringUtils.isBlank(userEmailDTO.getPhone())) {
            return false;
        }
        if (!store.verifyCode("phone:" + userEmailDTO.getPhone(), userEmailDTO.getCode())) {
            return false;
        }
        return store.bindPhone(userId, userEmailDTO.getPhone());
    }

    @PostMapping("/untie-email")
    public Boolean untieEmail(UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        return userId != null && store.untieEmail(userId);
    }

    @PostMapping("/untie-phone")
    public Boolean untiePhone(UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        return userId != null && store.untiePhone(userId);
    }

    @PostMapping("/update-password")
    public Boolean updatePassword(@RequestBody UserPasswordDTO passwordDTO, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        UserDTO db = userId == null ? null : store.findUserById(userId);
        if (db == null || passwordDTO == null || StringUtils.isBlank(passwordDTO.getNewPassword())) {
            return false;
        }
        if (!StringUtils.equals(db.getPassword(), passwordDTO.getOldPassword())) {
            return false;
        }
        return store.updatePassword(userId, passwordDTO.getNewPassword());
    }

    @GetMapping("/is-valid-email")
    public Boolean isValidEmail(@RequestParam("email") String email) {
        return StringUtils.isNotBlank(email) && email.contains("@") && !StringUtils.containsWhitespace(email);
    }

    @GetMapping("/is-valid-phone")
    public Boolean isValidPhone(@RequestParam("phone") String phone) {
        return StringUtils.isNotBlank(phone) && phone.matches("^1\\d{10}$");
    }

    @GetMapping("/is-valid-user")
    public Boolean isValidUser(@RequestParam("username") String username, UserSsoDTO currentUser) {
        UserDTO existed = store.findUserByName(username);
        if (existed == null) {
            return true;
        }
        return currentUser != null && existed.getId().equals(currentUser.getUserId());
    }

    @PostMapping("/is-phone-exist")
    public Boolean isPhoneExist(@RequestParam("phone") String phone) {
        return store.findUserByPhone(phone) != null;
    }

    @PostMapping("/is-email-exist")
    public Boolean isEmailExist(@RequestParam("email") String email) {
        return store.findUserByEmail(email) != null;
    }

    @PostMapping("/phone-reset-password")
    public Boolean phoneResetPassword(@RequestBody UserEmailDTO userEmailDTO) {
        if (userEmailDTO == null || StringUtils.isBlank(userEmailDTO.getPhone()) || StringUtils.isBlank(userEmailDTO.getNewPassword())) {
            return false;
        }
        if (!store.verifyCode("phone:" + userEmailDTO.getPhone(), userEmailDTO.getCode())) {
            return false;
        }
        UserDTO user = store.findUserByPhone(userEmailDTO.getPhone());
        return user != null && store.updatePassword(user.getId(), userEmailDTO.getNewPassword());
    }

    @PostMapping("/email-reset-password")
    public Boolean emailResetPassword(@RequestBody UserEmailDTO userEmailDTO) {
        if (userEmailDTO == null || StringUtils.isBlank(userEmailDTO.getEmail()) || StringUtils.isBlank(userEmailDTO.getNewPassword())) {
            return false;
        }
        if (!store.verifyCode("email:" + userEmailDTO.getEmail(), userEmailDTO.getCode())) {
            return false;
        }
        UserDTO user = store.findUserByEmail(userEmailDTO.getEmail());
        return user != null && store.updatePassword(user.getId(), userEmailDTO.getNewPassword());
    }

    @PostMapping("/register")
    public UserTokenDTO register(@RequestBody UserDTO userDTO) {
        UserDTO created = store.createUser(userDTO == null ? new UserDTO() : userDTO);
        String token = store.createToken(created.getId());
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setUserId(created.getId());
        userTokenDTO.setUsername(created.getName());
        userTokenDTO.setToken(token);
        return userTokenDTO;
    }

    @PostMapping("/login")
    public UserTokenDTO login(@RequestBody UserLoginDTO userLoginDTO) {
        if (userLoginDTO == null || StringUtils.isBlank(userLoginDTO.getName())) {
            return emptyToken();
        }
        UserDTO user = findUserForLogin(userLoginDTO.getName());
        if (user == null || !StringUtils.equals(user.getPassword(), userLoginDTO.getPassword())) {
            return emptyToken();
        }
        String token = store.createToken(user.getId());
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setUserId(user.getId());
        userTokenDTO.setUsername(user.getName());
        userTokenDTO.setToken(token);
        return userTokenDTO;
    }

    @GetMapping("/logout")
    public void logout(@RequestParam("token") String token) {
        store.removeToken(token);
    }

    private UserDTO findUserForLogin(String account) {
        UserDTO byName = store.findUserByName(account);
        if (byName != null) {
            return byName;
        }
        UserDTO byPhone = store.findUserByPhone(account);
        if (byPhone != null) {
            return byPhone;
        }
        return store.findUserByEmail(account);
    }

    private UserTokenDTO emptyToken() {
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setUserId(-1L);
        userTokenDTO.setUsername("anonymous");
        userTokenDTO.setToken(StringUtils.EMPTY);
        return userTokenDTO;
    }
}
