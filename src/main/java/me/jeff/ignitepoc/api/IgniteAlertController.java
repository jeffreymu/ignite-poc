package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.model.AlertEntry;
import me.jeff.ignitepoc.service.AlertsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
//@RestController
public class IgniteAlertController {

    @Autowired
    private AlertsService alertsService;

    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<AlertEntry> getServiceAlerts(@PathVariable final String serviceId) {
        return alertsService.getAlertForServiceId(serviceId);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<AlertEntry> getAllAlerts() {
        return alertsService.getAllAlerts();
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void createAlert(@RequestBody AlertEntry request) {
        alertsService.createAlertEntry(request);
    }

    @RequestMapping(value = "/{serviceId}/{errorCodeId}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updateAlert(@PathVariable final String serviceId, @PathVariable final String errorCodeId,
                            @RequestBody AlertEntry request) {
        alertsService.updateAlertEntry(serviceId, errorCodeId, request);
    }

    @RequestMapping(value = "/{alertId}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteAlert(@PathVariable final String alertId) {
        log.debug("Trying to delete a alert: {},{}", alertId);
    }

}
