package com.zjk.hy.design.visitor;

/**
 * 抽象元素类
 */
public interface Element {
    void accept(Visitor visitor);
}
