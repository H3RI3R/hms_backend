package com.example.main.Security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

@Component
public class JwtEntryPoint  implements AuthenticationEntryPoint {



        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws AccessDeniedException, IOException, ServletException, java.io.IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("entryPoint exception");
            PrintWriter out = response.getWriter();
            out.println("Access Denied !! "+authException.getMessage());

        }



}


