public record TriangleData(PointData a, PointData b, PointData c) {
    public double perimeter() {
        return GeometryUtils.distance(a, b)
                + GeometryUtils.distance(b, c)
                + GeometryUtils.distance(c, a);
    }
}
