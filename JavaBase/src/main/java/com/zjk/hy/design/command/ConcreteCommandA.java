package com.zjk.hy.design.command;

/**
 * 具体的命令
 */
public class ConcreteCommandA extends Command {
    public ConcreteCommandA(){
        // 默认给一个接收者
        super(new ConcreteReceiverA());
    }
    // 通过构造函数传入一个接收者
    public ConcreteCommandA(Receiver receiver) {
        super(receiver);
    }

    @Override
    public void execute() {
        // 收到命令做某些操作
        receiver.doSomeThing();
    }
}
