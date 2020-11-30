package com.frogdevelopment.nihongo.export;

import org.springframework.jdbc.core.ResultSetExtractor;

public interface ExportDao {

    void export(final String lang, final ResultSetExtractor<Void> rse);


}
