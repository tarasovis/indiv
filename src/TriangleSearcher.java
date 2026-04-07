public class TriangleSearcher {
    public static TriangleSearchResult findBestTriangle(ProjectData projectData) {
        TriangleSearchResult bestResult = TriangleSearchResult.emptyResult();
        int pointCount = projectData.getPointCount();
        for (int firstIndex = 0; firstIndex < pointCount - 2; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < pointCount - 1; secondIndex++) {
                for (int thirdIndex = secondIndex + 1; thirdIndex < pointCount; thirdIndex++) {
                    TriangleData candidateTriangle = new TriangleData(
                            projectData.getPointAt(firstIndex),
                            projectData.getPointAt(secondIndex),
                            projectData.getPointAt(thirdIndex));
                    bestResult = chooseBetterResult(bestResult, candidateTriangle, projectData);
                }
            }
        }
        return bestResult;
    }

    private static TriangleSearchResult chooseBetterResult(
            TriangleSearchResult currentBestResult,
            TriangleData candidateTriangle,
            ProjectData projectData) {
        if (GeometryUtils.isDegenerateTriangle(candidateTriangle)) {
            return currentBestResult;
        }
        int outsideCircleCount = GeometryUtils.countOutsideCircles(candidateTriangle, projectData);
        TriangleSearchResult candidateResult = new TriangleSearchResult(candidateTriangle, outsideCircleCount);
        if (isCandidateBetter(currentBestResult, candidateResult)) {
            return candidateResult;
        }
        return currentBestResult;
    }

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
