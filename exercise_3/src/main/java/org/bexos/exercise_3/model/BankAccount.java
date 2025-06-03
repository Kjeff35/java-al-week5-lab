package org.bexos.exercise_3.model;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bexos.exercise_3.service.EncryptedStringConverter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Convert(converter = EncryptedStringConverter.class)
    private String accountNumber;

    @Column(nullable = false)
    @Convert(converter = EncryptedStringConverter.class)
    private String iban;

    @Column(nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
