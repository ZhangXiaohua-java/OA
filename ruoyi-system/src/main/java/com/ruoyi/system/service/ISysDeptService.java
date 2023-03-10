package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService {
	/**
	 * 查询部门管理数据
	 *
	 * @param dept 部门信息
	 * @return 部门信息集合
	 */
	public List<SysDept> selectDeptList(SysDept dept);

	/**
	 * 查询部门树结构信息
	 *
	 * @param dept 部门信息
	 * @return 部门树信息集合
	 */
	public List<TreeSelect> selectDeptTreeList(SysDept dept);

	/**
	 * 构建前端所需要树结构
	 *
	 * @param depts 部门列表
	 * @return 树结构列表
	 */
	public List<SysDept> buildDeptTree(List<SysDept> depts);

	/**
	 * 构建前端所需要下拉树结构
	 *
	 * @param depts 部门列表
	 * @return 下拉树结构列表
	 */
	public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

	/**
	 * 根据角色ID查询部门树信息
	 *
	 * @param roleId 角色ID
	 * @return 选中部门列表
	 */
	public List<Long> selectDeptListByRoleId(Long roleId);

	/**
	 * 根据部门ID查询信息
	 *
	 * @param deptId 部门ID
	 * @return 部门信息
	 */
	public SysDept selectDeptById(Long deptId);

	/**
	 * 根据ID查询所有子部门（正常状态）
	 *
	 * @param deptId 部门ID
	 * @return 子部门数
	 */
	public int selectNormalChildrenDeptById(Long deptId);

	/**
	 * 是否存在部门子节点
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	public boolean hasChildByDeptId(Long deptId);

	/**
	 * 查询部门是否存在用户
	 *
	 * @param deptId 部门ID
	 * @return 结果 true 存在 false 不存在
	 */
	public boolean checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	public String checkDeptNameUnique(SysDept dept);

	/**
	 * 校验部门是否有数据权限
	 *
	 * @param deptId 部门id
	 */
	public void checkDeptDataScope(Long deptId);

	/**
	 * 新增保存部门信息
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	public int insertDept(SysDept dept);

	/**
	 * 修改保存部门信息
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	public int updateDept(SysDept dept);

	/**
	 * 删除部门管理信息
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	public int deleteDeptById(Long deptId);

	/**
	 * @param countCode   区县代码
	 * @param unifiedCode 部门唯一识别号
	 * @return 查询营业部信息
	 */
	SysDept queryDeptByUnifiedCode(String countCode, String unifiedCode);

	/**
	 * 这个方法和上面的selectById区别开,上面的查询不知道为什么会导致卡死
	 *
	 * @param deptId 部门id
	 * @return 根据id查询部门信息
	 */
	SysDept selectById(Long deptId);


	/**
	 * @param unifiedCode 部门的唯一识别号
	 * @param countCode   区号
	 * @return 查询营业部门
	 */
	SysDept selectDeptByUnifiedCode(String countCode, String unifiedCode);


}
