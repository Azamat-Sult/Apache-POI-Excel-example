package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.entity.SomeEntity;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.model.SomeDataLoadDto;
import com.example.ApachePOIExcelExample.repository.SomeEntityRepository;
import com.example.ApachePOIExcelExample.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    public Page<SomeEntity> findAll(Pageable pageable) {
        return someEntityRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        someEntityRepository.deleteById(id);
    }

    public void create(SomeDataLoadDto dto) {
        someEntityRepository.save(dto.toEntity());
    }

    public ResponseEntity<List<ReportSheetDto>> getRowsInPages(Pageable pageable) {
        Page<SomeEntity> page = findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        var result = page.getContent().stream()
                .map(SomeEntity::toReportSheetDto)
                .toList();
        return ResponseEntity.ok().headers(headers).body(result);
    }

}