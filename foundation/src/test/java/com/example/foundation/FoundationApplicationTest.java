package com.example.foundation;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

class FoundationApplicationTest {
    @Test
    void testMainRunsSpringApplication() {
        // Mock SpringApplication and ApplicationContext
        ApplicationContext context = mock(ApplicationContext.class);
        Environment env = mock(Environment.class);
        when(context.getEnvironment()).thenReturn(env);
        when(env.getProperty("server.port", "8080")).thenReturn("8080");

        // Use a spy to verify SpringApplication.run is called
        try (var mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(FoundationApplication.class, new String[]{})).thenReturn(context);
            FoundationApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(FoundationApplication.class, new String[]{}));
        }
    }
}
