import java.util.Arrays;

public class ProjectData {
    private PlanePoint[] pointArray = new PlanePoint[8];
    private PlaneCircle[] circleArray = new PlaneCircle[8];
    private int pointCount;
    private int circleCount;

    public void addPoint(PlanePoint pointData) {
        ensurePointCapacity(pointCount + 1);
        pointArray[pointCount] = pointData;
        pointCount++;
    }

    public void addCircle(PlaneCircle circleData) {
        ensureCircleCapacity(circleCount + 1);
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

    private void ensurePointCapacity(int neededCapacity) {
        if (neededCapacity <= pointArray.length) {
            return;
        }
        pointArray = growPointArray();
    }

    private void ensureCircleCapacity(int neededCapacity) {
        if (neededCapacity <= circleArray.length) {
            return;
        }
        circleArray = growCircleArray();
    }

    private PlanePoint[] growPointArray() {
        return Arrays.copyOf(pointArray, pointArray.length * 2);
    }

    private PlaneCircle[] growCircleArray() {
        return Arrays.copyOf(circleArray, circleArray.length * 2);
    }
}
