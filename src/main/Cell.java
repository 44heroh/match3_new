package main;

import java.util.Random;

import static main.Constants.CELLS_COUNT_X;
import static main.Constants.CELLS_COUNT_Y;
import static main.Constants.CELL_SIZE;
import main.GUI;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by User on 16.02.2017.
 */
public class Cell {

    // Направления соседей по часовой стрелке
    public static final int NEIGHBOUR_TOP = 0;
    public static final int NEIGHBOUR_RIGHT = 1;
    public static final int NEIGHBOUR_BOTTOM = 2;
    public static final int NEIGHBOUR_LEFT = 3;


    private int x;
    private int y;
    private int state;/* 0 -> ячейка пуста
                        >0 -> в ячейке тело змеи, которое будет там ещё N фреймов
                        <0 -> Что-то необычное:
                            -1: Ягоды
                        */

    private int row; // Строка ячейки
    private int col; // Столбец ячейки

    ///Конструктор просто выставляет начальные значения координат и состояния
    public Cell(int row, int col, int state){
        this.col = col;
        this.row = row;
        this.x = col * CELL_SIZE;
        this.y = row * CELL_SIZE;
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
        else
        {
            ///Иначе проходимся свитчем по возможным объектам.
            ///Так как это демо -- я добавил только ягоды
            switch (this.state)
            {
                case 0 :
                    return Sprite.POISON;
                case -1:
                    return Sprite.CHERRIES;
                case -2 :
                    return Sprite.BANANAS;
                case -3 :
                    return Sprite.STRABERRY;
                default:return null;
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

    public boolean swap(int neighbourDirection)
    {
        Cell neighbour = null;
        switch (neighbourDirection) {
            case NEIGHBOUR_RIGHT:
                if (col <  CELLS_COUNT_X - 1) {
                    neighbour = GUI.cells[row][col + 1];
                }
                break;
            case NEIGHBOUR_LEFT:
                if (col > 0) {
                    neighbour = GUI.cells[row][col - 1];
                }
                break;
            case NEIGHBOUR_TOP:
                if (row < CELLS_COUNT_Y - 1) {
                    neighbour = GUI.cells[row + 1][col];
                }
                break;
            case NEIGHBOUR_BOTTOM:
                if (row > 0) {
                    neighbour = GUI.cells[row - 1][col];
                }
                break;
            default: neighbour = null;
        }
        // Если сосед существует, то меняем местами.
        if (neighbour != null)  {
            int neighbourCol = neighbour.col;
            int neighbourRow = neighbour.row;
            neighbour.setPosition(this.row, this.col);
            this.setPosition(neighbourRow, neighbourCol);

            System.out.println(String.format("This sprite - %s, neighbour sprite - %s", this.getSprite().toString(), neighbour.getSprite().toString()));
            return true;
        }
        return false;
    }

    protected void setPosition(int row, int col)
    {
        this.col = col;
        this.row = row;
        this.x = col * CELL_SIZE;
        this.y = row * CELL_SIZE;
        GUI.cells[row][col] = this;
    }
}
