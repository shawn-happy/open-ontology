import React from 'react';
import { Button, Result } from 'antd';
import { history } from '@umijs/max';

interface ErrorBoundaryState {
  error: Error | null;
}

/**
 * 全局渲染异常边界：捕获子组件树的 render 崩溃，展示友好降级页，
 * 避免白屏。通过 app.tsx 的 rootContainer 挂载到应用根部。
 */
export default class ErrorBoundary extends React.Component<
  { children: React.ReactNode },
  ErrorBoundaryState
> {
  state: ErrorBoundaryState = { error: null };

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error('[全局渲染异常]', error, errorInfo?.componentStack);
  }

  render() {
    const { error } = this.state;
    if (error) {
      return (
        <Result
          status="error"
          title="页面出现异常"
          subTitle={error.message || '渲染时发生未知错误'}
          extra={[
            <Button key="reload" type="primary" onClick={() => window.location.reload()}>
              刷新页面
            </Button>,
            <Button
              key="home"
              onClick={() => {
                this.setState({ error: null });
                history.push('/welcome');
              }}
            >
              返回首页
            </Button>,
          ]}
        />
      );
    }
    return this.props.children;
  }
}
