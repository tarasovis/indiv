import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
    // Константы оформления (единый стиль отрисовки).
    private static final int POINT_DIAMETER = 8;
    private static final Color OUTSIDE_CIRCLE_COLOR = new Color(0, 130, 0);
    private static final Color INTERSECTING_CIRCLE_COLOR = new Color(200, 120, 0);
    private static final Color TRIANGLE_FILL_COLOR = new Color(255, 0, 0, 50);
    private final MainFrame mainFrame;
    private ProjectData projectData;
    private TriangleSearchResult searchResult = TriangleSearchResult.emptyResult();
    // Параметры временного круга, который строится правой кнопкой мыши.
    private PlanePoint previewCircleCenter;
    private double previewCircleRadius;

    public DrawingPanel(MainFrame mainFrame, ProjectData projectData) {
        this.mainFrame = mainFrame;
        this.projectData = projectData;
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setProjectData(ProjectData projectData) {
        this.projectData = projectData;
        repaint();
    }

    public void setSearchResult(TriangleSearchResult searchResult) {
        this.searchResult = searchResult;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Рисуем слои в фиксированном порядке: фигуры, результат, предпросмотр, точки.
        Graphics2D graphics2D = prepareGraphics(graphics);
        drawAxes(graphics2D);
        drawStoredCircles(graphics2D);
        drawTriangleResult(graphics2D);
        drawPreviewCircle(graphics2D);
        drawStoredPoints(graphics2D);
        drawResultInfo(graphics2D);
        graphics2D.dispose();
    }

    private Graphics2D prepareGraphics(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2D;
    }

    private void drawAxes(Graphics2D graphics2D) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        graphics2D.setColor(Color.GRAY);
        graphics2D.drawLine(0, centerY, getWidth(), centerY);
        graphics2D.drawLine(centerX, 0, centerX, getHeight());
        graphics2D.drawString("X", getWidth() - 20, centerY - 6);
        graphics2D.drawString("Y", centerX + 6, 20);
    }

    private void drawStoredCircles(Graphics2D graphics2D) {
        for (PlaneCircle circleData : projectData.getCircleList()) {
            graphics2D.setColor(getCircleColor(circleData));
            drawCircle(graphics2D, circleData);
        }
    }

    private Color getCircleColor(PlaneCircle circleData) {
        // После поиска подсвечиваем круги по отношению к найденному треугольнику.
        if (!searchResult.hasTriangle()) {
            return Color.BLUE;
        }
        boolean isOutside = GeometryUtils.isCircleOutsideTriangle(searchResult.getTriangleData(), circleData);
        return isOutside ? OUTSIDE_CIRCLE_COLOR : INTERSECTING_CIRCLE_COLOR;
    }

    private void drawCircle(Graphics2D graphics2D, PlaneCircle circleData) {
        int radiusValue = (int) Math.round(circleData.getRadiusValue());
        int centerX = toScreenX(circleData.getCenterPoint().getXCoordinate());
        int centerY = toScreenY(circleData.getCenterPoint().getYCoordinate());
        graphics2D.drawOval(centerX - radiusValue, centerY - radiusValue, radiusValue * 2, radiusValue * 2);
    }

    private void drawTriangleResult(Graphics2D graphics2D) {
        // Результат выделяется полупрозрачной заливкой и жирным контуром.
        if (!searchResult.hasTriangle()) {
            return;
        }
        TriangleData triangleData = searchResult.getTriangleData();
        Polygon trianglePolygon = buildTrianglePolygon(triangleData);
        graphics2D.setColor(TRIANGLE_FILL_COLOR);
        graphics2D.fillPolygon(trianglePolygon);
        graphics2D.setColor(Color.RED);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawPolygon(trianglePolygon);
    }

    private Polygon buildTrianglePolygon(TriangleData triangleData) {
        Polygon trianglePolygon = new Polygon();
        trianglePolygon.addPoint(toScreenX(triangleData.getFirstVertex().getXCoordinate()), toScreenY(triangleData.getFirstVertex().getYCoordinate()));
        trianglePolygon.addPoint(toScreenX(triangleData.getSecondVertex().getXCoordinate()), toScreenY(triangleData.getSecondVertex().getYCoordinate()));
        trianglePolygon.addPoint(toScreenX(triangleData.getThirdVertex().getXCoordinate()), toScreenY(triangleData.getThirdVertex().getYCoordinate()));
        return trianglePolygon;
    }

    private void drawPreviewCircle(Graphics2D graphics2D) {
        // Пока круг не зафиксирован вторым кликом, показываем серый предпросмотр.
        if (previewCircleCenter == null) {
            return;
        }
        graphics2D.setColor(Color.LIGHT_GRAY);
        drawCircle(graphics2D, new PlaneCircle(previewCircleCenter, previewCircleRadius));
    }

    private void drawStoredPoints(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        for (PlanePoint pointData : projectData.getPointList()) {
            drawPoint(graphics2D, pointData);
        }
    }

    private void drawPoint(Graphics2D graphics2D, PlanePoint pointData) {
        int screenX = toScreenX(pointData.getXCoordinate());
        int screenY = toScreenY(pointData.getYCoordinate());
        graphics2D.fillOval(screenX - POINT_DIAMETER / 2, screenY - POINT_DIAMETER / 2, POINT_DIAMETER, POINT_DIAMETER);
    }

    private void drawResultInfo(Graphics2D graphics2D) {
        // На холсте дублируем ключевые метрики найденного решения.
        if (!searchResult.hasTriangle()) {
            return;
        }
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString("Снаружи кругов: " + searchResult.getOutsideCircleCount(), 12, 20);
        graphics2D.drawString("Периметр: " + String.format("%.2f", searchResult.getPerimeterValue()), 12, 38);
    }

    private int toScreenX(double xCoordinate) {
        return getWidth() / 2 + (int) Math.round(xCoordinate);
    }

    private int toScreenY(double yCoordinate) {
        return getHeight() / 2 - (int) Math.round(yCoordinate);
    }

    private PlanePoint toPlanePoint(Point screenPoint) {
        // Преобразуем экранные координаты в декартовы координаты рабочей плоскости.
        double xCoordinate = screenPoint.x - getWidth() / 2.0;
        double yCoordinate = getHeight() / 2.0 - screenPoint.y;
        return new PlanePoint(xCoordinate, yCoordinate);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // ЛКМ добавляет точку, ПКМ управляет двухшаговым построением круга.
        PlanePoint clickedPoint = toPlanePoint(mouseEvent.getPoint());
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            addPoint(clickedPoint);
            return;
        }
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            processCircleClick(clickedPoint);
        }
    }

    private void addPoint(PlanePoint clickedPoint) {
        projectData.addPoint(clickedPoint);
        mainFrame.notifyAboutDataChange();
        repaint();
    }

    private void processCircleClick(PlanePoint clickedPoint) {
        // Первый клик задает центр, второй — фиксирует радиус и добавляет круг.
        if (previewCircleCenter == null) {
            previewCircleCenter = clickedPoint;
            previewCircleRadius = 0.0;
            repaint();
            return;
        }
        double circleRadius = GeometryUtils.distanceBetween(previewCircleCenter, clickedPoint);
        projectData.addCircle(new PlaneCircle(previewCircleCenter, circleRadius));
        cancelCirclePreview();
        mainFrame.notifyAboutDataChange();
        repaint();
    }

    private void cancelCirclePreview() {
        previewCircleCenter = null;
        previewCircleRadius = 0.0;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        updateMouseData(mouseEvent);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        updateMouseData(mouseEvent);
    }

    private void updateMouseData(MouseEvent mouseEvent) {
        // Во время движения мыши обновляем радиус предпросмотра круга.
        PlanePoint mousePoint = toPlanePoint(mouseEvent.getPoint());
        mainFrame.updateMousePoint(mousePoint);
        if (previewCircleCenter != null) {
            previewCircleRadius = GeometryUtils.distanceBetween(previewCircleCenter, mousePoint);
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
