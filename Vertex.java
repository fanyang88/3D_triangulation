package delaunaythreed;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yu
 */
public class Vertex {
    float x;
    float y;
    float z;
    public Vertex(){
        x = 0;
        y = 0;
        z = 0;
    }
    
    public Vertex(float xx, float yy, float zz){
        x = xx;
        y = yy;
        z = zz;
    }
    
    public static float dist(Vertex a, Vertex b){
        double x2 = Math.pow(a.x - b.x, 2);
        double y2 = Math.pow(a.y - b.y, 2);
        double z2 = Math.pow(a.z - b.z, 2);
        return (float)Math.sqrt(x2 + y2 +z2);
    }
    
    public Vertex cross(Vertex v, Vertex target) {
    float crossX = y * v.z - v.y * z;
    float crossY = z * v.x - v.z * x;
    float crossZ = x * v.y - v.x * y;

    if (target == null) {
      target = new Vertex(crossX, crossY, crossZ);
    } else {
      target.set(crossX, crossY, crossZ);
    }
    return target;
  }
  
   public Vertex cross(Vertex v) {
    return cross(v, null);
  }
   
   public void set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
   }
   
   public void normalize() {
    float m = mag();
    if (m != 0 && m != 1) {
      div(m);
    }
  }
   
   public float mag() {
    return (float) Math.sqrt(x*x + y*y + z*z);
  }
   
   public void div(float n) {
    x /= n;
    y /= n;
    z /= n;
  }
   
   public float dot(Vertex v) {
    return x*v.x + y*v.y + z*v.z;
  }
}