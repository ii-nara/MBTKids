import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentJpaRepository;
import com.ureca.service.ParentServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ParentJoinTest {

  @Mock
  private ParentJpaRepository parentJpaRepository;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @InjectMocks
  private ParentServiceImpl parentService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void 회원가입_성공() {
    ParentSignUpRequestDto dto = ParentSignUpRequestDto.builder()
        .email("test@naver.com")
        .parentLoginId("test")
        .password("1234")
        .userName("testUser")
        .phoneNumber("01012345678")
        .provider("google")
        .infoAgreeYn(true)
        .build();

    when(parentJpaRepository.findByParentLoginId(dto.getParentLoginId())).thenReturn(
        Optional.empty());
    when(bCryptPasswordEncoder.encode(dto.getPassword())).thenReturn("encodedPwd");
    when(parentJpaRepository.save(any(ParentEntity.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    ParentEntity parent = parentService.create(dto);

    assertNotNull(parent);
    assertEquals("test@naver.com", parent.getEmail());
    assertEquals("test", parent.getParentLoginId());
    assertEquals("encodedPwd", parent.getPassword());
    verify(parentJpaRepository, times(1)).save(any(ParentEntity.class));
  }

  @Test
  void 중복아이디_회원가입_실패() {

    ParentSignUpRequestDto dto = ParentSignUpRequestDto.builder()
        .email("test@naver.com")
        .parentLoginId("testId")
        .password("1234")
        .userName("testUser")
        .phoneNumber("01012345678")
        .provider("Google")
        .infoAgreeYn(true)
        .build();

    ParentEntity parent = ParentEntity.builder()
        .email("test@naver.com")
        .parentLoginId("testId")
        .password("1234")
        .build();

    when(parentJpaRepository.findByParentLoginId(dto.getParentLoginId())).thenReturn(
        Optional.of(parent));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      parentService.create(dto);
    });

    assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());
    verify(parentJpaRepository, times(0)).save(any(ParentEntity.class));
  }
}
