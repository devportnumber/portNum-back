//package com.portnum.number;
//
//import com.portnum.number.admin.entity.Admin;
//import com.portnum.number.admin.repository.AdminRepository;
//import com.portnum.number.popup.dto.request.PopupCreateRequest;
//import com.portnum.number.popup.entity.Popup;
//import com.portnum.number.popup.entity.PopupCategory;
//import com.portnum.number.popup.entity.PopupStatus;
//import com.portnum.number.popup.entity.embeddable.Address;
//import com.portnum.number.popup.entity.embeddable.Point;
//import com.portnum.number.popup.repository.PopupRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//import static com.portnum.number.popup.entity.PopupCategory.*;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final PopupRepository popupRepository;
//    private final AdminRepository adminRepository;
//
//
////    @Bean
////    public CommandLineRunner initializeData() {
////        return args -> insertPopupData();
////    }
//
//    @PostConstruct
////    @Transactional
//    public void insertPopupData() {
//        Admin admin = adminRepository.findById(1L)
//                .orElseThrow(() -> new IllegalStateException("Admin with ID 1 not found"));
//
//        List<PopupCategory> categories = Arrays.asList(BAKERY, CAFE, FASHION, GOODS, BAR, RESTAURANT, EXHIBITION);
//
//        System.out.println(categories);
//        Random random = new Random();
//
//        List<String> keywords = Arrays.asList("keyword1", "keyword2", "keyword3");
//
//        for (int i = 1; i <= 100000; i++) {
//            PopupCreateRequest request = new PopupCreateRequest(
//                    1L,
//                    "테스트 팝업 " + i,
//                    categories.get(random.nextInt(categories.size())),
//                    LocalDateTime.now(),
//                    LocalDateTime.now().plusDays(7),
//                    "09:00-18:00",
//                    PopupStatus.Y,
//                    new Point(23F, 23F), // Seoul coordinates as example
//                    new Address("123", "123", "123"),
//                    "간단한 설명 " + i,
//                    "상세 설명 " + i,
//                    "https://map.example.com/" + i,
//                    null,
//                    null,
//                    keywords
//            );
//
//            Popup popup = Popup.of(request, admin);
//
//            popupRepository.save(popup);
//
//            // Optional: Add logic to handle batch insert or save in chunks for performance.
//            if (i % 1000 == 0) {
//                System.out.println(i + " popups inserted...");
//            }
//        }
//        System.out.println("Data initialization completed.");
//    }
//}
//
