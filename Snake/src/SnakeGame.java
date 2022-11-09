import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;


public class SnakeGame extends JFrame {
    private GamePanel p = new GamePanel();


    public SnakeGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("뱀 게임");

        Container c = getContentPane();
        this.setLocationRelativeTo(null);

        c.add(p);

        ImageIcon img = new ImageIcon("/Users/sewoongyim/eclipse-workspace/boiru/Images/head.jpg");
        JLabel j = new JLabel(img);

        j.setLocation(30, 30);
        j.setSize(img.getIconWidth(), img.getIconWidth());

        c.add(j);

        p.requestFocus();
        p.setFocusable(true);

        Thread t = new Thread(p);
        t.start();

        //위쪽 줄 만들기
        int topRows = 20;
        JLabel[] topJl = new JLabel[43];

        for (int i = 0; i < 43; i++) {
            topJl[i] = new JLabel("ㅡ");
            topJl[i].setLocation(topRows, 10);
            topJl[i].setSize(20, 20);

            c.add(topJl[i]);

            topRows += 20;
        }


        //밑줄 만들기
        int underRows = 10;
        JLabel[] underJl = new JLabel[43];

        for (int i = 0; i < 43; i++) {
            underJl[i] = new JLabel("ㅡ");
            underJl[i].setLocation(underRows, 700);
            underJl[i].setSize(20, 20);

            c.add(underJl[i]);

            underRows += 20;
        }

        //왼쪽 줄 만들기
        int leftColumn = 19;
        JLabel[] leftJl = new JLabel[35];

        for (int i = 0; i < 35; i++) {
            leftJl[i] = new JLabel("|");
            leftJl[i].setLocation(15, leftColumn);
            leftJl[i].setSize(20, 20);

            c.add(leftJl[i]);

            leftColumn += 20;
        }

        //오른쪽 줄 만들기
        int rightColumn = 19;
        JLabel[] rightJl = new JLabel[35];

        for (int i = 0; i < 35; i++) {
            rightJl[i] = new JLabel("|");
            rightJl[i].setLocation(865, rightColumn);
            rightJl[i].setSize(20, 20);

            c.add(rightJl[i]);

            rightColumn += 20;
        }

        setSize(900, 900);
        setVisible(true);
    }

    public class GamePanel extends JPanel implements Runnable {
        SnakeBody sb;
        private int direction = 0;
        private final int STANDBY = 0;
        private final int RIGHT = 1;
        private final int LEFT = 2;
        private final int TOP = 3;
        private final int BOTTOM = 4;

        public GamePanel() {
            this.setLocation(30, 30);
            this.setLayout(null);

            sb = new SnakeBody();
            sb.AddBody(this);

            direction = STANDBY;

            this.addKeyListener(new MyKeyListener());
            this.setSize(830, 670);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300);
                    sb.Move(direction);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        public class MyKeyListener extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    direction = 1;
                } else if ((e.getKeyCode() == KeyEvent.VK_LEFT)) {
                    direction = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    direction = 3;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    direction = 4;
                }
            }
        }
    }

    public class SnakeBody {
        private JLabel[] jl = new JLabel[100];
        private int size = 5;
        private int x = 200, y = 500;
        private JLabel apple = new JLabel("■");
        private int appleX, appleY;
        private Random r = new Random();
        boolean eatApple = false;

        public SnakeBody() {
            Font headFont = new Font("Serif", Font.BOLD, 30);
            Font bodyFont = new Font("Serif", Font.BOLD, 20);
            Font appleFont = new Font("Serif", Font.BOLD, 30);

            //head
            jl[0] = new JLabel("■");
            jl[0].setFont(headFont);
            jl[0].setLocation(x, y);
            jl[0].setSize(20, 20);

            //apple
            appleX = r.nextInt(500);
            appleY = r.nextInt(500);

            apple.setFont(appleFont);
            apple.setLocation(appleX, appleY);
            apple.setSize(30, 30);

            for (int i = 1; i < 100; i++) {
                jl[i] = new JLabel("■");
                jl[i].setFont(bodyFont);
                jl[i].setSize(20, 20);
            }

            for (int j = 1; j < size; j++) {
                x -= 20;
                jl[j].setLocation(x, y);
            }
        }

        // 뱀 이동하는 Move
        public void Move(int direction) {
            if (direction == 0) {
                return;
            }

            //죽었을 때
            for (int i = 1; i < size; i++) {
                if ((jl[0].getX() - jl[i].getX() < 3 && jl[0].getX() - jl[i].getX() > -3)
                        && (jl[0].getY() - jl[i].getY() < 3 && jl[0].getY() - jl[i].getY() > -3)) {
                    JLabel endLabel = new JLabel("D    I    E");
                    endLabel.setSize(100, 400);
                    endLabel.setLocation(400, 30);

                    p.add(endLabel);

                    return;
                }
            }

            for (int i = size - 1; i > 0; i--) {
                jl[i].setLocation(jl[i - 1].getX(), jl[i - 1].getY());
            }

            switch (direction) {
                case 1:
                    jl[0].setLocation(jl[0].getX() + 20, jl[0].getY());
                    break;
                case 2:
                    jl[0].setLocation(jl[0].getX() - 20, jl[0].getY());
                    break;
                case 3:
                    jl[0].setLocation(jl[0].getX(), jl[0].getY() + 20);
                    break;
                case 4:
                    jl[0].setLocation(jl[0].getX(), jl[0].getY() - 20);
                    break;
                default:
                    break;
            }

            //사과 먹었을 때
            if ((jl[0].getX() - apple.getX() < 20 && jl[0].getX() - apple.getX() > -20)
                    && (jl[0].getY() - apple.getY() < 20 && jl[0].getY() - apple.getY() > -20)) {
                appleX = r.nextInt(500);
                appleY = r.nextInt(500);
                apple.setLocation(appleX, appleY);

                //뱀 몸통 추가
                switch (direction) {
                    case 1:
                        jl[size].setLocation(jl[size].getX() - 20, jl[size].getY());
                        p.add(jl[size]);
                        size++;
                        break;
                    case 2:
                        jl[size].setLocation(jl[size].getX() + 20, jl[size].getY());
                        p.add(jl[size]);
                        size++;
                        break;
                    case 3:
                        jl[size].setLocation(jl[size].getX(), jl[size].getY() - 20);
                        p.add(jl[size]);
                        size++;
                        break;
                    case 4:
                        jl[size].setLocation(jl[size].getX(), jl[size].getY() + 20);
                        p.add(jl[size]);
                        size++;
                        break;
                    default:
                        break;
                }
            }
        }

        // 패널에 뱀 몸 추가하기
        public void AddBody(JPanel jp) {
            ImageIcon img = new ImageIcon("Images\\head.jpg");
            JTextField j = new JTextField("img");

            j.setLocation(30, 30);
            j.setSize(img.getIconWidth(), img.getIconWidth());

            jp.add(j);

            for (int i = 0; i < size; i++) {
                jp.add(jl[i]);
            }
            jp.add(apple);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SnakeGame();
    }
}