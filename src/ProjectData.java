import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectData {
    // Коллекции исходных объектов задачи.
    private final List<PlanePoint> pointList = new ArrayList<PlanePoint>();
    private final List<PlaneCircle> circleList = new ArrayList<PlaneCircle>();

    public void addPoint(PlanePoint pointData) {
        pointList.add(pointData);
    }

    public void addCircle(PlaneCircle circleData) {
        circleList.add(circleData);
    }

    public void clearAllData() {
        pointList.clear();
        circleList.clear();
    }

    public List<PlanePoint> getPointList() {
        // Возвращаем только read-only представление для защиты данных.
        return Collections.unmodifiableList(pointList);
    }

    public List<PlaneCircle> getCircleList() {
        return Collections.unmodifiableList(circleList);
    }

    public int getPointCount() {
        return pointList.size();
    }

    public int getCircleCount() {
        return circleList.size();
    }
}
