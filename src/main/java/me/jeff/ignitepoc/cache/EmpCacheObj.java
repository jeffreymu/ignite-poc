package me.jeff.ignitepoc.cache;

import me.jeff.ignitepoc.examples.model.Address;
import me.jeff.ignitepoc.examples.model.Employee;
import me.jeff.ignitepoc.examples.model.EmployeeKey;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.Arrays;

public class EmpCacheObj implements CustomCache<EmployeeKey, Employee> {

    public static void create(Ignite ignite, EmpCacheConfigFactory factory) {
        IgniteCache<EmployeeKey, Employee>  employeeCache = ignite.getOrCreateCache(
                factory.createCache(factory.getCacheName(), null, null));

        employeeCache.put(new EmployeeKey(1, 1), new Employee(
                "James Wilson",
                12500,
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                Arrays.asList("Human Resources", "Customer Service")
        ));

        employeeCache.put(new EmployeeKey(2, 1), new Employee(
                "Daniel Adams",
                11000,
                new Address("184 Fidler Drive, San Antonio, TX", 78130),
                Arrays.asList("Development", "QA")
        ));

        employeeCache.put(new EmployeeKey(3, 1), new Employee(
                "Cristian Moss",
                12500,
                new Address("667 Jerry Dove Drive, Florence, SC", 29501),
                Arrays.asList("Logistics")
        ));
    }

}
