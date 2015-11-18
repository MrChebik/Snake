import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

/**
 * Created by alex on 30.10.15.
 * @author MrChebik
 * @version 0.4.1
 */

public class Main extends JPanel {
    /**
     * Во время паузы запрещаем менять направление
     */
    int pause_key = 0;

    /**
     * Во время движения, запрещаем обрабатывать более 1 раза нажатия W, A, S, D, или аналогичные для стрелок
     */
    int pause_key_timer = 1;

    /**
     * Для паузы
     */
    static int start_game = 0;

    /**
     * Для генерации яблок
     */
    int signal = 1;
    int signal1 = 0;

    /**
     * Значение прозрачности
     */
    float over_int = 0;

    /**
     * Переменная необходима лишь 1 раз, для корректного отображения Аркады
     */
    int lvl = 0;

    static int col_score = 0;
    int best_score = 0;
    int best_length = 0;

    /**
     * Узнаем название ОС
     */
    static String OSname = System.getProperty("os.name");
    static int OS = 0, OS1 = 0, OS2 = 0;

    static JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    static JLabel score = new JLabel("2");
    static JLabel pause = new JLabel("START");
    JMenuItem new_game = new JMenuItem("New game");
    static JMenuItem exit = new JMenuItem("Exit");
    JMenu preferences = new JMenu("Preferences");
    JLabel game_mode = new JLabel("    Game Mode");
    JLabel difficulty = new JLabel("    Level Difficulty");
    JLabel modifications = new JLabel("    Modifications");
    static JLabel win = new JLabel("YOU WON");
    JCheckBoxMenuItem teleport = new JCheckBoxMenuItem("Teleportation of Apple");
    JCheckBoxMenuItem two = new JCheckBoxMenuItem("Two Apple");
    JRadioButtonMenuItem arcade = new JRadioButtonMenuItem("Arcade");
    JRadioButtonMenuItem classic = new JRadioButtonMenuItem("Classic");
    JRadioButtonMenuItem unlimited = new JRadioButtonMenuItem("Unlimited");
    JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy");
    JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Normal");
    JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard");
    JMenu settings = new JMenu("Settings");
    JCheckBoxMenuItem sound = new JCheckBoxMenuItem("Sound");
    JCheckBoxMenuItem threeD = new JCheckBoxMenuItem("3D effect");
    JLabel score_player = new JLabel(" Length: ");
    JLabel best_score_player = new JLabel("                                                              Best Score: 0");
    JLabel best_length_player = new JLabel("                                                         Best Length: 0");
    JCheckBoxMenuItem animation = new JCheckBoxMenuItem("Animation");
    JMenu help = new JMenu("Help");
    JMenuItem about = new JMenuItem("About");
    static JLabel game_over = new JLabel("GAME OVER");

    static Snake s = new Snake(10, 10, 9, 10);
    Apple a = new Apple(new Random().nextInt(19), new Random().nextInt(19));
    Apple2 b = new Apple2(new Random().nextInt(19), new Random().nextInt(19));

    /**
     * Масштаб клетки X, Y
     */
    static final int SCALE = 32;

    /**
     * Кол-во клеток
     */
    static final int WIDTH = 20;
    static final int HEIGHT = 20;

    static int SPEED = 175;

    /**
     * Скорость до телепорта
     */
    int SPEED_TELEPORT = 4000;

    /**
     * Появление и исчезновение надписи: GAME OVER
     * @since 0.2
     */
    Timer tick_tack = new Timer(1000, e -> game_over.setVisible(game_over.isVisible() ? false : true));

    /**
     * Таймер до телепорта яблок, если активирована модификация - Teleportation of Apple
     * @since 0.3
     */
    Timer teleporting = new Timer(SPEED_TELEPORT, e -> {
        getSpeed();
        a.random();
        a_rerandom();
        if (two.isSelected() && two.isEnabled()) {
            b.random();
            b_rerandom();
        }
    });

