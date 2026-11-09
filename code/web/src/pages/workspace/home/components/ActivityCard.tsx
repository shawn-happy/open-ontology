import { Card, Empty, Timeline } from 'antd';
import type { HomeActivityVO } from '@/services/workspace';
import styles from '../index.less';

interface ActivityCardProps {
  activities: HomeActivityVO[];
  loading?: boolean;
}

/** 动态类型 → Timeline 节点颜色（warn 类动态橙色高亮，与原型一致） */
const TYPE_COLORS: Record<string, string> = {
  PUBLISH: 'blue',
  UPDATE: 'blue',
  WARN: 'orange',
  CREATE: 'green',
};

/**
 * 近期动态区块（本期后端返回空数组 → 空态）
 */
export default function ActivityCard({ activities, loading }: ActivityCardProps) {
  return (
    <Card size="small" title="近期动态" className={styles.halfCard} loading={loading}>
      {activities.length === 0 ? (
        <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="暂无近期动态" />
      ) : (
        <Timeline
          items={activities.map((a) => ({
            color: TYPE_COLORS[a.type] || 'blue',
            children: (
              <div>
                <div className={styles.tlTime}>{a.occurredAt}</div>
                <div className={styles.tlTitle}>{a.content}</div>
                {a.operatorName && <div className={styles.tlDesc}>{a.operatorName}</div>}
              </div>
            ),
          }))}
        />
      )}
    </Card>
  );
}
