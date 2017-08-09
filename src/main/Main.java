package main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static main.Constants.*;

/**
 * Created by User on 04.08.2017.
 */
public class Main extends Applet {
    ///Переменная, при обращении которой в true приложение закрывается
    private static boolean isExitRequested=false;

    ///Флаг, который обращается в false, если на данном тике змея что-то съела
    private static boolean have_to_decrease = true;

    // Хранят координаты курсора мыши.
    private int lastx, lasty;

    // Объект Graphics, который необходимо нарисовать.
    Graphics g;

    public static void main(String[] args) {
        ///Инициализируем графический интерфейс
        GUI.init();

        //Счетчик ходов
        int countStep = 0;

        ///Создаём ягодку в случайном месте
        generate_new_obj();

        // Флаг - признак была ли нажата кнопка мыши
        boolean mousePressed = false;
        // Позиция мыши, когда была нажата кнопка мыши
        int mousePressedX = -1;
        int mousePressedY = -1;

        // Был ли произведен обмен в результате текущего удерживания кнопки мыши. Чтобы повторно не проводить операцию обмена.
        boolean swapped = false;

        ///Обновляем и рисуем графические элементы
        while(!Display.isCloseRequested()) {

            glClear(GL_COLOR_BUFFER_BIT);
            GUI.draw();

            countStep++;
            //System.out.println("countStep = " + countStep);
            SimpleText.drawString("countStep - " + countStep, 30, 40);
            if(Mouse.isButtonDown(0)){
                // Проверяем было ли это первое нажатие или же пользователь удерживает кнопку несколько кадров
                if (!mousePressed) {   // Кнопка нажата впервые
                    System.out.println(String.format("LEFT_MOUSE, x = %d, y = %d", Mouse.getX( ), Mouse.getY()));
                    //Запоминаем позицию
                    mousePressedX = Mouse.getX();
                    mousePressedY = Mouse.getY();
                    mousePressed = true;
                }
                else if (!swapped) {
                    // Получаем ячейку, по которой был сделан первоначальный щелчок
                    Cell cell = GUI.getCellByCoordinates(mousePressedX, mousePressedY);
                    if (Mouse.getX() - mousePressedX > CELL_SIZE / 2) { // Движение вправо
                        swapped = cell.swap(Cell.NEIGHBOUR_RIGHT);
                    }
                    else if (Mouse.getX() - mousePressedX < - CELL_SIZE / 2) { // Движение влево
                        swapped = cell.swap(Cell.NEIGHBOUR_LEFT);
                    }
                    else if (Mouse.getY() - mousePressedY > CELL_SIZE / 2) {
                        swapped = cell.swap(Cell.NEIGHBOUR_TOP);
                    }
                    else if (Mouse.getY() - mousePressedY < -CELL_SIZE / 2) {
                        swapped = cell.swap(Cell.NEIGHBOUR_BOTTOM);
                    }
                }
            }
            else {
                mousePressed = false;
                swapped = false;
            }

            GUI.update(have_to_decrease);
        }
        GUI.ExitWindow();
    }

    private static void generate_new_obj() {
        int point = new Random().nextInt(CELLS_COUNT_X*CELLS_COUNT_Y);

        for(int i=0; i<CELLS_COUNT_X; i++){
            for(int j=0; j<CELLS_COUNT_Y; j++){
                /*System.out.println("X = " + i + ", Y = " + j);*/
                GUI.setState(i, j, GUI.randomNTexture()); //TODO randomize objects
            }
        }
    }

    /** Реакция на нажатие кнопки мыши */
    public boolean mouseDown(Event e, int x, int y) {
        lastx = x; lasty = y;
        System.out.println(String.format("LEFT_MOUSE, x = %d, y = %d", Mouse.getX(), Mouse.getY()));
        return true;
    }
    /** Реакция на перетаскивание с помощью мыши */
    public boolean mouseDrag(Event e, int x, int y) {
        g.setColor(Color.black) ;
        g.drawLine(lastx, lasty, x, y);
        lastx = x; lasty = y;
        return true;
    }

}
