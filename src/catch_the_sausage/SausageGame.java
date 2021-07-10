package catch_the_sausage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class SausageGame extends JFrame{

    private static SausageGame game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image sausage;
    private static float sausage_left = 200;
    private static float sausage_top = -100;
    private static float sausage_v = 200;
    private static int score;

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(SausageGame.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(SausageGame.class.getResourceAsStream("game_over.png"));
        sausage = ImageIO.read(SausageGame.class.getResourceAsStream("sausage.png"));
        game_window = new SausageGame();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = sausage_left + sausage.getWidth(null);
                float drop_bottom = sausage_top + sausage.getHeight(null);
                boolean is_drop = x >= sausage_left && x <= drop_right && y >= sausage_top && y <= drop_bottom;
                if(is_drop) {
                    sausage_top = -100;
                    sausage_left = (int) (Math.random() * (game_field.getWidth() - sausage.getWidth(null)));
                    sausage_v = sausage_v + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        sausage_top = sausage_top + sausage_v * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(sausage, (int) sausage_left, (int) sausage_top, null);
        if (sausage_top > game_window.getHeight()) g.drawImage(game_over, 280, 120, null);
    }

    private static class GameField extends JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}