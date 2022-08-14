package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.model.SomeDataLoadDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploaderService {

    @Autowired
    private final SomeEntityService someEntityService;

    public void uploadFile(MultipartFile file) {
        var fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new InvalidFileNameException(null, "Пустое имя файла");
        }

        var data = parseFile(file);
        someEntityService.saveAll(data);
        someEntityService.findAll().forEach(System.out::println);
    }

    private List<SomeDataLoadDto> parseFile(MultipartFile file) {
        var parser = new ExcelParser<>(SomeDataLoadDto.class);
        return parser.parse(file, true, false);
    }

}