    /**
     * Отрисовка анимации - затухание
     * @since 0.4
     */
    Timer over_time_dark = new Timer(10, e -> {
        if (over_int > 0.75) {
            this.over_time_dark.stop();
            over_int = (float) 0.76;
            gameover();
        } else {
            repaint();
            over_int += 0.01;
        }
        if (!animation.isSelected()) {
            over_int = 0;
            this.over_time_dark.stop();
            gameover();
            repaint();
        }
    });

    /**
     * Отрисовка анимации - "разжигание"
     * @since 0.4
     */
    Timer over_time_lightly = new Timer(10, e -> {
        if (over_int < 0.01) {
            this.over_time_lightly.stop();
            over_int = 0;
            start();
        } else {
            repaint();
            over_int -= 0.01;
        }
        if (!animation.isSelected()) {
            over_int = 0;
            this.over_time_lightly.stop();
            start();
            repaint();
        }
    });

    /**
     * Основная отрисовка
     */
    Timer t = new Timer(SPEED, e -> {
        s.move();
        if (classic.isSelected()) {
            tail();
            wall();
        } else if ((normal.isSelected() && normal.isEnabled()) || (easy.isSelected() && easy.isEnabled()) || unlimited.isSelected())
            s.mirror();
        if (arcade.isSelected())
            tail();
        if (unlimited.isSelected())
            s.eats();
        if (hard.isSelected() && hard.isEnabled())
            wall();

        /**
         * Яблоко съето
         */
        if ((s.snakeX[0] == a.posX && s.snakeY[0] == a.posY) || (s.snakeX[0] == b.posX && s.snakeY[0] == b.posY && two.isSelected() && two.isEnabled())) {
            s.length++;
            if (s.snakeX[0] == a.posX && s.snakeY[0] == a.posY) {
                a.random();
                a_rerandom();
            }
            if (two.isSelected() && two.isEnabled() && s.snakeX[0] == b.posX && s.snakeY[0] == b.posY) {
                b.random();
                b_rerandom();
            }
            if (teleport.isSelected() && teleport.isEnabled()) {
                teleporting.stop();
                getSpeed();
                teleporting.start();
            }
            if (classic.isSelected()) {
                col_score = s.length;
                score.setText(String.valueOf(col_score));
            }
            if (arcade.isSelected())
                score.setText(String.valueOf(col_score += new Random().nextInt(45) + 5));
            if (normal.isSelected() && normal.isEnabled() && SPEED > 150) {
                SPEED -= 2;
                this.t.setDelay(SPEED);
            }
            if (hard.isSelected() && hard.isEnabled() && SPEED > 75) {
                SPEED -= 4;
                this.t.setDelay(SPEED);
            }
            if (sound.isSelected())
                new addThread_Apple().start();
            if (classic.isSelected())
                check_length();
            if (arcade.isSelected())
                check_score();
        }

        repaint();
        pause_key_timer = 0;

        if (!teleport.isSelected() || !teleport.isEnabled())
            teleporting.stop();

        /**
         * WIN
         * @since 0.3
         */
        if (s.length == 400) {
            this.t.stop();
            if (teleporting.isRunning())
                teleporting.stop();
            win.setVisible(true);
        }
    });

