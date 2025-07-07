package com.edu.virtuallab.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceMapper {
    List<Map<String, Object>> countResourceUsage();
    List<Integer> countResourceRatingHistogram();
    List<Integer> countResourceDownloadTrend();
    List<Map<String, Object>> countResourceTags();
} 