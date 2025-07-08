package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.service.ResourceShareService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import com.edu.virtuallab.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.edu.virtuallab.resource.dto.ShareResponse;
import com.edu.virtuallab.resource.dto.ShareRequest;

@RestController
@RequestMapping("/resource-share")
public class ResourceShareController {
    @Autowired
    private ResourceShareService resourceShareService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // 生成分享链接
    @OperationLogRecord(operation = "CREATE_RESOURCE_SHARE", module = "RESOURCE", action = "创建资源分享", description = "用户创建资源分享", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/generate")
    public CommonResult<String> generateShareLink(@RequestParam Long resourceId,
                                                  @RequestParam String sharedBy,
                                                  @RequestParam(required = false) Long expireMinutes) {
        try {
            String link = resourceShareService.generateShareLink(resourceId, sharedBy, expireMinutes);
            return CommonResult.success(link, "生成分享链接成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("生成分享链接失败: " + e.getMessage());
        }
    }

    // 通过用户名分享资源
    @OperationLogRecord(operation = "SHARE_RESOURCE_BY_USERNAME", module = "RESOURCE", action = "通过用户名分享资源", description = "用户通过用户名分享资源", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/share-by-username")
    public CommonResult<ShareResponse> shareResourceByUsername(@RequestBody ShareRequest request) {
        try {
            ShareResponse response = resourceShareService.shareResourceByUsername(
                request.getResourceId(),
                request.getSharedBy(),
                request.getTargetUsername(),
                request.getPermission()
            );
            return CommonResult.success(response, "分享成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("分享失败: " + e.getMessage());
        }
    }

    // 查询某资源的所有分享
    @GetMapping("/list")
    public CommonResult<List<ResourceShare>> getShares(@RequestParam Long resourceId) {
        try {
            List<ResourceShare> shares = resourceShareService.getSharesByResourceId(resourceId);
            return CommonResult.success(shares, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询分享列表失败: " + e.getMessage());
        }
    }

    // 获取用户分享给其他人的资源列表
    @GetMapping("/shared-by-me")
    public CommonResult<List<ResourceShare>> getSharesBySharedBy(@RequestParam String sharedBy) {
        try {
            List<ResourceShare> shares = resourceShareService.getSharesBySharedBy(sharedBy);
            return CommonResult.success(shares, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询分享列表失败: " + e.getMessage());
        }
    }

    // 获取分享给用户的资源列表
    @GetMapping("/shared-with-me")
    public CommonResult<List<ResourceShare>> getSharesBySharedWith(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String sharedWith = jwtUtil.getUsernameFromToken(token);
            List<ResourceShare> shares = resourceShareService.getSharesBySharedWith(sharedWith);
            return CommonResult.success(shares, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询被分享列表失败: " + e.getMessage());
        }
    }

    // 检查用户是否有资源的访问权限
    @GetMapping("/check-access")
    public CommonResult<Boolean> checkResourceAccess(@RequestParam Long resourceId,
                                                     @RequestParam(defaultValue = "read") String permission,
                                                     HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String username = jwtUtil.getUsernameFromToken(token);
            boolean hasAccess = resourceShareService.hasResourceAccess(resourceId, username, permission);
            return CommonResult.success(hasAccess, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("检查访问权限失败: " + e.getMessage());
        }
    }

    // 取消分享
    @OperationLogRecord(operation = "DELETE_RESOURCE_SHARE", module = "RESOURCE", action = "删除资源分享", description = "用户删除资源分享", permissionCode = "RESOURCE_MANAGE")
    @DeleteMapping("/cancel")
    public CommonResult<String> cancelShare(@RequestParam Long id, @RequestParam String sharedBy) {
        try {
            resourceShareService.cancelShare(id, sharedBy);
            return CommonResult.success("取消分享成功", "取消分享成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("取消分享失败: " + e.getMessage());
        }
    }

    // 撤销分享
    @OperationLogRecord(operation = "REVOKE_RESOURCE_SHARE", module = "RESOURCE", action = "撤销资源分享", description = "用户撤销资源分享", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/revoke/{shareId}")
    public CommonResult<Boolean> revokeShare(@PathVariable Long shareId, @RequestParam String sharedBy) {
        try {
            boolean result = resourceShareService.revokeShare(shareId, sharedBy);
            return CommonResult.success(result, "撤销分享成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("撤销分享失败: " + e.getMessage());
        }
    }

    // 更新分享权限
    @OperationLogRecord(operation = "UPDATE_SHARE_PERMISSION", module = "RESOURCE", action = "更新分享权限", description = "用户更新分享权限", permissionCode = "RESOURCE_MANAGE")
    @PutMapping("/update-permission/{shareId}")
    public CommonResult<Boolean> updateSharePermission(@PathVariable Long shareId,
                                                       @RequestParam String permission,
                                                       @RequestParam String sharedBy) {
        try {
            boolean result = resourceShareService.updateSharePermission(shareId, permission, sharedBy);
            return CommonResult.success(result, "更新权限成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("更新权限失败: " + e.getMessage());
        }
    }
    
    /**
     * 从请求头中提取JWT Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new BusinessException("未找到有效的认证Token");
    }
} 