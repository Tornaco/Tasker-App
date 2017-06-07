package dev.tornaco.tasker.app;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Nick on 2017/5/24 12:53
 */
@RunWith(AndroidJUnit4.class)
public class WZRY {

    @Test
    public void testXXX() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        while (true) {
            // 赶路
            device.swipe(239, 848, 616, 534, 100);
            // 攻击
            device.click(1431, 940);
            device.click(1554, 763);

            // 赶路
            device.swipe(239, 848, 555, 480, 100);
            // 攻击
            device.click(1431, 940);
            device.click(1554, 763);

            // 赶路
            device.swipe(239, 848, 688, 600, 100);
            // 攻击
            device.click(1431, 940);
            device.click(1554, 763);

            // 学技能
            device.click(1324, 851);
            device.click(1462, 653);

            // 发信号
            device.click(1867, 308);

            // 买装备
            device.click(202, 451);
        }
    }
}
