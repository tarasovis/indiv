import java.util.List;

public class GeometryUtils {
    private static final double EPS = 1e-9;

    private GeometryUtils() {
    }

    public static TriangleSearchResult findBestTriangle(
            List<PointData> points, List<CircleData> circles) {
        TriangleSearchResult best = new TriangleSearchResult(null, -1, -1);
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                for (int k = j + 1; k < points.size(); k++) {
                    best = evaluateCandidate(points.get(i), points.get(j), points.get(k), circles, best);
                }
            }
        }
        return best;
    }

    private static TriangleSearchResult evaluateCandidate(
            PointData a, PointData b, PointData c,
            List<CircleData> circles, TriangleSearchResult best) {
        if (isCollinear(a, b, c)) {
            return best;
        }
        TriangleData triangle = new TriangleData(a, b, c);
        int outsideCount = countOutsideCircles(triangle, circles);
        double perimeter = triangle.perimeter();
        if (isBetterResult(outsideCount, perimeter, best)) {
            return new TriangleSearchResult(triangle, outsideCount, perimeter);
        }
        return best;
    }

    private static boolean isBetterResult(int outsideCount, double perimeter, TriangleSearchResult best) {
        if (outsideCount > best.outsideCount()) {
            return true;
        }
        return outsideCount == best.outsideCount() && perimeter > best.perimeter() + EPS;
    }

    private static int countOutsideCircles(TriangleData triangle, List<CircleData> circles) {
        int outsideCount = 0;
        for (CircleData circle : circles) {
            if (isCircleOutsideTriangle(circle, triangle)) {
                outsideCount++;
            }
        }
        return outsideCount;
    }

    public static boolean isCircleOutsideTriangle(CircleData circle, TriangleData triangle) {
        PointData center = new PointData((int) Math.round(circle.centerX()), (int) Math.round(circle.centerY()));
        if (isPointInsideOrOnTriangle(center, triangle)) {
            return false;
        }
        if (isVertexInsideOrOnCircle(circle, triangle)) {
            return false;
        }
        return !touchesAnyTriangleEdge(circle, triangle);
    }

    private static boolean isVertexInsideOrOnCircle(CircleData circle, TriangleData triangle) {
        return isPointInsideOrOnCircle(triangle.a(), circle)
                || isPointInsideOrOnCircle(triangle.b(), circle)
                || isPointInsideOrOnCircle(triangle.c(), circle);
    }

    private static boolean touchesAnyTriangleEdge(CircleData circle, TriangleData triangle) {
        return intersectsSegment(circle, triangle.a(), triangle.b())
                || intersectsSegment(circle, triangle.b(), triangle.c())
                || intersectsSegment(circle, triangle.c(), triangle.a());
    }

    private static boolean intersectsSegment(CircleData circle, PointData p1, PointData p2) {
        double distance = distancePointToSegment(circle.centerX(), circle.centerY(), p1, p2);
        return distance <= circle.radius() + EPS;
    }

    private static boolean isPointInsideOrOnCircle(PointData point, CircleData circle) {
        double dx = point.x() - circle.centerX();
        double dy = point.y() - circle.centerY();
        return dx * dx + dy * dy <= circle.radius() * circle.radius() + EPS;
    }

    private static double distancePointToSegment(double x, double y, PointData p1, PointData p2) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared < EPS) {
            return Math.hypot(x - p1.x(), y - p1.y());
        }
        double t = ((x - p1.x()) * dx + (y - p1.y()) * dy) / lengthSquared;
        double clamped = Math.max(0, Math.min(1, t));
        double projX = p1.x() + clamped * dx;
        double projY = p1.y() + clamped * dy;
        return Math.hypot(x - projX, y - projY);
    }

    private static boolean isPointInsideOrOnTriangle(PointData point, TriangleData triangle) {
        double d1 = cross(point, triangle.a(), triangle.b());
        double d2 = cross(point, triangle.b(), triangle.c());
        double d3 = cross(point, triangle.c(), triangle.a());
        boolean hasNeg = d1 < -EPS || d2 < -EPS || d3 < -EPS;
        boolean hasPos = d1 > EPS || d2 > EPS || d3 > EPS;
        return !(hasNeg && hasPos);
    }

    private static double cross(PointData p, PointData a, PointData b) {
        return (a.x() - p.x()) * (double) (b.y() - p.y())
                - (a.y() - p.y()) * (double) (b.x() - p.x());
    }

    private static boolean isCollinear(PointData a, PointData b, PointData c) {
        double area2 = (b.x() - a.x()) * (double) (c.y() - a.y())
                - (b.y() - a.y()) * (double) (c.x() - a.x());
        return Math.abs(area2) < EPS;
    }

    public static double distance(PointData p1, PointData p2) {
        return Math.hypot(p1.x() - p2.x(), p1.y() - p2.y());
    }
}
