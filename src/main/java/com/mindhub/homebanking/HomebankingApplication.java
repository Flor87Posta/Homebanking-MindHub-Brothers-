package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;



@SpringBootApplication

public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository  clientRepository, AccountRepository  accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientloanrepository, CardRepository cardRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("mel1212ba"));
			Client client2 = new Client("Lucia", "Lopez", "lucilopez@mindhub.com", passwordEncoder.encode("lu1010lo"));
			Client client3 = new Client("Clara", "Gonzalez", "claragonzalez@mindhub.com", passwordEncoder.encode("cla1313gon"));
			Client admin = new Client("Florencia", "Postacchini","florenciapostacchini@gmail.com", passwordEncoder.encode("flo1014pos"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.01);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.80);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 10000.50);
			Account account4 = new Account("VIN004", LocalDateTime.now(), 15000.30);

			Transaction transaction1 = new Transaction(CREDIT, 100.50, "transfer", LocalDateTime.now());
			Transaction transaction2 = new Transaction(DEBIT, -200.00, "payShop", LocalDateTime.now());
			Transaction transaction3 = new Transaction(CREDIT, 100.00, "deposit", LocalDateTime.now());
			Transaction transaction4 = new Transaction(DEBIT, -200.00, "buyFood", LocalDateTime.now());
			Transaction transaction5 = new Transaction(DEBIT, 50.00, "payCoffee", LocalDateTime.now().plusDays(1));

			Loan loan1 = new Loan("MORTGAGE", 500000, (List.of(12,24,36,48,60)));
			Loan loan2 = new Loan("PERSONAL", 100000, (List.of(6,12,24)));
			Loan loan3 = new Loan("AUTO", 300000, (List.of(6,12,24,36)));


			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientloan1 = new ClientLoan(400000, 60);
			ClientLoan clientloan2 = new ClientLoan(50000, 12);
			ClientLoan clientloan3 = new ClientLoan(100000, 24);
			ClientLoan clientloan4 = new ClientLoan(200000, 36);


			loan1.addClientLoan(clientloan1);
			loan2.addClientLoan(clientloan2);
			loan2.addClientLoan(clientloan3);
			loan3.addClientLoan(clientloan4);


			client1.addClientLoan(clientloan1);
			client1.addClientLoan(clientloan2);
			client2.addClientLoan(clientloan3);
			client3.addClientLoan(clientloan4);

			Card card1 = new Card (client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "4444-5555-6666-8888", 111, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card2 = new Card (client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "4455-7777-8888-9999", 222, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card3 = new Card (client2.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.SILVER, "4455-9999-1111-7777", 333, LocalDate.now(), LocalDate.now().plusYears(5));


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(admin);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client3.addAccount(account4);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

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


			clientloanrepository.save(clientloan1);
			clientloanrepository.save(clientloan2);
			clientloanrepository.save(clientloan3);
			clientloanrepository.save(clientloan4);


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
		};

	}
}




