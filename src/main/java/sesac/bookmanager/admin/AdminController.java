package sesac.bookmanager.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.admin.data.AdminInfoDto;

@Controller
@RequestMapping("/admin/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping
        public String getAdminList(Model model , @RequestParam(defaultValue = "0") int page){
        int size =10;
        Page<AdminInfoDto> admins = adminService.getAdminsList(page,size);
        model.addAttribute("admins", admins);
        return "admin/admin/adminList";
    }

    @GetMapping("/{adminId}")
    public String getAdmin(Model model, @PathVariable("adminId") int adminId){
        AdminInfoDto adminInfo = adminService.getAdminInfo(adminId);
        model.addAttribute("adminInfo", adminInfo);
        return "admin/admin/adminInfo";
    }


    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<ApiResponse<Long>> getAllAdminsCount(){
        return ResponseEntity.ok(ApiResponse.success(adminService.getCount()));
    }

    @PostMapping("/{adminId}")
    public String deleteAdmin(@PathVariable("adminId") int adminId){
        adminService.deleteAdmin(adminId);
        return "redirect:/admin/admins";
    }

}
