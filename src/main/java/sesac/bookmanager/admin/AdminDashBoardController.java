package sesac.bookmanager.admin;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashBoardController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}
