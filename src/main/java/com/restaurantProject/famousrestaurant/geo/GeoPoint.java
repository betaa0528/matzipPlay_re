package com.restaurantProject.famousrestaurant.geo;

public class GeoPoint {
    double x;

    double y;

    double z;


    /**

     *

     */

    public GeoPoint() {

        super();

    }


    /**

     * @param x

     * @param y

     */

    public GeoPoint(double x, double y) {

        super();

        this.x = x;

        this.y = y;

        this.z = 0;

    }


    /**

     * @param x

     * @param y

     * @param y

     */

    public GeoPoint(double x, double y, double z) {

        super();

        this.x = x;

        this.y = y;

        this.z = 0;

    }


    public double getX() {

        return x;

    }



    public double getY() {

        return y;

    }

    @Override
    public String toString() {
        return "GeoTransPoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
