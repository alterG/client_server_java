/**
 * Created by alterG on 24.12.2016.
 */
public class Dot {


    private double x;
    private double y;
    private boolean lastIncludeStatus;
    private boolean currentIncludeStatus;
    private boolean isReceived;
    private boolean isNew;

    //getters and setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public boolean getReceived() {
        return isReceived;
    }
    public void setReceived(boolean received) {
        isReceived = received;
    }
    public boolean getLastIncludeStatus() {
        return lastIncludeStatus;
    }
    public void setLastIncludeStatus(boolean lastIncludeStatus) {
        this.lastIncludeStatus = lastIncludeStatus;
    }
    public boolean getCurrentIncludeStatus() {
        return currentIncludeStatus;
    }
    public void setCurrentIncludeStatus(boolean currentIncludeStatus) {
        this.currentIncludeStatus = currentIncludeStatus;
    }
    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    //constructor
    public Dot(double x, double y) {
        this.x=x;
        this.y=y;
        isNew=true;
    }

    @Override
    public int hashCode() {
        return new Double(x).hashCode()+new Double(y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;
        if (this.getClass()!= obj.getClass()) return false;
        Dot temp = (Dot) obj;
        return (x==temp.getX() && y==temp.getY());
    }

    @Override
    public String toString() {
        return ("{"+x+", "+y+"}");
    }
}

