package goods_board.controller;

public class ConvertToDistance {
	/**
	 * 두 지점간의 거리 계산
	 * 
	 * @param lat 지점 1 위도 자신
	 * @param lng 지점 1 경도 자신
	 * @param boardLat 지점 2 위도 물품
	 * @param boardLng 지점 2 경도 물품
	 * @param unit 거리 표출단위
	 * @return
	 */
	public String getStrDistance(double lat, double lng, double boardLat, double boardLng) {
		double theta = lng - boardLng;
		double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(boardLat))
				+ Math.cos(deg2rad(lat)) * Math.cos(deg2rad(boardLat)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1609.344;		//meter
		if(dist >= 1000) {
			dist /= 1000;
			return dist+"km";
		}
		else {
			return dist+"m";
		}
	}
	
	public double getDistance(double lat, double lng, double boardLat, double boardLng) {
		double theta = lng - boardLng;
		double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(boardLat))
				+ Math.cos(deg2rad(lat)) * Math.cos(deg2rad(boardLat)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1609.344;		//meter
		return dist;
	}
	

	// This function converts decimal degrees to radians
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
