public class ProjectData {
    private PlanePoint[] pointArray = new PlanePoint[64];
    private PlaneCircle[] circleArray = new PlaneCircle[64];
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

    public void clearAllData() {
        pointCount = 0;
        circleCount = 0;
    }

    public PlanePoint getPointAt(int pointIndex) {
        return pointArray[pointIndex];
    }

    public PlaneCircle getCircleAt(int circleIndex) {
        return circleArray[circleIndex];
    }

    public int getPointCount() {
        return pointCount;
    }

    public int getCircleCount() {
        return circleCount;
    }

    private void ensurePointCapacity(int requiredCount) {
        if (requiredCount <= pointArray.length) {
            return;
        }
        pointArray = growPointArray(pointArray);
    }

    private void ensureCircleCapacity(int requiredCount) {
        if (requiredCount <= circleArray.length) {
            return;
        }
        circleArray = growCircleArray(circleArray);
    }

    private PlanePoint[] growPointArray(PlanePoint[] oldArray) {
        PlanePoint[] newArray = new PlanePoint[oldArray.length * 2];
        for (int pointIndex = 0; pointIndex < oldArray.length; pointIndex++) {
            newArray[pointIndex] = oldArray[pointIndex];
        }
        return newArray;
    }

    private PlaneCircle[] growCircleArray(PlaneCircle[] oldArray) {
        PlaneCircle[] newArray = new PlaneCircle[oldArray.length * 2];
        for (int circleIndex = 0; circleIndex < oldArray.length; circleIndex++) {
            newArray[circleIndex] = oldArray[circleIndex];
        }
        return newArray;
    }
}
