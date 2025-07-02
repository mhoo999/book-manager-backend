package sesac.bookmanager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.user.data.UserInfoDto;
import sesac.bookmanager.user.data.UserStatusDto;

@Controller
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public String getUsersList(Model model,@RequestParam(defaultValue = "0") int page){
        int size = 10;
        Page<UserInfoDto> users = adminUserService.getUsersList(page,size);
        model.addAttribute("users", users);
        return "admin/userList";
    }

    @GetMapping("/{user_id}")
    public String getUser(Model model, @PathVariable int user_id){
        UserInfoDto userInfo = adminUserService.getUserInfo(user_id);
        model.addAttribute("userInfo", userInfo);
        return "admin/userInfo";
    }

    /** 폐기 - 정보 수정할 필드가 크게 없음**/
//    @PutMapping("/{user_id}")
//    public String modifyUserByAdmin(Model model, @PathVariable String user_id){
//        return "";
//    }
    @DeleteMapping("/{user_id}")
    public String withdrawUserByAdmin(Model model, @PathVariable int user_id){
        UserInfoDto userInfo = adminUserService.withDrawUserByAdmin(user_id);
        model.addAttribute("userInfo", userInfo);
        return "admin/userInfo";
    }

    @ResponseBody
    @GetMapping("/status")
    public UserStatusDto getUserStatus(){
        return adminUserService.getUserStatus();
    }
}
