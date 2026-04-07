public class TriangleSearcher {
    public static TriangleSearchResult findBestTriangle(ProjectData projectData) {
        TriangleSearchResult bestResult = TriangleSearchResult.emptyResult();
        PlanePoint[] pointArray = projectData.getPointArray();
        for (int firstIndex = 0; firstIndex < pointArray.length - 2; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < pointArray.length - 1; secondIndex++) {
                for (int thirdIndex = secondIndex + 1; thirdIndex < pointArray.length; thirdIndex++) {
                    TriangleData currentTriangle = new TriangleData(
                            pointArray[firstIndex],
                            pointArray[secondIndex],
                            pointArray[thirdIndex]);
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
        if (GeometryUtils.isDegenerateTriangle(candidateTriangle)) {
            return currentBestResult;
        }
        int outsideCircleCount = GeometryUtils.countOutsideCircles(
                candidateTriangle,
                projectData.getCircleArray());
        TriangleSearchResult candidateResult = new TriangleSearchResult(
                candidateTriangle,
                outsideCircleCount);
        return isCandidateBetter(currentBestResult, candidateResult)
                ? candidateResult : currentBestResult;
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
