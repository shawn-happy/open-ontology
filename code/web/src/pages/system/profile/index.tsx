import { useState, useEffect, useCallback } from 'react';
import { Card, Button, Avatar, Space, Tag, message, Upload } from 'antd';
import {
  EditOutlined,
  LockOutlined,
  CameraOutlined,
} from '@ant-design/icons';
import { useSearchParams } from '@umijs/max';
import { getProfile, uploadAvatar } from '@/services/profile';
import type { UserProfileVO } from '@/services/profile';
import EditProfileModal from './components/EditProfile';
import ChangePasswordModal from './components/ChangePassword';
import styles from './index.less';

export default function ProfilePage() {
  const [searchParams] = useSearchParams();
  const [profile, setProfile] = useState<UserProfileVO | null>(null);
  const [loading, setLoading] = useState(false);
  const [editVisible, setEditVisible] = useState(false);
  const [passwordVisible, setPasswordVisible] = useState(() => {
    return searchParams.get('action') === 'changePassword';
  });

  const loadProfile = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getProfile();
      if (res.code === '000000') {
        setProfile(res.data);
      }
    } catch (error) {
      console.error('Failed to load profile:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const handleUploadAvatar = async (file: File) => {
    // 校验文件类型和大小
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('仅支持 JPG/PNG 格式');
      return false;
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error('文件大小不能超过 2MB');
      return false;
    }

    try {
      const res = await uploadAvatar(file);
      if (res.code === '000000') {
        message.success('头像上传成功');
        loadProfile();
      } else {
        message.error(res.msg);
      }
    } catch (error: any) {
      message.error(error?.message || '上传失败');
    }
    return false;
  };

  return (
    <div className={styles.profilePage}>
      <Card loading={loading} className={styles.profileCard}>
        <div className={styles.avatarSection}>
          <Upload
            showUploadList={false}
            beforeUpload={handleUploadAvatar}
            accept=".jpg,.jpeg,.png"
          >
            <div className={styles.avatarLarge}>
              {profile?.avatar ? (
                <Avatar src={profile.avatar} size={80} />
              ) : (
                <span className={styles.avatarTextLarge}>
                  {profile?.name?.charAt(0) || '?'}
                </span>
              )}
              <div className={styles.avatarOverlay}>
                <CameraOutlined /> 更换
              </div>
            </div>
          </Upload>
          <div>
            <h2 style={{ margin: 0 }}>{profile?.name}</h2>
            <p style={{ color: '#666', margin: '4px 0 8px' }}>@{profile?.username}</p>
            <Space>
              {(profile?.roleNames || []).map((name) => (
                <Tag key={name} color="blue">
                  {name}
                </Tag>
              ))}
            </Space>
          </div>
        </div>

        <div className={styles.infoGrid}>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>用户名</div>
            <div className={styles.infoValue}>{profile?.username || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>姓名</div>
            <div className={styles.infoValue}>{profile?.name || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>手机号</div>
            <div className={styles.infoValue}>{profile?.phone || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>邮箱</div>
            <div className={styles.infoValue}>{profile?.email || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>所属部门</div>
            <div className={styles.infoValue}>{profile?.deptName || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>最近登录</div>
            <div className={styles.infoValue}>{profile?.lastLoginAt || '-'}</div>
          </div>
          <div className={styles.infoItem}>
            <div className={styles.infoLabel}>注册时间</div>
            <div className={styles.infoValue}>{profile?.createdAt || '-'}</div>
          </div>
        </div>

        <div style={{ marginTop: 24, display: 'flex', gap: 12 }}>
          <Button type="primary" icon={<EditOutlined />} onClick={() => setEditVisible(true)}>
            修改个人信息
          </Button>
          <Button icon={<LockOutlined />} onClick={() => setPasswordVisible(true)}>
            修改密码
          </Button>
        </div>
      </Card>

      <EditProfileModal
        visible={editVisible}
        profile={profile}
        onCancel={() => setEditVisible(false)}
        onSuccess={() => {
          setEditVisible(false);
          loadProfile();
        }}
      />

      <ChangePasswordModal
        visible={passwordVisible}
        onCancel={() => setPasswordVisible(false)}
        onSuccess={() => {
          setPasswordVisible(false);
        }}
      />
    </div>
  );
}
