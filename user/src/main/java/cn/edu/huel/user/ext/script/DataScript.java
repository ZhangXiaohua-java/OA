package cn.edu.huel.user.ext.script;

import cn.edu.huel.user.domain.BusinessDepartment;
import cn.edu.huel.user.domain.DpParam;
import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.domain.ServiceSupportRange;
import cn.edu.huel.user.service.IBusinessDepartmentService;
import cn.edu.huel.user.service.RegionService;
import cn.edu.huel.user.service.ServiceSupportRangeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Slf4j
@Component
public class DataScript {

	@Resource
	private IBusinessDepartmentService businessDepartmentService;

	@Resource
	private RegionService regionService;

	@Resource
	private CloseableHttpClient httpClient;

	@Resource
	private ServiceSupportRangeService supportRangeService;

	/* 省一级列表 */
	private List<Region> provinces = null;

	/* 市一级列表 */
	private List<Region> cities = null;

	/* 3级区域,区县一级 */
	private List<Region> level3;

	String url = "https://www.deppon.com/gwapi/queryService/eco/query/branch/queryDeptNet";

	private String serviceUrl = "https://www.deppon.com/gwapi/queryService/eco/query/range/queryAddressImagine";


	@PostConstruct
	public void initial() {
		provinces = regionService.loadAllProvinces();
		//cities = provinces.stream()
		//		.map(region -> regionService.loadAllChildRegions(region.getRegionId()))
		//		.flatMap(e -> e.stream())
		//		.distinct()
		//		.collect(Collectors.toList());
		//level3 = cities.stream()
		//		.map(region -> regionService.loadAllChildRegions(region.getRegionId()))
		//		.flatMap(e -> e.stream())
		//		.distinct()
		//		.collect(Collectors.toList());
	}

	public void demo() {
		//List<BusinessDepartment> businessDepartments = new ArrayList<>();
		//DpParam param = null;
		ArrayList<Param> params = new ArrayList<>();
		for (Region province : provinces) {
			List<Region> cities = regionService.loadAllChildRegions(province.getRegionId());
			for (Region city : cities) {
				// 区县
				List<Region> areas = regionService.loadAllChildRegions(city.getRegionId());
				for (Region area : areas) {
					//outlets(province, city, area);
					Param param = new Param();
					param.setCityCode(area.getRegionCode());
					param.setCountyCode(area.getRegionCode());
					try {
						List<ServiceSupportRange> list = supportRange(param, area);
						supportRangeService.saveBatch(list);
					} catch (Exception e) {
						log.error("执行出错了...");
						e.printStackTrace();
					}
				}
			}
		}
	}


	public List<ServiceSupportRange> supportRange(Param param, Region region) {
		try {
			StringEntity entity = new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
			HttpPost post = new HttpPost(serviceUrl);
			post.setEntity(entity);
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				log.info("获取到了空数据...");
				return null;
			}
			String res = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
			String level1 = JSON.parseObject(res)
					.get("result")
					.toString();
			String level2 = JSON.parseObject(level1)
					.get("deliveryList").toString();
			JSONArray array = JSON.parseArray(level2);
			for (Object o : array) {
				String s = o.toString();
				Object desc = JSON.parseObject(s).get("rangeTypeDesc");
				if (Objects.nonNull(desc)) {
					System.out.println("----------");
					String resStr = desc.toString();
					List<ServiceSupportRange> supportRanges = JSON.parseObject(resStr, new TypeReference<List<ServiceSupportRange>>() {
					});
					// TODO 为每一项设置regionCode...
					supportRanges.stream()
							.forEach(e -> {
								e.setCountCode(region.getRegionCode());
								e.setCountName(region.getRegionName());
								e.setRangeTypeCode("1");
							});
					return supportRanges;
				}
			}

		} catch (Exception e) {
			log.error("出错了...");
		}
		return null;
	}

	private void outlets(Region province, Region city, Region area) {
		DpParam param;
		List<BusinessDepartment> businessDepartments;
		param = new DpParam();
		param.setLevel("4");
		param.setProvinceCode(province.getRegionCode());
		param.setProvinceName(province.getRegionName());
		param.setCityCode(city.getRegionCode());
		param.setCityName(city.getRegionName());
		param.setCountyCode(area.getRegionCode());
		param.setCountyName(area.getRegionName());
		businessDepartments = getData(param);
		log.info("获取到的结果:{}", businessDepartments);
		try {
			if (!Objects.isNull(businessDepartments) && !ObjectUtils.isEmpty(businessDepartments)) {
				businessDepartmentService.saveBatch(businessDepartments);
				businessDepartments.clear();
			} else {
				log.warn("获取到了空数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<BusinessDepartment> getData(DpParam param) {
		try {
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
			post.setEntity(entity);
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
			result = JSON.parseObject(result).get("result").toString();
			result = JSON.parseObject(result).get("netResponses").toString();
			return JSON.parseObject(result, new TypeReference<List<BusinessDepartment>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Data

	public static class Param {

		private String countyCode;

		private String cityCode;

	}


}
