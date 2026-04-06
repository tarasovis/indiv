import java.util.ArrayList;
import java.util.List;

public class GeometryUtils {
    private static final double EPS = 1e-9;

    private GeometryUtils() {
    }

    public static TriangleSearchResult findBestTriangle(List<PointData> points, List<CircleData> circles) {
        TriangleSearchResult best = new TriangleSearchResult(null, -1, -1);
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                for (int k = j + 1; k < points.size(); k++) {
                    best = checkTriangle(points.get(i), points.get(j), points.get(k), circles, best);
                }
            }
        }
        return best;
    }

    private static TriangleSearchResult checkTriangle(
            PointData first, PointData second, PointData third,
            List<CircleData> circles, TriangleSearchResult best) {
        if (isCollinear(first, second, third)) {
            return best;
        }
        TriangleData triangle = new TriangleData(first, second, third);
        int outside = countOutside(circles, triangle);
        double perimeter = triangle.perimeter();
        if (outside > best.outsideCount()) {
            return new TriangleSearchResult(triangle, outside, perimeter);
        }
        if (outside == best.outsideCount() && perimeter > best.perimeter() + EPS) {
            return new TriangleSearchResult(triangle, outside, perimeter);
        }
        return best;
    }

    private static int countOutside(List<CircleData> circles, TriangleData triangle) {
        int outside = 0;
        for (CircleData circle : circles) {
            if (isCircleOutsideTriangle(circle, triangle)) {
                outside++;
            }
        }
        return outside;
    }

    public static List<Boolean> classifyCircles(List<CircleData> circles, TriangleData triangle) {
        List<Boolean> flags = new ArrayList<>();
        for (CircleData circle : circles) {
            flags.add(isCircleOutsideTriangle(circle, triangle));
        }
        return flags;
    }

    public static boolean isCircleOutsideTriangle(CircleData circle, TriangleData triangle) {
        if (isCenterInsideTriangle(circle, triangle)) {
            return false;
        }
        if (hasVertexInsideCircle(circle, triangle)) {
            return false;
        }
        return !touchesEdges(circle, triangle);
    }

    private static boolean isCenterInsideTriangle(CircleData circle, TriangleData triangle) {
        return isPointInsideOrOnTriangle(new Point2(circle.centerX(), circle.centerY()), triangle);
    }

    private static boolean hasVertexInsideCircle(CircleData circle, TriangleData triangle) {
        return isPointInsideOrOnCircle(triangle.a(), circle)
                || isPointInsideOrOnCircle(triangle.b(), circle)
                || isPointInsideOrOnCircle(triangle.c(), circle);
    }

    private static boolean touchesEdges(CircleData circle, TriangleData triangle) {
        return segmentDistance(circle, triangle.a(), triangle.b()) <= circle.radius() + EPS
                || segmentDistance(circle, triangle.b(), triangle.c()) <= circle.radius() + EPS
                || segmentDistance(circle, triangle.c(), triangle.a()) <= circle.radius() + EPS;
    }

    private static boolean isPointInsideOrOnCircle(PointData point, CircleData circle) {
        double dx = point.x() - circle.centerX();
        double dy = point.y() - circle.centerY();
        return dx * dx + dy * dy <= circle.radius() * circle.radius() + EPS;
    }

    private static double segmentDistance(CircleData circle, PointData first, PointData second) {
        double dx = second.x() - first.x();
        double dy = second.y() - first.y();
        double lengthSq = dx * dx + dy * dy;
        if (lengthSq < EPS) {
            return Math.hypot(circle.centerX() - first.x(), circle.centerY() - first.y());
        }
        double ux = circle.centerX() - first.x();
        double uy = circle.centerY() - first.y();
        double t = (ux * dx + uy * dy) / lengthSq;
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double px = first.x() + clamped * dx;
        double py = first.y() + clamped * dy;
        return Math.hypot(circle.centerX() - px, circle.centerY() - py);
    }

    private static boolean isPointInsideOrOnTriangle(Point2 point, TriangleData triangle) {
        double d1 = orient(point, triangle.a(), triangle.b());
        double d2 = orient(point, triangle.b(), triangle.c());
        double d3 = orient(point, triangle.c(), triangle.a());
        boolean hasNeg = d1 < -EPS || d2 < -EPS || d3 < -EPS;
        boolean hasPos = d1 > EPS || d2 > EPS || d3 > EPS;
        return !(hasNeg && hasPos);
    }

    private static double orient(Point2 point, PointData first, PointData second) {
        return (first.x() - point.x) * (second.y() - point.y)
                - (first.y() - point.y) * (second.x() - point.x);
    }

    private static boolean isCollinear(PointData first, PointData second, PointData third) {
        double value = (second.x() - first.x()) * (double) (third.y() - first.y())
                - (second.y() - first.y()) * (double) (third.x() - first.x());
        return Math.abs(value) < EPS;
    }

    public static double distance(PointData first, PointData second) {
        return Math.hypot(first.x() - second.x(), first.y() - second.y());
    }

    private static class Point2 {
        private final double x;
        private final double y;

        private Point2(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
