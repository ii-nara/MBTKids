package ureca.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ureca.entity.AdminUserEntity;
import com.ureca.entity.BookEntity;
import com.ureca.repository.AdminUserRepository;
import com.ureca.repository.BookRepository;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminUserTest {
  private static final Logger logger = LoggerFactory.getLogger(AdminUserTest.class);


  @Autowired
  AdminUserRepository adminUserRepository;

  // 등록 테스트
  @Test
  void testCreateAdminUser() {
    AdminUserEntity newAdminUser = AdminUserEntity.builder()
        .adminLoginId("testAdminUser02")
        .password("1111")
        .createdAt(new Date())
        .build();

    AdminUserEntity savedAdminUser = adminUserRepository.save(newAdminUser);
    logger.info("데이터 등록 확인: {}", savedAdminUser);
    assertThat(savedAdminUser).isNotNull();
    assertThat(savedAdminUser.getAdminId()).isGreaterThan(0);
  }

  // 조회 테스트
  @Test
  void testReadAdminUser() {
    AdminUserEntity getAdminUser =
        adminUserRepository
            .findById(1)
            .orElseThrow(() -> new RuntimeException("data not found"));
    logger.info("데이터 조회 확인: { " + getAdminUser + " }");
  }

  // 삭제 테스트
  @Test
  void testDeleteAdminUser() {
    int adminIdToDelete = 2;

    adminUserRepository.deleteById(adminIdToDelete);
    logger.info("삭제 ID: " + adminIdToDelete);

    boolean exists = adminUserRepository.existsById(adminIdToDelete);
    logger.info("데이터 삭제 확인 " + adminIdToDelete + " exists: " + exists);
  }

}
