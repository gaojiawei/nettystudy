package ort.study.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

/**
 * . User: jiawei.gao Date: 14-3-9 qunar.com
 */
public class LampTest {

    public static void main(String[] args) {
        ArrayList<String> strings = Lists.newArrayList("hell", "zhege", "a");
        Collections.sort(strings,( left, right)-> left.length()-right.length());
        List<Integer> collect = strings.stream().flatMap((input) -> Stream.of(input.length())).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
}
