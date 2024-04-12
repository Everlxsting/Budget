package org.larin.budget;

import org.larin.budget.data.dto.DepositTxDTO;
import org.larin.budget.data.dto.WithdrawalTxDTO;
import org.larin.budget.data.entity.tx.DepositTx;
import org.larin.budget.data.entity.tx.WithdrawalTx;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class OrikaMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.registerClassMap(factory.classMap(WithdrawalTx.class,
                WithdrawalTxDTO.class).byDefault().toClassMap());
        
        factory.registerClassMap(factory.classMap(DepositTx.class,
                DepositTxDTO.class).byDefault().toClassMap());
        super.configure(factory);
    }
}
