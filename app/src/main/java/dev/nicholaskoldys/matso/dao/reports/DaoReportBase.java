package dev.nicholaskoldys.matso.dao.reports;

import java.util.List;

public interface DaoReportBase<T> {

    List<T> queryReport();
}
