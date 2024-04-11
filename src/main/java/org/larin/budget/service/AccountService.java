package org.larin.budget.service;

import org.larin.budget.OrikaMapper;
import org.larin.budget.controller.exception.ApiException;
import org.larin.budget.controller.exception.ForbiddenException;
import org.larin.budget.controller.exception.NotFoundException;
import org.larin.budget.data.entity.Account;
import org.larin.budget.data.entity.Currency;
import org.larin.budget.data.entity.tx.DepositTx;
import org.larin.budget.data.entity.tx.Transaction;
import org.larin.budget.data.entity.tx.WithdrawalTx;
import org.larin.budget.data.repository.AccountRepository;
import org.larin.budget.data.repository.CurrencyRepository;
import org.larin.budget.data.repository.TransactionRepository;
import org.larin.budget.data.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Supplier;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private CurrencyConverterProxy currencyConverterProxy;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TransactionRepository<Transaction> transactionRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private OrikaMapper mapper;

    @Transactional
    public List<AccountDTO> getMyAccounts() {
        List<Account> accList = repository.findByOwner(authService.getMyself());
        return mapper.mapAsList(accList, AccountDTO.class);
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccountById(Long id) {
        Account account = getAccount(id, false);
        return mapper.map(account, AccountDTO.class);
    }

    @Transactional
    public AccountDTO create(AccountNewDTO newAcc) {
        Account account = mapper.map(newAcc, Account.class);
        account.setCurrentValue(BigDecimal.ZERO);
        account.setCreateDate(OffsetDateTime.now());
        account.setOwner(authService.getMyself());


        
        account.setCurrency(getCurrency(newAcc.getCurrency()));

        Account res = repository.save(account);
        return mapper.map(res, AccountDTO.class);
    }

    @Transactional
    public TransactionDTO deposit(Long accountId, DepositTxDTO tx) {
        Account account = getAccount(accountId);
        DepositTx transaction = mapper.map(tx, DepositTx.class);
        transaction.setCreateDate(OffsetDateTime.now());
        transaction.setSrc(account);

        BigDecimal value = account.getCurrentValue();
        BigDecimal amount = convertToCurrency(
                transaction.getAmount(),
                transaction.getCurrency(),
                account.getCurrency());

        BigDecimal newValue = value.add(amount);
        account.setCurrentValue(newValue);
        transaction.setNewValue(newValue);

        DepositTx txResult = transactionRepository.save(transaction);
        repository.save(account);

        return mapper.map(txResult, TransactionDTO.class);
    }

    private Currency getCurrency(CurrencyIdDTO currency) {
        return currencyRepository.findById(currency.getCode())
                .orElseThrow(notFound("No such currency found"));
    }

    @Transactional
    public TransactionDTO withdraw(Long accountId, WithdrawalTxDTO tx) {
        Account acc = getAccount(accountId);

        WithdrawalTx transaction = mapper.map(tx, WithdrawalTx.class);
        transaction.setCreateDate(OffsetDateTime.now());
        transaction.setSrc(acc);

        BigDecimal value = acc.getCurrentValue();

        BigDecimal amount = convertToCurrency(transaction.getAmount(), transaction.getCurrency(), acc.getCurrency());

        BigDecimal newValue = value.subtract(amount);
        acc.setCurrentValue(newValue);
        transaction.setNewValue(newValue);

        WithdrawalTx txResult = transactionRepository.save(transaction);
        repository.save(acc);

        return mapper.map(txResult, TransactionDTO.class);
    }

    private Account getAccount(Long accountId) {
        return getAccount(accountId, true);
    }

    private Account getAccount(Long accountId, boolean needLock) {
        Account acc;
        String msg = "No account found [id = " + accountId + "]";
        if (needLock) {
            acc = repository.findByIdForUpdate(accountId).orElseThrow(notFound(msg));
        } else {
            acc = repository.findById(accountId).orElseThrow(notFound(msg));
        }

        ensureMine(acc);
        return acc;
    }

    public BigDecimal convertToCurrency(BigDecimal amount, Currency from, Currency to) {

        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(currencyConverterProxy.getRate(from.getCode(), to.getCode())));

        return amount.multiply(rate);
    }

    private void ensureMine(Account acc) {
        Long ownerId = acc.getOwner().getId();
        if (!ownerId.equals(authService.getMyself().getId())) {
            throw new ForbiddenException("You have no permission to access this account");
        }
    }

    private Supplier<ApiException> notFound(String s) {
        return () -> new NotFoundException(s);
    }

    public Page<TransactionDTO> getAccountTransactions(Pageable pageable, Long accountId) {
        Account account = getAccount(accountId, false);
        Page<Transaction> transactions = transactionRepository.findAllByAccount(pageable, account);
        return transactions.map((Transaction t) -> mapper.map(t, TransactionDTO.class));
    }

    @Transactional
    public void deleteTransaction(Long accountId, Long transactionId) {
        Account account = getAccount(accountId, false);

        transactionRepository.deleteById(account, transactionId);
    }

}
