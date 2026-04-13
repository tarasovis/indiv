/**
 * Результат работы алгоритма поиска:
 * найденный треугольник и число кругов, лежащих снаружи него.
 */
public class TriangleSearchResult {
    private final TriangleData triangleData;
    private final int outsideCircleCount;

    public TriangleSearchResult(TriangleData triangleData, int outsideCircleCount) {
        this.triangleData = triangleData;
        this.outsideCircleCount = outsideCircleCount;
    }

    /**
     * Возвращает специальный пустой результат, когда треугольник еще не найден.
     */
    public static TriangleSearchResult emptyResult() {
        return new TriangleSearchResult(null, -1);
    }

    /**
     * Проверяет, содержит ли результат найденный треугольник.
     */
    public boolean hasTriangle() {
        return triangleData != null;
    }

    public TriangleData getTriangleData() {
        return triangleData;
    }

    public int getOutsideCircleCount() {
        return outsideCircleCount;
    }

    /**
     * Возвращает периметр найденного треугольника или 0.0 для пустого результата.
     */
    public double getPerimeterValue() {
        if (!hasTriangle()) {
            return 0.0;
        }
        return triangleData.getPerimeterValue();
    }
}
