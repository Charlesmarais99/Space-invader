public class Missile
{
    double x;
    double y;
    double start_y;
    int rotate;
    String picture;
    GameTimer fire_rate;
    
    //Initialise the object Missile
    public Missile(double x, double y, String picture, long rate, int rotate)
    {
        this.x = x;
        this.y = y;
        start_y = y;
        this.rotate = rotate;
        this.picture = picture;
        fire_rate = new GameTimer(rate);
    }
    
    //Move the missile
    public void Move()
    {
        y += 15 * Math.cos(Math.toRadians(360 - rotate));
        x += 15 * Math.sin(Math.toRadians(360 - rotate));
    }
    
    public void MoveDown()
    {
        y -= 5;
    }
    
    //Draw the missile
    public void Draw()
    {
        StdDraw.picture(x, y, picture, rotate);
    }
}