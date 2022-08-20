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
    @ReportHeader("Строка 1")
    private String string1;

    @ValidString
    @ReportHeader("Строка 2")
    private String string2;

    @ValidString
    @ReportHeader("Строка 3")
    private String string3;

    @ReportHeader("Тип 1")
    private Type1 type1;

    @ValidNumber
    @ReportHeader("BigDecimal 1")
    private BigDecimal bigDecimal1;

    @ValidNumber
    @ReportHeader("BigDecimal 2")
    private BigDecimal bigDecimal2;

    public SomeEntity toEntity() {
        return SomeEntity.builder()
                .id(this.id)
                .string1(this.string1)
                .string2(this.string2)
                .string3(this.string3)
                .type1(this.type1)
                .bigDecimal1(this.bigDecimal1)
                .bigDecimal2(this.bigDecimal2)
                .build();
    }

}