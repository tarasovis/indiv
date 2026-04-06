public class TriangleSearchResult {
    private final TriangleData triangleData;
    private final int outsideCircleCount;

    public TriangleSearchResult(TriangleData triangleData, int outsideCircleCount) {
        this.triangleData = triangleData;
        this.outsideCircleCount = outsideCircleCount;
    }

    public static TriangleSearchResult emptyResult() {
        return new TriangleSearchResult(null, -1);
    }

    public boolean hasTriangle() {
        return triangleData != null;
    }

    public TriangleData getTriangleData() {
        return triangleData;
    }

    public int getOutsideCircleCount() {
        return outsideCircleCount;
    }

    public double getPerimeterValue() {
        if (!hasTriangle()) {
            return 0.0;
        }
        return triangleData.getPerimeterValue();
    }
}
