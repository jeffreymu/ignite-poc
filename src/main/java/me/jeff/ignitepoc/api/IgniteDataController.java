package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.example.IgniteSimpleHandler;
import me.jeff.ignitepoc.model.CityDTO;
import me.jeff.ignitepoc.service.SqlDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IgniteDataController {

    @Autowired
    private IgniteSimpleHandler handler;

    @Autowired
    private SqlDataService service;

    @GetMapping("/api/cache/query")
    public void queryData() {
        handler.doQuery();
    }

    @GetMapping("/api/cache/create")
    public void createData() {
        handler.doInsert();
        handler.doQuery();
    }

    @GetMapping("/api/cache/update")
    public void updateData() {
        handler.doUpdate();
        handler.doQuery();
    }

    @GetMapping("/api/cache/remove")
    public void removeData() {
        handler.doRemove();
        handler.doQuery();
    }

    @GetMapping("/api/city")
    public CityDTO getCitiesByID(@RequestParam(value = "id", required = true) int id) {
        return service.getCityByID(id);
    }


    @GetMapping("/api/cache/queryClient")
    public void queryClientData() {
        handler.doClientQuery();
    }

    @GetMapping("/api/cache/createClient")
    public void createClientData() {
        handler.doClientInsert();
        handler.doClientQuery();
    }

}
