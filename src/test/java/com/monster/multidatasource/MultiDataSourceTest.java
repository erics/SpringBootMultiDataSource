package com.monster.multidatasource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.monster.app.ApplicationDemo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = ApplicationDemo.class)
public class MultiDataSourceTest {

	@Autowired
	@Qualifier("primaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate1;

	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate2;

	@Before
	public void setUp() {
		jdbcTemplate1.update("delete from book");
		jdbcTemplate2.update("delete from book");
	}

	@Test
	public void test() throws Exception {
		jdbcTemplate1.update("insert into book(barcode,bookname,author) values(?, ?, ?)",
				"9781617292545", "Spring Boot in Action", "Craig Walls");
		jdbcTemplate1.update("insert into book(barcode,bookname,author) values(?, ?, ?)",
				"9781633430235", "Docker in Action", "Jeff Nickoloff");

		jdbcTemplate2.update("insert into book(barcode,bookname,author) values(?, ?, ?)",
				"9781617292545", "Spring Boot in Action", "Craig Walls");

		Assert.assertEquals("2",
				jdbcTemplate1.queryForObject("select count(1) from book", String.class));
		Assert.assertEquals("1",
				jdbcTemplate2.queryForObject("select count(1) from book", String.class));
	}
}
