package org.larin.budget.service;

import org.larin.budget.OrikaMapper;
import org.larin.budget.data.dto.CurrencyDTO;
import org.larin.budget.data.entity.Currency;
import org.larin.budget.data.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository repository;

    @Autowired
    private OrikaMapper mapper;

    @Transactional
    public List<CurrencyDTO> getCurrencies() {
        Iterable<Currency> t = repository.findAll();
        return mapper.mapAsList(t, CurrencyDTO.class);
    }


    @Transactional
    public CurrencyDTO create(CurrencyDTO curr) {

        Currency result = repository.save(mapper.map(curr, Currency.class));
        return mapper.map(result, CurrencyDTO.class);
    }
}
