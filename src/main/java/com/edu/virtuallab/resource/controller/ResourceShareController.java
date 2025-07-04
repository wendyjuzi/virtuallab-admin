package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.service.ResourceShareService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
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
    @OperationLogRecord(operation = "CREATE_RESOURCE_SHARE", module = "RESOURCE", action = "创建资源分享", description = "用户创建资源分享", permissionCode = "RESOURCE_MANAGE")
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
    @OperationLogRecord(operation = "DELETE_RESOURCE_SHARE", module = "RESOURCE", action = "删除资源分享", description = "用户删除资源分享", permissionCode = "RESOURCE_MANAGE")
    @DeleteMapping("/cancel")
    public CommonResult<String> cancelShare(@RequestParam Long id, Principal principal) {
        String sharedBy = principal.getName();
        resourceShareService.cancelShare(id, sharedBy);
        return CommonResult.success("取消分享成功", "取消分享成功");
    }
} 