package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication

public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository  clientRepository, AccountRepository  accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientloanrepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Lucia", "Lopez", "lucilopez@mindhub.com");
			Client client3 = new Client("Clara", "Gonzalez", "claragonzalez@mindhub.com");

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.01);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.80);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 10000.50);
			Account account4 = new Account("VIN004", LocalDateTime.now(), 15000.30);

			Transaction transaction1 = new Transaction(CREDIT, 100.50, "transfer", LocalDateTime.now());
			Transaction transaction2 = new Transaction(DEBIT, -200.00, "payShop", LocalDateTime.now());
			Transaction transaction3 = new Transaction(CREDIT, 100.00, "deposit", LocalDateTime.now());
			Transaction transaction4 = new Transaction(DEBIT, -200.00, "buyFood", LocalDateTime.now());
			Transaction transaction5 = new Transaction(DEBIT, 50.00, "payCoffee", LocalDateTime.now().plusDays(1));

			Loan loan1 = new Loan("Mortgage Loan", 500000, (List.of(12,24,36,48,60)));
			Loan loan2 = new Loan("Personal Loan", 100000, (List.of(6,12,24)));
			Loan loan3 = new Loan("Car Loan", 300000, (List.of(6,12,24,36)));

			ClientLoan clientloan1 = new ClientLoan(400000, 60, "Mortgage Loan", client1, loan1);
			ClientLoan clientloan2 = new ClientLoan(50000, 12, "Personal Loan", client1, loan2);

			ClientLoan clientloan3 = new ClientLoan(100000, 24, "Personal Loan", client2, loan2);
			ClientLoan clientloan4 = new ClientLoan(200000, 36, "Car Loan", client2, loan3);


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client3.addAccount(account4);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);


			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);
			account4.addTransaction(transaction4);
			account1.addTransaction(transaction5);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientloanrepository.save(clientloan1);
			clientloanrepository.save(clientloan2);
			clientloanrepository.save(clientloan3);
			clientloanrepository.save(clientloan4);
		};

	}
}




