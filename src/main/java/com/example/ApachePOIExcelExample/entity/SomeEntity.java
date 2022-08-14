package com.example.ApachePOIExcelExample.entity;

import com.example.ApachePOIExcelExample.enums.PostgreSQLEnumType;
import com.example.ApachePOIExcelExample.enums.Type1;
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

    @Column(name = "string")
    private String string;

    @Column(name = "type_1")
    @Type(type = "type_1_type")
    @Enumerated(EnumType.STRING)
    private Type1 type1;

    @Column(name = "big_decimal")
    private BigDecimal bigDecimal;

    public SomeDataLoadDto toSomeDataLoadDto() {
        return SomeDataLoadDto.builder()
                .id(this.id)
                .string(this.string)
                .type1(this.type1)
                .bigDecimal(this.bigDecimal)
                .build();
    }

}