package com.example.hadbackend.jwt;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.bean.auth.Login;
import com.example.hadbackend.service.LoginService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

        @Autowired
        private JWTUtil jwtUtil;
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        LoginService loginService;

        @Autowired
        LoginRepository loginRepository;

        @GetMapping("/")
        public String welcome() {
            return "Welcome to javatechie !!";
        }

        @PostMapping("/initiallogin")
        public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
//            try {
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
//                );
//            } catch (Exception ex) {
//                throw new Exception("inavalid username/password");
//            }
//            Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword());

            try {
                System.out.println("1"+authRequest.getUserName()+" "+authRequest.getPassword());
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
                );
                System.out.println("2"+authRequest.getUserName()+" "+authRequest.getPassword());
            } catch (Exception e) {
                System.out.println("3"+authRequest.getUserName()+" "+authRequest.getPassword());
                throw new Exception("inavalid username/password");
            }
            Login login=loginRepository.findAllByEmail(authRequest.getUserName());
//                if(login!=null)
//                return "Success";
//            return "Failure";
            return jwtUtil.generateToken(authRequest.getUserName());
        }

}
