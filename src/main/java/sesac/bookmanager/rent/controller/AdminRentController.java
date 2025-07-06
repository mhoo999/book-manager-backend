package sesac.bookmanager.rent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.request.UpdateRentRequestDto;
import sesac.bookmanager.rent.dto.response.PageRentResponseDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;
import sesac.bookmanager.rent.service.RentService;
import sesac.bookmanager.security.CustomAdminDetails;

@Controller
@RequestMapping("/admin/v1/rents")
@RequiredArgsConstructor
public class AdminRentController {

    private final RentService rentService;

    @GetMapping("/search")
    public String searchRents(SearchRentRequestDto request, Model model) {
        PageRentResponseDto rents = rentService.searchRents(request);
        model.addAttribute("rents", rents);
        model.addAttribute("searchCondition", request);
        return "admin/rents/list";
    }

    @GetMapping("/{rentId}")
    public String getRent(@PathVariable Long rentId, Model model) {
        RentResponseDto rent = rentService.getRent(rentId);
        model.addAttribute("rent", rent);
        return "admin/rents/detail";
    }

    @GetMapping("/{rentId}/edit")
    public String showUpdateForm(@PathVariable Long rentId, Model model) {
        RentResponseDto rent = rentService.getRent(rentId);
        model.addAttribute("rent", rent);
        return "admin/rents/edit";
    }

    @PostMapping("/{rentId}/edit")
    public String updateRent(
            @PathVariable Long rentId,
            @Valid @ModelAttribute UpdateRentRequestDto request,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomAdminDetails customAdminDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("rentId", rentId);
            return "admin/rents/edit";
        }

        rentService.updateRent(rentId, request, customAdminDetails);
        return "redirect:/admin/v1/rents/" + rentId;
    }

    @GetMapping
    public String rentList(Model model) {
        SearchRentRequestDto defaultRequest = new SearchRentRequestDto();
        PageRentResponseDto rents = rentService.searchRents(defaultRequest);
        model.addAttribute("rents", rents);
        model.addAttribute("searchCondition", defaultRequest);
        return "admin/rents/list";
    }
}
