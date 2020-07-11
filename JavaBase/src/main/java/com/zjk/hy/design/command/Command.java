package com.zjk.hy.design.command;

/**
 * 命令角色
 */
public abstract class Command {
    protected final Receiver receiver;
    // 实现类必须定义一个接收者
    public Command(Receiver receiver){
        this.receiver = receiver;
    }
    // 每个命令类都要有一个执行命令的方法
    public abstract void execute();
}
