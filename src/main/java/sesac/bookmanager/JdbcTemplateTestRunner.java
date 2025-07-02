package sesac.bookmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JdbcTemplateTestRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 간단한 쿼리 실행 (예: user 테이블 개수 조회)
        List<Map<String, Object>> columns = jdbcTemplate.queryForList("SHOW COLUMNS FROM user");
        for (Map<String, Object> column : columns) {
            System.out.println(column);
        }

    }
}