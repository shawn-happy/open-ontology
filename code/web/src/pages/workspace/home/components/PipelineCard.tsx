import { Card, Empty, Table, Tag, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import type { HomePipelineVO } from '@/services/workspace';
import styles from '../index.less';

interface PipelineCardProps {
  pipelines: HomePipelineVO[];
  loading?: boolean;
}

/** Pipeline 状态徽标（与原型一致：运行中蓝 / 成功绿 / 重试中橙 / 排队灰） */
const STATUS_META: Record<string, { color: string; text: string }> = {
  RUNNING: { color: 'blue', text: '运行中' },
  SUCCESS: { color: 'green', text: '成功' },
  RETRYING: { color: 'orange', text: '重试中' },
  QUEUED: { color: 'default', text: '排队' },
  FAILED: { color: 'red', text: '失败' },
};

const columns: ColumnsType<HomePipelineVO> = [
  {
    title: 'Pipeline',
    dataIndex: 'name',
    render: (v: string) => <b>{v}</b>,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    render: (v: string) => {
      const meta = STATUS_META[v] || { color: 'default', text: v };
      return <Tag color={meta.color}>{meta.text}</Tag>;
    },
  },
  {
    title: '进度',
    dataIndex: 'progress',
    width: 80,
    render: (v: number | null) => (v === null || v === undefined ? '—' : `${v}%`),
  },
  {
    title: '开始时间',
    dataIndex: 'startedAt',
    width: 160,
  },
];

/**
 * 进行中的 Pipeline 区块（本期后端返回空数组 → 空态）
 */
export default function PipelineCard({ pipelines, loading }: PipelineCardProps) {
  return (
    <Card
      size="small"
      title="进行中的 Pipeline"
      className={styles.halfCard}
      extra={<a onClick={() => message.info('「数据管理」模块建设中，敬请期待')}>全部 →</a>}
    >
      <Table<HomePipelineVO>
        rowKey="id"
        size="small"
        columns={columns}
        dataSource={pipelines}
        loading={loading}
        pagination={false}
        locale={{ emptyText: <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="暂无进行中的 Pipeline" /> }}
      />
    </Card>
  );
}
