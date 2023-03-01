package cn.edu.huel.user.base.utils;

import cn.edu.huel.user.domain.PositionParam;

/**
 * 计算两个经纬度之间的距离
 *
 * @author
 */
public class CalulateTwoLanLon {
	private static final double EARTH_RADIUS = 6378.137;//地球半径,单位千米

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * @param lat1 第一个纬度
	 * @param lng1 第一个经度
	 * @param lat2 第二个纬度
	 * @param lng2 第二个经度
	 * @return 两个经纬度的距离
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
				Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000) / 1000;
		return s;
	}


	/**
	 * @param start 起始地址
	 * @param end   终点
	 * @return 两地之间的距离
	 */
	public static double getDistance(PositionParam start, PositionParam end) {
		return getDistance(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
	}


	public static void main(String[] args) {
		PositionParam lk = new PositionParam(114.81961,34.8235);
		PositionParam nb = new PositionParam(121.55681,29.88776);
		double distance = getDistance(lk, nb);
		System.out.println(distance);
	}


}