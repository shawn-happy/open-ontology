import { Card, message } from 'antd';
import { RightOutlined } from '@ant-design/icons';
import styles from '../index.less';

/** 空间内功能模块快捷入口（与原型 workspace/home.html 一致；本期各模块建设中，点击占位提示） */
const SPACE_MODULES = [
  {
    icon: '◈',
    title: '本体资产管理',
    desc: '类 / 属性 / 关系 / 规则 / 动作 / 函数',
    items: ['本体图谱', '关联关系（含 RDF）', '共享属性 · 枚举类 · 值约束规则', '动作 · 函数'],
    colorClass: 'c1',
  },
  {
    icon: '⛃',
    title: '数据管理',
    desc: '数据源 / 数据集 / Pipeline / 探查',
    items: ['数据源管理', '数据集管理', 'Pipeline 管理', '数据探查'],
    colorClass: 'c2',
  },
  {
    icon: '⛨',
    title: '安全管理',
    desc: '标记 / 数据权限 / 加密 / 脱敏',
    items: ['标记管理', '数据权限', '加密 · 脱敏'],
    colorClass: 'c3',
  },
  {
    icon: '❖',
    title: '应用管理',
    desc: 'API Key / Workflow / Skills',
    items: ['API Key', 'Workflow 编排', 'Skills'],
    colorClass: 'c4',
  },
  {
    icon: '☰',
    title: '审计管理',
    desc: '日志 / 资产统计 / 运行与调用',
    items: ['审计日志', '本体资产统计', 'Pipeline 运行 · 应用调用'],
    colorClass: 'c5',
  },
];

/**
 * 功能模块卡片 ×5（色条：紫 / 青 / 红 / 绿 / 橙）
 */
export default function ModuleCards() {
  return (
    <div className={styles.modGrid}>
      {SPACE_MODULES.map((m) => (
        <Card
          key={m.title}
          size="small"
          hoverable
          className={`${styles.modCard} ${styles[m.colorClass]}`}
          onClick={() => message.info(`「${m.title}」模块建设中，敬请期待`)}
        >
          <div className={styles.modIcon}>{m.icon}</div>
          <h3 className={styles.modTitle}>
            {m.title} <RightOutlined className={styles.modArrow} />
          </h3>
          <div className={styles.modDesc}>{m.desc}</div>
          <ul className={styles.modItems}>
            {m.items.map((x) => (
              <li key={x}>{x}</li>
            ))}
          </ul>
        </Card>
      ))}
    </div>
  );
}
