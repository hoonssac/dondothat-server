package org.bbagisix.config;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@Log4j2
class RootConfigTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Test
	@DisplayName("Database 연결이 된다.")
	public void dataSource() throws SQLException {
		try (Connection con = dataSource.getConnection()) {
			log.info("DataSource 준비 완료");
			log.info("Connection URL: {}", con.getMetaData().getURL());
			log.info("Driver Name: {}", con.getMetaData().getDriverName());
			log.info("Database Product Name: {}", con.getMetaData().getDatabaseProductName());
			log.info("Database Product Version: {}", con.getMetaData().getDatabaseProductVersion());
			
			assertNotNull(con);
			assertTrue(con.getMetaData().getURL().contains("mysql"), "MySQL 연결이어야 합니다");
		}
	}

	@Test
	@DisplayName("SqlSessionFactory가 정상적으로 생성된다.")
	public void testSqlSessionFactory() {
		try (
			SqlSession session = sqlSessionFactory.openSession();
			Connection con = session.getConnection();
		) {
			log.info("SqlSession: {}", session);
			log.info("Connection: {}", con);
			log.info("MySQL Version: {}", con.getMetaData().getDatabaseProductVersion());
			
			assertNotNull(session);
			assertNotNull(con);
			
			// 간단한 MySQL 쿼리 테스트
			String result = session.selectOne("SELECT 'MySQL Connection Test' as message");
			log.info("MySQL 쿼리 결과: {}", result);

		} catch (Exception e) {
			log.error("SqlSessionFactory 테스트 실패: {}", e.getMessage());
			fail(e.getMessage());
		}
	}
	
	@Test
	@DisplayName("데이터베이스 연결 풀이 정상적으로 작동한다.")
	public void testConnectionPool() throws SQLException {
		// 여러 연결을 동시에 테스트
		try (Connection con1 = dataSource.getConnection();
			 Connection con2 = dataSource.getConnection()) {
			
			assertNotNull(con1);
			assertNotNull(con2);
			assertNotEquals(con1, con2, "서로 다른 연결이어야 합니다");
			
			log.info("Connection Pool 테스트 성공");
		}
	}
}
