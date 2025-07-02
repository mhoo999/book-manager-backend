package sesac.bookmanager.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.auth.data.AdminLoginFormDto;
import sesac.bookmanager.auth.data.AdminRegisterRequest;

@Controller
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthAdminController {

    private final AuthAdminService  authAdminService;

    @PostMapping("/login")
    public String login(@ModelAttribute AdminLoginFormDto adminLoginFormDto) {
        authAdminService.login(adminLoginFormDto);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        authAdminService.logout();
        request.getSession().invalidate();
        return "redirect:/admin/login";
    }

    @ResponseBody
    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse<Boolean>> checkId(@RequestParam String adminEmail) {
        return ResponseEntity.ok(ApiResponse.success(authAdminService.checkId(adminEmail)));
    }

    @PostMapping("/register")
    public String register(@RequestBody AdminRegisterRequest request) {
        authAdminService.register(request);
        return "redirect:/admin/admins";
    }
}
