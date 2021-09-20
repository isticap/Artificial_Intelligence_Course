public class Inventory
{
    private String item;
    private int x;
    private int y;

    public Inventory(String item, int x, int y)
    {
        this.item = item;
        this.x = x;
        this.y = y;
    }

    public String getItem()
    {
        return item;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String toString()
    {
        return "markerType: " + item + "\n" + "xAxis: " + x + "\n"
                    + "yAxis: " + y;
    }

}