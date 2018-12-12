package com.example.a15862.mytraveldiary.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class User {
    private String username;
    private String password;
    private String userid;
    private String phone;
    private String email;
    private String displayName;
    private int scoreCountUser = 0;
    private float totalScoreUser = 0;
    private List<String> following;
    private int like = 0;
    private String avatar;

    public User() {
    }

    public User(String username, String password, String userid, String phone, String email, List<String> following, String photouri) {
        this.username = username;
        this.password = password;
        this.userid = userid;
        this.phone = phone;
        this.email = email;
        this.following = following;
        this.avatar = photouri;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        User comparedUser = (User) obj;
        return username.equals(comparedUser.getUsername());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (userid != null) {
            map.put("userid", userid);
        }
        if (password != null) {
            map.put("password", password);
        }
        if (username != null) {
            map.put("username", username);
        }
        if (phone != null) {
            map.put("phone", phone);
        }
        if (email != null) {
            map.put("email", email);
        }
        if (displayName != null) {
            map.put("displayName", displayName);
        }
        if (following != null) {
            map.put("following", following);
        }
        return map;
    }
}
