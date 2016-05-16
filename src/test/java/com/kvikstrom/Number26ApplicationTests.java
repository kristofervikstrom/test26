package com.kvikstrom;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.model.Transaction;
import com.kvikstrom.repository.TransactionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Number26Application.class)
@WebIntegrationTest(randomPort = true)
public class Number26ApplicationTests {

	@Autowired
	private TransactionRepository repository;
	
	@Value("${local.server.port}")
	private int port;
	
	private String getUrl() {
		return "http://localhost:" + port;
	}
	
	@Test
	public void testInsertTransaction() {

		TransactionDTO request = new TransactionDTO();
		request.setAmount(5000);
		request.setType("cars");
		
		RestTemplate template = new TestRestTemplate();
		ResponseEntity<Void> response = template.exchange(this.getUrl() + "/transactionservice/transaction/1",
				HttpMethod.PUT, new HttpEntity<>(request), Void.class);
		assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));

		Transaction transaction = this.repository.findById(1L);
		assertNotNull(transaction);
		assertThat(transaction.getAmount(), is(5000.0));
		assertThat(transaction.getType(), is("cars"));
		assertNull(transaction.getParent_id());

	}
	
	@Test
	public void testFindByType() {
		TransactionDTO transaction1 = new TransactionDTO();
		transaction1.setAmount(5000);
		transaction1.setType("shopping");
		this.repository.insert(2L, transaction1);
		
		TransactionDTO transaction2 = new TransactionDTO();
		transaction2.setAmount(10000);
		transaction2.setType("shopping");
		this.repository.insert(3L, transaction2);
		
		when().get(this.getUrl() + "/transactionservice/types/shopping")
			.then().assertThat().equals(new Long[]{2L,3L});
		
		when().get(this.getUrl() + "/transactionservice/types/cars")
		.then().assertThat().equals(new Long[]{});
	}
	
	@Test
	public void testSum() {
		TransactionDTO transaction1 = new TransactionDTO();
		transaction1.setAmount(500);
		transaction1.setType("computer");
		this.repository.insert(4L, transaction1);
		
		TransactionDTO transaction2 = new TransactionDTO();
		transaction2.setAmount(100);
		transaction2.setType("monitor");
		transaction2.setParent_id(4L);
		this.repository.insert(5L, transaction2);
		
		TransactionDTO transaction3 = new TransactionDTO();
		transaction3.setAmount(2000);
		transaction3.setType("car");
		this.repository.insert(6L, transaction3);
		
		double sum1 = when().get(this.getUrl() + "/transactionservice/sum/4").then().extract().jsonPath()
				.getDouble("sum");
		assertThat(sum1, equalTo(600.0));

		double sum2 = when().get(this.getUrl() + "/transactionservice/sum/5").then().extract().jsonPath()
				.getDouble("sum");
		assertThat(sum2, equalTo(100.0));

		double sum3 = when().get(this.getUrl() + "/transactionservice/sum/6").then().extract().jsonPath()
				.getDouble("sum");
		assertThat(sum3, equalTo(2000.0));

	}
	
	@Test
	public void testEntityNotFound() {
		TransactionDTO transaction1 = new TransactionDTO();
		transaction1.setAmount(5000);
		transaction1.setType("shopping");
		this.repository.insert(7L, transaction1);
		
		Response response = RestAssured.get(this.getUrl() + "/transactionservice/transaction/{id}", new Random().nextInt());
		assertThat(response.getStatusCode(), equalTo(404));
	}
	
	@Test
	public void testBadData() {
		TransactionDTO request = new TransactionDTO();
		request.setAmount(-123);
		request.setType(null);
		
		RestTemplate template = new TestRestTemplate();
		ResponseEntity<Void> response = template.exchange(this.getUrl() + "/transactionservice/transaction/8",
				HttpMethod.PUT, new HttpEntity<>(request), Void.class);
		assertThat(response.getStatusCode(), is(equalTo(HttpStatus.BAD_REQUEST)));

	}
	
	@Test
	public void testDuplicateId() {
		TransactionDTO transaction1 = new TransactionDTO();
		transaction1.setAmount(5000);
		transaction1.setType("shopping");
		this.repository.insert(9L, transaction1);
		
		TransactionDTO request = new TransactionDTO();
		request.setAmount(1000);
		request.setType("computer");
		
		RestTemplate template = new TestRestTemplate();
		ResponseEntity<Void> response = template.exchange(this.getUrl() + "/transactionservice/transaction/9",
				HttpMethod.PUT, new HttpEntity<>(request), Void.class);
		assertThat(response.getStatusCode(), is(equalTo(HttpStatus.CONFLICT)));
	}

}
