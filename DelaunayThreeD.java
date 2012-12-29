package delaunaythreed;
/**
 * @author Yu
 */
public class DelaunayThreeD {

    public static void main(String[] args) {
        // Read vertices from dataset
        Delaunay d = new Delaunay();
        d.readData();
        d.computeDelaunay();
        System.out.println("triangles: "+d.triangles.size());
        System.out.println("edges: "+d.edges.size());
        System.out.println("tetra: "+d.tetras.size());
        d.computeWeight();
        
    }
}
