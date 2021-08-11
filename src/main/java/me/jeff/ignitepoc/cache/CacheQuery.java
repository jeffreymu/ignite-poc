package me.jeff.ignitepoc.cache;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.examples.model.Employee;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import java.util.List;

@Slf4j
@Component
public class CacheQuery {

    @Autowired
    private EmpCacheConfigFactory empCacheConfigFactory;

    @Autowired
    private OrgCacheConfigFactory orgCacheConfigFactory;

    public void textQuery(String param) {
        IgniteCache<BinaryObject, BinaryObject> cache = empCacheConfigFactory.getIgniteCache();
        TextQuery<BinaryObject, BinaryObject> qry = new TextQuery<>(Employee.class, param);
        QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> employees = cache.query(qry);
       log.info(">>> Employees living in Texas:");
        for (Cache.Entry<BinaryObject, BinaryObject> e : employees.getAll())
            log.info(">>>     " + e.getValue().deserialize());
    }

    public void sqlFieldsQuery() {
        IgniteCache<BinaryObject, BinaryObject> cache = empCacheConfigFactory.getIgniteCache();
        SqlFieldsQuery qry = new SqlFieldsQuery("select name, salary from Employee");
        QueryCursor<List<?>> employees = cache.query(qry);
        log.info(">>> Employee names and their salaries:");
        for (List<?> row : employees.getAll())
            log.info(">>>     [Name=" + row.get(0) + ", salary=" + row.get(1) + ']');
    }

    public void sqlJoinQuery() {
        IgniteCache<BinaryObject, BinaryObject> cache = empCacheConfigFactory.getIgniteCache();
        SqlFieldsQuery qry = new SqlFieldsQuery(
                "select e.* from Employee e, \"" + orgCacheConfigFactory.getCacheName() + "\".Organization as org " +
                        "where e.organizationId = org.keyId and org.name = ?");

        String organizationName = "GridGain";
        QueryCursor<List<?>> employees = cache.query(qry.setArgs(organizationName));
        log.info(">>> Employees working for " + organizationName + ':');

        for (List<?> row : employees.getAll())
            log.info(">>>     " + row);
    }
}
