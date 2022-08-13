package com.example.ApachePOIExcelExample.entity;

import com.example.ApachePOIExcelExample.enums.Type1;
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
@TypeDef(name = "type_1_types", typeClass = EnumType.class)
public class SomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "string")
    private String string;

    @Column(name = "type_1")
    @Type(type = "type_1_types")
    @Enumerated(EnumType.STRING)
    private Type1 type1;

    @Column(name = "big_decimal")
    private BigDecimal bigDecimal;

}