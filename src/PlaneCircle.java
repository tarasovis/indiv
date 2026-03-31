public class PlaneCircle {
    private final PlanePoint centerPoint;
    private final double radiusValue;

    public PlaneCircle(PlanePoint centerPoint, double radiusValue) {
        this.centerPoint = centerPoint;
        this.radiusValue = Math.abs(radiusValue);
    }

    public PlanePoint getCenterPoint() {
        return centerPoint;
    }

    public double getRadiusValue() {
        return radiusValue;
    }
}
