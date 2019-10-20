/**
 * @author: 马士兵教育
 * @create: 2019-08-24 16:13
 */
public class Point {
    int x;
    int y;

    //设置二维的坐标点
    public void set(int xx,int yy){
        x = xx;
        y = yy;
    }

    public double calcDistance(Point p){
        return Math.sqrt((p.x-x)*(p.x-x)+(p.y-y)*(p.y-y));
    }

    public void changePoint(Point p){
        int a = p.x;
        p.x = x;
        x = a;
        int b = p.y;
        p.y = y;
        y = b;
    }

    public void show(){
        System.out.println("["+x+","+y+"]");
    }

    public static void main(String[] args) {
        Point p1 = new Point();
        p1.set(3,5);
        Point p2 = new Point();
        p2.set(2,4);
        System.out.println(p1.calcDistance(p2));
        p1.changePoint(p2);
        p1.show();
        p2.show();

    }
}
