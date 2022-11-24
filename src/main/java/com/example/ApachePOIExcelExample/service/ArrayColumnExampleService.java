package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.entity.ArrayColumnExample;
import com.example.ApachePOIExcelExample.enums.Type2;
import com.example.ApachePOIExcelExample.model.SomeObject;
import com.example.ApachePOIExcelExample.repository.ArrayColumnExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArrayColumnExampleService {

    private final ArrayColumnExampleRepository arrayColumnExampleRepository;

    public void addOneAndPrintAllEntities() {
        var newEntity = ArrayColumnExample.builder()
                .type2List(List.of(Type2.TYPE_2_1, Type2.TYPE_2_2, Type2.TYPE_2_3))
                .someObject(new SomeObject(100L, "HELLO", List.of("asd", "fgh", "jkl")))
                .build();
        arrayColumnExampleRepository.save(newEntity);
        var allEntities = arrayColumnExampleRepository.findAll();
        allEntities.forEach(System.out::println);
    }

}