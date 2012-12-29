package delaunaythreed;

/**
 * @author Yu
 */
class Edge {
    public Vertex start, end;
    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }
    
    public void reverse() {
        Vertex tmp = this.start;
        this.start = this.end;
        this.end = tmp;
    }

    public boolean equals(Edge l) {
        if ((this.start == l.start && this.end == l.end)
                || (this.start == l.end && this.end == l.start))
            return true;
        return false;
    }
}