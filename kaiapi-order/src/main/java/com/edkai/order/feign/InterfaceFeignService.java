package com.edkai.order.feign;

import com.edkai.common.BaseResponse;
import com.edkai.common.model.vo.InterfaceInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 远程访问接口信息
 * @author lk
 */
@FeignClient(name = "interfaceClient",url = "http://localhost:7529/api/interfaceInfo/")
public interface InterfaceFeignService {
    /**
     * 根据id回去接口信息
     * @param id
     * @return
     */
    @GetMapping("/feign/get")
    BaseResponse<InterfaceInfoVo> feignGetInterfaceInfoById(@RequestParam("id") long id);
}
