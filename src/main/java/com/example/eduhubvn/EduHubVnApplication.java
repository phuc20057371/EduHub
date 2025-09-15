package com.example.eduhubvn;

import com.example.eduhubvn.dtos.auth.RegisterRequest;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.repositories.*;
import com.example.eduhubvn.services.AuthenticationService;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.micrometer.common.util.StringUtils.truncate;

@SpringBootApplication
public class EduHubVnApplication {

        public static void main(String[] args) {
                SpringApplication.run(EduHubVnApplication.class, args);
        }

        @PostConstruct
        public void started() {
                TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        }

        @Bean
        public CommandLineRunner init(UserRepository userRepository,
                        LecturerRepository lecturerRepository,
                        EducationInstitutionRepository educationInstitutionRepository,
                        PartnerOrganizationRepository partnerOrganizationRepository,
                        DegreeRepository degreeRepository,
                        CertificationRepository certificationRepository,
                        AttendedTrainingCourseRepository attendedTrainingCourseRepository,
                        OwnedTrainingCourseRepository ownedTrainingCourseRepository,
                        ResearchProjectRepository researchProjectRepository,

                        CourseRepository courseRepository,
                        CourseLecturerRepository courseLecturerRepository,

                        LecturerUpdateRepository lecturerUpdateRepository,
                        EducationInstitutionUpdateRepository educationInstitutionUpdateRepository,
                        PartnerOrganizationUpdateRepository partnerOrganizationUpdateRepository,
                        DegreeUpdateRepository degreeUpdateRepository,
                        CertificationUpdateRepository certificationUpdateRepository,
                        AttendedTrainingCourseUpdateRepository attendedTrainingCourseUpdateRepository,
                        OwnedTrainingCourseUpdateRepository ownedTrainingCourseUpdateRepository,
                        ResearchProjectUpdateRepository researchProjectUpdateRepository,

                        AuthenticationService authenticationService,
                        com.example.eduhubvn.services.SubAdminService subAdminService) {
                return args -> {
                        Faker faker = new Faker();
                        try {
                                if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                                        return;
                                }
                                // String domain = "eduhub.vn";
                                // 1. 10 Họ phổ biến ở Việt Nam
                                List<String> lastNames = new ArrayList<>(Arrays.asList(
                                                "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ",
                                                "Đặng"));

                                // 2. 50 Tên đệm phổ biến
                                List<String> middleNames = new ArrayList<>(Arrays.asList(
                                                "Văn", "Thị", "Hữu", "Đức", "Ngọc", "Xuân", "Quốc", "Gia", "Trung",
                                                "Minh",
                                                "Thanh", "Tấn", "Phúc", "Bá", "Duy", "Nhật", "Thái", "Thành", "Tiến",
                                                "Anh",
                                                "Viết", "Khánh", "Chí", "Tuấn", "Thiện", "Mạnh", "Đình", "Quang", "Tú",
                                                "Thi",
                                                "Thu", "Tường", "Kim", "Phương", "Tâm", "Thảo", "Hà", "Hải", "Hạnh",
                                                "Trà",
                                                "Trúc", "Cẩm", "Thùy", "Yến", "Diệu", "Mai", "Hương", "Lan", "Linh",
                                                "Loan"));

                                // 3. 50 Tên riêng phổ biến
                                List<String> firstNames = new ArrayList<>(Arrays.asList(
                                                "An", "Bình", "Châu", "Dương", "Đạt", "Dũng", "Giang", "Hà", "Hiếu",
                                                "Hoa",
                                                "Hòa", "Hùng", "Hương", "Huy", "Khánh", "Kiên", "Lan", "Linh", "Loan",
                                                "Long",
                                                "Mai", "Minh", "Nam", "Nga", "Ngân", "Ngọc", "Nhung", "Oanh", "Phong",
                                                "Phúc",
                                                "Quân", "Quỳnh", "Sơn", "Tâm", "Thành", "Thảo", "Thanh", "Thắng",
                                                "Thúy", "Trang",
                                                "Trúc", "Tuấn", "Tú", "Tùng", "Việt", "Vy", "Yến", "Vinh", "Bảo",
                                                "Tiến"));
                                List<String> addresses = new ArrayList<>(Arrays.asList(
                                                "Phường Bến Nghé, Quận 1, TP. Hồ Chí Minh",
                                                "Phường Cửa Nam, Quận Hoàn Kiếm, Hà Nội",
                                                "Phường Hòa Cường Bắc, Quận Hải Châu, Đà Nẵng",
                                                "Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh",
                                                "Phường Trần Hưng Đạo, TP. Hạ Long, Quảng Ninh",
                                                "Phường Vĩnh Ninh, TP. Huế, Thừa Thiên Huế",
                                                "Phường Tân An, TP. Buôn Ma Thuột, Đắk Lắk",
                                                "Phường Lê Lợi, TP. Vinh, Nghệ An",
                                                "Phường Hưng Lợi, Quận Ninh Kiều, Cần Thơ",
                                                "Phường An Khánh, Quận Ninh Kiều, Cần Thơ",
                                                "Phường Mỹ Phước, TP. Long Xuyên, An Giang",
                                                "Phường Nhơn Bình, TP. Quy Nhơn, Bình Định",
                                                "Phường Phú Hội, TP. Huế, Thừa Thiên Huế",
                                                "Phường Tân Phú, Quận 9, TP. Hồ Chí Minh",
                                                "Phường Trà Bá, TP. Pleiku, Gia Lai",
                                                "Phường Đông Vệ, TP. Thanh Hóa, Thanh Hóa",
                                                "Phường Tân Hiệp, TP. Biên Hòa, Đồng Nai",
                                                "Phường Thới An, Quận 12, TP. Hồ Chí Minh",
                                                "Phường Quang Trung, TP. Nam Định, Nam Định",
                                                "Phường Lê Bình, Quận Cái Răng, Cần Thơ",
                                                "Phường Nguyễn Cư Trinh, Quận 1, TP. Hồ Chí Minh",
                                                "Phường Tân Đông Hiệp, TP. Dĩ An, Bình Dương",
                                                "Phường Phước Long B, TP. Thủ Đức, TP. Hồ Chí Minh",
                                                "Phường Tân Tạo, Quận Bình Tân, TP. Hồ Chí Minh",
                                                "Phường 7, TP. Vũng Tàu, Bà Rịa - Vũng Tàu",
                                                "Phường Tân Lợi, TP. Buôn Ma Thuột, Đắk Lắk",
                                                "Phường Hà Huy Tập, TP. Vinh, Nghệ An",
                                                "Phường Nghĩa Tân, Quận Cầu Giấy, Hà Nội",
                                                "Phường Tân Thới Hòa, Quận Tân Phú, TP. Hồ Chí Minh",
                                                "Phường Long Bình, TP. Biên Hòa, Đồng Nai",
                                                "Phường Phú Thủy, TP. Phan Thiết, Bình Thuận",
                                                "Phường Trường Thi, TP. Vinh, Nghệ An",
                                                "Phường Phước Vĩnh, TP. Huế, Thừa Thiên Huế",
                                                "Phường Bình Chiểu, TP. Thủ Đức, TP. Hồ Chí Minh",
                                                "Phường Bửu Long, TP. Biên Hòa, Đồng Nai",
                                                "Phường Vĩnh Hải, TP. Nha Trang, Khánh Hòa",
                                                "Phường Thạch Linh, TP. Hà Tĩnh, Hà Tĩnh",
                                                "Phường Nguyễn Trãi, Quận Hà Đông, Hà Nội",
                                                "Phường 5, TP. Mỹ Tho, Tiền Giang",
                                                "Phường Trường An, TP. Huế, Thừa Thiên Huế",
                                                "Phường 2, TP. Sóc Trăng, Sóc Trăng",
                                                "Phường Đông Hòa, TP. Dĩ An, Bình Dương",
                                                "Phường Phú Lợi, TP. Thủ Dầu Một, Bình Dương",
                                                "Phường Tân Chánh Hiệp, Quận 12, TP. Hồ Chí Minh",
                                                "Phường Hòa Minh, Quận Liên Chiểu, Đà Nẵng",
                                                "Phường Hòa Thuận Tây, Quận Hải Châu, Đà Nẵng",
                                                "Phường Trảng Dài, TP. Biên Hòa, Đồng Nai",
                                                "Phường Hòa Phát, Quận Cẩm Lệ, Đà Nẵng",
                                                "Phường Phúc Lợi, Quận Long Biên, Hà Nội"));
                                List<String> bios = new ArrayList<>(Arrays.asList(
                                                "Tôi là một người năng động, cầu tiến và luôn sẵn sàng học hỏi. Với tinh thần trách nhiệm cao, tôi luôn cố gắng hoàn thành tốt mọi nhiệm vụ được giao.",
                                                "Tôi có nền tảng kiến thức vững chắc và đam mê trong lĩnh vực tôi theo đuổi. Tôi mong muốn được phát triển bản thân và đóng góp giá trị cho tập thể.",
                                                "Tôi là người kiên trì, cẩn thận và luôn chú trọng đến chất lượng công việc. Tôi tin rằng sự chăm chỉ và kỷ luật sẽ mang lại kết quả tốt đẹp.",
                                                "Với khả năng làm việc nhóm và giao tiếp tốt, tôi dễ dàng thích nghi với môi trường mới. Tôi luôn hướng đến sự chuyên nghiệp và hiệu quả trong công việc.",
                                                "Tôi là một người ham học hỏi, yêu thích khám phá và cải thiện bản thân mỗi ngày. Tôi luôn cố gắng hoàn thiện kỹ năng chuyên môn lẫn kỹ năng mềm.",
                                                "Tôi có kinh nghiệm làm việc trong môi trường áp lực cao và biết cách xử lý tình huống linh hoạt. Tôi luôn coi trọng tinh thần trách nhiệm và sự hợp tác.",
                                                "Tôi là người trung thực, nhiệt tình và có tinh thần học hỏi cao. Tôi mong muốn được làm việc trong môi trường năng động để phát triển kỹ năng và tích lũy kinh nghiệm.",
                                                "Tôi có tư duy logic, khả năng phân tích tốt và luôn tìm cách giải quyết vấn đề một cách hiệu quả. Tôi cũng rất quan tâm đến việc phát triển bền vững và sáng tạo trong công việc.",
                                                "Tôi là người cởi mở, thân thiện và thích chia sẻ kiến thức. Tôi tin rằng thái độ tích cực và tinh thần cầu tiến là chìa khóa của sự thành công.",
                                                "Với niềm đam mê trong lĩnh vực chuyên môn, tôi luôn tìm kiếm cơ hội để học hỏi và ứng dụng kiến thức vào thực tiễn. Tôi mong muốn được đồng hành cùng những người có cùng chí hướng."));

                                List<String> specializationWithRankList = new ArrayList<>(Arrays.asList(
                                                "Khoa học máy tính",
                                                "Quản trị kinh doanh",
                                                "Trí tuệ nhân tạo",
                                                "Marketing",
                                                "Kỹ thuật phần mềm",
                                                "Tài chính - Ngân hàng",
                                                "Sư phạm tiếng Anh",
                                                "Khoa học dữ liệu",
                                                "Kỹ thuật điện - điện tử",
                                                "Công nghệ thông tin"));
                                List<String> jobFieldWithExperienceList = new ArrayList<>(Arrays.asList(
                                                "Công nghệ thông tin",
                                                "Giáo dục và đào tạo",
                                                "Y tế và chăm sóc sức khỏe",
                                                "Tài chính và ngân hàng",
                                                "Kế toán và kiểm toán",
                                                "Marketing và truyền thông",
                                                "Xây dựng và kiến trúc",
                                                "Luật và pháp lý",
                                                "Du lịch và nhà hàng",
                                                "Sản xuất và kỹ thuật"));
                                List<String> urlCert = new ArrayList<>(Arrays.asList(
                                                "http://demoportal.ccvi.com.vn:8080/uploads/LECTURER/1/Bangtk_PMP KPS Cert.pdf",
                                                "http://demoportal.ccvi.com.vn:8080/uploads/LECTURER/1/Bangtk_CCNT_5971752_certificate.pdf"));
                                String degreeUrl = "http://demoportal.ccvi.com.vn:8080/uploads/LECTURER/1/Bangtotnghiep_Bangtk.pdf";

                                List<String> trainingCenters = new ArrayList<>(Arrays.asList(
                                                "Đại học Bách Khoa Hà Nội",
                                                "Đại học Khoa học Tự nhiên TP. HCM",
                                                "Trường Đại học Kinh tế Quốc dân",
                                                "Đại học FPT",
                                                "Trường Đại học Sư phạm Hà Nội",
                                                "Đại học Công nghệ Thông tin - ĐHQG TP.HCM",
                                                "Trung tâm Đào tạo Công nghệ Cao VTI",
                                                "Trung tâm Đào tạo Quốc tế Softech Aptech",
                                                "Đại học Ngân hàng TP. HCM",
                                                "Học viện Công nghệ Bưu chính Viễn thông"));
                                List<String> companies = new ArrayList<>(Arrays.asList(
                                                "Công ty Cổ phần VNG",
                                                "FPT Software",
                                                "Công ty TNHH Samsung Electronics Việt Nam",
                                                "Viettel Group",
                                                "Tập đoàn Vingroup",
                                                "Công ty Cổ phần Tiki",
                                                "Shopee Việt Nam",
                                                "Công ty TNHH KMS Technology",
                                                "Ngân hàng TMCP Quân Đội (MB Bank)",
                                                "Công ty TNHH Deloitte Việt Nam"));

                                List<String> degreesSample = List.of(
                                                "Cử nhân Khoa học Máy tính",
                                                "Thạc sĩ Quản trị Kinh doanh",
                                                "Tiến sĩ Công nghệ Sinh học",
                                                "Cử nhân Kinh tế",
                                                "Thạc sĩ Kỹ thuật Xây dựng",
                                                "Tiến sĩ Hóa học",
                                                "Cử nhân Sư phạm Tiếng Anh",
                                                "Thạc sĩ Luật học",
                                                "Tiến sĩ Khoa học Giáo dục",
                                                "Cử nhân Công nghệ Thông tin");
                                List<String> certificationsSample = List.of(
                                                "Chứng chỉ IELTS 7.5",
                                                "Chứng chỉ TOEIC 900",
                                                "Chứng chỉ MOS Excel Expert",
                                                "Chứng chỉ Lập trình Java",
                                                "Chứng chỉ Quản lý dự án PMP",
                                                "Chứng chỉ TESOL",
                                                "Chứng chỉ Ứng dụng CNTT nâng cao",
                                                "Chứng chỉ Kỹ năng mềm",
                                                "Chứng chỉ An toàn lao động",
                                                "Chứng chỉ Nghiệp vụ sư phạm");

                                List<String> courseTitles = List.of(
                                                "Lập trình Java cơ bản",
                                                "Phân tích và thiết kế hệ thống",
                                                "Trí tuệ nhân tạo ứng dụng",
                                                "Phát triển ứng dụng web với Spring Boot",
                                                "Kỹ năng giao tiếp chuyên nghiệp",
                                                "Quản lý dự án Agile và Scrum",
                                                "Phát triển ứng dụng di động với React Native",
                                                "Kiến trúc máy tính và Hệ điều hành",
                                                "Thiết kế giao diện người dùng (UI/UX)",
                                                "Phân tích dữ liệu với Python",
                                                "Tiếng Anh chuyên ngành CNTT",
                                                "Thương mại điện tử và Marketing số",
                                                "Kỹ thuật phần mềm nâng cao",
                                                "Cơ sở dữ liệu và SQL nâng cao",
                                                "An toàn thông tin và bảo mật hệ thống",
                                                "Lập trình Frontend với ReactJS",
                                                "Học máy (Machine Learning) cơ bản",
                                                "Điện toán đám mây với AWS",
                                                "Kỹ năng viết báo cáo khoa học",
                                                "Quản trị mạng và bảo mật hệ thống");
                                List<String> courseTopics = List.of(
                                                "Khoa học máy tính",
                                                "Phát triển phần mềm",
                                                "Trí tuệ nhân tạo",
                                                "Phân tích dữ liệu",
                                                "Thiết kế web",
                                                "Quản lý dự án",
                                                "An toàn thông tin",
                                                "Kỹ năng mềm",
                                                "Marketing số",
                                                "Khởi nghiệp và đổi mới sáng tạo",
                                                "Giáo dục và sư phạm",
                                                "Quản trị kinh doanh",
                                                "Lập trình nhúng",
                                                "Kỹ thuật mạng",
                                                "Phát triển game",
                                                "Thiết kế đồ họa",
                                                "Điện tử viễn thông",
                                                "Ngôn ngữ lập trình mới",
                                                "Công nghệ Blockchain",
                                                "Điện toán đám mây");

                                List<String> researchTopics = List.of(
                                                "Ứng dụng trí tuệ nhân tạo trong chẩn đoán hình ảnh y tế",
                                                "Phát triển hệ thống học tập cá nhân hóa dựa trên dữ liệu lớn",
                                                "Tác động của biến đổi khí hậu đến đa dạng sinh học tại Việt Nam",
                                                "Tối ưu hóa tiêu thụ năng lượng trong mạng cảm biến không dây",
                                                "Nghiên cứu giải pháp an toàn thông tin cho hệ thống chính phủ điện tử",
                                                "Phân tích hành vi người tiêu dùng trên nền tảng thương mại điện tử",
                                                "Mô phỏng giao thông đô thị thông minh bằng công nghệ IoT",
                                                "Ứng dụng học sâu trong phát hiện gian lận tài chính",
                                                "Đánh giá tác động của mạng xã hội đến sức khỏe tâm thần của giới trẻ",
                                                "Nghiên cứu phát triển vật liệu xanh trong xây dựng thân thiện môi trường");

                                for (int i = 1; i <= 200; i++) {
                                        String email;
                                        Role role;
                                        if (i <= 100) {
                                                email = "lecturer" + i + "@gmail.com";
                                                role = Role.LECTURER;
                                        } else if (i <= 110) {
                                                email = "school" + (i - 100) + "@gmail.com";
                                                role = Role.SCHOOL;
                                        } else if (i <= 120) {
                                                email = "organization" + (i - 110) + "@gmail.com";
                                                role = Role.ORGANIZATION;
                                        } else {
                                                email = "user" + (i - 120) + "@gmail.com";
                                                role = Role.USER;
                                        }
                                        RegisterRequest request = RegisterRequest.builder()
                                                        .email(email)
                                                        .password("SGL@2025")
                                                        .role(role)
                                                        .build();
                                        authenticationService.register(request);
                                }

                                List<Lecturer> lecturers = new ArrayList<>();
                                for (int i = 1; i <= 100; i++) {
                                        boolean isApproved = true;
                                        if (i > 80) {
                                                isApproved = false;
                                        }
                                        String citizenId = String.format("%011d",
                                                        faker.number().numberBetween(10000000000L, 99999999999L));
                                        String phoneNumber = "09" + faker.number().numberBetween(100000000, 999999999);
                                        String email = "lecturer" + i + "@gmail.com";
                                        User user = userRepository.findByEmail(email)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "User not found: " + email));
                                        LocalDate dob = faker.date()
                                                        .birthday(25, 60)
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();

                                        Lecturer lecturer = Lecturer.builder()
                                                        .user(user)
                                                        .citizenId(citizenId)
                                                        .fullName(
                                                                        faker.options().option(
                                                                                        lastNames.get(faker.random()
                                                                                                        .nextInt(lastNames
                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        middleNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(middleNames
                                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        firstNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(firstNames
                                                                                                                                        .size()))))
                                                        .phoneNumber(phoneNumber)
                                                        .dateOfBirth(dob)
                                                        .gender(faker.bool().bool())
                                                        .address(
                                                                        addresses.get(faker.random()
                                                                                        .nextInt(addresses.size())))
                                                        .bio(
                                                                        bios.get(faker.random().nextInt(bios.size())))
                                                        .specialization(
                                                                        specializationWithRankList.get(faker.random()
                                                                                        .nextInt(specializationWithRankList
                                                                                                        .size())))
                                                        .academicRank(AcademicRank.values()[faker.random()
                                                                        .nextInt(AcademicRank.values().length)])
                                                        .experienceYears(faker.number().numberBetween(1, 40))
                                                        .jobField(
                                                                        jobFieldWithExperienceList.get(faker.random()
                                                                                        .nextInt(jobFieldWithExperienceList
                                                                                                        .size())))
                                                        .hidden(false)
                                                        .status(PendingStatus.values()[isApproved ? 1 : 0])
                                                        // .status(PendingStatus.APPROVED)
                                                        .avatarUrl("https://picsum.photos/200")
                                                        .build();
                                        lecturers.add(lecturer);
                                }
                                // Save lecturers one by one to avoid detached entity issues
                                for (Lecturer lecturer : lecturers) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(lecturer.getUser().getId())
                                                .orElseThrow(() -> new RuntimeException("User not found for lecturer"));
                                        lecturer.setUser(managedUser);
                                        lecturerRepository.saveAndFlush(lecturer);
                                }

                                List<EducationInstitution> institutions = new ArrayList<>();
                                for (int i = 101; i <= 110; i++) {
                                        User user = userRepository.findByEmail("school" + (i - 100) + "@gmail.com")
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "User not found: institution"));
                                        if (user == null) {
                                                throw new RuntimeException(
                                                                "User not found for institution: " + (i - 100));
                                        }
                                        EducationInstitution inst = EducationInstitution.builder()
                                                        .user(user)
                                                        .businessRegistrationNumber(faker.number().digits(10))
                                                        .institutionName(
                                                                        trainingCenters.get(faker.random().nextInt(
                                                                                        trainingCenters.size())))
                                                        .institutionType(faker.bool().bool()
                                                                        ? EducationInstitutionType.UNIVERSITY
                                                                        : EducationInstitutionType.TRAINING_CENTER)
                                                        .phoneNumber(faker.phoneNumber().cellPhone())
                                                        .website(faker.internet().url())
                                                        .address(
                                                                        addresses.get(faker.random()
                                                                                        .nextInt(addresses.size())))
                                                        .representativeName(
                                                                        faker.options().option(
                                                                                        lastNames.get(faker.random()
                                                                                                        .nextInt(lastNames
                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        middleNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(middleNames
                                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        firstNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(firstNames
                                                                                                                                        .size()))))
                                                        .position("Giám đốc")
                                                        .description(faker.lorem().sentence())
                                                        .logoUrl("https://picsum.photos/200")
                                                        .establishedYear(faker.number().numberBetween(1990, 2022))
                                                        .adminNote("Dữ liệu mẫu")
                                                        .status(PendingStatus.values()[faker.random().nextInt(2)])
                                                        .build();
                                        institutions.add(inst);
                                }
                                // Save institutions one by one to avoid detached entity issues
                                for (EducationInstitution institution : institutions) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(institution.getUser().getId()).orElse(null);
                                        if (managedUser != null) {
                                                institution.setUser(managedUser);
                                        }
                                        educationInstitutionRepository.saveAndFlush(institution);
                                }
                                List<PartnerOrganization> organizations = new ArrayList<>();
                                for (int i = 111; i <= 120; i++) {
                                        User user = userRepository
                                                        .findByEmail("organization" + (i - 110) + "@gmail.com")
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "User not found: partner"));
                                        if (user == null) {
                                                throw new RuntimeException("User not found for partner organization: "
                                                                + (i - 110));
                                        }
                                        PartnerOrganization partner = PartnerOrganization.builder()
                                                        .businessRegistrationNumber(faker.number().digits(10))
                                                        .user(user)
                                                        .organizationName(
                                                                        companies.get(faker.random()
                                                                                        .nextInt(companies.size())))
                                                        .industry(
                                                                        faker.options().option(
                                                                                        "Công nghệ thông tin",
                                                                                        "Tài chính - Ngân hàng",
                                                                                        "Giáo dục và Đào tạo",
                                                                                        "Y tế và Chăm sóc sức khỏe",
                                                                                        "Sản xuất và Chế biến",
                                                                                        "Dịch vụ và Du lịch",
                                                                                        "Bất động sản", "Nông nghiệp",
                                                                                        "Logistics và Vận tải",
                                                                                        "Xây dựng"))
                                                        .phoneNumber(faker.phoneNumber().cellPhone())
                                                        .website(faker.internet().url())
                                                        .address(
                                                                        addresses.get(faker.random()
                                                                                        .nextInt(addresses.size())))
                                                        .representativeName(
                                                                        faker.options().option(
                                                                                        lastNames.get(faker.random()
                                                                                                        .nextInt(lastNames
                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        middleNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(middleNames
                                                                                                                                        .size()))
                                                                                                        + " " +
                                                                                                        firstNames.get(faker
                                                                                                                        .random()
                                                                                                                        .nextInt(firstNames
                                                                                                                                        .size()))))
                                                        .position("Giám đốc")
                                                        .description(faker.lorem().paragraph())
                                                        .logoUrl("https://picsum.photos/200")
                                                        .establishedYear(faker.number().numberBetween(1990, 2024))
                                                        .adminNote("Đây là dữ liệu mẫu cho tổ chức đối tác")
                                                        .status(PendingStatus.values()[faker.random().nextInt(2)])
                                                        .build();

                                        organizations.add(partner);
                                }
                                // Save organizations one by one to avoid detached entity issues
                                for (PartnerOrganization organization : organizations) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(organization.getUser().getId()).orElse(null);
                                        if (managedUser != null) {
                                                organization.setUser(managedUser);
                                        }
                                        partnerOrganizationRepository.saveAndFlush(organization);
                                }

                                List<Degree> degrees = new ArrayList<>();
                                for (Lecturer lecturer : lecturers) {
                                        int degreeCount = faker.number().numberBetween(1, 4); // mỗi lecturer có từ 1
                                                                                              // đến 3 degree
                                        for (int j = 0; j < degreeCount; j++) {
                                                int startYear = faker.number().numberBetween(1995, 2018);
                                                int graduationYear = startYear + faker.number().numberBetween(3, 5);
                                                Degree degree = Degree.builder()
                                                                .referenceId("DEG" + faker.number().digits(6))
                                                                .name(
                                                                                degreesSample.get(faker.random()
                                                                                                .nextInt(degreesSample
                                                                                                                .size())))
                                                                .major(
                                                                                specializationWithRankList.get(faker
                                                                                                .random()
                                                                                                .nextInt(specializationWithRankList
                                                                                                                .size())))
                                                                .institution(
                                                                                trainingCenters.get(faker.random()
                                                                                                .nextInt(trainingCenters
                                                                                                                .size())))
                                                                .startYear(startYear)
                                                                .graduationYear(graduationYear)
                                                                .level(
                                                                                faker.options().option(
                                                                                                "Cử nhân", "Thạc sĩ",
                                                                                                "Tiến sĩ", "Cao đẳng",
                                                                                                "Trung cấp", "Kỹ sư"))
                                                                .url(degreeUrl)
                                                                .description(faker.lorem().sentence())
                                                                .adminNote("Tự động sinh")
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .lecturer(lecturer)
                                                                .build();
                                                degrees.add(degree);
                                        }
                                }
                                degreeRepository.saveAll(degrees);
                                List<Certification> certifications = new ArrayList<>();

                                for (Lecturer lecturer : lecturers) {
                                        int certCount = faker.number().numberBetween(1, 4); // mỗi lecturer có từ 1 đến
                                                                                            // 3 chứng chỉ
                                        for (int j = 0; j < certCount; j++) {
                                                LocalDate issueDate = faker.date()
                                                                .past(2000, TimeUnit.of(ChronoUnit.DAYS)).toInstant()
                                                                .atZone(ZoneId.systemDefault()).toLocalDate();
                                                LocalDate expiryDate = issueDate
                                                                .plusYears(faker.number().numberBetween(1, 5));

                                                Certification cert = Certification.builder()
                                                                .referenceId("CERT" + faker.number().digits(6))
                                                                .name(
                                                                                certificationsSample.get(faker.random()
                                                                                                .nextInt(certificationsSample
                                                                                                                .size())))
                                                                .issuedBy(
                                                                                trainingCenters.get(faker.random()
                                                                                                .nextInt(trainingCenters
                                                                                                                .size())))
                                                                .issueDate(issueDate)
                                                                .expiryDate(faker.bool().bool() ? expiryDate : null) // có
                                                                                                                     // thể
                                                                                                                     // null
                                                                                                                     // nếu
                                                                                                                     // không
                                                                                                                     // có
                                                                                                                     // hạn
                                                                .certificateUrl(
                                                                                urlCert.get(faker.random().nextInt(
                                                                                                urlCert.size())))
                                                                .level(
                                                                                faker.options().option(
                                                                                                "Cơ bản", "Trung cấp",
                                                                                                "Nâng cao",
                                                                                                "Chuyên gia"))
                                                                .description(faker.lorem().sentence())
                                                                .adminNote("Tự động sinh")
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .lecturer(lecturer)
                                                                .build();
                                                certifications.add(cert);
                                        }
                                }
                                certificationRepository.saveAll(certifications);

                                List<AttendedTrainingCourse> attendedCourses = new ArrayList<>();

                                for (Lecturer lecturer : lecturers) {
                                        int numberOfCourses = faker.number().numberBetween(1, 4); // mỗi giảng viên tham
                                                                                                  // gia từ 1 đến 3 khóa
                                        for (int j = 0; j < numberOfCourses; j++) {
                                                LocalDate startDate = faker.date()
                                                                .past(1500, java.util.concurrent.TimeUnit.DAYS)
                                                                .toInstant()
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate();
                                                LocalDate endDate = startDate
                                                                .plusDays(faker.number().numberBetween(2, 10));

                                                AttendedTrainingCourse course = AttendedTrainingCourse.builder()
                                                                .title(
                                                                                courseTitles.get(faker.random().nextInt(
                                                                                                courseTitles.size())))
                                                                .topic(
                                                                                courseTopics.get(faker.random().nextInt(
                                                                                                courseTopics.size())))
                                                                .organizer(
                                                                                companies.get(faker.random().nextInt(
                                                                                                companies.size())))
                                                                .courseType(CourseType.values()[faker.random()
                                                                                .nextInt(CourseType.values().length)])
                                                                .scale(Scale.values()[faker.random()
                                                                                .nextInt(Scale.values().length)])
                                                                .startDate(startDate)
                                                                .endDate(endDate)
                                                                .numberOfHour(faker.number().numberBetween(8, 80))
                                                                .location(
                                                                                addresses.get(faker.random().nextInt(
                                                                                                addresses.size())))
                                                                .description(faker.lorem().sentence())
                                                                .courseUrl("https://www.google.com/"
                                                                                + UUID.randomUUID())
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .adminNote("Đây là dữ liệu mẫu cho khóa học đã tham gia")
                                                                .lecturer(lecturer)
                                                                .build();

                                                attendedCourses.add(course);
                                        }
                                }

                                attendedTrainingCourseRepository.saveAll(attendedCourses);

                                List<OwnedTrainingCourse> ownedCourses = new ArrayList<>();

                                for (Lecturer lecturer : lecturers) {
                                        int numberOfCourses = faker.number().numberBetween(1, 4); // mỗi giảng viên sở
                                                                                                  // hữu từ 1 đến 3 khóa
                                        for (int j = 0; j < numberOfCourses; j++) {
                                                LocalDate startDate = faker.date()
                                                                .past(2000, java.util.concurrent.TimeUnit.DAYS)
                                                                .toInstant()
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate();
                                                LocalDate endDate = startDate
                                                                .plusDays(faker.number().numberBetween(2, 15));
                                                boolean isOnline = faker.bool().bool();

                                                OwnedTrainingCourse course = OwnedTrainingCourse.builder()
                                                                .title(
                                                                                courseTitles.get(faker.random().nextInt(
                                                                                                courseTitles.size())))
                                                                .topic(
                                                                                courseTopics.get(faker.random().nextInt(
                                                                                                courseTopics.size())))
                                                                .courseType(CourseType.values()[faker.random()
                                                                                .nextInt(CourseType.values().length)])
                                                                .scale(Scale.values()[faker.random()
                                                                                .nextInt(Scale.values().length)])
                                                                .thumbnailUrl("https://picsum.photos/200/300?random="
                                                                                + j)
                                                                .contentUrl("https://www.google.com/"
                                                                                + UUID.randomUUID())
                                                                .level(faker.options().option(
                                                                                "Cơ bản", "Trung cấp", "Nâng cao",
                                                                                "Chuyên gia"))
                                                                .requirements("Không yêu cầu")
                                                                .language(truncate(faker.options().option("English",
                                                                                "Vietnamese", "French", "Japanese"),
                                                                                255))
                                                                .isOnline(isOnline)
                                                                .address(
                                                                                isOnline ? "https://app.zoom.us/wc"
                                                                                                : addresses.get(faker
                                                                                                                .random()
                                                                                                                .nextInt(addresses
                                                                                                                                .size())))
                                                                .price(faker.number().randomDouble(2, 0, 5000000))
                                                                .startDate(startDate)
                                                                .endDate(endDate)
                                                                .description(faker.lorem().sentence())
                                                                .courseUrl("https://www.google.com/"
                                                                                + UUID.randomUUID())
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .adminNote("Đây là dữ liệu mẫu cho khóa học sở hữu")
                                                                .lecturer(lecturer)
                                                                .course(null) // có thể gán course nếu muốn liên kết đến
                                                                              // entity Course
                                                                .build();

                                                ownedCourses.add(course);
                                        }
                                }

                                ownedTrainingCourseRepository.saveAll(ownedCourses);
                                List<ResearchProject> projects = new ArrayList<>();

                                for (Lecturer lecturer : lecturers) {
                                        int numberOfProjects = faker.number().numberBetween(1, 4); // Mỗi giảng viên có
                                                                                                   // từ 1 đến 3 đề tài
                                                                                                   // nghiên cứu
                                        for (int j = 0; j < numberOfProjects; j++) {
                                                LocalDate startDate = faker.date()
                                                                .past(2500, java.util.concurrent.TimeUnit.DAYS)
                                                                .toInstant()
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate();
                                                LocalDate endDate = startDate
                                                                .plusDays(faker.number().numberBetween(90, 720));

                                                ResearchProject project = ResearchProject.builder()
                                                                .title(
                                                                                researchTopics.get(faker.random()
                                                                                                .nextInt(researchTopics
                                                                                                                .size())))
                                                                .researchArea(
                                                                                faker.options().option(
                                                                                                "Khoa học máy tính",
                                                                                                "Khoa học dữ liệu",
                                                                                                "Trí tuệ nhân tạo",
                                                                                                "Công nghệ sinh học",
                                                                                                "Vật lý học", "Hóa học",
                                                                                                "Khoa học môi trường",
                                                                                                "Khoa học xã hội",
                                                                                                "Kinh tế học", "Y học"))
                                                                .scale(Scale.values()[faker.random()
                                                                                .nextInt(Scale.values().length)])
                                                                .startDate(startDate)
                                                                .endDate(endDate)
                                                                .foundingAmount(faker.number().randomDouble(2, 200000,
                                                                                5000000))
                                                                .foundingSource(
                                                                                faker.options().option(
                                                                                                "Ngân sách nhà nước",
                                                                                                "Quỹ nghiên cứu",
                                                                                                "Hợp tác doanh nghiệp",
                                                                                                "Tài trợ cá nhân",
                                                                                                "Tài trợ quốc tế",
                                                                                                "Tài trợ từ tổ chức phi lợi nhuận"))
                                                                .projectType(ProjectType.values()[faker.random()
                                                                                .nextInt(ProjectType.values().length)])
                                                                .roleInProject(faker.options().option("Chủ nhiệm",
                                                                                "Thành viên", "Tư vấn",
                                                                                "Đồng nghiên cứu"))
                                                                .publishedUrl("https://www.google.com/"
                                                                                + UUID.randomUUID())
                                                                .courseStatus(faker.options().option("Đang thực hiện",
                                                                                "Hoàn thành", "Tạm dừng"))
                                                                .description(faker.lorem().sentence())
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .adminNote("Đây là dữ liệu mẫu cho đề tài nghiên cứu")
                                                                .lecturer(lecturer)
                                                                .build();

                                                projects.add(project);
                                        }
                                }

                                researchProjectRepository.saveAll(projects);

                                List<Course> courses = new ArrayList<>();

                                for (int i = 1; i <= 10; i++) {
                                        String title = courseTitles.get(faker.random().nextInt(courseTitles.size()));
                                        String topic = courseTopics.get(faker.random().nextInt(courseTopics.size()));
                                        CourseType courseType = CourseType.values()[faker.random()
                                                        .nextInt(CourseType.values().length)];
                                        String description = truncate(faker.lorem().paragraph(), 255);
                                        String thumbnailUrl = "https://picsum.photos/200/300?random=" + i;
                                        String contentUrl = "https://www.google.com/";
                                        String level = truncate(faker.options().option(
                                                        "Cơ bản", "Trung cấp", "Nâng cao", "Chuyên gia"), 255);
                                        String requirements = truncate("Không yêu cầu", 255);
                                        String language = truncate(faker.options().option("English", "Vietnamese",
                                                        "French", "Japanese"), 255);

                                        Boolean isOnline = faker.bool().bool();
                                        String address = isOnline ? "Zoom"
                                                        : addresses.get(faker.random().nextInt(researchTopics.size()));

                                        LocalDate startDate = LocalDate.now()
                                                        .plusDays(faker.number().numberBetween(0, 30));
                                        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(5, 30));
                                        Double price = faker.number().randomDouble(2, 500, 5000);
                                        Boolean isPublished = faker.bool().bool();

                                        Course course = Course.builder()
                                                        .title(title)
                                                        .topic(topic)
                                                        .courseType(courseType)
                                                        .description(description)
                                                        .thumbnailUrl(thumbnailUrl)
                                                        .contentUrl(contentUrl)
                                                        .level(level)
                                                        .requirements(requirements)
                                                        .language(language)
                                                        .isOnline(isOnline)
                                                        .address(address)
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .price(price)
                                                        .isPublished(isPublished)
                                                        .build();

                                        courses.add(course);
                                }

                                courseRepository.saveAll(courses);

                                List<CourseLecturer> courseLecturers = new ArrayList<>();

                                for (Course course : courses) {
                                        int numberOfLecturers = faker.number().numberBetween(1, 4); // 1-3 lecturers
                                        Set<Lecturer> selectedLecturers = new HashSet<>();

                                        while (selectedLecturers.size() < numberOfLecturers) {
                                                Lecturer randomLecturer = lecturers
                                                                .get(faker.number().numberBetween(0, lecturers.size()));
                                                selectedLecturers.add(randomLecturer);
                                        }

                                        List<Lecturer> lecturerList = new ArrayList<>(selectedLecturers);

                                        int authorIndex = faker.number().numberBetween(0, lecturerList.size());
                                        Lecturer author = lecturerList.get(authorIndex);

                                        for (Lecturer lecturer : lecturerList) {
                                                CourseRole role;

                                                if (lecturer.equals(author)) {
                                                        role = CourseRole.AUTHOR;
                                                } else {
                                                        role = faker.options().option(CourseRole.ASSIGNED,
                                                                        CourseRole.ASSISTANT);
                                                }

                                                CourseLecturer courseLecturer = CourseLecturer.builder()
                                                                .course(course)
                                                                .lecturer(lecturer)
                                                                .role(role)
                                                                .build();

                                                courseLecturers.add(courseLecturer);
                                        }
                                }

                                courseLecturerRepository.saveAll(courseLecturers);

                                // Thêm vào cuối hàm init, sau khi đã tạo
                                // courseLecturerRepository.saveAll(courseLecturers);

                                // Tạo LecturerUpdate
                                List<LecturerUpdate> lecturerUpdates = new ArrayList<>();
                                for (int i = 0; i < 30; i++) { // Tạo 30 bản cập nhật cho lecturer
                                        Lecturer randomLecturer = lecturers.get(i);

                                        LocalDate dob = faker.date()
                                                        .birthday(25, 60)
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();

                                        LecturerUpdate lecturerUpdate = LecturerUpdate.builder()
                                                        .lecturer(randomLecturer)
                                                        .citizenId(faker.number().digits(11))
                                                        .phoneNumber("09" + faker.number().numberBetween(100000000,
                                                                        999999999))
                                                        .fullName(
                                                                        lastNames.get(faker.random()
                                                                                        .nextInt(lastNames.size()))
                                                                                        + " " +
                                                                                        middleNames.get(faker.random()
                                                                                                        .nextInt(middleNames
                                                                                                                        .size()))
                                                                                        + " " +
                                                                                        firstNames.get(faker.random()
                                                                                                        .nextInt(firstNames
                                                                                                                        .size())))
                                                        .dateOfBirth(dob)
                                                        .gender(faker.bool().bool())
                                                        .bio(bios.get(faker.random().nextInt(bios.size())))
                                                        .address(addresses
                                                                        .get(faker.random().nextInt(addresses.size())))
                                                        .avatarUrl("https://picsum.photos/200?random=" + i)
                                                        .academicRank(AcademicRank.values()[faker.random()
                                                                        .nextInt(AcademicRank.values().length)])
                                                        .specialization(specializationWithRankList.get(faker.random()
                                                                        .nextInt(specializationWithRankList.size())))
                                                        .experienceYears(faker.number().numberBetween(1, 40))
                                                        .jobField(jobFieldWithExperienceList.get(faker.random()
                                                                        .nextInt(jobFieldWithExperienceList.size())))
                                                        .adminNote("Yêu cầu cập nhật thông tin cá nhân")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        lecturerUpdates.add(lecturerUpdate);
                                }
                                lecturerUpdateRepository.saveAll(lecturerUpdates);

                                // Tạo DegreeUpdate
                                List<DegreeUpdate> degreeUpdates = new ArrayList<>();
                                for (int i = 0; i < 20; i++) { // Tạo 50 bản cập nhật cho degree
                                        Degree randomDegree = degrees.get(i);

                                        int startYear = faker.number().numberBetween(1995, 2018);
                                        int graduationYear = startYear + faker.number().numberBetween(3, 5);

                                        DegreeUpdate degreeUpdate = DegreeUpdate.builder()
                                                        .degree(randomDegree)
                                                        .referenceId("DEG_UPD" + faker.number().digits(6))
                                                        .name(degreesSample.get(
                                                                        faker.random().nextInt(degreesSample.size())))
                                                        .major(specializationWithRankList.get(faker.random()
                                                                        .nextInt(specializationWithRankList.size())))
                                                        .institution(trainingCenters.get(
                                                                        faker.random().nextInt(trainingCenters.size())))
                                                        .startYear(startYear)
                                                        .graduationYear(graduationYear)
                                                        .level(faker.options().option("Cử nhân", "Thạc sĩ", "Tiến sĩ",
                                                                        "Cao đẳng", "Trung cấp", "Kỹ sư"))
                                                        .url(degreeUrl)
                                                        .description(faker.lorem().sentence())
                                                        .adminNote("Yêu cầu cập nhật thông tin bằng cấp")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        degreeUpdates.add(degreeUpdate);
                                }
                                degreeUpdateRepository.saveAll(degreeUpdates);

                                // Tạo CertificationUpdate
                                List<CertificationUpdate> certificationUpdates = new ArrayList<>();
                                for (int i = 0; i < 20; i++) { // Tạo 20 bản cập nhật cho certification
                                        Certification randomCert = certifications.get(i);

                                        LocalDate issueDate = faker.date().past(2000, TimeUnit.of(ChronoUnit.DAYS))
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault()).toLocalDate();
                                        LocalDate expiryDate = issueDate.plusYears(faker.number().numberBetween(1, 5));

                                        CertificationUpdate certUpdate = CertificationUpdate.builder()
                                                        .certification(randomCert)
                                                        .referenceId("CERT_UPD" + faker.number().digits(6))
                                                        .name(certificationsSample.get(faker.random()
                                                                        .nextInt(certificationsSample.size())))
                                                        .issuedBy(trainingCenters.get(
                                                                        faker.random().nextInt(trainingCenters.size())))
                                                        .issueDate(issueDate)
                                                        .expiryDate(faker.bool().bool() ? expiryDate : null)
                                                        .certificateUrl(urlCert
                                                                        .get(faker.random().nextInt(urlCert.size())))
                                                        .level(faker.options().option("Cơ bản", "Trung cấp", "Nâng cao",
                                                                        "Chuyên gia"))
                                                        .description(faker.lorem().sentence())
                                                        .adminNote("Yêu cầu cập nhật thông tin chứng chỉ")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        certificationUpdates.add(certUpdate);
                                }
                                certificationUpdateRepository.saveAll(certificationUpdates);
                                // Thêm vào cuối hàm init, sau khi đã tạo
                                // certificationUpdateRepository.saveAll(certificationUpdates);

                                // Tạo OwnedTrainingCourseUpdate
                                List<OwnedTrainingCourseUpdate> ownedCourseUpdates = new ArrayList<>();
                                for (int i = 0; i < 30; i++) { // Tạo 35 bản cập nhật cho owned training course
                                        OwnedTrainingCourse randomOwnedCourse = ownedCourses.get(i);

                                        LocalDate startDate = faker.date()
                                                        .past(2000, java.util.concurrent.TimeUnit.DAYS)
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();
                                        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(2, 15));
                                        boolean isOnline = faker.bool().bool();

                                        OwnedTrainingCourseUpdate courseUpdate = OwnedTrainingCourseUpdate.builder()
                                                        .ownedTrainingCourse(randomOwnedCourse)
                                                        .title(courseTitles.get(
                                                                        faker.random().nextInt(courseTitles.size())))
                                                        .topic(courseTopics.get(
                                                                        faker.random().nextInt(courseTopics.size())))
                                                        .courseType(CourseType.values()[faker.random()
                                                                        .nextInt(CourseType.values().length)])
                                                        .scale(Scale.values()[faker.random()
                                                                        .nextInt(Scale.values().length)])
                                                        .thumbnailUrl("https://picsum.photos/200/300?random=" + i)
                                                        .contentUrl("https://www.google.com/" + UUID.randomUUID())
                                                        .level(faker.options().option("Cơ bản", "Trung cấp", "Nâng cao",
                                                                        "Chuyên gia"))
                                                        .requirements("Cập nhật yêu cầu khóa học")
                                                        .language(faker.options().option("English", "Vietnamese",
                                                                        "French", "Japanese"))
                                                        .isOnline(isOnline)
                                                        .address(isOnline ? "https://app.zoom.us/wc"
                                                                        : addresses.get(faker.random()
                                                                                        .nextInt(addresses.size())))
                                                        .price(faker.number().randomDouble(2, 0, 5000000))
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .description(faker.lorem().sentence())
                                                        .courseUrl("https://www.google.com/" + UUID.randomUUID())
                                                        .adminNote("Yêu cầu cập nhật thông tin khóa học sở hữu")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        ownedCourseUpdates.add(courseUpdate);
                                }
                                ownedTrainingCourseUpdateRepository.saveAll(ownedCourseUpdates);

                                // Tạo AttendedTrainingCourseUpdate
                                List<AttendedTrainingCourseUpdate> attendedCourseUpdates = new ArrayList<>();
                                for (int i = 0; i < 30; i++) { // Tạo 150 bản cập nhật cho attended training course
                                        AttendedTrainingCourse randomAttendedCourse = attendedCourses.get(i);

                                        LocalDate startDate = faker.date()
                                                        .past(1500, java.util.concurrent.TimeUnit.DAYS)
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();
                                        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(2, 10));

                                        AttendedTrainingCourseUpdate courseUpdate = AttendedTrainingCourseUpdate
                                                        .builder()
                                                        .attendedTrainingCourse(randomAttendedCourse)
                                                        .title(courseTitles.get(
                                                                        faker.random().nextInt(courseTitles.size())))
                                                        .topic(courseTopics.get(
                                                                        faker.random().nextInt(courseTopics.size())))
                                                        .organizer(companies
                                                                        .get(faker.random().nextInt(companies.size())))
                                                        .courseType(CourseType.values()[faker.random()
                                                                        .nextInt(CourseType.values().length)])
                                                        .scale(Scale.values()[faker.random()
                                                                        .nextInt(Scale.values().length)])
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .numberOfHour(faker.number().numberBetween(8, 80))
                                                        .location(addresses
                                                                        .get(faker.random().nextInt(addresses.size())))
                                                        .description(faker.lorem().sentence())
                                                        .courseUrl("https://www.google.com/" + UUID.randomUUID())
                                                        .adminNote("Yêu cầu cập nhật thông tin khóa học đã tham gia")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        attendedCourseUpdates.add(courseUpdate);
                                }
                                attendedTrainingCourseUpdateRepository.saveAll(attendedCourseUpdates);

                                // Tạo ResearchProjectUpdate
                                List<ResearchProjectUpdate> researchProjectUpdates = new ArrayList<>();
                                for (int i = 0; i < 30; i++) { // Tạo 200 bản cập nhật cho research project
                                        ResearchProject randomProject = projects.get(i);

                                        LocalDate startDate = faker.date()
                                                        .past(2500, java.util.concurrent.TimeUnit.DAYS)
                                                        .toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();
                                        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(90, 720));

                                        ResearchProjectUpdate projectUpdate = ResearchProjectUpdate.builder()
                                                        .researchProject(randomProject)
                                                        .title(researchTopics.get(
                                                                        faker.random().nextInt(researchTopics.size())))
                                                        .researchArea(faker.options().option(
                                                                        "Khoa học máy tính", "Khoa học dữ liệu",
                                                                        "Trí tuệ nhân tạo",
                                                                        "Công nghệ sinh học", "Vật lý học", "Hóa học",
                                                                        "Khoa học môi trường",
                                                                        "Khoa học xã hội", "Kinh tế học", "Y học"))
                                                        .scale(Scale.values()[faker.random()
                                                                        .nextInt(Scale.values().length)])
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .foundingAmount(faker.number().randomDouble(2, 200000, 5000000))
                                                        .foundingSource(faker.options().option(
                                                                        "Ngân sách nhà nước", "Quỹ nghiên cứu",
                                                                        "Hợp tác doanh nghiệp",
                                                                        "Tài trợ cá nhân", "Tài trợ quốc tế",
                                                                        "Tài trợ từ tổ chức phi lợi nhuận"))
                                                        .projectType(ProjectType.values()[faker.random()
                                                                        .nextInt(ProjectType.values().length)])
                                                        .roleInProject(faker.options().option("Chủ nhiệm", "Thành viên",
                                                                        "Tư vấn", "Đồng nghiên cứu"))
                                                        .publishedUrl("https://www.google.com/" + UUID.randomUUID())
                                                        .courseStatus(faker.options().option("Đang thực hiện",
                                                                        "Hoàn thành", "Tạm dừng"))
                                                        .description(faker.lorem().sentence())
                                                        .adminNote("Yêu cầu cập nhật thông tin đề tài nghiên cứu")
                                                        .status(PendingStatus.PENDING)
                                                        .build();

                                        researchProjectUpdates.add(projectUpdate);
                                }
                                researchProjectUpdateRepository.saveAll(researchProjectUpdates);

                                // Create EducationInstitutionUpdate sample data
                                List<EducationInstitutionUpdate> institutionUpdates = new ArrayList<>();
                                for (int i = 0; i < 7; i++) { // Create 7 updates for education institutions
                                        EducationInstitution inst = institutions.get(i);
                                        EducationInstitutionUpdate update = EducationInstitutionUpdate.builder()
                                                        .educationInstitution(inst)
                                                        .businessRegistrationNumber("BRN" + faker.number().digits(6))
                                                        .institutionName(trainingCenters.get(
                                                                        faker.random().nextInt(trainingCenters.size())))
                                                        .institutionType(faker.bool().bool()
                                                                        ? EducationInstitutionType.UNIVERSITY
                                                                        : EducationInstitutionType.TRAINING_CENTER)
                                                        .phoneNumber(faker.phoneNumber().cellPhone())
                                                        .website(faker.internet().url())
                                                        .address(addresses
                                                                        .get(faker.random().nextInt(addresses.size())))
                                                        .representativeName(
                                                                        lastNames.get(faker.random()
                                                                                        .nextInt(lastNames.size()))
                                                                                        + " " +
                                                                                        middleNames.get(faker.random()
                                                                                                        .nextInt(middleNames
                                                                                                                        .size()))
                                                                                        + " " +
                                                                                        firstNames.get(faker.random()
                                                                                                        .nextInt(firstNames
                                                                                                                        .size())))
                                                        .position("Giám đốc")
                                                        .description(faker.lorem().sentence())
                                                        .logoUrl("https://picsum.photos/200?random=" + i)
                                                        .establishedYear(faker.number().numberBetween(1990, 2022))
                                                        .adminNote("Yêu cầu cập nhật thông tin trường/TT")
                                                        .status(PendingStatus.PENDING)
                                                        .build();
                                        institutionUpdates.add(update);
                                }
                                educationInstitutionUpdateRepository.saveAll(institutionUpdates);

                                // Create PartnerOrganizationUpdate sample data
                                List<PartnerOrganizationUpdate> orgUpdates = new ArrayList<>();
                                for (int i = 0; i < 8; i++) { // Create 8 updates for partner organizations
                                        PartnerOrganization org = organizations.get(i);
                                        PartnerOrganizationUpdate update = PartnerOrganizationUpdate.builder()
                                                        .partnerOrganization(org)
                                                        .organizationName(companies
                                                                        .get(faker.random().nextInt(companies.size())))
                                                        .businessRegistrationNumber("BRN" + faker.number().digits(6))
                                                        .industry(faker.options().option(
                                                                        "Công nghệ thông tin", "Tài chính - Ngân hàng",
                                                                        "Giáo dục và Đào tạo",
                                                                        "Y tế và Chăm sóc sức khỏe",
                                                                        "Sản xuất và Chế biến", "Dịch vụ và Du lịch",
                                                                        "Bất động sản", "Nông nghiệp",
                                                                        "Logistics và Vận tải", "Xây dựng"))
                                                        .phoneNumber(faker.phoneNumber().cellPhone())
                                                        .website(faker.internet().url())
                                                        .address(addresses
                                                                        .get(faker.random().nextInt(addresses.size())))
                                                        .representativeName(
                                                                        lastNames.get(faker.random()
                                                                                        .nextInt(lastNames.size()))
                                                                                        + " " +
                                                                                        middleNames.get(faker.random()
                                                                                                        .nextInt(middleNames
                                                                                                                        .size()))
                                                                                        + " " +
                                                                                        firstNames.get(faker.random()
                                                                                                        .nextInt(firstNames
                                                                                                                        .size())))
                                                        .position("Giám đốc")
                                                        .description(faker.lorem().paragraph())
                                                        .logoUrl("https://picsum.photos/200?random=" + i)
                                                        .establishedYear(faker.number().numberBetween(1990, 2024))
                                                        .adminNote("Yêu cầu cập nhật thông tin tổ chức đối tác")
                                                        .status(PendingStatus.PENDING)
                                                        .build();
                                        orgUpdates.add(update);
                                }
                                partnerOrganizationUpdateRepository.saveAll(orgUpdates);

                                var admin = RegisterRequest.builder()
                                                .email("admin@gmail.com")
                                                .password("SGL@2025")
                                                .role(Role.ADMIN)
                                                .build();
                                var sub_admin1 = RegisterRequest.builder()
                                                .email("sub_admin1@gmail.com")
                                                .password("SGL@2025")
                                                .role(Role.SUB_ADMIN)
                                                .build();

                                var sub_admin2 = RegisterRequest.builder()
                                                .email("sub_admin2@gmail.com")
                                                .password("SGL@2025")
                                                .role(Role.SUB_ADMIN)
                                                .build();
                                
                                // Register users and get their info
                                var adminResponse = authenticationService.register(admin);
                                var subAdmin1Response = authenticationService.register(sub_admin1);
                                var subAdmin2Response = authenticationService.register(sub_admin2);
                                
                                // Get admin user for permission assignment
                                User adminUser = userRepository.findByEmail("admin@gmail.com").orElseThrow();
                                User subAdmin1User = userRepository.findByEmail("sub_admin1@gmail.com").orElseThrow();
                                User subAdmin2User = userRepository.findByEmail("sub_admin2@gmail.com").orElseThrow();
                                
                                // Assign permissions to sub_admin1 (có quyền về organization và lecturer)
                                List<Permission> subAdmin1Permissions = List.of(
                                    Permission.ORGANIZATION_READ,
                                    Permission.ORGANIZATION_UPDATE,
                                    Permission.ORGANIZATION_APPROVE,
                                    Permission.LECTURER_READ,
                                    Permission.LECTURER_UPDATE,
                                    Permission.LECTURER_APPROVE
                                );
                                subAdminService.assignPermissionsToUser(subAdmin1User, subAdmin1Permissions, adminUser);
                                
                                // Assign permissions to sub_admin2 (có quyền về school và một số quyền lecturer)
                                List<Permission> subAdmin2Permissions = List.of(
                                    Permission.SCHOOL_READ,
                                    Permission.SCHOOL_UPDATE,
                                    Permission.SCHOOL_APPROVE,
                                    Permission.LECTURER_READ
                                );
                                subAdminService.assignPermissionsToUser(subAdmin2User, subAdmin2Permissions, adminUser);
                                
                                System.out.println("token admin: " + adminResponse.getAccessToken());
                                System.out.println("token sub_admin1: " + subAdmin1Response.getAccessToken());
                                System.out.println("token sub_admin2: " + subAdmin2Response.getAccessToken());

                        } catch (Exception e) {
                                throw new RuntimeException("không thể khởi tạo dữ liệu mẫu: " + e.getMessage(), e);
                        }
                };
        }
}
