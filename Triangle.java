package delaunaythreed;

/**
 * @author Yu
 */
class Triangle {
    public Vertex v1, v2, v3;
    
    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Vertex getNormal() {
        Vertex edge1 = new Vertex(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
        Vertex edge2 = new Vertex(v3.x-v1.x, v3.y-v1.y, v3.z-v1.z);

        Vertex normal = edge1.cross(edge2);
        normal.normalize();
        return normal;
    }

    public void turnBack() {
        Vertex tmp = this.v3;
        this.v3 = this.v1;
        this.v1 = tmp;
    }

    // \u7dda\u5206\u306e\u30ea\u30b9\u30c8\u3092\u5f97\u308b
    public Edge[] getEdges() {
        Edge[] l = {
            new Edge(v1, v2),
            new Edge(v2, v3),
            new Edge(v3, v1)
        };
        return l;
    }

    // \u540c\u3058\u304b\u3069\u3046\u304b\u3002\u3059\u3052\u30fc\u7c21\u6613\u7684\u306a\u30c1\u30a7\u30c3\u30af
    public boolean equals(Triangle t) {
        Edge[] Edges1 = this.getEdges();
        Edge[] Edges2 = t.getEdges();

        int cnt = 0;
        for(int i = 0; i < Edges1.length; i++) {
            for(int j = 0; j < Edges2.length; j++) {
                if (Edges1[i].equals(Edges2[j]))
                    cnt++;
            }
        }
        if (cnt == 3) return true;
        else return false;

    }
}