package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.entity.ArrayColumnExample;
import com.example.ApachePOIExcelExample.enums.Type2;
import com.example.ApachePOIExcelExample.model.SomeObject;
import com.example.ApachePOIExcelExample.model.SubSomeObject;
import com.example.ApachePOIExcelExample.repository.ArrayColumnExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArrayColumnExampleService {

    private final ArrayColumnExampleRepository arrayColumnExampleRepository;

    public void addTwoAndPrintAllEntities() {

        var subObject1 = SubSomeObject.builder()
                .longNum(111L)
                .someString("OBJ")
                .someStringList(List.of("asd", "fgh", "jkl"))
                .build();
        var newEntity1 = ArrayColumnExample.builder()
                .type2List(List.of(Type2.TYPE_2_1, Type2.TYPE_2_2, Type2.TYPE_2_3))
                .someObject(new SomeObject(100L, "HELLO", List.of(subObject1)))
                .build();
        arrayColumnExampleRepository.save(newEntity1);

        var subObject2 = SubSomeObject.builder()
                .longNum(222L)
                .someString("JOB")
                .someStringList(List.of("zxc", "vbn", "qwe"))
                .build();
        var newEntity2 = ArrayColumnExample.builder()
                .type2List(List.of(Type2.TYPE_2_1, Type2.TYPE_2_3))
                .someObject(new SomeObject(200L, "BYE", List.of(subObject2)))
                .build();
        arrayColumnExampleRepository.save(newEntity2);

        var allEntities = arrayColumnExampleRepository.findAll();
        allEntities.forEach(System.out::println);
    }

    public List<ArrayColumnExample> findByArrayColumnContainingIgnoreCase(String search) {
        return arrayColumnExampleRepository.findByArrayColumnContainingIgnoreCase(search);
    }

    public String someStringListContainingText(String search) {
        var contains = arrayColumnExampleRepository.someStringListContainingText(search);
        return contains ? "CONTAINS" : "NOT FOUND";
    }

}