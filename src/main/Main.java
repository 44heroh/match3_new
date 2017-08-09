package main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

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

        ///Обновляем и рисуем графические элементы
        while(!Display.isCloseRequested()) {

            countStep++;

            System.out.println("countStep = " + countStep);
            if(Mouse.isButtonDown(0)){
                System.out.println("LEFT_MOUSE");
                SimpleText.drawString("countStep - " + countStep, 30, 40);
            }

            ///Создаём ягодку в случайном месте
            generate_new_obj();

            GUI.draw();

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
