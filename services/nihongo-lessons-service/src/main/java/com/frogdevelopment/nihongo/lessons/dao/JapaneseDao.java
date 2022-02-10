package com.frogdevelopment.nihongo.lessons.dao;

import javax.transaction.Transactional;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import static javax.transaction.Transactional.TxType.MANDATORY;

@Transactional(MANDATORY)
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface JapaneseDao extends CrudRepository<Japanese, Long> {
}

