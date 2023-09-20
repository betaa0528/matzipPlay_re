package com.restaurantProject.famousrestaurant.geo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class geoTest {

    @Test
    public void geoTransTest(){
        GeoPoint oKA = new GeoPoint(375206174, 1270298430);

        GeoPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);


        Double lat = oGeo.getY() * 1E6;

        Double lng = oGeo.getX() * 1E6;

//        GeoPoint oLatLng = new GeoPoint(lat.intValue(), lng.intValue());
//        System.out.println(oGeo.getX() + " , " + oGeo.getY());
//        GeoPoint pt1 = new GeoPoint(126.9035425 , 37.5169933 );
//        GeoPoint pt2= new GeoPoint(126.8863995 , 37.5269193);
        GeoPoint pt1= new GeoPoint(197228.183, 452706.9723);
        GeoPoint tm_geo = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, pt1);
        System.out.println(tm_geo);

//        GeoPoint katec_pt = new GeoPoint(1270298430, 375206174);
//        GeoPoint wgs_pt = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, katec_pt);
//        System.out.println("X : " + wgs_pt.getX() + ", Y : " + wgs_pt.getY());
//        GeoPoint in_pt = new GeoPoint(127.0639755, 37.6576359);
//        System.out.println("geo in : xGeo="  + in_pt.getX() + ", yGeo=" + in_pt.getY());
//        GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
//        System.out.println("tm : xTM=" + tm_pt.getX() + ", yTM=" + tm_pt.getY());
//        GeoPoint katec_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.KATEC, tm_pt);
//        System.out.println("katec : xKATEC=" + katec_pt.getX() + ", yKATEC=" + katec_pt.getY());
//        GeoPoint out_pt = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, katec_pt);
//        System.out.println("geo out : xGeo=" + out_pt.getX() + ", yGeo=" + out_pt.getY());
//        GeoPoint in2_pt = new GeoPoint(128., 38.);
//        System.out.println("geo distance between (127,38) and (128,38) =" + GeoTrans.getDistancebyGeo(in_pt, in2_pt) + "km");
//        double distancebyGeo = GeoTrans.getDistancebyGeo(pt1, pt2);
//        System.out.println(distancebyGeo);
    }
}
