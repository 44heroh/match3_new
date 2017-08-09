package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static main.Constants.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * Created by User on 16.02.2017.
 */
public class GUI {
    ///CELLS_COUNT_X и CELLS_COUNT_Y -- константы
    //Cell -- класс, который реализует main.GUIElement; им займёмся немного позже
    private static Cell[][] cells;

    public static void init() {
        initializeOpenGL();

        cells = new Cell[CELLS_COUNT_Y][CELLS_COUNT_X];

        Random rnd = new Random();

        for (int i = 0; i < CELLS_COUNT_Y; i++) {
            for (int j = 0; j < CELLS_COUNT_X; j++) {
                cells[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, (rnd.nextInt(100) < INITIAL_SPAWN_CHANCE?randomNTexture():0));
            }
        }
    }

    //Этот метод будет вызываться извне
    public static void update(boolean have_to_decrease)
    {
        updateOpenGL();

        for (Cell[] line:cells)
        {
            for (Cell cell:line)
            {
                cell.update(have_to_decrease);
            }
        }
    }

    ///А этот метод будет использоваться только локально,
    /// т.к. базовым другие классы должны работать на более высоком уровне
    private static void updateOpenGL() {
        Display.update(); //Обновляем дисплей
        Display.sync(FPS); //Синхронизируем FPS до 60.
        /*while(!Display.isCloseRequested()){ //Пока не поступил запрос о закрытии
            CleanWindow();
            Display.update(); //Обновляем дисплей
            Display.sync(FPS); //Синхронизируем FPS до 60.
        }
        ExitWindow();*/
    }

    public static void CleanWindow(){
        glClear(GL_COLOR_BUFFER_BIT); //Очистка буфера который выводится на экран.(проще говоря просто очистка экрана)
    }

    public static void ExitWindow(){
        Display.destroy(); //После того как вышли из цикла, уничтожаем дисплей...
        System.exit(0); //...и закрываем программу
    }

    ///Рисует все клетки
    public static void draw()
    {
        // Для поддержки текстур
        glEnable(GL_TEXTURE_2D);

        ///Очищает экран от старого изображения
        glClear(GL_COLOR_BUFFER_BIT);

        for (Cell[] line:cells)
        {
            for (Cell cell:line)
            {
                cell.draw();
            }
        }
    }

    public static int getState(int x, int y)
    {
        return cells[x][y].getState();
    }

    public static void setState(int x, int y, int state)
    {
        cells[x][y].setState(state);
    }

    private static void initializeOpenGL() {
        try{
            //Задаём размер будущего окна
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));

            //Задаём имя будущего окна
            Display.setTitle(SCREEN_NAME);

            //Создаём окно
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0,SCREEN_WIDTH,0,SCREEN_HEIGHT,1,-1);
        glMatrixMode(GL_MODELVIEW);

        // Для поддержки прозрачности
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);



        // Белый фоновый цвет

        glClearColor(1,1,1,1);
    }

    public static int randomNTexture()
    {
        int texture = (int) Math.round((Math.random() * 4) - 4) + 1;
        System.out.println("texture " + texture);
//        System.out.println("texture = " + texture);
        return texture;
    }

    public static Cell getCellByCoordinates(int x, int y)
    {

        int row = y / CELL_SIZE ;
        int col = x / CELL_SIZE;
        System.out.println(String.format("row = %d, col = %d, state - %d", row, col, cells[row][col].getState()));
        return cells[row][col];
    }

}
