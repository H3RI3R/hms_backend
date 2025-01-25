package com.example.main.Configuration;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtils {
    public static <T> List<T> getPaginatedList(List<T> originalList, Integer page, Integer size) {
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, originalList.size());

        if (startIndex > endIndex) {
            return List.of();
        }

        return IntStream.range(startIndex, endIndex)
                .mapToObj(originalList::get)
                .collect(Collectors.toList());
    }
}