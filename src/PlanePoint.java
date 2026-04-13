/**
 * Точка на плоскости в декартовых координатах
 */
public class PlanePoint {
    private final double xCoordinate;
    private final double yCoordinate;

    public PlanePoint(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", xCoordinate, yCoordinate);
    }
}
