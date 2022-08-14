package com.example.ApachePOIExcelExample.model;

import com.example.ApachePOIExcelExample.annotation.ReportHeader;
import com.example.ApachePOIExcelExample.annotation.ValidNumber;
import com.example.ApachePOIExcelExample.annotation.ValidString;
import com.example.ApachePOIExcelExample.entity.SomeEntity;
import com.example.ApachePOIExcelExample.enums.Type1;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SomeDataLoadDto {

    private Long id;

    @ValidString
    @ReportHeader("Строка")
    private String string;

    @ReportHeader("Тип 1")
    private Type1 type1;

    @ValidNumber
    @ReportHeader("BigDecimal")
    private BigDecimal bigDecimal;

    public SomeEntity toEntity() {
        return SomeEntity.builder()
                .id(this.id)
                .string(this.string)
                .type1(this.type1)
                .bigDecimal(this.bigDecimal)
                .build();
    }

}