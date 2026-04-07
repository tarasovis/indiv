public class GeometryUtils {
    private static final double EPSILON = 1e-7;

    public static double distanceBetween(PlanePoint firstPoint, PlanePoint secondPoint) {
        double dxValue = firstPoint.getXCoordinate() - secondPoint.getXCoordinate();
        double dyValue = firstPoint.getYCoordinate() - secondPoint.getYCoordinate();
        return Math.hypot(dxValue, dyValue);
    }

    public static boolean isDegenerateTriangle(TriangleData triangleData) {
        return Math.abs(signedDoubleArea(
                triangleData.getFirstVertex(),
                triangleData.getSecondVertex(),
                triangleData.getThirdVertex())) <= EPSILON;
    }

    public static int countOutsideCircles(TriangleData triangleData, ProjectData projectData) {
        int outsideCircleCount = 0;
        for (int circleIndex = 0; circleIndex < projectData.getCircleCount(); circleIndex++) {
            PlaneCircle circleData = projectData.getCircleAt(circleIndex);
            if (isCircleOutsideTriangle(triangleData, circleData)) {
                outsideCircleCount++;
            }
        }
        return outsideCircleCount;
    }

    public static boolean isCircleOutsideTriangle(TriangleData triangleData, PlaneCircle circleData) {
        if (isTriangleVertexInsideCircle(triangleData, circleData)) {
            return false;
        }
        if (isPointInsideOrOnTriangle(circleData.getCenterPoint(), triangleData)) {
            return false;
        }
        return !doesTriangleEdgeTouchCircle(triangleData, circleData);
    }

    private static boolean isTriangleVertexInsideCircle(TriangleData triangleData, PlaneCircle circleData) {
        return isPointInsideOrOnCircle(triangleData.getFirstVertex(), circleData)
                || isPointInsideOrOnCircle(triangleData.getSecondVertex(), circleData)
                || isPointInsideOrOnCircle(triangleData.getThirdVertex(), circleData);
    }

    private static boolean doesTriangleEdgeTouchCircle(TriangleData triangleData, PlaneCircle circleData) {
        return doesSegmentTouchCircle(triangleData.getFirstVertex(), triangleData.getSecondVertex(), circleData)
                || doesSegmentTouchCircle(triangleData.getSecondVertex(), triangleData.getThirdVertex(), circleData)
                || doesSegmentTouchCircle(triangleData.getThirdVertex(), triangleData.getFirstVertex(), circleData);
    }

    private static boolean isPointInsideOrOnCircle(PlanePoint pointData, PlaneCircle circleData) {
        return distanceBetween(pointData, circleData.getCenterPoint()) <= circleData.getRadiusValue() + EPSILON;
    }

    public static boolean isPointInsideOrOnTriangle(PlanePoint pointData, TriangleData triangleData) {
        double firstSign = signedDoubleArea(triangleData.getFirstVertex(), triangleData.getSecondVertex(), pointData);
        double secondSign = signedDoubleArea(triangleData.getSecondVertex(), triangleData.getThirdVertex(), pointData);
        double thirdSign = signedDoubleArea(triangleData.getThirdVertex(), triangleData.getFirstVertex(), pointData);
        return haveSameSign(firstSign, secondSign, thirdSign);
    }

    private static boolean haveSameSign(double firstValue, double secondValue, double thirdValue) {
        boolean hasNegative = firstValue < -EPSILON || secondValue < -EPSILON || thirdValue < -EPSILON;
        boolean hasPositive = firstValue > EPSILON || secondValue > EPSILON || thirdValue > EPSILON;
        return !(hasNegative && hasPositive);
    }

    private static boolean doesSegmentTouchCircle(PlanePoint segmentStart, PlanePoint segmentEnd, PlaneCircle circleData) {
        double distanceToSegment = distanceFromPointToSegment(circleData.getCenterPoint(), segmentStart, segmentEnd);
        return distanceToSegment <= circleData.getRadiusValue() + EPSILON;
    }

    private static double distanceFromPointToSegment(PlanePoint pointData, PlanePoint segmentStart, PlanePoint segmentEnd) {
        double dxValue = segmentEnd.getXCoordinate() - segmentStart.getXCoordinate();
        double dyValue = segmentEnd.getYCoordinate() - segmentStart.getYCoordinate();
        double lengthSquare = dxValue * dxValue + dyValue * dyValue;
        if (lengthSquare <= EPSILON) {
            return distanceBetween(pointData, segmentStart);
        }
        double projection = computeProjection(pointData, segmentStart, dxValue, dyValue, lengthSquare);
        double clippedProjection = Math.max(0.0, Math.min(1.0, projection));
        return distanceToProjectedPoint(pointData, segmentStart, dxValue, dyValue, clippedProjection);
    }

    private static double computeProjection(
            PlanePoint pointData,
            PlanePoint segmentStart,
            double dxValue,
            double dyValue,
            double lengthSquare) {
        double xPart = (pointData.getXCoordinate() - segmentStart.getXCoordinate()) * dxValue;
        double yPart = (pointData.getYCoordinate() - segmentStart.getYCoordinate()) * dyValue;
        return (xPart + yPart) / lengthSquare;
    }

    private static double distanceToProjectedPoint(
            PlanePoint pointData,
            PlanePoint segmentStart,
            double dxValue,
            double dyValue,
            double clippedProjection) {
        double projectedX = segmentStart.getXCoordinate() + clippedProjection * dxValue;
        double projectedY = segmentStart.getYCoordinate() + clippedProjection * dyValue;
        double diffX = pointData.getXCoordinate() - projectedX;
        double diffY = pointData.getYCoordinate() - projectedY;
        return Math.hypot(diffX, diffY);
    }

    private static double signedDoubleArea(PlanePoint firstPoint, PlanePoint secondPoint, PlanePoint thirdPoint) {
        double firstPart = (secondPoint.getXCoordinate() - firstPoint.getXCoordinate())
                * (thirdPoint.getYCoordinate() - firstPoint.getYCoordinate());
        double secondPart = (secondPoint.getYCoordinate() - firstPoint.getYCoordinate())
                * (thirdPoint.getXCoordinate() - firstPoint.getXCoordinate());
        return firstPart - secondPart;
    }
}
