package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
public class SysUser extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
	private Long userId;

	/**
	 * 部门ID
	 */
	@Excel(name = "部门编号", type = Type.IMPORT)
	private Long deptId;

	/**
	 * 用户账号
	 */
	@Excel(name = "登录名称")
	private String userName;

	/**
	 * 用户昵称
	 */
	@Excel(name = "用户名称")
	private String nickName;

	/**
	 * 用户邮箱
	 */
	@Excel(name = "用户邮箱")
	private String email;

	/**
	 * 手机号码
	 */
	@Excel(name = "手机号码")
	private String phonenumber;

	/**
	 * 用户性别
	 */
	@Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
	private String sex;

	/**
	 * 用户头像
	 */
	private String avatar;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 帐号状态（0正常 1停用）
	 */
	@Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
	private String status;

	/**
	 * 删除标志（0代表存在 2代表删除）
	 */
	private String delFlag;

	/**
	 * 最后登录IP
	 */
	@Excel(name = "最后登录IP", type = Type.EXPORT)
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	@Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
	private Date loginDate;

	/**
	 * 部门对象
	 */
	@Excels({
			@Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
			@Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
	})
	private SysDept dept;

	/**
	 * 角色对象
	 */
	private List<SysRole> roles;

	/**
	 * 角色组
	 */
	private Long[] roleIds;

	/**
	 * 岗位组
	 */
	private Long[] postIds;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/* 员工所属营业网点id */
	private Integer outletsId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SysUser sysUser = (SysUser) o;

		if (userId != null ? !userId.equals(sysUser.userId) : sysUser.userId != null) return false;
		if (deptId != null ? !deptId.equals(sysUser.deptId) : sysUser.deptId != null) return false;
		if (userName != null ? !userName.equals(sysUser.userName) : sysUser.userName != null) return false;
		if (nickName != null ? !nickName.equals(sysUser.nickName) : sysUser.nickName != null) return false;
		if (email != null ? !email.equals(sysUser.email) : sysUser.email != null) return false;
		if (phonenumber != null ? !phonenumber.equals(sysUser.phonenumber) : sysUser.phonenumber != null) return false;
		if (sex != null ? !sex.equals(sysUser.sex) : sysUser.sex != null) return false;
		if (avatar != null ? !avatar.equals(sysUser.avatar) : sysUser.avatar != null) return false;
		if (password != null ? !password.equals(sysUser.password) : sysUser.password != null) return false;
		if (status != null ? !status.equals(sysUser.status) : sysUser.status != null) return false;
		if (delFlag != null ? !delFlag.equals(sysUser.delFlag) : sysUser.delFlag != null) return false;
		if (loginIp != null ? !loginIp.equals(sysUser.loginIp) : sysUser.loginIp != null) return false;
		if (loginDate != null ? !loginDate.equals(sysUser.loginDate) : sysUser.loginDate != null) return false;
		if (dept != null ? !dept.equals(sysUser.dept) : sysUser.dept != null) return false;
		if (roles != null ? !roles.equals(sysUser.roles) : sysUser.roles != null) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(roleIds, sysUser.roleIds)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(postIds, sysUser.postIds)) return false;
		if (roleId != null ? !roleId.equals(sysUser.roleId) : sysUser.roleId != null) return false;
		return outletsId != null ? outletsId.equals(sysUser.outletsId) : sysUser.outletsId == null;
	}

	@Override
	public int hashCode() {
		int result = userId != null ? userId.hashCode() : 0;
		result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
		result = 31 * result + (userName != null ? userName.hashCode() : 0);
		result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phonenumber != null ? phonenumber.hashCode() : 0);
		result = 31 * result + (sex != null ? sex.hashCode() : 0);
		result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (delFlag != null ? delFlag.hashCode() : 0);
		result = 31 * result + (loginIp != null ? loginIp.hashCode() : 0);
		result = 31 * result + (loginDate != null ? loginDate.hashCode() : 0);
		result = 31 * result + (dept != null ? dept.hashCode() : 0);
		result = 31 * result + (roles != null ? roles.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(roleIds);
		result = 31 * result + Arrays.hashCode(postIds);
		result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
		result = 31 * result + (outletsId != null ? outletsId.hashCode() : 0);
		return result;
	}

	public SysUser() {

	}


	public Integer getOutlets_id() {
		return outletsId;
	}

	public void setOutletsId(Integer outletsId) {
		this.outletsId = outletsId;
	}

	public SysUser(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isAdmin() {
		return isAdmin(this.userId);
	}

	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Xss(message = "用户昵称不能包含脚本字符")
	@Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Xss(message = "用户账号不能包含脚本字符")
	@NotBlank(message = "用户账号不能为空")
	@Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public SysDept getDept() {
		return dept;
	}

	public void setDept(SysDept dept) {
		this.dept = dept;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Long[] getPostIds() {
		return postIds;
	}

	public void setPostIds(Long[] postIds) {
		this.postIds = postIds;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("userId", getUserId())
				.append("deptId", getDeptId())
				.append("userName", getUserName())
				.append("nickName", getNickName())
				.append("email", getEmail())
				.append("phonenumber", getPhonenumber())
				.append("sex", getSex())
				.append("avatar", getAvatar())
				.append("password", getPassword())
				.append("status", getStatus())
				.append("delFlag", getDelFlag())
				.append("loginIp", getLoginIp())
				.append("loginDate", getLoginDate())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("remark", getRemark())
				.append("dept", getDept())
				.append("outletsId", getOutlets_id())
				.toString();
	}
}
