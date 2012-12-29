/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delaunaythreed;
import java.util.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 *
 * @author Yu
 */
public class Delaunay {
    List<Vertex> vertices = new CopyOnWriteArrayList<Vertex>();
    List<Tetrahedron> tetras = new CopyOnWriteArrayList<Tetrahedron>();
    List<Edge> edges = new CopyOnWriteArrayList<Edge>();
    List<Triangle> triangles = new CopyOnWriteArrayList<Triangle>();
    
    public void readData(){
        this.vertices.clear();
        try{
            FileReader fr = new FileReader("dataset1.txt"); 
            BufferedReader br = new BufferedReader(fr);
            String data = br.readLine();
            while(data != null){
                StringTokenizer st = new StringTokenizer(data);
                Vertex new_vertex = new Vertex();
                new_vertex.x = Float.valueOf(st.nextElement().toString());
                new_vertex.y = Float.valueOf(st.nextElement().toString());
                new_vertex.z = Float.valueOf(st.nextElement().toString());
                this.vertices.add(new_vertex);
                data = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Error reading data!");
        }
    }
    
    public void computeDelaunay(){
        tetras.clear();
        edges.clear();
        Vertex vMax = new Vertex(-999, -999, -999);
        Vertex vMin = new Vertex( 999,  999,  999);
        for(Vertex v : vertices) {
            if (vMax.x < v.x) vMax.x = v.x;
            if (vMax.y < v.y) vMax.y = v.y;
            if (vMax.z < v.z) vMax.z = v.z;
            if (vMin.x > v.x) vMin.x = v.x;
            if (vMin.y > v.y) vMin.y = v.y;
            if (vMin.z > v.z) vMin.z = v.z;
        }

        Vertex center = new Vertex();     // \u5916\u63a5\u7403\u306e\u4e2d\u5fc3\u5ea7\u6a19
        center.x = 0.5f * (vMax.x - vMin.x);
        center.y = 0.5f * (vMax.y - vMin.y);
        center.z = 0.5f * (vMax.z - vMin.z);
        float r = -1;                       // \u534a\u5f84
        for(Vertex v : vertices) {
            if (r < Vertex.dist(center, v)) r = Vertex.dist(center, v);
        }
        r += 0.1f;                          // \u3061\u3087\u3063\u3068\u304a\u307e\u3051

        //   1-2: \u7403\u306b\u5916\u63a5\u3059\u308b\u56db\u9762\u4f53\u3092\u6c42\u3081\u308b
        Vertex v1 = new Vertex();
        v1.x = center.x;
        v1.y = center.y + 3.0f*r;
        v1.z = center.z;

        Vertex v2 = new Vertex();
        v2.x = center.x - 2.0f*(float)Math.sqrt(2)*r;
        v2.y = center.y - r;
        v2.z = center.z;

        Vertex v3 = new Vertex();
        v3.x = center.x + (float)Math.sqrt(2)*r;
        v3.y = center.y - r;
        v3.z = center.z + (float)Math.sqrt(6)*r;

        Vertex v4 = new Vertex();
        v4.x = center.x + (float)Math.sqrt(2)*r;
        v4.y = center.y - r;
        v4.z = center.z - (float)Math.sqrt(6)*r;

        Vertex[] outer = {v1, v2, v3, v4};
        tetras.add(new Tetrahedron(v1, v2, v3, v4));

        ArrayList<Tetrahedron> tmpTList = new ArrayList<Tetrahedron>();
        ArrayList<Tetrahedron> newTList = new ArrayList<Tetrahedron>();
        ArrayList<Tetrahedron> removeTList = new ArrayList<Tetrahedron>();
        for(Vertex v : vertices) {
            tmpTList.clear();
            newTList.clear();
            removeTList.clear();
            for (Tetrahedron t : tetras) {
                if((t.center != null) && (t.radius > Vertex.dist(v, t.center))) {
                    tmpTList.add(t);
                }
            }

            for (Tetrahedron t1 : tmpTList) {
                // \u307e\u305a\u305d\u308c\u3089\u3092\u524a\u9664
                tetras.remove(t1);

                v1 = t1.vertices[0];
                v2 = t1.vertices[1];
                v3 = t1.vertices[2];
                v4 = t1.vertices[3];
                newTList.add(new Tetrahedron(v1, v2, v3, v));
                newTList.add(new Tetrahedron(v1, v2, v4, v));
                newTList.add(new Tetrahedron(v1, v3, v4, v));
                newTList.add(new Tetrahedron(v2, v3, v4, v));
            }

            boolean[] isRedundancy = new boolean[newTList.size()];
            for (int i = 0; i < isRedundancy.length; i++) isRedundancy[i] = false;
            for (int i = 0; i < newTList.size()-1; i++) {
                for (int j = i+1; j < newTList.size(); j++) {
                    if(newTList.get(i).equals(newTList.get(j))) {
                        isRedundancy[i] = isRedundancy[j] = true;
                    }
                }
            }
            
            for (int i = 0; i < isRedundancy.length; i++) {
                if (!isRedundancy[i]) {
                    tetras.add(newTList.get(i));
                }

            }
            
        }

        
        boolean isOuter = false;
        for (Tetrahedron t4 : tetras) {
            isOuter = false;
            for (Vertex p1 : t4.vertices) {
                for (Vertex p2 : outer) {
                    if (p1.x == p2.x && p1.y == p2.y && p1.z == p2.z) {
                        isOuter = true;
                    }
                }
            }
            if (isOuter) {
                tetras.remove(t4);
            }
        }

        triangles.clear();
        boolean isSame = false;
        for (Tetrahedron t : tetras) {
            for (Edge l1 : t.getLines()) {
                isSame = false;
                for (Edge l2 : edges) {
                    if (l2.equals(l1)) {
                        isSame = true;
                        break;
                    }
                }
                if (!isSame) {
                    edges.add(l1);
                }
            }
        }


       
        ArrayList<Triangle> triList = new ArrayList<Triangle>();
        for (Tetrahedron t : tetras) {
            v1 = t.vertices[0];
            v2 = t.vertices[1];
            v3 = t.vertices[2];
            v4 = t.vertices[3];

            Triangle tri1 = new Triangle(v1, v2, v3);
            Triangle tri2 = new Triangle(v1, v3, v4);
            Triangle tri3 = new Triangle(v1, v4, v2);
            Triangle tri4 = new Triangle(v4, v3, v2);

            Vertex n;

            n = tri1.getNormal();
            if(n.dot(v1) > n.dot(v4)) tri1.turnBack();

            n = tri2.getNormal();
            if(n.dot(v1) > n.dot(v2)) tri2.turnBack();

            n = tri3.getNormal();
            if(n.dot(v1) > n.dot(v3)) tri3.turnBack();

            n = tri4.getNormal();
            if(n.dot(v2) > n.dot(v1)) tri4.turnBack();

            triList.add(tri1);
            triList.add(tri2);
            triList.add(tri3);
            triList.add(tri4);
        }
        
        boolean[] isSameTriangle = new boolean[triList.size()];
        for(int i = 0; i < triList.size()-1; i++) {
            for(int j = i+1; j < triList.size(); j++) {
                if (triList.get(i).equals(triList.get(j))) isSameTriangle[j] = true;
            }
        }
        int xx = 0;
        for(int i = 0; i < isSameTriangle.length; i++) {
            if (!isSameTriangle[i]) {
                triangles.add(triList.get(i));
                xx++;
            }
        }
/*
        surfaceEdges.clear();
        ArrayList<Edge> surfaceEdgeList = new ArrayList<Edge>();
        for(Triangle tri : triangles) {
            surfaceEdgeList.addAll(Arrays.asList(tri.getEdges()));
        }
        boolean[] isRedundancy = new boolean[surfaceEdgeList.size()];
        for(int i = 0; i < surfaceEdgeList.size()-1; i++) {
            for (int j = i+1; j < surfaceEdgeList.size(); j++) {
                if (surfaceEdgeList.get(i).equals(surfaceEdgeList.get(j))) isRedundancy[j] = true;
            }
        }

        for (int i = 0; i < isRedundancy.length; i++) {
            if (!isRedundancy[i]) surfaceEdges.add(surfaceEdgeList.get(i));
        }*/
        
    }
    
    public void computeWeight(){
        float totalWeight = 0;
        for (Edge e: edges)
            totalWeight += Vertex.dist(e.start, e.end);
        
        System.out.println("Total Weight: " + totalWeight);
    }
}
