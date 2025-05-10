package com.teamname.plant.plant.controller;

import com.teamname.plant.plant.dto.LoginRequest;
import com.teamname.plant.plant.model.User;
import com.teamname.plant.plant.repository.UserRepository;
import com.teamname.plant.plant.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "존재하지 않는 사용자입니다."));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}


