package org.bexos.exercise_3.service;

import org.bexos.exercise_3.model.BankAccount;
import org.bexos.exercise_3.model.User;

public interface BankAccountService {
    BankAccount createAccountForUser(User user);
}
