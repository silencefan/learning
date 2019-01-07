package com.to8to.learn.design.pattern.creational.factory;

/**
 * @Description: 工厂模式
 * @author: felix.fan
 * @date: 2019/1/7 16:28
 * @version: 1.0
 */
public class Demo1 {

    /*
     * 属于创建性模式 创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象
     */

    public static void main(String[] args) {
        Sharpe sharpe1 = SharpeFactory.getSharpe(Sharpe.CIRCLE);
        sharpe1.draw();

        Sharpe sharpe2 = SharpeFactory.getSharpe(Sharpe.RECTANGLE);
        sharpe2.draw();

        Sharpe sharpe3 = SharpeFactory.getSharpe(Sharpe.SQUARE);
        sharpe3.draw();
    }
}

interface Sharpe{
    String CIRCLE = "circle";
    String SQUARE = "square";
    String RECTANGLE = "rectangle";

    /**
     * 画图形
     */
    void draw();
}

class Circle implements Sharpe {
    @Override
    public void draw() {
        System.out.println("------->draw circle");
    }
}

class Square implements Sharpe {
    @Override
    public void draw() {
        System.out.println("------->draw square");
    }
}

class Rectangle implements Sharpe {
    @Override
    public void draw() {
        System.out.println("------->draw rectangle");
    }
}

class SharpeFactory {
    public static Sharpe getSharpe(String sharpeName) {
        if (sharpeName == null) {
            return null;
        }
        if (sharpeName.equalsIgnoreCase(Sharpe.CIRCLE)) {
            return new Circle();
        } else if (sharpeName.equalsIgnoreCase(Sharpe.RECTANGLE)) {
            return new Rectangle();
        } else if (sharpeName.equalsIgnoreCase(Sharpe.SQUARE)) {
            return new Square();
        }
        return null;
    }
}