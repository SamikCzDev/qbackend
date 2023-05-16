package me.michalkunc.qbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserInfoDTO   {
    private int status;
    private int id;
    private String userName;
    private int level;

    public UserInfoDTO(int status) {
        this.status = status;
    }

    public UserInfoDTO(int status, int id, String userName, int level) {
        this.status = status;
        this.id = id;
        this.userName = userName;
        this.level = level;
    }
}