    public static void main(String[] args) {
        if (!OSname.equals("Linux")) {
            OS = 2;
            OS1 = 3;
            OS2 = 8;
        }
        pause.setFont(new Font(null, Font.BOLD, 140));
        pause.setSize(480 + OS, 120 + OS1);
        pause.setForeground(Color.WHITE);
        pause.setLocation(89 - OS2, 260);
        game_over.setSize(700, 100);
        game_over.setLocation(13, 270);
        game_over.setFont(new Font(null, Font.BOLD, 100));
        game_over.setForeground(Color.WHITE);
        game_over.setVisible(false);
        win.setSize(750 + OS, 120 + OS1);
        win.setLocation(19 - OS2, 260);
        win.setFont(new Font(null, Font.BOLD, 120));
        win.setForeground(Color.WHITE);
        win.setVisible(false);
        JFrame f = new JFrame();
        f.add(game_over);
        f.add(win);
        f.setTitle("Snake");
        f.setSize(WIDTH * SCALE + 5 + OS, HEIGHT * SCALE + 49 + OS1);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setLayout(null);
        f.setJMenuBar(menu);
        f.add(pause);
        f.add(new Main());
        f.setVisible(true);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 102, 51));
        if (threeD.isSelected())
            g2.fill3DRect(0, 0, WIDTH * SCALE + 1, HEIGHT * SCALE + 1, true);
        else
            g2.fillRect(0, 0, WIDTH * SCALE + 1, HEIGHT * SCALE + 1);

        g2.setColor(new Color(128, 0, 0));
        for (int d = 1; d < s.length; d++)
            if (threeD.isSelected())
                g2.fill3DRect(s.snakeX[d] * SCALE + 1, s.snakeY[d] * SCALE + 1, SCALE, SCALE, true);

        g2.setColor(new Color(200, 0, 0));
        for (int d = 1; d < s.length; d++)
            if (threeD.isSelected())
                g2.fill3DRect(s.snakeX[d] * SCALE + 1, s.snakeY[d] * SCALE + 1, SCALE - 1, SCALE - 1, true);
            else
                g2.fillRect(s.snakeX[d] * SCALE + 1, s.snakeY[d] * SCALE + 1, SCALE - 1, SCALE - 1);

        g2.setColor(new Color(92, 0, 0));
        if (threeD.isSelected())
            g2.fill3DRect(s.snakeX[0] * SCALE + 1, s.snakeY[0] * SCALE + 1, SCALE, SCALE, true);

        g2.setColor(new Color(148, 0, 0));
        if (threeD.isSelected())
            g2.fill3DRect(s.snakeX[0] * SCALE + 1, s.snakeY[0] * SCALE + 1, SCALE - 1, SCALE - 1, true);
        else
            g2.fillRect(s.snakeX[0] * SCALE + 1, s.snakeY[0] * SCALE + 1, SCALE - 1, SCALE - 1);

        g2.setColor(new Color(166, 130, 2));
        if (threeD.isSelected())
            g2.fill3DRect(a.posX * SCALE + 1, a.posY * SCALE + 1, SCALE, SCALE, true);
        if (two.isSelected() && two.isEnabled())
            if (threeD.isSelected())
                g2.fill3DRect(b.posX * SCALE + 1, b.posY * SCALE + 1, SCALE, SCALE, true);

        g2.setColor(new Color(200, 165, 0));
        if (threeD.isSelected())
            g2.fill3DRect(a.posX * SCALE + 1, a.posY * SCALE + 1, SCALE - 1, SCALE - 1, true);
        else
            g2.fillRect(a.posX * SCALE + 1, a.posY * SCALE + 1, SCALE - 1, SCALE - 1);
        if (two.isSelected() && two.isEnabled())
            if (threeD.isSelected())
                g2.fill3DRect(b.posX * SCALE + 1, b.posY * SCALE + 1, SCALE - 1, SCALE - 1, true);
            else
                g2.fillRect(b.posX * SCALE + 1, b.posY * SCALE + 1, SCALE - 1, SCALE - 1);

        /**
         * Анимация затухания и наоборот
         * @since 0.4
         */
        g2.setColor(new Color(0, 0, 0));
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, over_int);
        g2.setComposite(alphaComposite);
        g2.fillRect(0, 0, 640, 640);
    }

    Main() {
        a_rerandom();

        /**
         * Меню бар
         * @since 0.2
         */
        menu.add(file);
        file.add(new_game);
        file.addSeparator();
        file.add(exit);
        exit.addActionListener(e -> System.exit(0));
        menu.add(preferences);
        preferences.add(game_mode);
        preferences.add(arcade);
        arcade.addActionListener(e -> {
            if (!classic.isSelected() && !unlimited.isSelected())
                arcade.setSelected(true);
            else
                start_anim_tick();
            classic.setSelected(false);
            unlimited.setSelected(false);
            preferences.doClick();
            easy.setEnabled(true);
            normal.setEnabled(true);
            hard.setEnabled(true);
            if (lvl == 0) {
                setNormal();
                b.random();
                b_rerandom();
                lvl = 1;
            }
            score.setVisible(true);
            score_player.setVisible(true);
            score_player.setText(" Score: ");
            score.setText("0");
            best_score_player.setVisible(true);
            best_length_player.setVisible(false);
            teleport.setEnabled(true);
            two.setEnabled(true);
        });
        preferences.add(classic);
        classic.addActionListener(e -> {
            if (!arcade.isSelected() && !unlimited.isSelected())
                classic.setSelected(true);
            else
                start_anim_tick();
            score_player.setText(" Length: ");
            arcade.setSelected(false);
            unlimited.setSelected(false);
            easy.setEnabled(false);
            normal.setEnabled(false);
            hard.setEnabled(false);
            score.setVisible(true);
            score_player.setVisible(true);
            best_score_player.setVisible(false);
            best_length_player.setVisible(true);
            teleport.setEnabled(false);
            two.setEnabled(false);
        });
        preferences.add(unlimited);
        unlimited.addActionListener(e -> {
            if (!arcade.isSelected() && !classic.isSelected())
                unlimited.setSelected(true);
            else
                start_anim_tick();
            arcade.setSelected(false);
            classic.setSelected(false);
            easy.setEnabled(false);
            normal.setEnabled(false);
            hard.setEnabled(false);
            score.setVisible(false);
            best_score_player.setVisible(false);
            best_length_player.setVisible(false);
            score_player.setVisible(false);
            teleport.setEnabled(false);
            two.setEnabled(false);
        });
        preferences.addSeparator();
        preferences.add(difficulty);
        preferences.add(easy);
        easy.addActionListener(e -> {
            if (!normal.isSelected() && !hard.isSelected())
                easy.setSelected(true);
            else
                start_anim_tick();
            normal.setSelected(false);
            hard.setSelected(false);
            preferences.doClick();
        });
        easy.setEnabled(false);
        preferences.add(normal);
        normal.addActionListener(e -> {
            setNormal();
            preferences.doClick();
        });
        normal.setEnabled(false);
        normal.setSelected(true);
        preferences.add(hard);
        hard.addActionListener(e -> {
            if (!easy.isSelected() && !normal.isSelected())
                hard.setSelected(true);
            else
                start_anim_tick();
            easy.setSelected(false);
            normal.setSelected(false);
            preferences.doClick();
        });
        hard.setEnabled(false);
        preferences.addSeparator();
        preferences.add(modifications);
        preferences.add(teleport);
        teleport.addActionListener(e -> {
            start_anim_tick();
            preferences.doClick();
        });
        teleport.setSelected(true);
        teleport.setEnabled(false);
        preferences.add(two);
        two.addActionListener(e -> {
            start_anim_tick();
            preferences.doClick();
        });
        two.setSelected(true);
        two.setEnabled(false);
        classic.setSelected(true);
        menu.add(settings);
        settings.add(threeD);
        threeD.addActionListener(e -> {
            repaint();
            settings.doClick();
        });
        threeD.setSelected(true);
        settings.addSeparator();
        settings.add(animation);
        animation.addActionListener(e -> {
            over_int = 0;
            repaint();
            settings.doClick();
        });
        animation.setSelected(true);
        settings.addSeparator();
        settings.add(sound);
        sound.addActionListener(e -> settings.doClick());
        sound.setSelected(true);
        menu.add(help);
        help.add(about);
        menu.add(score_player);
        menu.add(score);
        menu.add(best_length_player);
        menu.add(best_score_player);
        best_score_player.setVisible(false);
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, "Version:      0.4.1\nDeveloper:  MrChebik", "About", JOptionPane.PLAIN_MESSAGE));
        new_game.addActionListener(e -> start_anim_tick());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && s.direction != 2 && pause_key == 0 && pause_key_timer == 0 && !over_time_dark.isRunning())
                    s.direction = 0;
                if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && s.direction != 3 && pause_key == 0 && pause_key_timer == 0 && !over_time_dark.isRunning())
                    s.direction = 1;
                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && s.direction != 0 && pause_key == 0 && pause_key_timer == 0 && !over_time_dark.isRunning())
                    s.direction = 2;
                if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && s.direction != 1 && pause_key == 0 && pause_key_timer == 0 && !over_time_dark.isRunning())
                    s.direction = 3;
                if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_UP || key == KeyEvent.VK_W && pause_key_timer == 0 && pause_key == 0)
                    pause_key_timer = 1;
                if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_PAUSE && !over_time_dark.isRunning() && !over_time_lightly.isRunning())
                    if (win.isVisible()) {
                        win.setVisible(false);
                        start();
                    } else if (t.isRunning()) {
                        pause.setVisible(true);
                        t.stop();
                        if (teleport.isSelected() && teleport.isEnabled())
                            teleporting.stop();
                        pause_key = 1;
                    } else {
                        if (tick_tack.isRunning()) {
                            s.length = 2;
                            s.snakeX[0] = 10;
                            s.snakeY[0] = 10;
                            s.snakeX[1] = 9;
                            s.snakeY[1] = 10;
                            score.setText(String.valueOf(col_score = classic.isSelected() ? 2 : 0));
                            if (classic.isSelected())
                                best_length_player.setText("                                                         Best Length: " + best_length);
                            if (arcade.isSelected())
                                best_score_player.setText("                                                              Best Score: " + best_score);
                            over_time_lightly.start();
                        } else if (start_game == 0) {
                            pause.setText("PAUSE");
                            pause.setVisible(false);
                            start_game = 1;
                            t.start();
                            if (teleport.isSelected() && teleport.isEnabled()) {
                                getSpeed();
                                teleporting.start();
                            }
                            pause_key = 0;
                        } else {
                            pause.setVisible(false);
                            t.start();
                            if (teleport.isSelected() && teleport.isEnabled()) {
                                getSpeed();
                                teleporting.start();
                            }
                            pause_key = 0;
                        }
                    }
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (start_game == 1)
                    if (!tick_tack.isRunning()) {
                        pause.setVisible(false);
                        t.start();
                    }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!tick_tack.isRunning() && !over_time_lightly.isRunning() && !over_time_dark.isRunning()) {
                    pause.setVisible(true);
                    t.stop();
                }
            }
        });
        setFocusable(true);
        setSize(646 + OS, 699 + OS1);
        setLocation(0, 0);
    }

    void start() {
        s.length = 2;
        s.snakeX[0] = 10;
        s.snakeY[0] = 10;
        s.snakeX[1] = 9;
        s.snakeY[1] = 10;
        pause.setText("START");
        pause.setVisible(false);
        pause.setVisible(true);
        score.setText(String.valueOf(col_score = classic.isSelected() ? 2 : 0));
        t.stop();
        s.direction = 0;
        start_game = 0;
        repaint();
        if (tick_tack.isRunning()) {
            tick_tack.stop();
            game_over.setVisible(false);
        }
        if (classic.isSelected() || (hard.isSelected() && hard.isEnabled()))
            SPEED = 175;
        if (unlimited.isSelected() || (easy.isSelected() && easy.isEnabled()) || (normal.isSelected() && normal.isEnabled()))
            SPEED = 200;
        t.setDelay(SPEED);
        if (teleporting.isRunning())
            teleporting.stop();
        if (classic.isSelected())
            best_length_player.setText("                                                         Best Length: " + best_length);
        if (arcade.isSelected())
            best_score_player.setText("                                                              Best Score: " + best_score);
    }

    void gameover() {
        if (classic.isSelected())
            if (col_score > best_length) {
                best_length = col_score;
                check_length();
            }
        if (arcade.isSelected())
            if (col_score > best_score) {
                best_score = col_score;
                check_score();
            }
        if (teleporting.isRunning())
            teleporting.stop();
        game_over.setVisible(true);
        tick_tack.start();
    }

    /**
     * Аркада-Normal
     * @since  0.3
     */
    void setNormal() {
        SPEED = 200;
        if (!easy.isSelected() && !hard.isSelected())
            normal.setSelected(true);
        else if (tick_tack.isRunning()) start_anim_tick();
        else start();
        easy.setSelected(false);
        hard.setSelected(false);
    }

    /**
     * Проверка на скорость => изменение таймера до телепорта, при определенном значении SPEED
     * @since  0.4
     */
    void getSpeed() {
        if (SPEED == 200)
            SPEED_TELEPORT = 4000;
        else if (SPEED > 150)
            SPEED_TELEPORT = 3000;
        else SPEED_TELEPORT = 2000;
        if (two.isSelected() && two.isEnabled())
            if (SPEED == 200)
                SPEED_TELEPORT -= 750;
            else if (SPEED > 150)
                SPEED_TELEPORT -= 250;
            else SPEED_TELEPORT -= 125;
        teleporting.setInitialDelay(SPEED_TELEPORT);
    }

    /**
     * Ecли пользователь включил анимацию, то она отображается, змейка встает на исходное место, и очки = определенному значению
     * Иначе обычный старт, без анимации
     * @since  0.4
     */
    void start_anim_tick() {
        if (tick_tack.isRunning() && animation.isSelected()) {
            s.length = 2;
            s.snakeX[0] = 10;
            s.snakeY[0] = 10;
            s.snakeX[1] = 9;
            s.snakeY[1] = 10;
            score.setText(String.valueOf(col_score = classic.isSelected() ? 2 : 0));
            over_time_lightly.start();
        } else start();
    }

    /**
     * Проверка на кол-во очков, и изменение лучшего результата, убирая или прибавляя пробелы
     * @since  0.4
     */
    void check_score() {
        if (col_score < 10)
            best_score_player.setText("                                                              Best Score: " + best_score);
        else if (col_score < 100)
            best_score_player.setText("                                                            Best Score: " + best_score);
        else if (col_score < 1000)
            best_score_player.setText("                                                          Best Score: " + best_score);
        else
            best_score_player.setText("                                                        Best Score: " + best_score);
    }

    /**
     * Проверка на длину змейки, и изменение лучшей длины, убирая или прибавляя пробелы
     * @since  0.4
     */
    void check_length() {
        if (col_score < 10)
            best_length_player.setText("                                                         Best Length: " + best_length);
        else if (col_score < 100)
            best_length_player.setText("                                                       Best Length: " + best_length);
        else
            best_length_player.setText("                                                     Best Length: " + best_length);
    }

    /**
     * Генерирование яблока на поле, пока яблоко не будет на хвосте или другом яблоке
     * @since  0.3
     */
    void a_rerandom() {
        while (signal != 0) {
            signal = 1;
            for (int k = 0; k < s.length; k++)
                if ((s.snakeX[k] == a.posX && s.snakeY[k] == a.posY) || (a.posX == b.posX && a.posY == b.posY && two.isSelected() && two.isEnabled())) {
                    signal = 2;
                    a.random();
                } else signal1++;
            if (signal != 2 && signal1 == s.length)
                signal = 0;
            signal1 = 0;
        }
        signal = 1;
    }

    /**
     * Генерирование яблока на поле, пока яблоко не будет на хвосте или другом яблоке
     * @since  0.4
     */
    void b_rerandom() {
        while (signal != 0) {
            signal = 1;
            for (int k = 0; k < s.length; k++)
                if ((s.snakeX[k] == b.posX && s.snakeY[k] == b.posY) || (b.posX == a.posX && b.posY == a.posY && two.isSelected() && two.isEnabled())) {
                    signal = 2;
                    b.random();
                } else signal1++;
            if (signal != 2 && signal1 == s.length)
                signal = 0;
            signal1 = 0;
        }
        signal = 1;
    }

    /**
     * Если змейка касайтеся стены, то игра закончена
     * @since  0.3
     */
    void wall() {
        if (s.snakeX[0] > 19 || s.snakeX[0] < 0 || s.snakeY[0] > 19 || s.snakeY[0] < 0) {
            s.back();
            if (animation.isSelected())
                over_time_dark.start();
            else
                gameover();
            t.stop();
        }
    }

    /**
     * Если змейка кусает хвост, то игра закончена
     * @since  0.3
     */
    void tail() {
        for (int d = s.length; d > 0; d--)
            if (s.snakeX[0] == s.snakeX[d] && s.snakeY[0] == s.snakeY[d]) {
                s.back();
                if (animation.isSelected()) {
                    over_time_dark.start();
                    t.stop();
                } else gameover();
                break;
            }
    }
}