import java.lang.Math;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;    
import java.awt.Font;
import java.awt.Color;

public class Game
{
    //Launch the game and apply all the different interactions
    public static void main(String[] args) throws Exception
    {
        StdDraw.setCanvasSize(1920, 1080);
        StdDraw.setXscale(0, 1920);
        StdDraw.setYscale(0, 1080);
        StdDraw.enableDoubleBuffering();
        Player player = new Player(960, 100, 1, 3);
        Missile[] missile_arr = new Missile[20];
        Missile[] ennemy_missile_arr = new Missile[20];
        Ennemy[][] ennemy_arr = Ennemy.Initialize_arr();
        int[] move = new int[2];
        move[0] = 1;
        move[1] = 0;
        int[] is_finished = {0};
        Missile last_missile = null;
        Missile last_ennemy_missile = null;
        int score = 0;
        int isTitleMenu = 1;
        int isLeaderboard = 0;
        
        while(is_finished[0] == 0)
        {
            while(isTitleMenu == 1)
            {
                if(isLeaderboard == 0)
                {
                    StdDraw.picture(960, 540, "Home.png");
                    StdDraw.show();
                }
                if(StdDraw.isKeyPressed(32))
                {
                    isTitleMenu = 0;
                }
                if(StdDraw.isKeyPressed(67))
                {
                    isLeaderboard = 0;
                }
                if(StdDraw.isKeyPressed(76))
                {
                    isLeaderboard = 1;
                    StdDraw.picture(960, 540, "Leaderboard.png");
                    DisplayLeaderboard();
                    StdDraw.show();
                }
                if(StdDraw.isKeyPressed(69))
                    System.exit(0);
            }
            if(StdDraw.isKeyPressed(27))
            {
                while(true)
                {
                    StdDraw.clear();
                    StdDraw.picture(960, 540, "Pause menu.png");
                    StdDraw.show();
                    if(StdDraw.isKeyPressed(69))
                    {
                        player = new Player(960, 100, 1, 3);
                        missile_arr = new Missile[20];
                        ennemy_missile_arr = new Missile[20];
                        ennemy_arr = Ennemy.Initialize_arr();
                        move = new int[2];
                        move[0] = 1;
                        move[1] = 0;
                        is_finished[0] = 0;
                        last_missile = null;
                        last_ennemy_missile = null;
                        score = 0;
                        isTitleMenu = 1;
                        break;
                    }
                    if(StdDraw.isKeyPressed(71))
                    {
                        break;
                    }
                }
            }
            StdDraw.picture(960, 540, "Background.png");
            player.Draw();
            Draw_Ennemies(ennemy_arr);
            move = Ennemy.UpdateEnnemies(ennemy_arr, move);
            if(StdDraw.isKeyPressed(32))
            {
                if(last_missile == null || last_missile.fire_rate.getTime() == 0)
                {
                    Missile missile = new Missile(player.x, player.y, "Missile.png", 500, player.rotate);
                    last_missile = AddMissile(missile, missile_arr);
                }
            }
            if(StdDraw.isKeyPressed(38))
            {
                if(player.rotate == 360)
                    player.rotate = 0;
                if(player.rotate < 46 || player.rotate >= 314)
                    player.rotate += 2;
            }
            if(StdDraw.isKeyPressed(40))
            {
                if(player.rotate == 0)
                    player.rotate = 360;
                if(player.rotate > 314 || player.rotate <= 46)
                    player.rotate -= 2;
            }
            if(StdDraw.isKeyPressed(37))
            {
                player.Move_left();
            }
            if (StdDraw.isKeyPressed(39))
            {
                player.Move_right();
            }
            
            //probabilities to see which ennemy will shot
            int proba = (int)(Math.random() * 40);
            while(ennemy_arr[proba / 10][proba % 10] == null)
                proba = (int)(Math.random() * 40);
            if(last_ennemy_missile == null || (last_ennemy_missile.fire_rate.getTime() == 0 
                                                   && ennemy_arr[proba / 10][proba % 10].can_fire.getTime() == 0))
            {
                Ennemy shooter = ennemy_arr[proba / 10][proba % 10];
                Missile missile = new Missile(shooter.x, shooter.y, "Bomb.png", 200, 0);
                last_ennemy_missile = AddMissile(missile, ennemy_missile_arr);
            }
            score = UpdateMissiles(missile_arr, ennemy_arr, score);
            Draw_missiles(missile_arr);
            player = UpdateEnnemiesMissiles(player, ennemy_arr, missile_arr, move, ennemy_missile_arr, is_finished);
            Draw_missiles(ennemy_missile_arr);
            
            //Case lose because of lifes
            if(player.lives == 0)
            {
                StdDraw.clear();
                StdDraw.picture(960, 540, "Game over.png");
                ChangeLeaderboard(score);
                while(true)
                {
                    StdDraw.show();
                    if(StdDraw.isKeyPressed(82))
                    {
                        player = new Player(960, 100, 1, 3);
                        missile_arr = new Missile[20];
                        ennemy_missile_arr = new Missile[20];
                        ennemy_arr = Ennemy.Initialize_arr();
                        move = new int[2];
                        move[0] = 1;
                        move[1] = 0;
                        is_finished[0] = 0;
                        last_missile = null;
                        last_ennemy_missile = null;
                        score = 0;
                        break;
                    }
                    if(StdDraw.isKeyPressed(69))
                        System.exit(0);
                    if(StdDraw.isKeyPressed(83))
                    {
                        StdDraw.picture(960, 540, "Leaderboard.png");
                        DisplayLeaderboard();
                    }
                    if(StdDraw.isKeyPressed(67))
                    {
                        StdDraw.picture(960, 540, "Game over.png");
                    }
                }
            }
            
            //Case win
            if (CheckWin(ennemy_arr) == 1)
            {
                StdDraw.clear();
                StdDraw.picture(960, 590, "Winner.png");
                while(true)
                {
                    StdDraw.show();
                    if(StdDraw.isKeyPressed(67))
                    {
                        player = new Player(960, 100, 1, player.lives);
                        missile_arr = new Missile[20];
                        ennemy_missile_arr = new Missile[20];
                        ennemy_arr = Ennemy.Initialize_arr();
                        move = new int[2];
                        move[0] = 1;
                        move[1] = 0;
                        is_finished[0] = 0;
                        last_missile = null;
                        last_ennemy_missile = null;
                        break;
                    }
                    if(StdDraw.isKeyPressed(72))
                    {
                        ChangeLeaderboard(score);
                        isTitleMenu = 1;
                        break;
                    }
                    if(StdDraw.isKeyPressed(69))
                        System.exit(0);
                }
            }
            
            //Case lose because of ennemy touch ground
            if(CheckGround(ennemy_arr) == 1)
            {
                StdDraw.clear();
                StdDraw.picture(960, 540, "Game over.png");
                ChangeLeaderboard(score);
                while(true)
                {
                    StdDraw.show();
                    if(StdDraw.isKeyPressed(82))
                    {
                        player = new Player(960, 100, 1, 3);
                        missile_arr = new Missile[20];
                        ennemy_missile_arr = new Missile[20];
                        ennemy_arr = Ennemy.Initialize_arr();
                        move = new int[2];
                        move[0] = 1;
                        move[1] = 0;
                        is_finished[0] = 0;
                        last_missile = null;
                        last_ennemy_missile = null;
                        score = 0;
                        break;
                    }
                    if(StdDraw.isKeyPressed(69))
                        System.exit(0);
                    if(StdDraw.isKeyPressed(83))
                    {
                        StdDraw.picture(960, 540, "Leaderboard.png");
                        DisplayLeaderboard();
                    }
                    if(StdDraw.isKeyPressed(67))
                    {
                        StdDraw.picture(960, 540, "Game over.png");
                    }
                }
            }
            StdDraw.setFont(new Font("Arial",  Font.BOLD, 30));
            StdDraw.setPenColor(StdDraw.ORANGE);
            StdDraw.textLeft(50, 50, "Score : " + Integer.toString(score));
            StdDraw.textLeft(50, 100, "Lives : " + Integer.toString(player.lives));
            StdDraw.show();
            StdDraw.clear();
        }
        StdDraw.show();
    }
    
