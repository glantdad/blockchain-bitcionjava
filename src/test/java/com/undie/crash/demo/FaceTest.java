package com.undie.crash.demo;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/1 18:31
 **/
public class FaceTest {
//    private static int N1 = 1!;
//    private static int N2 = 2!;
//    private static int N3 = 3!;
//    private static int N4 = 4!;
//    private static int N5 = 5!;

    public static void main(String[] args) {
        for(int i = 5; i <= 15; i++) {
            System.err.println(fun1(i));
        }
    }

    /**
     * 1, 代码比较简洁清晰
     * 2, 鲁棒性, 对于输入,可以有好的处理,如果发生异常,可以有好的提示
     * 3, 资源消耗,性能等问题
     *
     *
     * 1, 团队气氛2
     * 2, 业务前景1
     * 3, 薪资待遇3
     * 4, 工作生活平衡4
     * 5, 技术成长5
     * @param n
     * @return
     */
    public static int fun1(int n) {
        if (n < 0) {
            throw new RuntimeException("input n must gt 0");
        }
        if (n == 0) {
            return 1;
        }
        try {
            if (n == 1) return 1;
            return n * fun1(n - 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
