package com.hae.library.controllerTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@WebMvcTest(controllers = MemberControllerTest.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberControllerTest 단위 테스트")
public class MemberControllerTest {

}
