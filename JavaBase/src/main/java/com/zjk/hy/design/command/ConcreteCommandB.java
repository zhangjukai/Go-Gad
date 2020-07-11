package com.zjk.hy.design.command;

/**
 * 具体的命令
 */
public class ConcreteCommandB extends Command {
    public ConcreteCommandB(){
        // 默认给一个接收者
        super(new ConcreteReceiverB());
    }
    // 通过构造函数传入一个接收者
    public ConcreteCommandB(Receiver receiver) {
        super(receiver);
    }

    @Override
    public void execute() {
        // 收到命令做某些操作
        receiver.doSomeThing();
    }
}
