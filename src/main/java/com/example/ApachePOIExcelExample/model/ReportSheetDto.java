package com.example.ApachePOIExcelExample.model;

import com.example.ApachePOIExcelExample.annotation.ReportFieldControl;
import com.example.ApachePOIExcelExample.annotation.ReportHeader;
import com.example.ApachePOIExcelExample.annotation.ReportTemplate;
import com.example.ApachePOIExcelExample.enums.Type1;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ReportTemplate(name = "Содержимое БД")
public class ReportSheetDto {

    @ReportHeader("Строка 1")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.SHOW_ALWAYS)
    private String string1;

    @ReportHeader("Строка 2")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.IF_ASKED)
    private String string2;

    @ReportHeader("Строка 3")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.HIDDEN)
    private String string3;

    @ReportHeader("Тип 1")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.PART_OF, linkedTo = {"string2"})
    private Type1 type1;

    @ReportHeader("BigDecimal 1")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.IF_ASKED)
    private BigDecimal bigDecimal1;

    @ReportHeader("BigDecimal 2")
    @ReportFieldControl(accessType = ReportFieldControl.AccessType.TECH)
    private BigDecimal bigDecimal2;

}