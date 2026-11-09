package com.prism.platform.service;

import com.prism.platform.vo.HomeActivityVO;
import com.prism.platform.vo.HomePipelineVO;
import com.prism.platform.vo.WorkspaceHomeStatsVO;

import java.util.List;

/**
 * 空间首页服务
 */
public interface WorkspaceHomeService {

    /**
     * 首页统计卡片（memberCount 真实统计；资产/数据/API 为扩展点，未注册模块返回 0）
     */
    WorkspaceHomeStatsVO stats(String workspaceCode);

    /**
     * 进行中 Pipeline（本期空数组，数据模块上线后接入）
     */
    List<HomePipelineVO> pipelines(String workspaceCode);

    /**
     * 近期动态（本期空数组，审计模块接入后填充）
     */
    List<HomeActivityVO> activities(String workspaceCode);
}
