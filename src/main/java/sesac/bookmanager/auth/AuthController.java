package sesac.bookmanager.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.Security.CustomUserDetails;
import sesac.bookmanager.auth.data.LoginRequestDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletResponse response
    ) {
        authService.logout(customUserDetails);
        // 클라이언트에 쿠키 무효화 지시
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(true).sameSite("Strict").path("/").maxAge(0)
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
        return ResponseEntity.ok(ApiResponse.success(null));
    }



//    @PostMapping("/find-id")
//    public  ResponseEntity<ApiResponse<Object>> findId(@RequestBody FindIdRequest request) {
//        return ResponseEntity.ok(authService.findId(request));
//    }
//
//    @PostMapping("/find-password")
//    public  ResponseEntity<ApiResponse<Object>> findPassword(@RequestBody FindPasswordRequest request) {
//        return ResponseEntity.ok(authService.findPassword(request));
//    }

//
//    @GetMapping("/check-id")
//    public ResponseEntity<Boolean> checkId(@RequestParam String username) {
//        return ResponseEntity.ok(authService.isIdDuplicate(username));
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        authService.register(request);
//        return ResponseEntity.ok("회원가입 완료");
//    }
}
