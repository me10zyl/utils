package com.yilnz.bluesteel.array;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayUtil {

    /**
     * 数组列表
     *
     * @param nums 数组
     * @return {@link List<Integer> }
     * @author zyl
     * @date 2020/10/10
     */
    public static List<Integer> arrayToList(Integer[] nums){
        return Arrays.asList(nums);
    }

    /**
     * 删除重复并保持原有序列 20W数据-55ms
     *
     * @param nums 数组
     * @return {@link List<Integer> }
     * @author zyl
     * @date 2020/10/10
     */
    public static List<Integer> removeDuplicatesWithSequence(List<Integer> nums){
        if (nums.size() == 0) return new ArrayList<>();
        Collections.sort(nums);
        int i = 0;
        for (int j = 1; j < nums.size(); j++) {
            if (nums.get(j) != nums.get(i)) {
                i++;
                nums.set(i, nums.get(j));
            }
        }
        int len = i + 1;
        List<Integer> newList = new ArrayList<>();
        for(int c = 0;c < len;c++){
            newList.add(nums.get(c));
        }
        return newList;
    }

    /**
     * 删除重复的-乱序 20W数据-18ms
     *
     * @param nums 数组
     * @return int
     * @author zyl
     * @date 2020/10/10
     */
    public static List<Integer> removeDuplicates(List<Integer> nums){
        Set<Integer> hashSet = new HashSet<>();
        hashSet.addAll(nums);
        return Arrays.stream(new Integer[]{}).collect(Collectors.toList());
    }
}