    //Change the leaderboard with the score after a game over
    public static void ChangeLeaderboard(int finalscore) throws Exception
    {
        int changed = 0;
        try (BufferedReader leaderBoard = new BufferedReader(new FileReader("Score.txt")))
        {
            int[] score = new int[10];
            for(int i = 0; i < 10; i++)
            {
                score[i] = Integer.parseInt(leaderBoard.readLine());
                if(finalscore > score[i])
                {
                    int a = score[i];
                    score[i] = finalscore;
                    finalscore = a;
                }
            }
            changed = 1;
            PrintWriter file = new PrintWriter("Score.txt", "UTF-8");
            for(int i = 0; i < 10; i++)
            {
                file.println(score[i]);
            }
            
            file.close();
            leaderBoard.close();
        }
    }
    
    //Display the leaderboard with values in .txt
    public static void DisplayLeaderboard() throws Exception
    {
        int displayed = 0;
        try (BufferedReader leaderBoard = new BufferedReader(new
                                                                 FileReader("Score.txt")))
        {
            int[] score = new int[10];
            int y = 800;
            int x = 400;
            for(int i = 0; i < 10; i++)
            {
                score[i] = Integer.parseInt(leaderBoard.readLine());
                StdDraw.setFont(new Font("Arial",  Font.BOLD, 100));
                StdDraw.setPenColor(StdDraw.ORANGE);
                StdDraw.textLeft(x, y, Integer.toString(i + 1) + " : " + Integer.toString(score[i]));
                y -= 150;
                if(i == 4)
                {
                    x += 800;
                    y = 800;
                }
            }
            
            displayed = 1;
            leaderBoard.close();
        }
    }
    
