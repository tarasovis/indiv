/**
 * Перебор всех треугольников по набору точек и выбор лучшего по условию задачи
 */
public class TriangleSearcher {
    /**
     * Собственно перебор треугольников
     */
    public static TriangleSearchResult findBestTriangle(ProjectData projectData) {
        TriangleSearchResult bestResult = TriangleSearchResult.emptyResult();
        PlanePoint[] pointArray = projectData.getPointArray();
        PlaneCircle[] circleArray = projectData.getCircleArray();
        for (int firstIndex = 0; firstIndex < pointArray.length - 2; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < pointArray.length - 1; secondIndex++) {
                for (int thirdIndex = secondIndex + 1; thirdIndex < pointArray.length; thirdIndex++) {
                    TriangleData currentTriangle = new TriangleData(
                            pointArray[firstIndex],
                            pointArray[secondIndex],
                            pointArray[thirdIndex]);
                    bestResult = chooseBetterResult(bestResult, currentTriangle, circleArray);
                }
            }
        }
        return bestResult;
    }

    private static TriangleSearchResult chooseBetterResult(
            TriangleSearchResult currentBestResult,
            TriangleData candidateTriangle,
            PlaneCircle[] circleArray) {
        if (GeometryUtils.isDegenerateTriangle(candidateTriangle)) {
            return currentBestResult;
        }
        int outsideCircleCount = GeometryUtils.countOutsideCircles(candidateTriangle, circleArray);
        TriangleSearchResult candidateResult = new TriangleSearchResult(
                candidateTriangle,
                outsideCircleCount);
        return isCandidateBetter(currentBestResult, candidateResult)
                ? candidateResult : currentBestResult;
    }

    /**
     * Сравнивает два результата по приоритетам задачи: сначала число внешних кругов, затем периметр
     */
    private static boolean isCandidateBetter(
            TriangleSearchResult currentBestResult,
            TriangleSearchResult candidateResult) {
        if (!currentBestResult.hasTriangle()) {
            return true;
        }
        if (candidateResult.getOutsideCircleCount() != currentBestResult.getOutsideCircleCount()) {
            return candidateResult.getOutsideCircleCount() > currentBestResult.getOutsideCircleCount();
        }
        return candidateResult.getPerimeterValue() > currentBestResult.getPerimeterValue();
    }
}
