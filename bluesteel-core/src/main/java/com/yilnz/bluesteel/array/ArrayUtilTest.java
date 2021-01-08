package com.yilnz.bluesteel.array;

import com.yilnz.bluesteel.test.MethodTimeCaculator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayUtilTest {

    List<Integer> randomNums;

    @Before
    public void init(){
        randomNums = IntStream.generate(()->{
            return new Random().nextInt(500000);
        }).limit(200000).boxed().collect(Collectors.toList());
    }

    @Test
    public void arrayToList() {
        MethodTimeCaculator.invokeMethod(()->ArrayUtil.arrayToList(randomNums.toArray(new Integer[]{})));
    }

    @Test
    public void removeDuplicatesWithSequence() {
        MethodTimeCaculator.invokeMethod(()->ArrayUtil.removeDuplicatesWithSequence(randomNums));
    }

    @Test
    public void removeDuplicates() {
        MethodTimeCaculator.invokeMethod(()->ArrayUtil.removeDuplicates(randomNums));
    }
}