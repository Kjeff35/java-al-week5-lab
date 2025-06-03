package org.bexos.exercise_3.service.impl;

import lombok.RequiredArgsConstructor;
import org.bexos.exercise_3.model.BankAccount;
import org.bexos.exercise_3.model.User;
import org.bexos.exercise_3.repository.BankAccountRepository;
import org.bexos.exercise_3.service.BankAccountService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccount createAccountForUser(User user) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setAccountNumber(generateAccountNumber());
        account.setIban(generateIban());
        account.setPassword(user.getPassword());
        return bankAccountRepository.save(account);
    }

    private String generateAccountNumber() {
        return String.valueOf(1000000000L + (long)(Math.random() * 9000000000L));
    }

    private String generateIban() {
        return "DE" + (int)(Math.random() * 90 + 10) + " 3704 0044 0532 0130 00";
    }

}
