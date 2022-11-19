package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.entity.SomeEntity;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.model.SomeDataLoadDto;
import com.example.ApachePOIExcelExample.repository.SomeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SomeEntityService {

    @Autowired
    private SomeEntityRepository someEntityRepository;

    public void saveAll(List<SomeDataLoadDto> data) {
        var entities = data.stream()
                .map(SomeDataLoadDto::toEntity)
                .collect(Collectors.toList());

        someEntityRepository.saveAll(entities);
    }

    public SomeEntity getById(Long id) {
        return someEntityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Элемент не найден"));
    }

    public List<ReportSheetDto> findAll() {
        return someEntityRepository.findAll().stream()
                .map(SomeEntity::toReportSheetDto)
                .toList();
    }

    public void deleteById(Long id) {
        someEntityRepository.deleteById(id);
    }

}