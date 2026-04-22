package com.liang.bbs.rest.client;

import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserEmailDTO;
import com.liang.manage.auth.facade.dto.user.UserLoginDTO;
import com.liang.manage.auth.facade.dto.user.UserPasswordDTO;
import com.liang.manage.auth.facade.dto.user.UserTokenDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        contextId = "restUserServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        path = "/user"
)
public interface UserServiceClient {

    @GetMapping("/inner-login-url")
    String innerLoginUrl(@RequestParam("referer") String referer);

    @GetMapping("/verify-token")
    Boolean verifyToken(@RequestParam("token") String token);

    @GetMapping("/is-expired")
    Boolean isExpired(@RequestParam("token") String token);

    @GetMapping("/token-user")
    UserSsoDTO getUserSsoDTOByToken(@RequestParam("token") String token);

    @GetMapping("/by-id")
    UserDTO getById(@RequestParam("id") Long id);

    @GetMapping("/by-email")
    UserDTO getByEmail(@RequestParam("email") String email);

    @GetMapping("/by-phone")
    UserDTO getByPhone(@RequestParam("phone") String phone);

    @PostMapping("/upload-user-picture")
    Boolean uploadUserPicture(@RequestParam("bytes") byte[] bytes,
                              @RequestParam("sourceFileName") String sourceFileName,
                              @SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/update-user-basic-info")
    Boolean updateUserBasicInfo(@RequestBody UserDTO userDTO, @SpringQueryMap UserSsoDTO currentUser);

    @GetMapping("/send-email-verify-code")
    Boolean sendEmailVerifyCode(@RequestParam("email") String email, @SpringQueryMap UserSsoDTO currentUser);

    @GetMapping("/send-sms-verify-code")
    Boolean sendSmsVerifyCode(@RequestParam("phone") String phone, @SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/bind-email")
    Boolean bindEmail(@RequestBody UserEmailDTO userEmailDTO, @SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/bind-phone")
    Boolean bindPhone(@RequestBody UserEmailDTO userEmailDTO, @SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/untie-email")
    Boolean untieEmail(@SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/untie-phone")
    Boolean untiePhone(@SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/update-password")
    Boolean updatePassword(@RequestBody UserPasswordDTO passwordDTO, @SpringQueryMap UserSsoDTO currentUser);

    @GetMapping("/is-valid-email")
    Boolean isValidEmail(@RequestParam("email") String email);

    @GetMapping("/is-valid-phone")
    Boolean isValidPhone(@RequestParam("phone") String phone);

    @GetMapping("/is-valid-user")
    Boolean isValidUser(@RequestParam("username") String username, @SpringQueryMap UserSsoDTO currentUser);

    @PostMapping("/is-phone-exist")
    Boolean isPhoneExist(@RequestParam("phone") String phone);

    @PostMapping("/is-email-exist")
    Boolean isEmailExist(@RequestParam("email") String email);

    @PostMapping("/phone-reset-password")
    Boolean phoneResetPassword(@RequestBody UserEmailDTO userEmailDTO);

    @PostMapping("/email-reset-password")
    Boolean emailResetPassword(@RequestBody UserEmailDTO userEmailDTO);

    @PostMapping("/register")
    UserTokenDTO register(@RequestBody UserDTO userDTO);

    @PostMapping("/login")
    UserTokenDTO login(@RequestBody UserLoginDTO userLoginDTO);

    @GetMapping("/logout")
    void logout(@RequestParam("token") String token);
}
