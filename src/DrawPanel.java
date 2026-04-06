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
    private final List<Boolean> outsideFlags = new ArrayList<>();
    private TriangleData resultTriangle;
    private PointData circleCenterDraft;
    private PointData mousePoint;

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

    public void clearAll() {
        points.clear();
        circles.clear();
        dropSolution();
        circleCenterDraft = null;
        repaint();
    }

    public void setData(DataSet dataSet) {
        points.clear();
        circles.clear();
        points.addAll(dataSet.points());
        circles.addAll(dataSet.circles());
        dropSolution();
        circleCenterDraft = null;
        repaint();
    }

    public void dropSolution() {
        resultTriangle = null;
        outsideFlags.clear();
    }

    public void applySearchResult(TriangleSearchResult result) {
        resultTriangle = result.triangle();
        outsideFlags.clear();
        if (resultTriangle != null) {
            outsideFlags.addAll(GeometryUtils.classifyCircles(circles, resultTriangle));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        drawCircles(g2);
        drawPoints(g2);
        drawCircleDraft(g2);
        drawTriangle(g2);
    }

    private void drawCircles(Graphics2D g2) {
        for (int index = 0; index < circles.size(); index++) {
            CircleData circle = circles.get(index);
            g2.setColor(resolveCircleColor(index));
            drawCircle(g2, circle);
        }
    }

    private Color resolveCircleColor(int index) {
        if (outsideFlags.isEmpty() || index >= outsideFlags.size()) {
            return new Color(45, 95, 200);
        }
        return outsideFlags.get(index) ? new Color(0, 150, 70) : new Color(200, 60, 60);
    }

    private void drawCircle(Graphics2D g2, CircleData circle) {
        int diameter = (int) Math.round(2 * circle.radius());
        int x = (int) Math.round(circle.centerX() - circle.radius());
        int y = (int) Math.round(circle.centerY() - circle.radius());
        g2.drawOval(x, y, diameter, diameter);
    }

    private void drawPoints(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        for (PointData point : points) {
            g2.fillOval(point.x() - 3, point.y() - 3, 6, 6);
        }
    }

    private void drawCircleDraft(Graphics2D g2) {
        if (circleCenterDraft == null || mousePoint == null) {
            return;
        }
        g2.setColor(new Color(90, 90, 90));
        double radius = GeometryUtils.distance(circleCenterDraft, mousePoint);
        drawCircle(g2, new CircleData(circleCenterDraft.x(), circleCenterDraft.y(), radius));
    }

    private void drawTriangle(Graphics2D g2) {
        if (resultTriangle == null) {
            return;
        }
        g2.setColor(new Color(220, 30, 30));
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));
        drawEdge(g2, resultTriangle.a(), resultTriangle.b());
        drawEdge(g2, resultTriangle.b(), resultTriangle.c());
        drawEdge(g2, resultTriangle.c(), resultTriangle.a());
        g2.setStroke(oldStroke);
    }

    private void drawEdge(Graphics2D g2, PointData first, PointData second) {
        g2.drawLine(first.x(), first.y(), second.x(), second.y());
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            addPoint(event.getX(), event.getY());
            return;
        }
        if (SwingUtilities.isRightMouseButton(event)) {
            processCircleClick(event.getX(), event.getY());
        }
    }

    private void addPoint(int x, int y) {
        points.add(new PointData(x, y));
        dropSolution();
        repaint();
    }

    private void processCircleClick(int x, int y) {
        if (circleCenterDraft == null) {
            circleCenterDraft = new PointData(x, y);
            mousePoint = circleCenterDraft;
            repaint();
            return;
        }
        finishCircle(x, y);
    }

    private void finishCircle(int x, int y) {
        double radius = GeometryUtils.distance(circleCenterDraft, new PointData(x, y));
        if (radius > 0.5) {
            circles.add(new CircleData(circleCenterDraft.x(), circleCenterDraft.y(), radius));
        }
        circleCenterDraft = null;
        dropSolution();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mousePoint = new PointData(event.getX(), event.getY());
        if (circleCenterDraft != null) {
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        mouseMoved(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }
}
