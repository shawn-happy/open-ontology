import { useEffect, useState } from 'react';
import { history, useModel } from '@umijs/max';
import { Button, Row, Col } from 'antd';
import { HomeOutlined } from '@ant-design/icons';
import type {
  HomeActivityVO,
  HomePipelineVO,
  WorkspaceHomeStatsVO,
} from '@/services/workspace';
import {
  getHomeActivities,
  getHomePipelines,
  getHomeStats,
} from '@/services/workspace';
import StatCards from './components/StatCards';
import ModuleCards from './components/ModuleCards';
import PipelineCard from './components/PipelineCard';
import ActivityCard from './components/ActivityCard';
import styles from './index.less';

/**
 * 空间首页：统计卡片（memberCount 真实，资产类本期为 0）+ 功能模块快捷入口
 * + 进行中 Pipeline（本期空态）+ 近期动态（本期空态）。
 * 三个接口 Promise.allSettled 独立降级（PRD FR-5）：任一失败仅该区块显示 "—"/空态。
 */
export default function WorkspaceHomePage() {
  const { workspaceCode, workspaceName } = useModel('workspace');
  const [stats, setStats] = useState<WorkspaceHomeStatsVO | null>(null);
  const [pipelines, setPipelines] = useState<HomePipelineVO[]>([]);
  const [activities, setActivities] = useState<HomeActivityVO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!workspaceCode) {
      return;
    }
    let cancelled = false;
    (async () => {
      const [statsRes, pipelinesRes, activitiesRes] = await Promise.allSettled([
        getHomeStats(workspaceCode),
        getHomePipelines(workspaceCode),
        getHomeActivities(workspaceCode),
      ]);
      if (cancelled) {
        return;
      }
      // 异常已由全局请求层统一 toast，此处仅做区块降级
      if (statsRes.status === 'fulfilled' && statsRes.value.code === '000000') {
        setStats(statsRes.value.data);
      }
      if (pipelinesRes.status === 'fulfilled' && pipelinesRes.value.code === '000000') {
        setPipelines(pipelinesRes.value.data || []);
      }
      if (activitiesRes.status === 'fulfilled' && activitiesRes.value.code === '000000') {
        setActivities(activitiesRes.value.data || []);
      }
      setLoading(false);
    })();
    return () => {
      cancelled = true;
    };
  }, [workspaceCode]);

  return (
    <div className={styles.homePage}>
      {/* 页头 */}
      <div className={styles.pageHeader}>
        <div>
          <h2 className={styles.pageTitle}>{workspaceName || workspaceCode} · 空间首页</h2>
          <div className={styles.pageDesc}>
            当前空间内本体资产、数据、应用与安全策略的统一概览。可通过侧边栏进入成员管理，或返回平台首页管理其他空间。
          </div>
        </div>
        <Button icon={<HomeOutlined />} onClick={() => history.push('/')}>
          返回平台首页
        </Button>
      </div>

      {/* 统计卡片 ×4 */}
      <StatCards stats={stats} />

      {/* 功能模块快捷入口 ×5 */}
      <ModuleCards />

      {/* 进行中 Pipeline + 近期动态 */}
      <Row gutter={16} className={styles.bottomRow}>
        <Col span={14}>
          <PipelineCard pipelines={pipelines} loading={loading} />
        </Col>
        <Col span={10}>
          <ActivityCard activities={activities} loading={loading} />
        </Col>
      </Row>
    </div>
  );
}
