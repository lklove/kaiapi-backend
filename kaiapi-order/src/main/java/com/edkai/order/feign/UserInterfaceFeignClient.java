package com.edkai.order.feign;

import com.edkai.common.BaseResponse;
import com.edkai.common.model.to.AddUserInterfaceTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lk
 */
@FeignClient(name = "userInterfaceFeignClient",url = "http://localhost:7529/api/userInterfaceInfo/")
public interface UserInterfaceFeignClient {
    /**
     * 添加用户与接口之间的关系
     * @param addUserInterfaceTo
     * @return
     */
    @PostMapping("/feign/add")
     BaseResponse<Boolean> addUserInterfaceByFeign(@RequestBody AddUserInterfaceTo addUserInterfaceTo);
}

