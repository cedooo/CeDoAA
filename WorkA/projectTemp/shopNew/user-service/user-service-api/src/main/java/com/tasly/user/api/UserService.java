package com.tasly.user.api;

import com.tasly.user.dto.UserDTO;
import com.tasly.user.dto.UsernameAndPasswordDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author chendong
 * @date 2020-01-05
 */
@FeignClient(value = "user-service")
public interface UserService {

    /**
     * 通过用户名和密码(POST)获取用户数据传输对象
     * @param usernameAndPasswordDTO 用户账号密码
     * @return 用户数据传输对象
     */
    @RequestMapping(method = RequestMethod.POST)
    UserDTO getByUsernameAndPasswordDTO(UsernameAndPasswordDTO usernameAndPasswordDTO);


    /**
     * (GET)获取用户传输对象
     * @param uid  用户id
     * @return 用户数据传输对象
     */
    @RequestMapping(value = "/{uid}",method = RequestMethod.GET)
    UserDTO get(@PathVariable("uid") Long uid);

}
