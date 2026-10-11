import { useState } from 'react';
import { Modal, Upload, Button, message, Table, Typography, Alert } from 'antd';
import { InboxOutlined, DownloadOutlined } from '@ant-design/icons';
import { importUsers, downloadTemplate } from '@/services/user';
import type { ImportResultVO } from '@/services/user';

const { Dragger } = Upload;
const { Text } = Typography;

interface ImportUsersModalProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function ImportUsersModal({ visible, onCancel, onSuccess }: ImportUsersModalProps) {
  const [fileList, setFileList] = useState<any[]>([]);
  const [importResult, setImportResult] = useState<ImportResultVO | null>(null);
  const [loading, setLoading] = useState(false);

  const handleDownloadTemplate = async () => {
    try {
      const res = await downloadTemplate();
      const blob = new Blob([res.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = '用户导入模板.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    } catch {
      message.error('下载模板失败');
    }
  };

  const handleImport = async () => {
    if (fileList.length === 0) {
      message.warning('请选择文件');
      return;
    }
    const file = fileList[0];
    if (file.size > 10 * 1024 * 1024) {
      message.error('文件大小不能超过10MB');
      return;
    }
    setLoading(true);
    try {
      const res = await importUsers(file.originFileObj || file);
      if (res.code === '000000') {
        setImportResult(res.data);
        if (res.data.failCount === 0) {
          message.success(`导入成功，共导入 ${res.data.successCount} 条数据`);
          onSuccess();
        } else {
          message.warning(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
        }
      } else {
        message.error(res.msg);
      }
    } catch (error: any) {
      message.error(error?.message || '导入失败');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setFileList([]);
    setImportResult(null);
    onCancel();
  };

  const failColumns = [
    { title: '行号', dataIndex: 'rowNum', width: 80 },
    { title: '用户名', dataIndex: 'username', width: 120 },
    { title: '失败原因', dataIndex: 'reason' },
  ];

  return (
    <Modal
      title="批量导入用户"
      open={visible}
      onCancel={handleClose}
      onOk={importResult ? handleClose : handleImport}
      okText={importResult ? '关闭' : '开始导入'}
      confirmLoading={loading}
      destroyOnHidden
      width={600}
    >
      {!importResult ? (
        <>
          <div style={{ marginBottom: 16 }}>
            <Button
              type="link"
              icon={<DownloadOutlined />}
              onClick={handleDownloadTemplate}
              style={{ padding: 0 }}
            >
              下载导入模板
            </Button>
          </div>

          <Dragger
            accept=".xlsx"
            maxCount={1}
            fileList={fileList}
            beforeUpload={() => false}
            onChange={({ fileList: newFileList }) => setFileList(newFileList)}
          >
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">点击或拖拽文件到此区域上传</p>
            <p className="ant-upload-hint">仅支持 .xlsx 格式，文件大小不超过 10MB</p>
          </Dragger>
        </>
      ) : (
        <>
          <Alert
            type={importResult.failCount > 0 ? 'warning' : 'success'}
            message={`导入完成：总计 ${importResult.totalCount} 条，成功 ${importResult.successCount} 条，失败 ${importResult.failCount} 条`}
            style={{ marginBottom: 16 }}
          />
          {importResult.failDetails && importResult.failDetails.length > 0 && (
            <Table
              dataSource={importResult.failDetails}
              columns={failColumns}
              size="small"
              pagination={false}
              scroll={{ y: 200 }}
              rowKey="rowNum"
            />
          )}
        </>
      )}
    </Modal>
  );
}
