package com.example.ApachePOIExcelExample.entity;

import com.example.ApachePOIExcelExample.enums.PostgreSQLEnumType;
import com.example.ApachePOIExcelExample.enums.Type1;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.model.SomeDataLoadDto;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "some_entities")
@TypeDef(name = "type_1_type", typeClass = PostgreSQLEnumType.class)
public class SomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "string_1")
    private String string1;

    @Column(name = "string_2")
    private String string2;

    @Column(name = "string_3")
    private String string3;

    @Column(name = "type_1")
    @Type(type = "type_1_type")
    @Enumerated(EnumType.STRING)
    private Type1 type1;

    @Column(name = "big_decimal_1")
    private BigDecimal bigDecimal1;

    @Column(name = "big_decimal_2")
    private BigDecimal bigDecimal2;

    public SomeDataLoadDto toSomeDataLoadDto() {
        return SomeDataLoadDto.builder()
                .id(this.id)
                .string1(this.string1)
                .string2(this.string2)
                .string3(this.string3)
                .type1(this.type1)
                .bigDecimal1(this.bigDecimal1)
                .bigDecimal2(this.bigDecimal2)
                .build();
    }

    public ReportSheetDto toReportSheetDto() {
        return ReportSheetDto.builder()
                .string1(this.string1)
                .string2(this.string2)
                .string3(this.string3)
                .type1(this.type1)
                .bigDecimal1(this.bigDecimal1)
                .bigDecimal2(this.bigDecimal2)
                .build();
    }

}