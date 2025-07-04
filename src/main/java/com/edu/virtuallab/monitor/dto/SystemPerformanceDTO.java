package com.edu.virtuallab.monitor.dto;

import java.util.List;

public class SystemPerformanceDTO {
    private List<String> timePoints;
    private List<Double> cpuUsage;
    private List<Double> memoryUsage;
    private List<Double> diskUsage;
    private String redisConnectedClients;
    private String redisUsedMemory;
    private String redisUsedMemoryHuman;
    private String redisTotalCommandsProcessed;
    private String redisUptimeInSeconds;
    private String redisKeyspaceHits;
    private String redisKeyspaceMisses;
    private String redisTotalKeys;

    public List<String> getTimePoints() { return timePoints; }
    public void setTimePoints(List<String> timePoints) { this.timePoints = timePoints; }
    public List<Double> getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(List<Double> cpuUsage) { this.cpuUsage = cpuUsage; }
    public List<Double> getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(List<Double> memoryUsage) { this.memoryUsage = memoryUsage; }
    public List<Double> getDiskUsage() { return diskUsage; }
    public void setDiskUsage(List<Double> diskUsage) { this.diskUsage = diskUsage; }
    public String getRedisConnectedClients() { return redisConnectedClients; }
    public void setRedisConnectedClients(String redisConnectedClients) { this.redisConnectedClients = redisConnectedClients; }
    public String getRedisUsedMemory() { return redisUsedMemory; }
    public void setRedisUsedMemory(String redisUsedMemory) { this.redisUsedMemory = redisUsedMemory; }
    public String getRedisUsedMemoryHuman() { return redisUsedMemoryHuman; }
    public void setRedisUsedMemoryHuman(String redisUsedMemoryHuman) { this.redisUsedMemoryHuman = redisUsedMemoryHuman; }
    public String getRedisTotalCommandsProcessed() { return redisTotalCommandsProcessed; }
    public void setRedisTotalCommandsProcessed(String redisTotalCommandsProcessed) { this.redisTotalCommandsProcessed = redisTotalCommandsProcessed; }
    public String getRedisUptimeInSeconds() { return redisUptimeInSeconds; }
    public void setRedisUptimeInSeconds(String redisUptimeInSeconds) { this.redisUptimeInSeconds = redisUptimeInSeconds; }
    public String getRedisKeyspaceHits() { return redisKeyspaceHits; }
    public void setRedisKeyspaceHits(String redisKeyspaceHits) { this.redisKeyspaceHits = redisKeyspaceHits; }
    public String getRedisKeyspaceMisses() { return redisKeyspaceMisses; }
    public void setRedisKeyspaceMisses(String redisKeyspaceMisses) { this.redisKeyspaceMisses = redisKeyspaceMisses; }
    public String getRedisTotalKeys() { return redisTotalKeys; }
    public void setRedisTotalKeys(String redisTotalKeys) { this.redisTotalKeys = redisTotalKeys; }
} 