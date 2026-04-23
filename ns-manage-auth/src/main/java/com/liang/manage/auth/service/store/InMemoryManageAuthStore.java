package com.liang.manage.auth.service.store;

import com.liang.manage.auth.facade.dto.notify.NotifyOutDTO;
import com.liang.manage.auth.facade.dto.notify.NotifySearchDTO;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserListDTO;
import com.liang.manage.auth.facade.dto.visit.VisitDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryManageAuthStore {

    private final AtomicLong userIdGenerator = new AtomicLong(1000L);
    private final AtomicInteger notifyIdGenerator = new AtomicInteger(1);
    private final Map<Long, UserDTO> users = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenToUserId = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> tokenExpireAt = new ConcurrentHashMap<>();
    private final Map<String, String> verifyCodes = new ConcurrentHashMap<>();
    private final List<VisitDTO> visits = new CopyOnWriteArrayList<>();
    private final List<NotifyOutDTO> notifies = new CopyOnWriteArrayList<>();

    @Value("${local.manage-auth.default-password:nacos}")
    private String defaultPassword;

    @Value("${local.manage-auth.token-expire-minutes:43200}")
    private long tokenExpireMinutes;

    @Value("${local.manage-auth.base-url:http://127.0.0.1:7014}")
    private String baseUrl;

    public synchronized UserDTO createUser(UserDTO input) {
        UserDTO user = new UserDTO();
        LocalDateTime now = LocalDateTime.now();
        user.setId(userIdGenerator.incrementAndGet());
        user.setName(StringUtils.defaultIfBlank(input.getName(), "user" + userIdGenerator.get()));
        user.setPassword(StringUtils.defaultIfBlank(input.getPassword(), defaultPassword));
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        String normalizedBaseUrl = StringUtils.removeEnd(baseUrl, "/");
        user.setPicture(StringUtils.defaultIfBlank(input.getPicture(),
                normalizedBaseUrl + "/mock-user-picture/" + user.getId() + "/default.png"));
        user.setIntro(StringUtils.defaultIfBlank(input.getIntro(), "这个用户由本地 ns-manage-auth 模拟服务创建"));
        user.setGender(input.getGender());
        user.setBirthday(input.getBirthday());
        user.setPosition(input.getPosition());
        user.setCompany(input.getCompany());
        user.setHomePage(input.getHomePage());
        user.setOrgId(input.getOrgId());
        user.setState(input.getState() == null ? Boolean.TRUE : input.getState());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        users.put(user.getId(), user);
        return cloneUser(user);
    }

    public UserDTO findUserById(Long id) {
        return cloneUser(users.get(id));
    }

    public UserDTO findUserByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        return users.values().stream()
                .filter(u -> StringUtils.equalsIgnoreCase(u.getEmail(), email))
                .findFirst()
                .map(this::cloneUser)
                .orElse(null);
    }

    public UserDTO findUserByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        return users.values().stream()
                .filter(u -> StringUtils.equals(u.getPhone(), phone))
                .findFirst()
                .map(this::cloneUser)
                .orElse(null);
    }

    public UserDTO findUserByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return users.values().stream()
                .filter(u -> StringUtils.equals(u.getName(), name))
                .findFirst()
                .map(this::cloneUser)
                .orElse(null);
    }

    public List<UserDTO> findUsersByIds(List<Long> userIds) {
        if (userIds == null) {
            return Collections.emptyList();
        }
        return userIds.stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .map(this::cloneUser)
                .collect(Collectors.toList());
    }

    public List<UserListDTO> allUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(UserDTO::getId))
                .map(this::toUserListDTO)
                .collect(Collectors.toList());
    }

    public synchronized boolean updateUserBasicInfo(UserDTO update, Long userId) {
        UserDTO old = users.get(userId);
        if (old == null) {
            return false;
        }
        if (StringUtils.isNotBlank(update.getName())) {
            old.setName(update.getName());
        }
        if (StringUtils.isNotBlank(update.getBirthday())) {
            old.setBirthday(update.getBirthday());
        }
        if (StringUtils.isNotBlank(update.getPosition())) {
            old.setPosition(update.getPosition());
        }
        if (StringUtils.isNotBlank(update.getCompany())) {
            old.setCompany(update.getCompany());
        }
        if (StringUtils.isNotBlank(update.getHomePage())) {
            old.setHomePage(update.getHomePage());
        }
        if (StringUtils.isNotBlank(update.getIntro())) {
            old.setIntro(update.getIntro());
        }
        if (update.getGender() != null) {
            old.setGender(update.getGender());
        }
        old.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean updatePicture(Long userId, String pictureUrl) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setPicture(pictureUrl);
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean bindEmail(Long userId, String email) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setEmail(email);
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean bindPhone(Long userId, String phone) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setPhone(phone);
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean untieEmail(Long userId) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setEmail(null);
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean untiePhone(Long userId) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setPhone(null);
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public synchronized boolean updatePassword(Long userId, String newPassword) {
        UserDTO user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setPassword(StringUtils.defaultIfBlank(newPassword, defaultPassword));
        user.setUpdateTime(LocalDateTime.now());
        return true;
    }

    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenToUserId.put(token, userId);
        tokenExpireAt.put(token, LocalDateTime.now().plusMinutes(tokenExpireMinutes));
        return token;
    }

    public void removeToken(String token) {
        tokenToUserId.remove(token);
        tokenExpireAt.remove(token);
    }

    public boolean tokenExists(String token) {
        return tokenToUserId.containsKey(token);
    }

    public boolean tokenExpired(String token) {
        LocalDateTime expireAt = tokenExpireAt.get(token);
        return expireAt == null || LocalDateTime.now().isAfter(expireAt);
    }

    public UserSsoDTO tokenUser(String token) {
        Long userId = tokenToUserId.get(token);
        if (userId == null) {
            return null;
        }
        UserDTO user = users.get(userId);
        if (user == null) {
            return null;
        }
        UserSsoDTO sso = new UserSsoDTO();
        sso.setUserId(user.getId());
        sso.setUserName(user.getName());
        sso.setGender(user.getGender());
        sso.setBirthday(user.getBirthday());
        sso.setPhone(user.getPhone());
        sso.setEmail(user.getEmail());
        sso.setPicture(user.getPicture());
        sso.setIntro(user.getIntro());
        sso.setOrgId(user.getOrgId());
        return sso;
    }

    public void saveVerifyCode(String key, String code) {
        verifyCodes.put(key, code);
    }

    public boolean verifyCode(String key, String code) {
        return StringUtils.equals(verifyCodes.get(key), code);
    }

    public void addVisit(VisitDTO visitDTO) {
        visits.add(visitDTO);
    }

    public long visitTotal() {
        return visits.size();
    }

    public synchronized NotifyOutDTO createNotify(Long toUserId, Integer type, String message) {
        NotifyOutDTO notify = new NotifyOutDTO();
        LocalDateTime now = LocalDateTime.now();
        notify.setId(notifyIdGenerator.getAndIncrement());
        notify.setType(type);
        notify.setMessage(message);
        notify.setIsRead(false);
        notify.setProjectId(1);
        notify.setProjectName("ns-bbs");
        notify.setCreateUser(toUserId);
        notify.setCreateTime(now);
        notify.setUpdateTime(now);
        notifies.add(notify);
        return notify;
    }

    public boolean markRead(List<Integer> ids, Long userId) {
        Set<Integer> idSet = new HashSet<>(ids);
        notifies.stream()
                .filter(n -> Objects.equals(n.getCreateUser(), userId) && idSet.contains(n.getId()))
                .forEach(n -> {
                    n.setIsRead(true);
                    n.setUpdateTime(LocalDateTime.now());
                });
        return true;
    }

    public boolean haveRead(Long userId, Integer type) {
        return notifies.stream()
                .filter(n -> Objects.equals(n.getCreateUser(), userId))
                .filter(n -> type == null || Objects.equals(n.getType(), type))
                .noneMatch(n -> !Boolean.TRUE.equals(n.getIsRead()));
    }

    public int notReadCount(Long userId, Integer type) {
        return (int) notifies.stream()
                .filter(n -> Objects.equals(n.getCreateUser(), userId))
                .filter(n -> type == null || Objects.equals(n.getType(), type))
                .filter(n -> !Boolean.TRUE.equals(n.getIsRead()))
                .count();
    }

    public List<NotifyOutDTO> listNotify(NotifySearchDTO search, Long userId) {
        return notifies.stream()
                .filter(n -> Objects.equals(n.getCreateUser(), userId))
                .filter(n -> search == null || search.getType() == null || Objects.equals(n.getType(), search.getType()))
                .filter(n -> search == null || search.getIsRead() == null || Objects.equals(n.getIsRead(), search.getIsRead()))
                .sorted(Comparator.comparing(NotifyOutDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public void init() {
        if (!users.isEmpty()) {
            return;
        }
        UserDTO admin = new UserDTO();
        admin.setName("nacos");
        admin.setPassword(defaultPassword);
        admin.setEmail("nacos@local.dev");
        admin.setPhone("13800000000");
        admin.setIntro("默认本地联调账号");
        admin.setOrgId(1);
        admin.setState(Boolean.TRUE);
        UserDTO created = createUser(admin);
        createNotify(created.getId(), 1, "欢迎使用本地 ns-manage-auth 服务");
    }

    @PostConstruct
    public void bootstrap() {
        init();
    }

    private UserListDTO toUserListDTO(UserDTO user) {
        UserListDTO dto = new UserListDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setBirthday(user.getBirthday());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setPicture(user.getPicture());
        dto.setPosition(user.getPosition());
        dto.setCompany(user.getCompany());
        dto.setHomePage(user.getHomePage());
        dto.setIntro(user.getIntro());
        dto.setOrgId(user.getOrgId());
        dto.setState(user.getState());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        return dto;
    }

    private UserDTO cloneUser(UserDTO user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setSalt(user.getSalt());
        dto.setGender(user.getGender());
        dto.setBirthday(user.getBirthday());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setPicture(user.getPicture());
        dto.setPosition(user.getPosition());
        dto.setCompany(user.getCompany());
        dto.setHomePage(user.getHomePage());
        dto.setIntro(user.getIntro());
        dto.setOrgId(user.getOrgId());
        dto.setState(user.getState());
        dto.setAuthId(user.getAuthId());
        dto.setAuthSource(user.getAuthSource());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        return dto;
    }
}
