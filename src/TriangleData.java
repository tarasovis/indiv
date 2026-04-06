public class TriangleData {
    private final PlanePoint firstVertex;
    private final PlanePoint secondVertex;
    private final PlanePoint thirdVertex;

    public TriangleData(
            PlanePoint firstVertex,
            PlanePoint secondVertex,
            PlanePoint thirdVertex) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.thirdVertex = thirdVertex;
    }

    public PlanePoint getFirstVertex() {
        return firstVertex;
    }

    public PlanePoint getSecondVertex() {
        return secondVertex;
    }

    public PlanePoint getThirdVertex() {
        return thirdVertex;
    }

    public double getPerimeterValue() {
        double firstSideLength = GeometryUtils.distanceBetween(firstVertex, secondVertex);
        double secondSideLength = GeometryUtils.distanceBetween(secondVertex, thirdVertex);
        double thirdSideLength = GeometryUtils.distanceBetween(thirdVertex, firstVertex);
        return firstSideLength + secondSideLength + thirdSideLength;
    }
}
