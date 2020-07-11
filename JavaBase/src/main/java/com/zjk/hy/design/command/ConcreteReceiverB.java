package com.zjk.hy.design.command;
/**
 * 具体的命令接收者B
 */
public class ConcreteReceiverB extends Receiver {
    @Override
    public void doSomeThing() {
        System.out.println("ConcreteReceiverB接到命令，做了一些事情");
    }
}
