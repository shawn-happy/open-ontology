package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prism.common.aspect.AuditLog;
import com.prism.common.exception.BusinessException;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.CreateDeptDTO;
import com.prism.platform.dto.DeptTreeQuery;
import com.prism.platform.dto.UpdateDeptDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.exception.SystemErrorCode;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.service.DeptService;
import com.prism.platform.vo.DeptTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final SystemDeptMapper systemDeptMapper;
    private final SystemUserDeptMapper systemUserDeptMapper;

    @Override
    @AuditLog(module = "部门管理", operation = "新增部门", type = "CREATE")
    @Transactional
    public void create(CreateDeptDTO dto) {
        // 根节点不可作为操作的 parentId 校验
        if (dto.getParentId() == null) {
            dto.setParentId(0L);
        }

        // 部门编码全局唯一
        SystemDept byCode = systemDeptMapper.selectByCode(dto.getCode());
        if (byCode != null) {
            throw new BusinessException(SystemErrorCode.DEPT_CODE_EXISTS.getCode(),
                    SystemErrorCode.DEPT_CODE_EXISTS.getMessage());
        }

        // 同级部门名称唯一
        LambdaQueryWrapper<SystemDept> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(SystemDept::getParentId, dto.getParentId())
                .eq(SystemDept::getName, dto.getName());
        if (systemDeptMapper.selectCount(nameWrapper) > 0) {
            throw new BusinessException(SystemErrorCode.DEPT_NAME_SAME_LEVEL_EXISTS.getCode(),
                    SystemErrorCode.DEPT_NAME_SAME_LEVEL_EXISTS.getMessage());
        }

        // 计算层级路径和深度
        String path;
        int depth;
        if (dto.getParentId() == 0L) {
            path = "/";
            depth = 0;
        } else {
            SystemDept parent = systemDeptMapper.selectById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException("999904", "上级部门不存在");
            }
            path = parent.getPath() + parent.getId() + "/";
            depth = parent.getDepth() + 1;
        }

        SystemDept dept = new SystemDept();
        dept.setParentId(dto.getParentId());
        dept.setName(dto.getName());
        dept.setCode(dto.getCode());
        dept.setPath(path);
        dept.setDepth(depth);
        dept.setLeaderId(dto.getLeaderId());
        dept.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        dept.setStatus("ENABLED");
        systemDeptMapper.insert(dept);
    }

    @Override
    @AuditLog(module = "部门管理", operation = "编辑部门", type = "UPDATE")
    @Transactional
    public void update(UpdateDeptDTO dto) {
        SystemDept dept = systemDeptMapper.selectById(dto.getId());
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }

        // 编辑操作必填校验
        if (StringUtils.isBlank(dto.getName())) {
            throw new BusinessException("999900", "部门名称不能为空");
        }

        // 根节点不可修改
        if (SystemConstants.ROOT_DEPT_ID.equals(dto.getId())) {
            throw new BusinessException(SystemErrorCode.ROOT_NODE_READONLY.getCode(),
                    SystemErrorCode.ROOT_NODE_READONLY.getMessage());
        }

        // 同级部门名称唯一
        Long parentId = dto.getParentId() != null ? dto.getParentId() : dept.getParentId();
        LambdaQueryWrapper<SystemDept> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(SystemDept::getParentId, parentId)
                .eq(SystemDept::getName, dto.getName())
                .ne(SystemDept::getId, dto.getId());
        if (systemDeptMapper.selectCount(nameWrapper) > 0) {
            throw new BusinessException(SystemErrorCode.DEPT_NAME_SAME_LEVEL_EXISTS.getCode(),
                    SystemErrorCode.DEPT_NAME_SAME_LEVEL_EXISTS.getMessage());
        }

        // 上级不可为自身或下级
        if (dto.getParentId() != null && !dto.getParentId().equals(dept.getParentId())) {
            if (dto.getParentId().equals(dto.getId())) {
                throw new BusinessException(SystemErrorCode.PARENT_CANNOT_BE_SELF_OR_CHILD.getCode(),
                        SystemErrorCode.PARENT_CANNOT_BE_SELF_OR_CHILD.getMessage());
            }
            // 检查是否为下级
            List<SystemDept> children = systemDeptMapper.selectChildrenByPath(dept.getPath() + dept.getId() + "/");
            if (children.stream().anyMatch(c -> c.getId().equals(dto.getParentId()))) {
                throw new BusinessException(SystemErrorCode.PARENT_CANNOT_BE_SELF_OR_CHILD.getCode(),
                        SystemErrorCode.PARENT_CANNOT_BE_SELF_OR_CHILD.getMessage());
            }
        }

        SystemDept update = new SystemDept();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setParentId(dto.getParentId());
        update.setLeaderId(dto.getLeaderId());
        update.setSortOrder(dto.getSortOrder());

        // 如果 parentId 变了，需要更新 path 和 depth 以及子节点 path
        if (dto.getParentId() != null && !dto.getParentId().equals(dept.getParentId())) {
            String newPath;
            int newDepth;
            if (dto.getParentId() == 0L) {
                newPath = "/";
                newDepth = 0;
            } else {
                SystemDept parent = systemDeptMapper.selectById(dto.getParentId());
                if (parent == null) {
                    throw new BusinessException("999904", "上级部门不存在");
                }
                newPath = parent.getPath() + parent.getId() + "/";
                newDepth = parent.getDepth() + 1;
            }
            update.setPath(newPath);
            update.setDepth(newDepth);

            // 更新子节点 path
            updateChildrenPath(dept, newPath, newDepth);
        }

        systemDeptMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "部门管理", operation = "删除部门", type = "DELETE")
    @Transactional
    public void delete(Long id) {
        SystemDept dept = systemDeptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }

        // 根节点不可删除
        if (SystemConstants.ROOT_DEPT_ID.equals(id)) {
            throw new BusinessException(SystemErrorCode.ROOT_NODE_READONLY.getCode(),
                    "根节点不可删除");
        }

        // 校验无下级部门
        List<SystemDept> children = systemDeptMapper.selectByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new BusinessException("999904", "存在下级部门不可删除");
        }

        systemDeptMapper.deleteById(id);
    }

    @Override
    public List<DeptTreeVO> tree(DeptTreeQuery query) {
        LambdaQueryWrapper<SystemDept> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(SystemDept::getStatus, query.getStatus());
        }
        wrapper.orderByAsc(SystemDept::getSortOrder);
        List<SystemDept> allDepts = systemDeptMapper.selectList(wrapper);
        return buildTree(allDepts, 0L);
    }

    @Override
    @AuditLog(module = "部门管理", operation = "启用/停用部门", type = "ENABLE")
    @Transactional
    public void enableDisable(Long id, String status) {
        SystemDept dept = systemDeptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }

        if (SystemConstants.ROOT_DEPT_ID.equals(id)) {
            throw new BusinessException(SystemErrorCode.ROOT_NODE_READONLY.getCode(),
                    "根节点不可操作");
        }

        // 停用时将用户迁移至上级部门
        if ("DISABLED".equals(status)) {
            List<SystemUserDept> userDepts = systemUserDeptMapper.selectByDeptId(id);
            for (SystemUserDept ud : userDepts) {
                if (dept.getParentId() != null && dept.getParentId() > 0) {
                    SystemUserDept update = new SystemUserDept();
                    update.setId(ud.getId());
                    update.setDeptId(dept.getParentId());
                    systemUserDeptMapper.updateById(update);
                } else {
                    systemUserDeptMapper.deleteByUserId(ud.getUserId());
                }
            }
        }

        SystemDept update = new SystemDept();
        update.setId(id);
        update.setStatus(status);
        systemDeptMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "部门管理", operation = "分配用户到部门", type = "UPDATE")
    @Transactional
    public void assignUsers(Long deptId, List<Long> userIds) {
        SystemDept dept = systemDeptMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }

        if (userIds != null) {
            for (Long userId : userIds) {
                // 先删除旧的部门关联
                systemUserDeptMapper.deleteByUserId(userId);
                // 新增关联
                SystemUserDept ud = new SystemUserDept();
                ud.setUserId(userId);
                ud.setDeptId(deptId);
                systemUserDeptMapper.insert(ud);
            }
        }
    }

    @Override
    @AuditLog(module = "部门管理", operation = "设置负责人", type = "UPDATE")
    public void setLeader(Long deptId, Long leaderId) {
        SystemDept dept = systemDeptMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }
        SystemDept update = new SystemDept();
        update.setId(deptId);
        update.setLeaderId(leaderId);
        systemDeptMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "部门管理", operation = "部门转移", type = "UPDATE")
    @Transactional
    public void transfer(Long sourceDeptId, Long targetDeptId) {
        SystemDept source = systemDeptMapper.selectById(sourceDeptId);
        if (source == null) {
            throw new BusinessException("999904", "源部门不存在");
        }
        SystemDept target = systemDeptMapper.selectById(targetDeptId);
        if (target == null) {
            throw new BusinessException("999904", "目标部门不存在");
        }

        // 将源部门的用户迁移到目标部门
        List<SystemUserDept> userDepts = systemUserDeptMapper.selectByDeptId(sourceDeptId);
        for (SystemUserDept ud : userDepts) {
            SystemUserDept update = new SystemUserDept();
            update.setId(ud.getId());
            update.setDeptId(targetDeptId);
            systemUserDeptMapper.updateById(update);
        }

        // 将源部门的子部门移到目标部门下
        List<SystemDept> children = systemDeptMapper.selectByParentId(sourceDeptId);
        for (SystemDept child : children) {
            String newPath = target.getPath() + target.getId() + "/";
            int newDepth = target.getDepth() + 1;

            SystemDept childUpdate = new SystemDept();
            childUpdate.setId(child.getId());
            childUpdate.setParentId(targetDeptId);
            childUpdate.setPath(newPath);
            childUpdate.setDepth(newDepth);
            systemDeptMapper.updateById(childUpdate);

            updateChildrenPath(child, newPath, newDepth);
        }
    }

    @Override
    @AuditLog(module = "部门管理", operation = "更新排序", type = "UPDATE")
    public void updateSort(Long id, Integer sortOrder) {
        SystemDept dept = systemDeptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }
        SystemDept update = new SystemDept();
        update.setId(id);
        update.setSortOrder(sortOrder);
        systemDeptMapper.updateById(update);
    }

    private void updateChildrenPath(SystemDept parent, String newParentPath, int newParentDepth) {
        List<SystemDept> children = systemDeptMapper.selectChildrenByPath(
                parent.getPath() + parent.getId() + "/");
        for (SystemDept child : children) {
            String oldPath = child.getPath();
            String prefix = parent.getPath() + parent.getId() + "/";
            String suffix = oldPath.substring(prefix.length());
            String updatedPath = newParentPath + parent.getId() + "/" + suffix;
            int depthDiff = newParentDepth - parent.getDepth();

            SystemDept update = new SystemDept();
            update.setId(child.getId());
            update.setPath(updatedPath);
            update.setDepth(child.getDepth() + depthDiff);
            systemDeptMapper.updateById(update);
        }
    }

    private List<DeptTreeVO> buildTree(List<SystemDept> depts, Long parentId) {
        Map<Long, List<SystemDept>> grouped = depts.stream()
                .collect(Collectors.groupingBy(SystemDept::getParentId));
        return buildChildren(grouped, parentId);
    }

    private List<DeptTreeVO> buildChildren(Map<Long, List<SystemDept>> grouped, Long parentId) {
        List<SystemDept> children = grouped.getOrDefault(parentId, List.of());
        return children.stream()
                .map(dept -> {
                    // 查询成员数
                    List<SystemUserDept> userDepts = systemUserDeptMapper.selectByDeptId(dept.getId());
                    int memberCount = userDepts.size();

                    // 查询负责人名称
                    String leaderName = null;
                    if (dept.getLeaderId() != null) {
                        // 简单处理：不额外查询用户表，返回 null 由前端补充
                        leaderName = null;
                    }

                    return DeptTreeVO.builder()
                            .id(dept.getId())
                            .parentId(dept.getParentId())
                            .name(dept.getName())
                            .code(dept.getCode())
                            .depth(dept.getDepth())
                            .leaderName(leaderName)
                            .memberCount(memberCount)
                            .sortOrder(dept.getSortOrder())
                            .status(dept.getStatus())
                            .children(buildChildren(grouped, dept.getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