    //Check if all the ennemies are dead
    public static int CheckWin(Ennemy[][] ennemy_arr)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(ennemy_arr[i][j] != null)
                {
                    return 0;
                }
            }
        }
        
        return 1;
    }
    
    //Check if the ennemies reached the ground
    public static int CheckGround(Ennemy[][] ennemy_arr)
    {
        for(int j = 0; j < 4; j++)
        {
            for(int k = 0; k < 10; k++)
            {
                if(ennemy_arr[j][k] != null)
                {
                    if (ennemy_arr[j][k].y <= 50)
                        return 1;
                }
            }
        }
        return 0;
    }
    
    //Reset the game
    public static Player ResetGame(Player player, Ennemy[][] ennemy_arr, Missile[] missile_arr, int[] move, 
        Missile[] ennemy_missile_arr)
    {
        int lives = player.lives;
        player = new Player(player.x, player.y, 1, lives);
        missile_arr = new Missile[20];
        ennemy_missile_arr = new Missile[20];
        player.Draw();
        
        return player;
    }
    
    //Draw all the missiles on the screen
    public static void Draw_missiles(Missile[] missile_arr)
    {
        for(int i = 0; i < 20; i++)
        {
            if(missile_arr[i] != null)
                missile_arr[i].Draw();
        }
    }
    
    //Add a missile in the list of missiles which have to be drawn
    public static Missile AddMissile(Missile missile, Missile[] missile_arr)
    {
        for(int i = 0; i < 20; i++)
        {
            if(missile_arr[i] == null)
            {
                missile_arr[i] = missile;
                break;
            }
        }
        
        return missile;
    }
    
    //Update the position of all the missiles of the list
    //Delete a missile if he is out of the screen
    //Delete the missile and the ennemy if the ennemy get shot and updatethe score
    public static int UpdateMissiles(Missile[] missile_arr, Ennemy[][] ennemy_arr, int score)
    {
        for(int i = 0; i < 20; i++)
        {
            if(missile_arr[i] != null)
            {
                if(missile_arr[i].y >= 1080)
                {
                    missile_arr[i] = null;
                }
                else
                {
                    missile_arr[i].Move();
                    for(int j = 0; j < 4; j++)
                    {
                        for(int k = 0; k < 10; k++)
                        {
                            if(ennemy_arr[j][k] != null && missile_arr[i] != null && ennemy_arr[j][k].x - 20 <= missile_arr[i].x 
                                   && ennemy_arr[j][k].x + 20 >= missile_arr[i].x && ennemy_arr[j][k].y - 20 <= missile_arr[i].y
                              && ennemy_arr[j][k].y + 20 >= missile_arr[i].y)
                            {
                                ennemy_arr[j][k] = null;
                                missile_arr[i] = null;
                                score += 10;
                            }
                        }
                    }
                }
            }
        }
        return score;
    }
    
    //
    public static Player UpdateEnnemiesMissiles(Player player, Ennemy[][] ennemy_arr, Missile[] missile_arr, int[] move, 
        Missile[] ennemy_missile_arr, int[] is_finished)
    {
        for(int i = 0; i < 20; i++)
        {
            if(ennemy_missile_arr[i] != null)
            {
                if(ennemy_missile_arr[i].y <= 0)
                {
                    ennemy_missile_arr[i] = null;
                }
                else
                {
                    ennemy_missile_arr[i].MoveDown();
                    if(player.x - 30 <= ennemy_missile_arr[i].x && player.x + 30 >= ennemy_missile_arr[i].x 
                           && player.y + 20 >= ennemy_missile_arr[i].y)
                    {
                        player.lives--;
                        ennemy_missile_arr[i] = null;
                        if(player.lives == 0)
                        {
                            is_finished[0] = 1;
                        }
                        else
                        {
                            player = ResetGame(player, ennemy_arr, missile_arr, move, ennemy_missile_arr);
                        }
                    }
                }
            }
        }
        
        return player;
    }
    
    //Draw all the ennemies on the screen
    public static void Draw_Ennemies(Ennemy[][] ennemy_arr)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(ennemy_arr[i][j] != null)
                    ennemy_arr[i][j].Draw();
            }
        }
    }
}