import { Card, Col, Row } from 'antd';
import { ArrowDownOutlined, ArrowUpOutlined } from '@ant-design/icons';
import type { WorkspaceHomeStatsVO } from '@/services/workspace';
import styles from '../index.less';

interface StatCardsProps {
  /** 统计数据；接口失败时为 null，各卡片显示 "—"（PRD §4.5 区块降级） */
  stats: WorkspaceHomeStatsVO | null;
}

/** 环比趋势：null 不渲染箭头；正数绿色▲ / 负数红色▼ */
function Trend({ value, percent }: { value: number | null | undefined; percent?: boolean }) {
  if (value === null || value === undefined) {
    return null;
  }
  const up = value >= 0;
  return (
    <span className={up ? styles.deltaUp : styles.deltaDown}>
      {up ? <ArrowUpOutlined /> : <ArrowDownOutlined />}{' '}
      {percent ? `${Math.abs(value)}%` : `${up ? '+' : ''}${value}`}
    </span>
  );
}

/**
 * 首页统计卡片 ×4：空间成员（真实统计）/ 本体资产 / 数据集·Pipeline / 今日 API 调用
 * 本期资产类指标后端返回 0，环比统一 null（不渲染 ▲▼）
 */
export default function StatCards({ stats }: StatCardsProps) {
  const fmt = (v: number | null | undefined) =>
    stats === null || v === null || v === undefined ? '—' : v.toLocaleString();

  return (
    <Row gutter={16} className={styles.statRow}>
      <Col span={6}>
        <Card size="small">
          <div className={styles.statLabel}>空间成员</div>
          <div className={styles.statValue}>{fmt(stats?.memberCount)}</div>
          <Trend value={stats?.memberCountTrend} />
        </Card>
      </Col>
      <Col span={6}>
        <Card size="small">
          <div className={styles.statLabel}>本体资产</div>
          <div className={styles.statValue}>{fmt(stats?.ontologyAssetCount)}</div>
          <Trend value={stats?.ontologyAssetTrend} />
        </Card>
      </Col>
      <Col span={6}>
        <Card size="small">
          <div className={styles.statLabel}>数据集 / Pipeline</div>
          <div className={styles.statValue}>
            {stats === null ? '—' : `${stats.datasetCount ?? 0} / ${stats.pipelineCount ?? 0}`}
          </div>
        </Card>
      </Col>
      <Col span={6}>
        <Card size="small">
          <div className={styles.statLabel}>今日 API 调用</div>
          <div className={styles.statValue}>{fmt(stats?.apiCallToday)}</div>
          <Trend value={stats?.apiCallTrend} percent />
        </Card>
      </Col>
    </Row>
  );
}
