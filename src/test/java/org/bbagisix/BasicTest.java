package org.bbagisix;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CI/CD 파이프라인 테스트를 위한 기본 테스트 클래스
 */
public class BasicTest {

    @Test
    @DisplayName("기본 덧셈 테스트")
    void testAddition() {
        // Given
        int a = 5;
        int b = 3;
        
        // When
        int result = a + b;
        
        // Then
        assertEquals(8, result, "5 + 3은 8이어야 합니다");
    }

    @Test
    @DisplayName("문자열 테스트")
    void testStringOperation() {
        // Given
        String greeting = "Hello";
        String name = "DonDoThat";
        
        // When
        String result = greeting + " " + name;
        
        // Then
        assertEquals("Hello DonDoThat", result);
        assertNotNull(result);
    }

    @Test
    @DisplayName("빌드 환경 테스트")
    void testBuildEnvironment() {
        // Given & When
        String javaVersion = System.getProperty("java.version");
        
        // Then
        assertNotNull(javaVersion, "Java 버전 정보가 있어야 합니다");
        System.out.println("현재 Java 버전: " + javaVersion);
    }
}
