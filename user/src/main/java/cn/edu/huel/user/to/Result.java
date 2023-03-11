/**
 * Copyright 2023 json.cn
 */
package cn.edu.huel.user.to;

import lombok.Data;

/**
 * Auto-generated: 2023-03-09 22:24:50
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Result {

	/* 脱敏后的身份证号 */
	private String IdCardNo;

	/*脱敏后的姓名*/
	private String Name;

	/* 检验结果,
		-1,身份证和姓名不一致 -2,公安库中无此身份证记录 -3,公安身份证库中没有此号码的照片-4 照片参数不合格
		 -5 照片相片体积过大 -6,请检查图片编码 -7,照片相片体积过小 1,系统分析为同一人 ，2,系统分析可能为同一人
		 3, 系统分析为不是同人 4,没检测到人脸 5,疑似非活体 6,出现多张脸 7,身份证和姓名一致，官方人脸比对失败
	* */
	private Integer Validate_Result;

	/*相似度
	相似度 1~100 (当validate_result>0时，本值才有效(相似度>=45 为同一人； 40<=相似度<45 不确定为同一人； 相似度<40 确定为不同人))
	* */
	private Double Similarity;


}