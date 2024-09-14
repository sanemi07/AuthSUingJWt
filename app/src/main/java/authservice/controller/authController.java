package authservice.controller;

import authservice.entities.UserInfo;
import authservice.response.JwtResponse;
import authservice.service.RefreshtokenServiceImpl;
import authservice.service.UserDetailsServiceimpl;
import authservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

public class authController {


    @RestController
    @RequestMapping("/auth")
    public class AuthController {

        @Autowired
        private UserDetailsServiceimpl userInfoService;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private RefreshtokenServiceImpl refreshTokenService;

        @PostMapping("/signup")
        public ResponseEntity<?> register(@RequestBody UserInfo user) {
            UserInfo savedUser = userInfoService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.generateAccessToken(username);
            String refreshToken = jwtUtils.generateRefreshToken(username);
            refreshTokenService.createRefreshToken(refreshToken, userInfoService.findByUsername(username));

            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        }

        @PostMapping("/refresh")
        public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
            if (jwtUtils.validateToken(refreshToken)) {
                String username = jwtUtils.getUsernameFromToken(refreshToken);
                String newAccessToken = jwtUtils.generateAccessToken(username);

                return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
            } else {
                return ResponseEntity.status(403).body("Invalid refresh token");
            }
        }
    }
}
