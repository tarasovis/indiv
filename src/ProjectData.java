import java.util.Arrays;

public class ProjectData {
    private static final int MAX_POINT_COUNT = 1000;
    private static final int MAX_CIRCLE_COUNT = 1000;

    private final PlanePoint[] pointArray = new PlanePoint[MAX_POINT_COUNT];
    private final PlaneCircle[] circleArray = new PlaneCircle[MAX_CIRCLE_COUNT];
    private int pointCount;
    private int circleCount;

    public void addPoint(PlanePoint pointData) {
        validatePointLimit();
        pointArray[pointCount] = pointData;
        pointCount++;
    }

    public void addCircle(PlaneCircle circleData) {
        validateCircleLimit();
        circleArray[circleCount] = circleData;
        circleCount++;
    }

    public PlanePoint[] getPointArray() {
        return Arrays.copyOf(pointArray, pointCount);
    }

    public PlaneCircle[] getCircleArray() {
        return Arrays.copyOf(circleArray, circleCount);
    }

    public int getPointCount() {
        return pointCount;
    }

    public int getCircleCount() {
        return circleCount;
    }

    private void validatePointLimit() {
        if (pointCount >= MAX_POINT_COUNT) {
            throw new IllegalStateException("Point limit is 1000.");
        }
    }

    private void validateCircleLimit() {
        if (circleCount >= MAX_CIRCLE_COUNT) {
            throw new IllegalStateException("Circle limit is 1000.");
        }
    }
}
