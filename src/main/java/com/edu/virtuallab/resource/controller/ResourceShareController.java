package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.service.ResourceShareService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/resource-share")
public class ResourceShareController {
    @Autowired
    private ResourceShareService resourceShareService;

    // 生成分享链接
    @PostMapping("/generate")
    public CommonResult<String> generateShareLink(@RequestParam Long resourceId,
                                                  @RequestParam(required = false) Long expireMinutes,
                                                  Principal principal) {
        String sharedBy = principal.getName();
        String link = resourceShareService.generateShareLink(resourceId, sharedBy, expireMinutes);
        return CommonResult.success(link, "生成分享链接成功");
    }

    // 查询某资源的所有分享
    @GetMapping("/list")
    public CommonResult<List<ResourceShare>> getShares(@RequestParam Long resourceId) {
        return CommonResult.success(resourceShareService.getSharesByResourceId(resourceId), "查询成功");
    }

    // 取消分享
    @DeleteMapping("/cancel")
    public CommonResult<String> cancelShare(@RequestParam Long id, Principal principal) {
        String sharedBy = principal.getName();
        resourceShareService.cancelShare(id, sharedBy);
        return CommonResult.success("取消分享成功", "取消分享成功");
    }
} 