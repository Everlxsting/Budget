package org.larin.budget.controller;

import org.larin.budget.data.dto.CurrencyDTO;
import org.larin.budget.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/currencies")
public class CurrencyController {
    @Autowired
    private CurrencyService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<CurrencyDTO> getAll() {
        return service.getCurrencies();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDTO create(@RequestBody @Valid CurrencyDTO curr) {
        return service.create(curr);
    }
}
