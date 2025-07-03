package sesac.bookmanager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.user.data.UserInfoDto;
import sesac.bookmanager.user.data.UserStatusDto;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public String getUsersList(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String status) {
        int size = 10;

        Page<UserInfoDto> users = adminUserService.getUsersList(page, size, type, keyword, status);

        model.addAttribute("users", users);
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/user/userList";
    }


    @GetMapping("/quits")
    public String getQuitUsersList(Model model,@RequestParam(defaultValue = "0") int page){
        int size = 10;
        Page<UserInfoDto> users = adminUserService.getQuitUsersList(page,size);
        model.addAttribute("users", users);
        return "admin/user/quitUsers";
    }

    @GetMapping("/{user_id}")
    public String getUser(Model model, @PathVariable int user_id){
        UserInfoDto userInfo = adminUserService.getUserInfo(user_id);
        model.addAttribute("userInfo", userInfo);
        return "admin/user/userInfo";
    }
    @ResponseBody
    @PutMapping("/{user_id}")
    public ResponseEntity<ApiResponse<?>> restoreUserByAdmin(@PathVariable int user_id) {
        adminUserService.restoreUserByAdmin(user_id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @ResponseBody
    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponse<?>> withdrawUserByAdmin(@PathVariable int user_id) {
        adminUserService.withDrawUserByAdmin(user_id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 폐기 - 정보 수정할 필드가 크게 없음**/
//    @PutMapping("/{user_id}")
//    public String modifyUserByAdmin(Model model, @PathVariable String user_id){
//        return "";
//    }


    @ResponseBody
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<UserStatusDto>> getUserStatus(){
        return ResponseEntity.ok(ApiResponse.success(adminUserService.getUserStatus()));
    }
}
