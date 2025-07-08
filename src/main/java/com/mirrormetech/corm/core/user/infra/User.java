package com.mirrormetech.corm.core.user.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

/**
 * 用户实体
 */
@Data
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("nick_name")
    private String nickName;

    @TableField(value = "password", select = false)
    private String password;

    @TableField("email")
    private String email;

    @TableField(value = "password_hash", select = false)
    private String passwordHash;

    @TableField(value = "salt", select = false)
    private String salt;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("phone_num")
    private String phoneNum;

    private Integer status;

    @TableField("user_type")
    private Integer userType;

    @TableField("uid")
    private String uid;

    @TableField("bio")
    private String bio;

    @TableField("background_url")
    private String backgroundUrl;

    @TableField("collaboration_url")
    private String collaborationUrl;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField(exist = false)
    private String role;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Integer> codes;

    /**
     * userId是否为当前用户id
     * @param userId 前端传入的userId参数
     * @return userId == user. Id
     */
    public boolean isCurrent(Long userId){
        return this.id.equals(userId);
    }

    public User(Long id, String username, String passwordHash, String salt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public static User create(String username, String password) {
        String salt = generateSalt();
        return new User(null, username, hashPassword(password, salt), salt);
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    // 改进后的密码哈希方法（示例）
    private static String hashPassword(String password, String salt) {
        int iterations = 10000;
        int keyLength = 256;
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    Hex.decodeHex(salt),
                    iterations,
                    keyLength
            );
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Hex.encodeHexString(skf.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    public boolean verifyPassword(String password) {
        return passwordHash.equals(hashPassword(password, salt));
    }
}
