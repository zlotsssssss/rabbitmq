package wx.th.zlo.utils;

/**
 * ClassName: SleepUtils
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/4 12:35
 * Version: 1.0.0
 */
public class SleepUtils {
    public static void  sleep(int minutes) {
        try {
            Thread.sleep(1000 * minutes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
