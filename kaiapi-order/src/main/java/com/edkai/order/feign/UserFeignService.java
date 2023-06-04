package com.edkai.order.feign;

import com.edkai.common.BaseResponse;
import com.edkai.common.model.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userClient",url = "http://localhost:7529/api/user")
public interface UserFeignService {
    /**
     * 获取当前登录用户
     * @param
     * @return
     */
    @GetMapping("/get/login")
    BaseResponse<UserVO> getLoginUser();
}
