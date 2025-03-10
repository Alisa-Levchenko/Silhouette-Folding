package silhouetteFolding;

import java.awt.Point;

class Edge {
    Point start, end;

    public Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Edge from (" + start.x + "," + start.y + ") to (" + end.x + "," + end.y + ")";
    }
}
