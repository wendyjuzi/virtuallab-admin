// 监控相关API
import request from '@/utils/request'

// 获取系统状态
export function getSystemStatus() {
  return request({
    url: '/monitor/status',
    method: 'get'
  })
}

// 获取实时操作
export function getRealtimeOperations(params) {
  return request({
    url: '/monitor/realtime-operations',
    method: 'get',
    params
  })
}

// 获取权限统计
export function getPermissionStats() {
  return request({
    url: '/monitor/permission-stats',
    method: 'get'
  })
}

// 获取操作日志
export function getOperationLogs(params) {
  return request({
    url: '/monitor/operation-logs',
    method: 'get',
    params
  })
}

// 获取日志详情
export function getOperationLogDetail(id) {
  return request({
    url: `/monitor/operations/logs/${id}`,
    method: 'get'
  })
}

// 获取用户行为分析 - 这是前端需要的核心方法
export function getUserBehaviorAnalysis() {
  return request({
    url: '/monitor/user-behavior',
    method: 'get'
  })
}

// 获取系统性能监控
export function getSystemPerformance() {
  return request({
    url: '/monitor/performance',
    method: 'get'
  })
} 