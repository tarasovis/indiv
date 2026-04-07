import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
    private static final int POINT_DIAMETER = 8;
    private static final Color CIRCLE_COLOR = new Color(40, 90, 180);
    private static final Color TRIANGLE_COLOR = new Color(220, 40, 40);
    private final MainFrame mainFrame;
    private ProjectData projectData;
    private TriangleSearchResult searchResult = TriangleSearchResult.emptyResult();
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
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        drawStoredCircles(graphics2D);
        drawTriangleResult(graphics2D);
        drawPreviewCircle(graphics2D);
        drawStoredPoints(graphics2D);
        graphics2D.dispose();
    }

    private void drawStoredCircles(Graphics2D graphics2D) {
        graphics2D.setColor(CIRCLE_COLOR);
        for (int circleIndex = 0; circleIndex < projectData.getCircleCount(); circleIndex++) {
            drawCircle(graphics2D, projectData.getCircleAt(circleIndex));
        }
    }

    private void drawCircle(Graphics2D graphics2D, PlaneCircle circleData) {
        int radiusValue = (int) Math.round(circleData.getRadiusValue());
        int centerX = (int) Math.round(circleData.getCenterPoint().getXCoordinate());
        int centerY = (int) Math.round(circleData.getCenterPoint().getYCoordinate());
        graphics2D.drawOval(centerX - radiusValue, centerY - radiusValue, radiusValue * 2, radiusValue * 2);
    }

    private void drawTriangleResult(Graphics2D graphics2D) {
        if (!searchResult.hasTriangle()) {
            return;
        }
        TriangleData triangleData = searchResult.getTriangleData();
        int[] xArray = buildXArray(triangleData);
        int[] yArray = buildYArray(triangleData);
        graphics2D.setColor(TRIANGLE_COLOR);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawPolygon(xArray, yArray, 3);
    }

    private int[] buildXArray(TriangleData triangleData) {
        int[] xArray = new int[3];
        xArray[0] = (int) Math.round(triangleData.getFirstVertex().getXCoordinate());
        xArray[1] = (int) Math.round(triangleData.getSecondVertex().getXCoordinate());
        xArray[2] = (int) Math.round(triangleData.getThirdVertex().getXCoordinate());
        return xArray;
    }

    private int[] buildYArray(TriangleData triangleData) {
        int[] yArray = new int[3];
        yArray[0] = (int) Math.round(triangleData.getFirstVertex().getYCoordinate());
        yArray[1] = (int) Math.round(triangleData.getSecondVertex().getYCoordinate());
        yArray[2] = (int) Math.round(triangleData.getThirdVertex().getYCoordinate());
        return yArray;
    }

    private void drawPreviewCircle(Graphics2D graphics2D) {
        if (previewCircleCenter == null) {
            return;
        }
        graphics2D.setColor(CIRCLE_COLOR);
        drawCircle(graphics2D, new PlaneCircle(previewCircleCenter, previewCircleRadius));
    }

    private void drawStoredPoints(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        for (int pointIndex = 0; pointIndex < projectData.getPointCount(); pointIndex++) {
            PlanePoint pointData = projectData.getPointAt(pointIndex);
            int screenX = (int) Math.round(pointData.getXCoordinate());
            int screenY = (int) Math.round(pointData.getYCoordinate());
            graphics2D.fillOval(screenX - POINT_DIAMETER / 2, screenY - POINT_DIAMETER / 2,
                    POINT_DIAMETER, POINT_DIAMETER);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        PlanePoint clickedPoint = new PlanePoint(mouseEvent.getX(), mouseEvent.getY());
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
        if (previewCircleCenter == null) {
            previewCircleCenter = clickedPoint;
            previewCircleRadius = 0.0;
            repaint();
            return;
        }
        double circleRadius = GeometryUtils.distanceBetween(previewCircleCenter, clickedPoint);
        projectData.addCircle(new PlaneCircle(previewCircleCenter, circleRadius));
        previewCircleCenter = null;
        previewCircleRadius = 0.0;
        mainFrame.notifyAboutDataChange();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        updatePreviewRadius(mouseEvent);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        updatePreviewRadius(mouseEvent);
    }

    private void updatePreviewRadius(MouseEvent mouseEvent) {
        if (previewCircleCenter == null) {
            return;
        }
        PlanePoint mousePoint = new PlanePoint(mouseEvent.getX(), mouseEvent.getY());
        previewCircleRadius = GeometryUtils.distanceBetween(previewCircleCenter, mousePoint);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
