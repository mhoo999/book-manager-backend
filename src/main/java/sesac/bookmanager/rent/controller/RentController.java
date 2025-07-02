package sesac.bookmanager.rent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.rent.dto.request.CreateRentRequestDto;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.request.UpdateRentRequestDto;
import sesac.bookmanager.rent.dto.response.PageRentResponseDto;
import sesac.bookmanager.rent.dto.response.RentIdResponseDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;
import sesac.bookmanager.rent.service.RentService;
import sesac.bookmanager.security.CustomAdminDetails;
import sesac.bookmanager.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;

    @PostMapping("/register")
    public ResponseEntity<RentIdResponseDto> register(
            @RequestBody CreateRentRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.register(request, customUserDetails));
    }

    @GetMapping("/search")
    public ResponseEntity<PageRentResponseDto> searchRents(SearchRentRequestDto request) {
        return ResponseEntity.ok(rentService.searchRents(request));
    }

    @GetMapping("/{rentId}")
    public ResponseEntity<RentResponseDto> getRent(@PathVariable Long rentId) {
        return ResponseEntity.ok(rentService.getRent(rentId));
    }

    @PutMapping("{rentId}")
    public ResponseEntity<RentIdResponseDto> updateRent(
            @PathVariable Long rentId,
            @RequestBody UpdateRentRequestDto request,
            @AuthenticationPrincipal CustomAdminDetails customAdminDetails) {
        return ResponseEntity.ok(rentService.updateRent(rentId, request, customAdminDetails));
    }

}
