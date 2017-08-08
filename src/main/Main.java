package main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import java.util.Random;

import static main.Constants.*;

/**
 * Created by User on 04.08.2017.
 */
public class Main {
    ///Переменная, при обращении которой в true приложение закрывается
    private static boolean isExitRequested=false;

    ///Флаг, который обращается в false, если на данном тике змея что-то съела
    private static boolean have_to_decrease = true;

    public static void main(String[] args) {
        ///Инициализируем графический интерфейс
        GUI.init();

        //Счетчик уровней
        int countLvl = 1;

        SimpleText.drawString("countLvl - " + countLvl, 30, 40);
        ///Обновляем и рисуем графические элементы
        while(!Display.isCloseRequested()) {

            ///Создаём ягодку в случайном месте
            generate_new_obj();

            GUI.draw();

            countLvl++;

            GUI.update(have_to_decrease);

            System.out.println("countLvl = " + countLvl);
            if(Mouse.isButtonDown(0)){
                System.out.println("LEFT_MOUSE");
                SimpleText.drawString("countLvl - " + countLvl, 30, 40);
            }
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

}
