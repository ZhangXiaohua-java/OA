package cn.edu.huel.user.base.utils;

import cn.edu.huel.user.domain.PositionParam;
import cn.edu.huel.user.service.IAreaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Component
public class PostCostCounter {


	@Resource
	private IAreaService areaService;


	private Integer rate = 6000;


	/**
	 * 计算运费,重量全部向上取整
	 *
	 * @param weight 物品重量
	 * @param origin 始发地
	 * @param dest   目的地
	 * @param volume 体积
	 *               基准首重12元,超出一公斤的部分每公斤加价5元
	 * @return
	 */
	public Integer countCost(Integer weight, String origin, String dest, Integer volume) {
		// TODO 泡重比计算,体积重量和实际重量取较大值
		int w = volume / rate;
		int ceil = ceil(weight);
		int postWeight = Math.max(ceil, w);
		int basePrice = postWeight <= 1 ? 12 : 12 + (postWeight - 1) * 5;
		// TODO 如果两地之间的距离超过1000公里加价100元
		PositionParam start = areaService.queryLngAndLatByZipCode(origin);
		PositionParam end = areaService.queryLngAndLatByZipCode(dest);
		Double distance = CalulateTwoLanLon.getDistance(start, end);
		if (distance.compareTo(1000.0) >= 0) {
			basePrice += 100;
		}
		return basePrice;
	}

	public static int ceil(double num) {
		return Double.valueOf(Math.ceil(num)).intValue();
	}


	public static void main(String[] args) {
		int num = 100 * 100 * 100;
		num /= 6000;
		System.out.println(num);
	}

	private static void demo() {
		int postWeight = ceil(2.2);
		int basePrice = postWeight <= 1 ? 12 : 12 + (postWeight - 1) * 5;
		System.out.println(basePrice);
	}


}
