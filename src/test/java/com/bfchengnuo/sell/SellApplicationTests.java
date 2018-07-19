package com.bfchengnuo.sell;

import org.junit.Assert;
import org.junit.Test;

public class SellApplicationTests {

    @Test
    public void contextLoads() {
        long a = 135482464L / 1024;
        long b = 135482464L >> 10;
        Assert.assertEquals(a,b);
    }

}
