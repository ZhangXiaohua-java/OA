package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
public class SysDept extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 父部门ID
	 */
	private Long parentId;

	/**
	 * 祖级列表
	 */
	private String ancestors;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 显示顺序
	 */
	private Integer orderNum;

	/**
	 * 负责人
	 */
	private String leader;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 部门状态:0正常,1停用
	 */
	private String status;

	/**
	 * 删除标志（0代表存在 2代表删除）
	 */
	private String delFlag;

	/**
	 * 父部门名称
	 */
	private String parentName;


	/* 该部门所在的区县代码,因为一个部门可能会有许多分部,如一个营业部,会有开封营业部,也会有兰考营业部,
	值如410225的形式,不再使用邮编了... */
	private String countCode;

	/* 唯一识别码,只对营业部生效,用来唯一标识一个营业部,因为存在多个营业部,所以使用这么一个代码来和营业网点建立映射关系 */
	private String unifiedCode;

	/**
	 * 子部门
	 */
	private List<SysDept> children = new ArrayList<SysDept>();

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAncestors() {
		return ancestors;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors;
	}

	@NotBlank(message = "部门名称不能为空")
	@Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<SysDept> getChildren() {
		return children;
	}

	public void setChildren(List<SysDept> children) {
		this.children = children;
	}


	public String getCountCode() {
		return countCode;
	}

	public void setCountCode(String countCode) {
		this.countCode = countCode;
	}

	public String getUnifiedCode() {
		return unifiedCode;
	}

	public void setUnifiedCode(String unifiedCode) {
		this.unifiedCode = unifiedCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("deptId", getDeptId())
				.append("parentId", getParentId())
				.append("ancestors", getAncestors())
				.append("deptName", getDeptName())
				.append("orderNum", getOrderNum())
				.append("leader", getLeader())
				.append("phone", getPhone())
				.append("email", getEmail())
				.append("status", getStatus())
				.append("delFlag", getDelFlag())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.toString();
	}


}
