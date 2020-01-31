package com.tasly.user.dto;

import lombok.Data;

/**
 *
 * @author dulei
 * @date 18/1/8
 */
@Data
public class UserDTO {
    private Long id;
    private String name;
    private Integer age;

    private String username;
    private String password;
}
