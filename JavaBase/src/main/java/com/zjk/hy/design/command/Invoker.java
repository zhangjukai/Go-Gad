package com.zjk.hy.design.command;

/**
 * 调用者
 */
public class Invoker {
    private Command command;

    // 接受命令
    public Invoker(Command command){
        this.command = command;
    }
    // 执行命令
    public void action(){
        command.execute();
    }
}
