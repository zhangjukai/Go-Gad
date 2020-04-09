package com.zjk.hy.Drools;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTest01 {
    @Test
    public void costPrice() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession("costPrice");

        Person zjk = new Person("zjk", 70);
        Car car = new Car("宝马", 100000D);
        car.setPerson(zjk);
        kieSession.insert(car);
        int i = kieSession.fireAllRules();
        System.out.println("触发规则数量："+i);
        kieSession.dispose();

    }

    public static void main(String[] args) {
        /*KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession("costPrice");*/
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        KieSession kieSession = kc.newKieSession("costPrice");

        Person zjk = new Person("zjk", 70);
        Car car = new Car("宝马", 100000D);
        car.setPerson(zjk);
        kieSession.insert(car);
        int i = kieSession.fireAllRules(new RuleNameMatchesAgendaFilter("大于等于60"));
        System.out.println("触发规则数量："+i);
        kieSession.dispose();
    }
}
