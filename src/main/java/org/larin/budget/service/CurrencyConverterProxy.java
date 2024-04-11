package org.larin.budget.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "AccountService", url = "https://api.coingate.com/v2/rates/merchant/")
public interface CurrencyConverterProxy {

    @RequestMapping(method = RequestMethod.GET, value = "/{from}/{to}")
    String getRate(@PathVariable("from") String from, @PathVariable("to") String to);
}