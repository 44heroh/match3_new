package main;

import java.util.Random;

import static main.Constants.CELL_SIZE;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by User on 16.02.2017.
 */
public class Cell {
    private int x;
    private int y;
    private int state;/* 0 -> ячейка пуста
                        >0 -> в ячейке тело змеи, которое будет там ещё N фреймов
                        <0 -> Что-то необычное:
                            -1: Ягоды
                        */
    ///Конструктор просто выставляет начальные значения координат и состояния
    public Cell (int x, int y, int state){
        this.x=x;
        this.y=y;
        this.state=state;
    }

    ///==== Ничем не примечательные геттеры и
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight(){
        return CELL_SIZE;
    }

    public int getWidth(){
        return CELL_SIZE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    ///Метод обновления клетки. Уменьшаем время "горения", если это необходимо
    public void update(boolean have_to_decrease) {
        if(have_to_decrease && this.state > 0)
        {
            this.state--;
        }
    }

    ///Ячейка "думает" как она должна выглядеть
    public Sprite getSprite() {
        if(this.state > 0)
        {
            ///Если в ней тело змеи -- как змея
            return Sprite.BODY;
        }
        /*else if(this.state == 0)
        {
            ///Если в ней нет ничего -- никак выглядеть и не должна
            return null;
        }*/
        else if(this.state == 0)
        {
            ///Если в ней нет ничего -- никак выглядеть и не должна
            return Sprite.POISON;
        }
        else
        {
            ///Иначе проходимся свитчем по возможным объектам.
            ///Так как это демо -- я добавил только ягоды

            switch (this.state)
            {
                case -2 :
                    return Sprite.BANANAS;
                case -3 :
                    return Sprite.STRABERRY;
                /*case -4 :
                    return Sprite.POISON;*/
                default:return Sprite.CHERRIES;
            }
        }
    }

    ///Рисует элемент, переданный в аргумент
    public void draw() {
        ///Если у ячейки нет спрайта, то рисовать её не нужно
        if( getSprite() == null) return;

        ///Собственно, рисуем. Подробно не останавливаюсь, так как нам интересна сама логика игры, а не LWJGL

         getSprite().getTexture().bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f( getX(),  getY()+ getHeight());
        glTexCoord2f(1,0);
        glVertex2f( getX()+ getWidth(),  getY()+ getHeight());
        glTexCoord2f(1,1);
        glVertex2f( getX()+ getWidth(),  getY());
        glTexCoord2f(0,1);
        glVertex2f( getX(),  getY());
        glEnd();
    }
}
