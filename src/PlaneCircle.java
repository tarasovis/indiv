/**
 * Модель круга: центр и радиус.
 */
public class PlaneCircle {
    private final PlanePoint centerPoint;
    private final double radiusValue;

    /**
     * Создает круг и валидирует неотрицательность радиуса.
     */
    public PlaneCircle(PlanePoint centerPoint, double radiusValue) {
        validateRadius(radiusValue);
        this.centerPoint = centerPoint;
        this.radiusValue = radiusValue;
    }

    private void validateRadius(double radiusValue) {
        if (radiusValue < 0) {
            throw new IllegalArgumentException("Радиус круга не может быть отрицательным.");
        }
    }

    public PlanePoint getCenterPoint() {
        return centerPoint;
    }

    public double getRadiusValue() {
        return radiusValue;
    }
}
