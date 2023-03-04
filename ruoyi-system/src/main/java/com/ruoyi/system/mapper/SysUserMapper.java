package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
public interface SysUserMapper {
	/**
	 * 根据条件分页查询用户列表
	 *
	 * @param sysUser 用户信息
	 * @return 用户信息集合信息
	 */
	public List<SysUser> selectUserList(SysUser sysUser);

	/**
	 * 根据条件分页查询已配用户角色列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	public List<SysUser> selectAllocatedList(SysUser user);

	/**
	 * 根据条件分页查询未分配用户角色列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	public List<SysUser> selectUnallocatedList(SysUser user);

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	public SysUser selectUserByUserName(String userName);

	/**
	 * 通过用户ID查询用户
	 *
	 * @param userId 用户ID
	 * @return 用户对象信息
	 */
	public SysUser selectUserById(Long userId);

	/**
	 * 新增用户信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	public int insertUser(SysUser user);

	/**
	 * 修改用户信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	public int updateUser(SysUser user);

	/**
	 * 修改用户头像
	 *
	 * @param userName 用户名
	 * @param avatar   头像地址
	 * @return 结果
	 */
	public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

	/**
	 * 重置用户密码
	 *
	 * @param userName 用户名
	 * @param password 密码
	 * @return 结果
	 */
	public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

	/**
	 * 通过用户ID删除用户
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	public int deleteUserById(Long userId);

	/**
	 * 批量删除用户信息
	 *
	 * @param userIds 需要删除的用户ID
	 * @return 结果
	 */
	public int deleteUserByIds(Long[] userIds);

	/**
	 * 校验用户名称是否唯一
	 *
	 * @param userName 用户名称
	 * @return 结果
	 */
	public SysUser checkUserNameUnique(String userName);

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param phonenumber 手机号码
	 * @return 结果
	 */
	public SysUser checkPhoneUnique(String phonenumber);

	/**
	 * 校验email是否唯一
	 *
	 * @param email 用户邮箱
	 * @return 结果
	 */
	public SysUser checkEmailUnique(String email);

	/**
	 * @param deptId 部门id
	 * @return 该部门的员工人数
	 */
	Long getEmployeeCountByDeptId(@Param("deptId") Long deptId);

	/**
	 * @param deptId 部门id
	 * @return 根据部门id查询该部门下的所有员工信息
	 */
	List<SysUser> selectAllEmployeesByDeptId(@Param("deptId") Long deptId);


	/**
	 * @param deptId 部门id
	 * @param start  limit开始
	 * @param end    limit结束
	 * @return 查询指定个数的员工信息
	 */
	List<SysUser> selectRandomEmployeesByDeptId(@Param("deptId") Long deptId, @Param("start") int start, @Param("end") int end);

	/**
	 * @param deptId     部门id
	 * @param employeeId 员工id
	 * @return 填充过的员工信息
	 */
	SysUser selectUserByDeptIdAndId(@Param("deptId") Long deptId, @Param("employeeId") Long employeeId);


}
