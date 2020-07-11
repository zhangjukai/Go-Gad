package com.zjk.hy.design.command;
/**
 * 具体的命令接收者A
 */
public class ConcreteReceiverA extends Receiver {
    @Override
    public void doSomeThing() {
        System.out.println("ConcreteReceiverA接到命令，做了一些事情");
    }
}
