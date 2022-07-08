import com.netflix.ribbon.hystrix.FallbackHandler;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/28 11:57
 **/
public class FaceTest {

    public static void main(String[] args) {
        System.err.println(new FaceTest().fun("1.2", "1.2.0"));;
    }
    public int fun(String v1, String v2) {
        if (v1 == null || v2 == null) {
            throw new RuntimeException("param is null");
        }
        String[] arr1 = v1.split("\\.");
        String[] arr2 = v2.split("\\.");

        for (int i = 0; i < arr1.length; i++) {
            String s = arr1[i];

            while (s.startsWith("0")) {
                s = s.substring(1);
            }


        }
        for (int i = 0; i < arr2.length; i++) {
            String s = arr2[i];
            while (s.startsWith("0")) {
                s = s.substring(1);
            }
        }

        int num = arr1.length > arr2.length ? arr2.length : arr1.length;
        int flag = 0;
        for (int i = 0; i < num; i++) {
            int i1 = Integer.parseInt(arr1[i]);
            int i2 = Integer.parseInt(arr2[i]);
            if (i1 > i2) {
                flag = 1;
                return flag;
            }
            if (i1 < i2) {
                flag =  -1;
                return flag;
            }
        }
        if (flag == 0) {

            if (arr1.length > arr2.length) {
                for (int i = arr2.length; i < arr1.length;  i++) {
                    String s = arr1[i];
                    String temp = "";
                    for (int j =0; j < s.length(); j++) {
                        temp += "0";
                    }
                    if (temp.equals(s)) {
                        flag = 0;
                        continue;
                    } else {
                        flag = 1;
                    }

                }
            } else {
                for (int i = arr1.length; i < arr1.length;  i++) {
                    String s = arr2[i];
                    String temp = "";
                    for (int j =0; j < s.length(); j++) {
                        temp += "0";
                    }
                    if (temp.equals(s)) {
                        flag = 0;
                        continue;
                    }
                    flag = 1;
                }
            }
        }
        return flag;
    }
}

