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

    public void clearAllData() {
        pointArray = new PlanePoint[8];
        circleArray = new PlaneCircle[8];
        pointCount = 0;
        circleCount = 0;
    }

    public PlanePoint[] getPointArray() {
        PlanePoint[] copyArray = new PlanePoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            copyArray[i] = pointArray[i];
        }
        return copyArray;
    }

    public PlaneCircle[] getCircleArray() {
        PlaneCircle[] copyArray = new PlaneCircle[circleCount];
        for (int i = 0; i < circleCount; i++) {
            copyArray[i] = circleArray[i];
        }
        return copyArray;
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
        PlanePoint[] newArray = new PlanePoint[pointArray.length * 2];
        for (int i = 0; i < pointArray.length; i++) {
            newArray[i] = pointArray[i];
        }
        return newArray;
    }

    private PlaneCircle[] growCircleArray() {
        PlaneCircle[] newArray = new PlaneCircle[circleArray.length * 2];
        for (int i = 0; i < circleArray.length; i++) {
            newArray[i] = circleArray[i];
        }
        return newArray;
    }
}
