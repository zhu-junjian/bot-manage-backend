package com.mirrormetech.corm.core.user.domain.dto;

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

/**
 * 用户实体
 * 包含关注关系的用户实体
 */
@Data
@TableName("tb_user")
public class UserFollowDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码（明文）
     */
    @TableField("password")
    private String password;

    /**
     * 密码哈希
     */
    @TableField("password_hash")
    private String passwordHash;

    /**
     * 随机生成的盐值
     */
    @TableField("salt")
    private String salt;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 头像URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 用户手机号
     */
    @TableField("phone_num")
    private String phoneNum;

    /**
     * 用户状态 0-正常 1-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 用户类型 0-普通用户 9-官方用户
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 用户唯一ID（对外展示）
     */
    @TableField("uid")
    private String uid;

    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;

    /**
     * 背景图URL
     */
    @TableField("background_url")
    private String backgroundUrl;

    /**
     * 联名边框/头像URL
     */
    @TableField("collaboration_url")
    private String collaborationUrl;

    private Integer relation;

    /**
     * 生日日期
     */
    //@TableField("birthday")
    private LocalDate birthday;

    public UserFollowDTO(Long id, String username, String passwordHash, String salt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public static UserFollowDTO create(String username, String password) {
        String salt = generateSalt();
        return new UserFollowDTO(null, username, hashPassword(password, salt), salt);
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
