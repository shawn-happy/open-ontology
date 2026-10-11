import { PageContainer } from '@ant-design/pro-components';
import { Card, Typography } from 'antd';

const { Title, Paragraph } = Typography;

export default function Welcome() {
  return (
    <PageContainer>
      <Card>
        <Typography>
          <Title level={2}>欢迎使用 Prism 本体语义平台</Title>
          <Paragraph>
            Prism 是面向企业的数据语义化中台产品，以本体（Ontology）为核心，
            将分散在多源异构系统中的数据映射为可理解的业务对象，
            提供数据查询、规则校验、动作写回、函数计算、工作流编排与开放 API 能力。
          </Paragraph>
          <Paragraph>
            <strong>技术栈：</strong> React 18 + Ant Design 5 + UmiJS 4 + Java 21 + Spring Boot 3.5
          </Paragraph>
        </Typography>
      </Card>
    </PageContainer>
  );
}
