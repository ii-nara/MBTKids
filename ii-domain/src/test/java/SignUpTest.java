import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ureca.domain.Parent;
import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.service.ParentServiceImpl;
import com.ureca.service.port.ParentRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class SignUpTest {

  @Mock
  private ParentRepository parentRepository;

  @InjectMocks
  private ParentServiceImpl parentService;

  private ParentSignUpRequestDto dto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    dto = ParentSignUpRequestDto.builder()
        .email("test@naver.com")
        .parentLoginId("testId")
        .password("1234")
        .userName("testName")
        .phoneNumber("01012345678")
        .infoAgreeYn(true)
        .build();
  }

  @Test
  void createParent() {
    // Given

    Parent newParent = Parent.builder()
        .email("test@naver.com")
        .parentLoginId("testId")
        .password("1234")
        .userName("testName")
        .phoneNumber("01012345678")
        .infoAgreeYn(true)
        .build();

    // When
    when(parentRepository.save(any(Parent.class))).thenReturn(newParent);
    Parent result = parentService.create(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("test@naver.com");
    assertThat(result.getParentLoginId()).isEqualTo("testId");
    assertThat(result.getUserName()).isEqualTo("testName");
    assertThat(result.getPhoneNumber()).isEqualTo("01012345678");
    assertThat(result.isInfoAgreeYn()).isTrue();

    verify(parentRepository, times(1)).save(any(Parent.class));
  }

  @Test
  void duplicateLoginId() {

    Parent existingParent = Parent.builder()
        .email("aaa@naver.com")
        .parentLoginId("testId")
        .password("1234")
        .userName("newParent")
        .phoneNumber("01011112222")
        .infoAgreeYn(true)
        .build();

    when(parentRepository.findByParentLoginId("testId")).thenReturn(
        Optional.ofNullable(existingParent));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        parentService.create(dto));

    assertThat(exception.getMessage()).isEqualTo("이미 사용 중인 아이디입니다.");
    verify(parentRepository, times(1)).findByParentLoginId("testId");
  }
}
