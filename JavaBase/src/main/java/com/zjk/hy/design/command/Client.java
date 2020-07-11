package com.zjk.hy.design.command;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) {
        Command commandA = new ConcreteCommandA();
        Command commandB = new ConcreteCommandB();
        Invoker invoker = new Invoker(commandA);
        invoker.action();
        invoker = new Invoker(commandB);
        invoker.action();
        commandA = new ConcreteCommandA(new ConcreteReceiverB());
        invoker = new Invoker(commandA);
        invoker.action();
    }
}
