package sesac.bookmanager.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.auth.data.AdminLoginFormDto;
import sesac.bookmanager.auth.data.AdminRegisterRequest;

@Controller
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AuthAdminController {

    private final AuthAdminService  authAdminService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("adminLoginFormDto", new AdminLoginFormDto());
        return "admin/auth/login";
    }

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
    public ResponseEntity<ApiResponse<Boolean>> checkId(@RequestParam String adminAccountId) {
        return ResponseEntity.ok(ApiResponse.success(authAdminService.checkId(adminAccountId)));
    }
    @GetMapping("/registration")
    public String registerAdmin(Model model){
        model.addAttribute("adminRegisterRequest", new AdminRegisterRequest());
        return "admin/auth/registration";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute AdminRegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 에러 있으면 다시 등록 폼으로 돌려보내기
            return "admin/auth/registration";
        }
        authAdminService.register(request);
        return "redirect:/admin/admins";
    }
}
