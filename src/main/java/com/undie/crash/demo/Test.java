package com.undie.crash.demo;

import com.undie.crash.demo.proxy.Man;
import com.undie.crash.demo.proxy.Persion;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/3 16:12
 **/
public class Test {
    Lock lock = new ReentrantLock();
    Condition conditionAdd = lock.newCondition();
    Condition conditionDel = lock.newCondition();


    private int state;
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Test test = new Test();
//        test.f2();
        test.f1();
//        test.f5();
    }
    public void f1() throws InterruptedException {
        final int[] num = {0};
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    if (num[0] == 10) {
                        System.err.println("add thread await, num=" + num[0]);
                        conditionAdd.await();
                    }
                    conditionDel.signal();
                    Thread.sleep(200);
                    num[0]++;
                    System.err.println("num++, num=" + num[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "thread1").start();
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    if (num[0] == 0) {
                        System.err.println("delete thread await, num=" + num[0]);
                        conditionDel.await();
                    }
                    conditionAdd.signal();
                    Thread.sleep(200);
                    num[0]--;
                    System.err.println("num--, num=" + num[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "thread2").start();

    }

    public void f2() throws NoSuchFieldException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        VarHandle state = lookup.findVarHandle(Test.class, "state", int.class);
        boolean b = state.compareAndSet(this, 0, 3);
        Object acquire = state.get(this);
        System.err.println();
    }
    public void f3() {
        Man man = new Man();
        Persion persion = (Persion) Proxy.newProxyInstance(man.getClass().getClassLoader(), man.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.err.println("jdk before speak");
                return method.invoke(man, args);
            }
        });
        persion.speak("dachui");

        Persion son = (Man) Enhancer.create(man.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.err.println("cglib before speak");
                return methodProxy.invoke(man, objects);
            }
        });
        son.speak("son");
    }

    public void f4() {
        Queue queue = new ArrayDeque(1);
        queue.add(1);
        queue.add(1);
        queue.offer(1);
        queue.peek();
        queue.element();
        queue.remove();

    }

    public void f5() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "--null--";
            }
        };
        try {
            Object call = callable.call();
            System.err.println(call);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
