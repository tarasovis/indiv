import java.util.List;

public class TriangleSearcher {
    public static TriangleSearchResult findBestTriangle(ProjectData projectData) {
        // Полный перебор всех сочетаний из трех точек.
        TriangleSearchResult bestResult = TriangleSearchResult.emptyResult();
        List<PlanePoint> pointList = projectData.getPointList();
        for (int firstIndex = 0; firstIndex < pointList.size() - 2; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < pointList.size() - 1; secondIndex++) {
                for (int thirdIndex = secondIndex + 1; thirdIndex < pointList.size(); thirdIndex++) {
                    TriangleData currentTriangle = new TriangleData(
                            pointList.get(firstIndex),
                            pointList.get(secondIndex),
                            pointList.get(thirdIndex));
                    bestResult = chooseBetterResult(bestResult, currentTriangle, projectData);
                }
            }
        }
        return bestResult;
    }

    private static TriangleSearchResult chooseBetterResult(
            TriangleSearchResult currentBestResult,
            TriangleData candidateTriangle,
            ProjectData projectData) {
        // Вырожденные треугольники не участвуют в оптимизации.
        if (GeometryUtils.isDegenerateTriangle(candidateTriangle)) {
            return currentBestResult;
        }
        int outsideCircleCount = GeometryUtils.countOutsideCircles(
                candidateTriangle,
                projectData.getCircleList());
        TriangleSearchResult candidateResult = new TriangleSearchResult(
                candidateTriangle,
                outsideCircleCount);
        return isCandidateBetter(currentBestResult, candidateResult)
                ? candidateResult : currentBestResult;
    }

    private static boolean isCandidateBetter(
            TriangleSearchResult currentBestResult,
            TriangleSearchResult candidateResult) {
        // Приоритет 1: максимум внешних кругов. Приоритет 2: максимум периметра.
        if (!currentBestResult.hasTriangle()) {
            return true;
        }
        if (candidateResult.getOutsideCircleCount() != currentBestResult.getOutsideCircleCount()) {
            return candidateResult.getOutsideCircleCount() > currentBestResult.getOutsideCircleCount();
        }
        return candidateResult.getPerimeterValue() > currentBestResult.getPerimeterValue();
    }
}
