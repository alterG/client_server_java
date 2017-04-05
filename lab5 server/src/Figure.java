/**
 * Created by alterG on 24.12.2016.
 */
public class Figure {
    private static double R;
    public static void setR(double Radius) {
        R=Radius;
    }

 /*   public static int isIncluded(Dot dot) {
        boolean isIncluded = false;
        if ((dot.getX()>=-R/2) && (dot.getX()<=0)) {
            isIncluded = ((dot.getY()<=R) && (dot.getY()>=0));
        }
        if ((dot.getX()>0) && (dot.getX()<=R/2)) {
            isIncluded = ((dot.getY()<=R-dot.getX()) && (dot.getY()>=-Math.sqrt(R/4*R-dot.getX()*dot.getX())));
        }
        if ((dot.getX()>R/2) && (dot.getX()<=R)) {
            isIncluded = ((dot.getY()<=R-dot.getX()) && (dot.getY()>=0));
        }
        return  isIncluded?1:0;
    }*/

    public static int isIncluded(double x, double y) {
        boolean isIncluded = false;
        if ((x>=-R/2) && (x<=0)) {
            isIncluded = ((y<=R) && (y>=0));
        }
        if ((x>0) && (x)<=R/2) {
            isIncluded = ((y<=R-x) && (y>=-Math.sqrt(R/4*R-x*x)));
        }
        if ((x>R/2) && (x<=R)) {
            isIncluded = ((y<=R-x) && (y>=0));
        }
        return  isIncluded?1:0;
    }
}

