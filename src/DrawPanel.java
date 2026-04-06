import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final List<PointData> points = new ArrayList<>();
    private final List<CircleData> circles = new ArrayList<>();
    private TriangleData resultTriangle;
    private PointData previewCircleCenter;
    private PointData currentMouse;

    public DrawPanel() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public List<PointData> getPoints() {
        return points;
    }

    public List<CircleData> getCircles() {
        return circles;
    }

    public void setResultTriangle(TriangleData triangle) {
        this.resultTriangle = triangle;
    }

    public void clearResult() {
        resultTriangle = null;
    }

    public void clearAll() {
        points.clear();
        circles.clear();
        clearResult();
        previewCircleCenter = null;
        repaint();
    }

    public void setData(DataSet dataSet) {
        points.clear();
        circles.clear();
        points.addAll(dataSet.points());
        circles.addAll(dataSet.circles());
        clearResult();
        previewCircleCenter = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;
        drawCircles(graphics2d);
        drawPoints(graphics2d);
        drawPreviewCircle(graphics2d);
        drawResultTriangle(graphics2d);
    }

    private void drawPoints(Graphics2D graphics2d) {
        graphics2d.setColor(Color.BLACK);
        for (PointData point : points) {
            graphics2d.fillOval(point.x() - 3, point.y() - 3, 6, 6);
        }
    }

    private void drawCircles(Graphics2D graphics2d) {
        graphics2d.setColor(new Color(40, 90, 190));
        for (CircleData circle : circles) {
            int diameter = (int) Math.round(2 * circle.radius());
            int x = (int) Math.round(circle.centerX() - circle.radius());
            int y = (int) Math.round(circle.centerY() - circle.radius());
            graphics2d.drawOval(x, y, diameter, diameter);
        }
    }

    private void drawPreviewCircle(Graphics2D graphics2d) {
        if (previewCircleCenter == null || currentMouse == null) {
            return;
        }
        double radius = GeometryUtils.distance(previewCircleCenter, currentMouse);
        graphics2d.setColor(new Color(80, 80, 80));
        int diameter = (int) Math.round(2 * radius);
        int x = (int) Math.round(previewCircleCenter.x() - radius);
        int y = (int) Math.round(previewCircleCenter.y() - radius);
        graphics2d.drawOval(x, y, diameter, diameter);
    }

    private void drawResultTriangle(Graphics2D graphics2d) {
        if (resultTriangle == null) {
            return;
        }
        graphics2d.setColor(new Color(220, 30, 30));
        Stroke oldStroke = graphics2d.getStroke();
        graphics2d.setStroke(new BasicStroke(2f));
        drawEdge(graphics2d, resultTriangle.a(), resultTriangle.b());
        drawEdge(graphics2d, resultTriangle.b(), resultTriangle.c());
        drawEdge(graphics2d, resultTriangle.c(), resultTriangle.a());
        graphics2d.setStroke(oldStroke);
    }

    private void drawEdge(Graphics2D graphics2d, PointData p1, PointData p2) {
        graphics2d.drawLine(p1.x(), p1.y(), p2.x(), p2.y());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            addPoint(mouseEvent);
            return;
        }
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            processRightClick(mouseEvent);
        }
    }

    private void addPoint(MouseEvent mouseEvent) {
        points.add(new PointData(mouseEvent.getX(), mouseEvent.getY()));
        clearResult();
        repaint();
    }

    private void processRightClick(MouseEvent mouseEvent) {
        if (previewCircleCenter == null) {
            previewCircleCenter = new PointData(mouseEvent.getX(), mouseEvent.getY());
            currentMouse = previewCircleCenter;
            repaint();
            return;
        }
        finalizeCircle(mouseEvent);
    }

    private void finalizeCircle(MouseEvent mouseEvent) {
        PointData endPoint = new PointData(mouseEvent.getX(), mouseEvent.getY());
        double radius = GeometryUtils.distance(previewCircleCenter, endPoint);
        if (radius > 0.5) {
            circles.add(new CircleData(previewCircleCenter.x(), previewCircleCenter.y(), radius));
        }
        previewCircleCenter = null;
        clearResult();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        currentMouse = new PointData(mouseEvent.getX(), mouseEvent.getY());
        if (previewCircleCenter != null) {
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

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }
}
