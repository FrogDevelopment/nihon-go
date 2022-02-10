package com.frogdevelopment.nihongo.lessons.dao;

import javax.transaction.Transactional;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import static javax.transaction.Transactional.TxType.MANDATORY;

@Transactional(MANDATORY)
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface LessonDao extends CrudRepository<LessonInfo, Integer> {

    @Query("INSERT INTO lessons(lesson) VALUES (:lesson) ON CONFLICT (lesson) DO UPDATE SET update_datetime = NOW()")
    void upsertLesson(int lesson);

    @Query("UPDATE lessons SET export_datetime = NOW() WHERE lesson = :lesson")
    void updateExportTime(@Id int lesson);
}
