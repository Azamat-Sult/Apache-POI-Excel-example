package com.example.ApachePOIExcelExample.entity;

import com.example.ApachePOIExcelExample.enums.Type2;
import com.example.ApachePOIExcelExample.model.SomeObject;
import com.vladmihalcea.hibernate.type.array.EnumArrayType;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "ARRAY_COLUMN_EXAMPLE")
@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)
@TypeDef(
        typeClass = EnumArrayType.class,
        defaultForType = Type2[].class,
        parameters = {
                @Parameter(
                        name = EnumArrayType.SQL_ARRAY_TYPE,
                        value = "type_2_type"
                )
        }
)
public class ArrayColumnExample {

    private static final long serialVersionUID = 3243579873284628375L;

    @Id
    @SequenceGenerator(name = "S_ARRAY_COLUMN_EXAMPLE_GEN",
            sequenceName = "S_ARRAY_COLUMN_EXAMPLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ARRAY_COLUMN_EXAMPLE_GEN")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Type(
            type = "com.vladmihalcea.hibernate.type.array.ListArrayType",
            parameters = {
                    @Parameter(
                            name = ListArrayType.SQL_ARRAY_TYPE,
                            value = "type_2_type"
                    )
            }
    )
    @Column(name = "TYPE_2", columnDefinition = "type_2_type[]")
    @Builder.Default
    private List<Type2> type2List = new ArrayList<>();

    @Type(type = "JSONB")
    @Column(name = "SOME_OBJECT", columnDefinition = "JSONB")
    private SomeObject someObject;

}