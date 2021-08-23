package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.common.MoIResponse;
import me.jeff.ignitepoc.service.IgniteCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AppRequestController {

    @Autowired
    IgniteCompleteService igniteCompleteService;

    @GetMapping("/api/demo")
    public void testing() {
        log.info("***********Request testing***********");
    }

    @RequestMapping(value = "/api/getCache", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse get(@RequestParam String cacheName, @RequestParam String key) {
        igniteCompleteService.getPutCountry(key);
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/api/updateField", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse update(@RequestParam String cacheName, @RequestParam String key) {
        igniteCompleteService.updateSingleField(key);
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/api/population", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse population(@RequestParam String cacheName, @RequestParam String code, @RequestParam String number) {
        igniteCompleteService.updateCitiesPopulation(cacheName, code, Integer.parseInt(number));
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/api/calculate", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse calculate(@RequestParam String cacheName, @RequestParam String code) {
        igniteCompleteService.calculateAverageCountryPopulation(code);
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/api/subscribe", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse subscribe(@RequestParam String cacheName, @RequestParam String code) {
        igniteCompleteService.subscribeForDataUpdates();
        return new MoIResponse("OK", null);
    }
}
