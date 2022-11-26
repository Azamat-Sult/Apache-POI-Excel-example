package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.ArrayColumnExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrayColumnExampleRepository extends JpaRepository<ArrayColumnExample, Long> {

    @Query("SELECT a FROM ArrayColumnExample a WHERE LOWER(array_to_string(type_2, ',')) " +
            "LIKE LOWER(CONCAT('%',?1,'%'))")
    List<ArrayColumnExample> findByArrayColumnContainingIgnoreCase(String search);

    @Query(value = "SELECT EXISTS(" +
                            "SELECT * FROM (" +
                                "SELECT JSONB_ARRAY_ELEMENTS_TEXT(" +
                                     "JSONB_ARRAY_ELEMENTS(" +
                                        "SOME_OBJECT->'subSomeObjects'" +
                                     ") #> '{someStringList}'" +
                                ") AS someStringList FROM array_column_example" +
                            ") AS subSomeObjects WHERE subSomeObjects.someStringList = ?1)", nativeQuery = true)
    boolean someStringListContainingText(String search);

}