package com.example.eduhubvn;

import static io.micrometer.common.util.StringUtils.truncate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.eduhubvn.dtos.auth.request.RegisterReq;
import com.example.eduhubvn.entities.AcademicRank;
import com.example.eduhubvn.entities.Application;
import com.example.eduhubvn.entities.ApplicationModule;
import com.example.eduhubvn.entities.ApplicationStatus;
import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import com.example.eduhubvn.entities.Contract;
import com.example.eduhubvn.entities.ContractStatus;
import com.example.eduhubvn.entities.CourseInfo;
import com.example.eduhubvn.entities.CourseLevel;
import com.example.eduhubvn.entities.CourseModule;
import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.EducationInstitutionType;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import com.example.eduhubvn.entities.Interview;
import com.example.eduhubvn.entities.InterviewMode;
import com.example.eduhubvn.entities.InterviewResult;
import com.example.eduhubvn.entities.InterviewStatus;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.LecturerUpdate;
import com.example.eduhubvn.entities.Notification;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.Permission;
import com.example.eduhubvn.entities.Project;
import com.example.eduhubvn.entities.ProjectCategory;
import com.example.eduhubvn.entities.ProjectStatus;
import com.example.eduhubvn.entities.ProjectType;
import com.example.eduhubvn.entities.ResearchProject;
import com.example.eduhubvn.entities.ResearchProjectUpdate;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.Scale;
import com.example.eduhubvn.entities.TrainingProgram;
import com.example.eduhubvn.entities.TrainingProgramLevel;
import com.example.eduhubvn.entities.TrainingProgramMode;
import com.example.eduhubvn.entities.TrainingProgramRequest;
import com.example.eduhubvn.entities.TrainingProgramStatus;
import com.example.eduhubvn.entities.TrainingProgramType;
import com.example.eduhubvn.entities.TrainingUnit;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.ApplicationModuleRepository;
import com.example.eduhubvn.repositories.ApplicationRepository;
import com.example.eduhubvn.repositories.AttendedCourseRepository;
import com.example.eduhubvn.repositories.AttendedCourseUpdateRepository;
import com.example.eduhubvn.repositories.CertificationRepository;
import com.example.eduhubvn.repositories.CertificationUpdateRepository;
import com.example.eduhubvn.repositories.ContractRepository;
import com.example.eduhubvn.repositories.CourseInfoRepository;
import com.example.eduhubvn.repositories.CourseModuleRepository;
import com.example.eduhubvn.repositories.DegreeRepository;
import com.example.eduhubvn.repositories.DegreeUpdateRepository;
import com.example.eduhubvn.repositories.InstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import com.example.eduhubvn.repositories.InterviewRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.repositories.LecturerUpdateRepository;
import com.example.eduhubvn.repositories.NotificationRepository;
import com.example.eduhubvn.repositories.OwnedCourseRepository;
import com.example.eduhubvn.repositories.OwnedCourseUpdateRepository;
import com.example.eduhubvn.repositories.PartnerRepository;
import com.example.eduhubvn.repositories.PartnerUpdateRepository;
import com.example.eduhubvn.repositories.ProjectRespository;
import com.example.eduhubvn.repositories.ResearchProjectRepository;
import com.example.eduhubvn.repositories.ResearchProjectUpdateRepository;
import com.example.eduhubvn.repositories.TrainingProgramRepository;
import com.example.eduhubvn.repositories.TrainingProgramRequestRepository;
import com.example.eduhubvn.repositories.TrainingUnitRepository;
import com.example.eduhubvn.repositories.UserRepository;
import com.example.eduhubvn.services.AuthenticationService;
import com.github.javafaker.Faker;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class EduHubVnApplication {

        public static void main(String[] args) {
                SpringApplication.run(EduHubVnApplication.class, args);
        }

        @PostConstruct
        public void started() {
                TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        }

        // @Bean
        public CommandLineRunner setup(UserRepository userRepository, AuthenticationService authenticationService) {
                return args -> {
                        if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                                return;
                        }
                        var admin = RegisterReq.builder()
                                        .email("admin@gmail.com")
                                        .password("SGL@2025")
                                        .role(Role.ADMIN)
                                        .build();
                        var adminResponse = authenticationService.register(admin);
                        System.out.println("token admin: " + adminResponse.getAccessToken());
                };
        }

        @Bean
        public CommandLineRunner init(UserRepository userRepository,
                        LecturerRepository lecturerRepository,
                        InstitutionRepository educationInstitutionRepository,
                        PartnerRepository partnerOrganizationRepository,
                        DegreeRepository degreeRepository,
                        CertificationRepository certificationRepository,
                        AttendedCourseRepository attendedTrainingCourseRepository,
                        OwnedCourseRepository ownedTrainingCourseRepository,
                        ResearchProjectRepository researchProjectRepository,

                        ProjectRespository projectRepository,
                        ApplicationRepository applicationRepository,
                        ApplicationModuleRepository applicationModuleRepository,
                        InterviewRepository interviewRepository,
                        ContractRepository contractRepository,
                        CourseInfoRepository courseInfoRepository,
                        CourseModuleRepository courseModuleRepository,

                        LecturerUpdateRepository lecturerUpdateRepository,
                        EducationInstitutionUpdateRepository educationInstitutionUpdateRepository,
                        PartnerUpdateRepository partnerOrganizationUpdateRepository,
                        DegreeUpdateRepository degreeUpdateRepository,
                        CertificationUpdateRepository certificationUpdateRepository,
                        AttendedCourseUpdateRepository attendedTrainingCourseUpdateRepository,
                        OwnedCourseUpdateRepository ownedTrainingCourseUpdateRepository,
                        ResearchProjectUpdateRepository researchProjectUpdateRepository,

                        TrainingProgramRequestRepository trainingProgramRequestRepository,
                        TrainingProgramRepository trainingProgramRepository,
                        TrainingUnitRepository trainingUnitRepository,

                        com.example.eduhubvn.repositories.NotificationRepository notificationRepository,

                        com.example.eduhubvn.services.IdGeneratorService idGeneratorService,

                        AuthenticationService authenticationService,
                        com.example.eduhubvn.services.SubAdminService subAdminService) {
                return args -> {
                        if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                                System.out.println("Data already initialized, skipping seeding.");
                                return;
                        }
                        Faker faker = new Faker();
                        try {

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
                                                "Hoa", "Hòa", "Hùng", "Hương", "Huy", "Khánh", "Kiên", "Lan", "Linh",
                                                "Loan",
                                                "Long", "Mai", "Minh", "Nam", "Nga", "Ngân", "Ngọc", "Nhung", "Oanh",
                                                "Phong",
                                                "Phúc", "Quân", "Quỳnh", "Sơn", "Tâm", "Thành", "Thảo", "Thanh",
                                                "Thắng",
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
                                                "http://demoportal.ccvi.com.vn:8880/uploads/LECTURER/1/Bangtk_PMP KPS Cert.pdf",
                                                "http://demoportal.ccvi.com.vn:8880/uploads/LECTURER/1/Bangtk_CCNT_5971752_certificate.pdf"));
                                String degreeUrl = "http://demoportal.ccvi.com.vn:8880/uploads/LECTURER/1/Bangtotnghiep_Bangtk.pdf";

                                // Danh sách avatar URLs cho giảng viên - hình ảnh thực tế
                                List<String> lecturerAvatarUrls = Arrays.asList(
                                                "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1599566150163-29194dcaad36?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1519244703995-f4e0f30006d5?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1531427186611-ecfd6d936c79?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=200&h=200&fit=crop&crop=face",
                                                "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&h=200&fit=crop&crop=face");

                                // Danh sách logo URLs cho các tổ chức - hình ảnh thực tế
                                List<String> organizationLogoUrls = Arrays.asList(
                                                "https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1599566150163-29194dcaad36?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1556761175-b413da4baf72?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200&h=200&fit=crop",
                                                "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=200&h=200&fit=crop");

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
                                        RegisterReq request = RegisterReq.builder()
                                                        .email(email)
                                                        .password("SGL@2025")
                                                        .role(role)
                                                        .build();
                                        authenticationService.register(request);
                                }
                                int stt = 1;
                                List<Lecturer> lecturers = new ArrayList<>();
                                for (int i = 1; i <= 100; i++) {
                                        boolean isApproved = true;

                                        if (i > 80) {
                                                isApproved = false;
                                        }
                                        String citizenId = String.format("%012d",
                                                        faker.number().numberBetween(100000000000L, 999999999999L));
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
                                        String lecturerId = isApproved ? String.format("GV%03d", stt++) : null;
                                        Lecturer lecturer = Lecturer.builder()
                                                        .user(user)
                                                        .lecturerId(lecturerId)
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
                                                        .avatarUrl(lecturerAvatarUrls.get(faker.random()
                                                                        .nextInt(lecturerAvatarUrls.size())))
                                                        .build();
                                        lecturers.add(lecturer);
                                }
                                // Save lecturers one by one to avoid detached entity issues
                                for (Lecturer lecturer : lecturers) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(lecturer.getUser().getId())
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "User not found for lecturer"));
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
                                                        .logoUrl(organizationLogoUrls.get(faker.random()
                                                                        .nextInt(organizationLogoUrls.size())))
                                                        .establishedYear(faker.number().numberBetween(1990, 2022))
                                                        .adminNote("Dữ liệu mẫu")
                                                        .status(PendingStatus.values()[faker.random().nextInt(2)])
                                                        .build();
                                        institutions.add(inst);
                                }
                                // Save institutions one by one to avoid detached entity issues
                                for (EducationInstitution institution : institutions) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(institution.getUser().getId())
                                                        .orElse(null);
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
                                                        .logoUrl(organizationLogoUrls.get(faker.random()
                                                                        .nextInt(organizationLogoUrls.size())))
                                                        .establishedYear(faker.number().numberBetween(1990, 2024))
                                                        .adminNote("Đây là dữ liệu mẫu cho tổ chức đối tác")
                                                        .status(getPartnerOrganizationStatus(i - 110)) // 80% APPROVED
                                                                                                       // cho các tổ
                                                                                                       // chức đầu tiên
                                                        .build();

                                        organizations.add(partner);
                                }
                                // Save organizations one by one to avoid detached entity issues
                                for (PartnerOrganization organization : organizations) {
                                        // Re-fetch the user to ensure it's managed
                                        User managedUser = userRepository.findById(organization.getUser().getId())
                                                        .orElse(null);
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
                                                                .courseUrl("https://www.google.com/")
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
                                                                .thumbnailUrl("https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=200&h=300&fit=crop")
                                                                .contentUrl("https://www.google.com/")
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
                                                                .courseUrl("https://www.google.com/")
                                                                .status(PendingStatus.values()[faker.random()
                                                                                .nextInt(2)])
                                                                .adminNote("Đây là dữ liệu mẫu cho khóa học sở hữu")
                                                                .lecturer(lecturer)
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
                                                                .publishedUrl("https://www.google.com/")
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
                                                        .citizenId(faker.number().digits(12))
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
                                                        .avatarUrl("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop&crop=face")
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
                                                        .thumbnailUrl("https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=200&h=300&fit=crop")
                                                        .contentUrl("https://www.google.com/")
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
                                                        .courseUrl("https://www.google.com/")
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
                                                        .courseUrl("https://www.google.com/")
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
                                                        .publishedUrl("https://www.google.com/")
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
                                                        .logoUrl("https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=200&h=200&fit=crop")
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
                                                        .logoUrl("https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=200&h=200&fit=crop")
                                                        .establishedYear(faker.number().numberBetween(1990, 2024))
                                                        .adminNote("Yêu cầu cập nhật thông tin tổ chức đối tác")
                                                        .status(PendingStatus.PENDING)
                                                        .build();
                                        orgUpdates.add(update);
                                }
                                partnerOrganizationUpdateRepository.saveAll(orgUpdates);

                                var admin = RegisterReq.builder()
                                                .email("admin@gmail.com")
                                                .password("SGL@2025")
                                                .role(Role.ADMIN)
                                                .build();
                                var sub_admin1 = RegisterReq.builder()
                                                .email("sub_admin1@gmail.com")
                                                .password("SGL@2025")
                                                .role(Role.SUB_ADMIN)
                                                .build();

                                var sub_admin2 = RegisterReq.builder()
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
                                                Permission.LECTURER_APPROVE);
                                subAdminService.assignPermissionsToUser(subAdmin1User, subAdmin1Permissions, adminUser);

                                // Assign permissions to sub_admin2 (có quyền về school và một số quyền
                                // lecturer)
                                List<Permission> subAdmin2Permissions = List.of(
                                                Permission.SCHOOL_READ,
                                                Permission.SCHOOL_UPDATE,
                                                Permission.SCHOOL_APPROVE,
                                                Permission.LECTURER_READ);
                                subAdminService.assignPermissionsToUser(subAdmin2User, subAdmin2Permissions, adminUser);

                                // ===== TẠO DỮ LIỆU MẪU CHO PROJECT VÀ CÁC ENTITY LIÊN QUAN =====
                                createSampleProjectData(projectRepository, applicationRepository,
                                                applicationModuleRepository, interviewRepository,
                                                contractRepository, courseInfoRepository, courseModuleRepository,
                                                educationInstitutionRepository, partnerOrganizationRepository,
                                                lecturerRepository, faker);

                                for (User user : userRepository.findAll()) {
                                        if (user.getEmail().equalsIgnoreCase("lecturer1@gmail.com")) {
                                                user.setSubEmails(Set.of("foxfessor@gmail.com"));
                                        } else {
                                                user.setSubEmails(Set.of("user" + user.getEmail() + ".backup@gmail.com",
                                                                "user" + user.getEmail() + ".personal@gmail.com"));
                                        }
                                        userRepository.save(user);
                                }

                                // ===== TẠO DỮ LIỆU MẪU CHO TRAINING PROGRAM =====
                                createSampleTrainingProgramData(trainingProgramRequestRepository,
                                                trainingProgramRepository, trainingUnitRepository,
                                                partnerOrganizationRepository, lecturerRepository,
                                                userRepository, faker);

                                // ===== TẠO DỮ LIỆU MẪU CHO NOTIFICATION =====
                                createSampleNotificationData(notificationRepository, userRepository, faker);

                                System.out.println("token admin: " + adminResponse.getAccessToken());
                                System.out.println("token sub_admin1: " + subAdmin1Response.getAccessToken());
                                System.out.println("token sub_admin2: " + subAdmin2Response.getAccessToken());

                        } catch (Exception e) {
                                throw new RuntimeException("không thể khởi tạo dữ liệu mẫu: " + e.getMessage(), e);
                        }
                };
        }

        /**
         * Trả về trạng thái cho PartnerOrganization sao cho 80% đầu tiên có trạng thái
         * APPROVED
         * 
         * @param index chỉ số từ 1-10 (tổng cộng 10 PartnerOrganization)
         * @return PendingStatus
         */
        private PendingStatus getPartnerOrganizationStatus(int index) {
                // 80% đầu tiên (8/10) sẽ có status APPROVED
                if (index <= 8) {
                        return PendingStatus.APPROVED;
                } else {
                        // 20% còn lại (2/10) sẽ có status PENDING hoặc REJECTED
                        return (index == 9) ? PendingStatus.PENDING : PendingStatus.REJECTED;
                }
        }

        private void createSampleProjectData(
                        ProjectRespository projectRepository,
                        ApplicationRepository applicationRepository,
                        ApplicationModuleRepository applicationModuleRepository,
                        InterviewRepository interviewRepository,
                        ContractRepository contractRepository,
                        CourseInfoRepository courseInfoRepository,
                        CourseModuleRepository courseModuleRepository,
                        InstitutionRepository educationInstitutionRepository,
                        PartnerRepository partnerOrganizationRepository,
                        LecturerRepository lecturerRepository,
                        Faker faker) {

                try {
                        // Lấy dữ liệu đã được APPROVED
                        List<EducationInstitution> institutions = educationInstitutionRepository.findAll().stream()
                                        .filter(institution -> institution.getStatus() == PendingStatus.APPROVED)
                                        .toList();
                        List<PartnerOrganization> partners = partnerOrganizationRepository.findAll().stream()
                                        .filter(partner -> partner.getStatus() == PendingStatus.APPROVED)
                                        .toList();
                        List<Lecturer> lecturers = lecturerRepository.findAll().stream()
                                        .filter(lecturer -> lecturer.getStatus() == PendingStatus.APPROVED)
                                        .toList();

                        if (institutions.isEmpty() || partners.isEmpty() || lecturers.isEmpty()) {
                                System.out.println(
                                                "Không có đủ dữ liệu APPROVED để tạo projects. Cần có ít nhất 1 institution, 1 partner và 1 lecturer với status APPROVED.");
                                return;
                        }

                        List<String> projectFields = Arrays.asList(
                                        "Công nghệ thông tin", "Khoa học máy tính", "Kỹ thuật phần mềm",
                                        "Trí tuệ nhân tạo", "An ninh mạng", "Khoa học dữ liệu",
                                        "Kinh tế", "Quản trị kinh doanh", "Marketing", "Tài chính",
                                        "Y dược", "Sinh học", "Hóa học", "Vật lý",
                                        "Giáo dục", "Tâm lý học", "Ngôn ngữ học");

                        List<String> researchTitles = Arrays.asList(
                                        "Nghiên cứu ứng dụng AI trong giáo dục",
                                        "Phân tích dữ liệu lớn cho doanh nghiệp",
                                        "Nghiên cứu bảo mật thông tin",
                                        "Ứng dụng blockchain trong tài chính",
                                        "Nghiên cứu machine learning trong y tế",
                                        "Phát triển hệ thống IoT thông minh",
                                        "Nghiên cứu về tâm lý học nhận thức",
                                        "Ứng dụng VR/AR trong giáo dục",
                                        "Nghiên cứu về kinh tế số",
                                        "Phát triển thuật toán tối ưu hóa");

                        List<String> courseTitles = Arrays.asList(
                                        "Khóa học lập trình Java Spring Boot",
                                        "Khóa học phát triển ứng dụng mobile",
                                        "Khóa học thiết kế UX/UI chuyên nghiệp",
                                        "Khóa học marketing digital",
                                        "Khóa học quản trị dự án",
                                        "Khóa học phân tích dữ liệu với Python",
                                        "Khóa học phát triển web fullstack",
                                        "Khóa học cloud computing với AWS",
                                        "Khóa học machine learning cơ bản",
                                        "Khóa học cyber security");

                        List<Project> projects = new ArrayList<>();

                        // Tạo 20 projects (10 RESEARCH, 10 COURSE)
                        for (int i = 0; i < 20; i++) {
                                boolean isResearch = i < 10;
                                ProjectCategory category = isResearch ? ProjectCategory.RESEARCH
                                                : ProjectCategory.COURSE;

                                List<String> titles = isResearch ? researchTitles : courseTitles;
                                String title = titles.get(i % titles.size());

                                // Chọn owner ngẫu nhiên (institution hoặc partner)
                                boolean useInstitution = faker.random().nextBoolean();
                                EducationInstitution institution = useInstitution
                                                ? institutions.get(faker.random().nextInt(institutions.size()))
                                                : null;
                                PartnerOrganization partner = !useInstitution
                                                ? partners.get(faker.random().nextInt(partners.size()))
                                                : null;

                                LocalDate startDate = faker.date().between(
                                                java.sql.Date.valueOf(LocalDate.now().minusMonths(3)),
                                                java.sql.Date.valueOf(LocalDate.now().plusMonths(2))).toInstant()
                                                .atZone(ZoneId.systemDefault()).toLocalDate();

                                LocalDate endDate = startDate.plusMonths(faker.random().nextInt(3, 12));

                                Project project = Project.builder()
                                                .title(title.length() > 500 ? title.substring(0, 497) + "..." : title)
                                                .type(category)
                                                .field(projectFields.get(faker.random().nextInt(projectFields.size())))
                                                .description(faker.lorem().paragraph(2))
                                                .memberCount(faker.random().nextInt(2, 8))
                                                .budget(BigDecimal.valueOf(
                                                                faker.random().nextInt(50_000_000, 500_000_000)))
                                                .status(ProjectStatus.values()[faker.random()
                                                                .nextInt(ProjectStatus.values().length)])
                                                .jobDescription(truncate(faker.lorem().paragraph(5), 2000))
                                                .requirements(Arrays.asList(
                                                                truncate(faker.job().keySkills(), 255),
                                                                truncate(faker.job().keySkills(), 255),
                                                                truncate(faker.job().keySkills(), 255)))
                                                .benefits(Arrays.asList(
                                                                "Lương thưởng cạnh tranh",
                                                                "Môi trường làm việc chuyên nghiệp",
                                                                "Cơ hội phát triển nghề nghiệp",
                                                                "Bảo hiểm đầy đủ"))
                                                .startDate(startDate)
                                                .endDate(endDate)
                                                .published(faker.random().nextBoolean())
                                                .duration(faker.random().nextInt(1, 24))
                                                .durationUnit(faker.options().option("MONTHS", "WEEKS", "DAYS"))
                                                .isRemote(faker.random().nextBoolean())
                                                .location(faker.random().nextBoolean() ? "Online"
                                                                : (faker.address().cityName().length() > 500
                                                                                ? faker.address().cityName().substring(
                                                                                                0, 497) + "..."
                                                                                : faker.address().cityName()))
                                                .educationInstitution(institution)
                                                .partnerOrganization(partner)
                                                .build();

                                projects.add(project);
                        }

                        // Lưu projects
                        projectRepository.saveAll(projects);
                        projectRepository.flush();

                        // Tạo CourseInfo và CourseModule cho các project có type = COURSE
                        List<CourseInfo> courseInfos = new ArrayList<>();
                        List<CourseModule> courseModules = new ArrayList<>();

                        // Dữ liệu có sẵn cho CourseInfo
                        List<String> courseIntroduces = Arrays.asList(
                                        "Khóa học này được thiết kế đặc biệt để trang bị cho học viên những kiến thức và kỹ năng cần thiết trong lĩnh vực công nghệ. Với đội ngũ giảng viên giàu kinh nghiệm và phương pháp giảng dạy hiện đại, chúng tôi cam kết mang đến trải nghiệm học tập tốt nhất.",
                                        "Chương trình đào tạo chuyên sâu, kết hợp giữa lý thuyết và thực hành, giúp học viên nắm vững kiến thức cốt lõi và áp dụng ngay vào công việc thực tế. Nội dung được cập nhật liên tục theo xu hướng công nghệ mới nhất.",
                                        "Khóa học toàn diện từ cơ bản đến nâng cao, phù hợp với mọi đối tượng từ người mới bắt đầu đến chuyên gia muốn nâng cao trình độ. Phương pháp học tập chủ động, kết hợp bài giảng, bài tập thực hành và dự án nhóm.",
                                        "Được xây dựng bởi các chuyên gia hàng đầu trong ngành, khóa học này mang đến cái nhìn toàn diện về lĩnh vực, từ khái niệm cơ bản đến ứng dụng thực tế, giúp học viên tự tin làm việc trong môi trường chuyên nghiệp.",
                                        "Khóa học tập trung vào việc phát triển kỹ năng thực tế, với các dự án thực tế và case study thực tế. Học viên sẽ được hướng dẫn bởi những giảng viên có nhiều năm kinh nghiệm trong lĩnh vực.");

                        List<String> courseDescriptions = Arrays.asList(
                                        "Khóa học lập trình Java Spring Boot toàn diện, từ cơ bản đến nâng cao với các dự án thực tế.",
                                        "Học phát triển ứng dụng di động với React Native và Flutter, tạo ra ứng dụng đa nền tảng.",
                                        "Thiết kế giao diện người dùng chuyên nghiệp với Figma và Adobe XD, từ ý tưởng đến prototype.",
                                        "Marketing số toàn diện với Google Ads, Facebook Ads và SEO, tối ưu hóa hiệu quả kinh doanh.",
                                        "Quản trị dự án Agile với Scrum và Kanban, nâng cao hiệu suất làm việc nhóm.",
                                        "Phân tích dữ liệu với Python, từ thu thập đến trực quan hóa dữ liệu chuyên nghiệp.",
                                        "Phát triển web fullstack với Node.js, React và MongoDB, xây dựng ứng dụng hoàn chỉnh.",
                                        "Cloud computing với AWS, từ EC2 đến serverless, triển khai ứng dụng đám mây.",
                                        "Machine learning cơ bản với Python, từ khái niệm đến mô hình dự đoán thực tế.",
                                        "An ninh mạng toàn diện, từ bảo mật mạng đến ứng dụng, bảo vệ hệ thống khỏi mối đe dọa.");

                        List<List<String>> courseKnowledgeData = Arrays.asList(
                                        Arrays.asList("Java Core", "Spring Framework", "RESTful API",
                                                        "Database Design"),
                                        Arrays.asList("React Native", "Flutter", "Mobile UI/UX",
                                                        "App Store Deployment"),
                                        Arrays.asList("Figma", "Adobe XD", "User Research", "Prototyping"),
                                        Arrays.asList("Google Ads", "Facebook Ads", "SEO", "Analytics"),
                                        Arrays.asList("Agile", "Scrum", "Kanban", "Team Management"),
                                        Arrays.asList("Python", "Pandas", "Matplotlib", "Data Visualization"),
                                        Arrays.asList("Node.js", "React", "MongoDB", "Authentication"),
                                        Arrays.asList("AWS EC2", "Lambda", "S3", "CloudFormation"),
                                        Arrays.asList("Python ML", "Scikit-learn", "TensorFlow", "Model Deployment"),
                                        Arrays.asList("Network Security", "Cryptography", "Penetration Testing",
                                                        "Security Tools"));

                        List<List<String>> courseRequirementsData = Arrays.asList(
                                        Arrays.asList("Kiến thức cơ bản về lập trình",
                                                        "Máy tính có cấu hình trung bình", "Kết nối internet ổn định"),
                                        Arrays.asList("Smartphone hoặc máy tính bảng", "Kiến thức cơ bản về lập trình",
                                                        "Sở thích với mobile development"),
                                        Arrays.asList("Máy tính có phần mềm thiết kế", "Khả năng sáng tạo",
                                                        "Kiến thức cơ bản về UX"),
                                        Arrays.asList("Máy tính kết nối internet", "Kiến thức marketing cơ bản",
                                                        "Kinh nghiệm sử dụng mạng xã hội"),
                                        Arrays.asList("Kinh nghiệm làm việc nhóm", "Máy tính cá nhân",
                                                        "Sở thích với project management"),
                                        Arrays.asList("Kiến thức toán cơ bản", "Máy tính có Python",
                                                        "Sở thích với data analysis"),
                                        Arrays.asList("Kiến thức JavaScript cơ bản", "Máy tính hiện đại",
                                                        "Kinh nghiệm web development"),
                                        Arrays.asList("Máy tính kết nối internet", "Kiến thức IT cơ bản",
                                                        "Sở thích với cloud computing"),
                                        Arrays.asList("Kiến thức Python và toán", "Máy tính cấu hình tốt",
                                                        "Sở thích với AI/ML"),
                                        Arrays.asList("Kiến thức IT cơ bản", "Máy tính hiện đại",
                                                        "Sở thích với cybersecurity"));

                        // Danh sách thumbnail URLs đa dạng
                        List<String> courseThumbnailUrls = Arrays.asList(
                                        "https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1571260899304-425eee4c7efc?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1516321497487-e288fb19713f?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1571260899304-425eee4c7efc?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1516321497487-e288fb19713f?w=800&h=600&fit=crop",
                                        "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=800&h=600&fit=crop");

                        int dataIndex = 0; // Index để chọn dữ liệu có sẵn

                        for (Project project : projects) {
                                if (project.getType() == ProjectCategory.COURSE) {
                                        // Lấy dữ liệu có sẵn theo index
                                        String introduce = courseIntroduces.get(dataIndex % courseIntroduces.size());
                                        String publicDescription = courseDescriptions
                                                        .get(dataIndex % courseDescriptions.size());
                                        List<String> knowledge = courseKnowledgeData
                                                        .get(dataIndex % courseKnowledgeData.size());
                                        List<String> requirements = courseRequirementsData
                                                        .get(dataIndex % courseRequirementsData.size());

                                        // Giới hạn độ dài dữ liệu
                                        String safePublicTitle = project.getTitle().length() > 500
                                                        ? project.getTitle().substring(0, 497) + "..."
                                                        : project.getTitle();
                                        String safeThumbnailUrl = courseThumbnailUrls
                                                        .get(dataIndex % courseThumbnailUrls.size());
                                        String safeAddress = project.getLocation().length() > 500
                                                        ? project.getLocation().substring(0, 497) + "..."
                                                        : project.getLocation();

                                        // Tạo CourseInfo
                                        CourseInfo courseInfo = CourseInfo.builder()
                                                        .project(project)
                                                        .publicTitle(safePublicTitle)
                                                        .publicDescription(publicDescription)
                                                        .thumbnailUrl(safeThumbnailUrl)
                                                        .knowledge(knowledge.stream().map(k -> truncate(k, 255))
                                                                        .toList())
                                                        .requirements(requirements.stream().map(r -> truncate(r, 255))
                                                                        .toList())
                                                        .address(safeAddress)
                                                        .isOnline(faker.random().nextBoolean())
                                                        .introduce(introduce)
                                                        .level(CourseLevel.values()[faker.random()
                                                                        .nextInt(CourseLevel.values().length)])
                                                        .price(BigDecimal.valueOf(
                                                                        faker.random().nextInt(1_000_000, 10_000_000)))
                                                        .published(faker.random().nextBoolean())
                                                        .build();
                                        courseInfos.add(courseInfo);

                                        dataIndex++; // Tăng index cho lần sau
                                }
                        }

                        // Dữ liệu mẫu cho CourseModule
                        List<String> moduleTitles = Arrays.asList(
                                        "Giới thiệu và tổng quan khóa học",
                                        "Cài đặt môi trường phát triển",
                                        "Ngôn ngữ lập trình cơ bản",
                                        "Cấu trúc dữ liệu và giải thuật",
                                        "Lập trình hướng đối tượng",
                                        "Xử lý file và I/O",
                                        "Làm việc với cơ sở dữ liệu",
                                        "Tạo giao diện người dùng",
                                        "Xử lý sự kiện và tương tác",
                                        "Debug và testing",
                                        "Triển khai ứng dụng",
                                        "Bảo mật và bảo mật",
                                        "Tối ưu hóa hiệu suất",
                                        "Design patterns",
                                        "Microservices và API",
                                        "DevOps và CI/CD",
                                        "Machine Learning cơ bản",
                                        "Blockchain và cryptocurrency",
                                        "Cloud computing với AWS",
                                        "Mobile development");

                        List<String> moduleDescriptions = Arrays.asList(
                                        "Khóa học bắt đầu với việc giới thiệu tổng quan về ngành công nghệ thông tin, các khái niệm cơ bản và xu hướng phát triển. Học viên sẽ được làm quen với môi trường học tập và các công cụ cần thiết.",
                                        "Hướng dẫn chi tiết cách cài đặt và cấu hình môi trường phát triển bao gồm JDK, IDE, và các công cụ hỗ trợ lập trình. Đảm bảo học viên có setup hoàn chỉnh trước khi bắt đầu.",
                                        "Làm quen với cú pháp cơ bản của ngôn ngữ lập trình, khai báo biến, toán tử, câu lệnh điều kiện và vòng lặp. Đây là nền tảng quan trọng cho các bài học tiếp theo.",
                                        "Tìm hiểu về các cấu trúc dữ liệu cơ bản như mảng, danh sách liên kết, ngăn xếp, hàng đợi và cây. Đồng thời học cách áp dụng các giải thuật sắp xếp và tìm kiếm.",
                                        "Khám phá khái niệm lập trình hướng đối tượng với các khái niệm trừu tượng, đóng gói, kế thừa và đa hình. Thực hành với ví dụ thực tế để hiểu rõ hơn.",
                                        "Học cách đọc và ghi file, xử lý luồng dữ liệu input/output. Bao gồm làm việc với file text, binary và xử lý exception khi thao tác với file system.",
                                        "Tìm hiểu cách kết nối và thao tác với cơ sở dữ liệu quan hệ. Bao gồm SQL cơ bản, JDBC và các pattern phổ biến trong database programming.",
                                        "Thiết kế và xây dựng giao diện người dùng thân thiện với HTML, CSS và JavaScript. Tập trung vào responsive design và user experience.",
                                        "Xử lý các sự kiện từ người dùng như click, hover, form submission. Tích hợp JavaScript để tạo tính tương tác cho ứng dụng web.",
                                        "Kỹ thuật debug code hiệu quả, sử dụng các công cụ debugging và testing. Viết unit test và integration test để đảm bảo chất lượng code.",
                                        "Hướng dẫn cách deploy ứng dụng lên server, cấu hình production environment và monitoring. Bao gồm containerization với Docker.",
                                        "Các khái niệm bảo mật cơ bản trong phát triển phần mềm, authentication, authorization và các best practices về security.",
                                        "Tối ưu hóa hiệu suất ứng dụng, database optimization, caching strategies và code optimization techniques.",
                                        "Tìm hiểu các design patterns phổ biến như Singleton, Factory, Observer. Áp dụng vào giải quyết vấn đề thực tế.",
                                        "Kiến trúc microservices, RESTful API design, API documentation và version management.",
                                        "DevOps practices, CI/CD pipelines, automated testing và deployment strategies.",
                                        "Giới thiệu machine learning cơ bản, supervised và unsupervised learning, các thuật toán phổ biến.",
                                        "Blockchain technology, smart contracts, cryptocurrency và các ứng dụng thực tế của blockchain.",
                                        "Cloud computing với AWS, các services cơ bản như EC2, S3, Lambda và best practices.",
                                        "Phát triển ứng dụng di động native và cross-platform, UI/UX design cho mobile apps.");

                        List<List<String>> moduleRequirements = Arrays.asList(
                                        Arrays.asList("Máy tính cá nhân", "Kết nối internet ổn định",
                                                        "Sở thích học tập"),
                                        Arrays.asList("Máy tính Windows/Mac/Linux", "4GB RAM", "10GB ổ cứng trống"),
                                        Arrays.asList("Kiến thức toán cơ bản", "Máy tính có trình biên dịch",
                                                        "Sổ tay ghi chép"),
                                        Arrays.asList("Kiến thức lập trình cơ bản", "Máy tính cấu hình trung bình",
                                                        "Tài liệu tham khảo"),
                                        Arrays.asList("Hiểu biết OOP cơ bản", "IDE đã cài đặt", "Dự án thực hành"),
                                        Arrays.asList("Kiến thức I/O cơ bản", "File system access",
                                                        "Exception handling"),
                                        Arrays.asList("Kiến thức SQL", "JDBC driver", "Database server"),
                                        Arrays.asList("HTML/CSS cơ bản", "Text editor", "Web browser"),
                                        Arrays.asList("JavaScript fundamentals", "DOM manipulation", "Event handling"),
                                        Arrays.asList("Debugging mindset", "Testing framework", "Code review skills"),
                                        Arrays.asList("Development environment", "Server knowledge",
                                                        "Deployment tools"),
                                        Arrays.asList("Security awareness", "Encryption basics",
                                                        "Authentication concepts"),
                                        Arrays.asList("Performance monitoring", "Profiling tools",
                                                        "Optimization mindset"),
                                        Arrays.asList("Problem solving", "Design thinking", "Code refactoring"),
                                        Arrays.asList("API knowledge", "Documentation skills", "Version control"),
                                        Arrays.asList("DevOps mindset", "Automation skills", "Monitoring tools"),
                                        Arrays.asList("Math background", "Statistics knowledge", "Programming skills"),
                                        Arrays.asList("Distributed systems", "Cryptography basics",
                                                        "Smart contract logic"),
                                        Arrays.asList("Cloud concepts", "AWS account", "Command line tools"),
                                        Arrays.asList("Mobile development", "UI/UX design", "Cross-platform tools"));

                        // Tạo 3-8 CourseModule cho mỗi course
                        int moduleDataIndex = 0;
                        for (Project project : projects) {
                                if (project.getType() == ProjectCategory.COURSE) {
                                        int moduleCount = faker.random().nextInt(3, 9);
                                        for (int j = 0; j < moduleCount; j++) {
                                                String moduleTitle = moduleTitles
                                                                .get(moduleDataIndex % moduleTitles.size());
                                                String moduleDescription = moduleDescriptions
                                                                .get(moduleDataIndex % moduleDescriptions.size());
                                                List<String> moduleReqs = moduleRequirements
                                                                .get(moduleDataIndex % moduleRequirements.size());

                                                CourseModule module = CourseModule.builder()
                                                                .project(project)
                                                                .title(moduleTitle.length() > 500
                                                                                ? moduleTitle.substring(0, 497) + "..."
                                                                                : moduleTitle)
                                                                .description(moduleDescription)
                                                                .moduleOrder(j + 1)
                                                                .duration(faker.random().nextInt(2, 8))
                                                                .requirements(moduleReqs.stream()
                                                                                .map(req -> truncate(req, 255))
                                                                                .toList())
                                                                .build();
                                                courseModules.add(module);

                                                moduleDataIndex++;
                                        }
                                }
                        }

                        // Lưu CourseInfo và CourseModule
                        if (!courseInfos.isEmpty()) {
                                courseInfoRepository.saveAll(courseInfos);
                                courseInfoRepository.flush();
                        }
                        if (!courseModules.isEmpty()) {
                                courseModuleRepository.saveAll(courseModules);
                                courseModuleRepository.flush();
                        }

                        // Tạo Applications cho các projects
                        List<Application> applications = new ArrayList<>();
                        List<ApplicationModule> applicationModules = new ArrayList<>();
                        List<Interview> interviews = new ArrayList<>();
                        List<Contract> contracts = new ArrayList<>();

                        // Tạo nhiều Applications cho tất cả projects
                        if (!projects.isEmpty() && !lecturers.isEmpty()) {
                                // Tạo 2-5 Applications cho mỗi project
                                for (Project project : projects) {
                                        int applicationCount = faker.random().nextInt(2, 6);

                                        for (int i = 0; i < applicationCount; i++) {
                                                Lecturer randomLecturer = lecturers
                                                                .get(faker.random().nextInt(lecturers.size()));

                                                // Chỉ application đầu tiên có status APPROVED
                                                ApplicationStatus status = (i == 0) ? ApplicationStatus.APPROVED
                                                                : ApplicationStatus.SUBMITTED;

                                                Application application = Application.builder()
                                                                .project(project)
                                                                .lecturer(randomLecturer)
                                                                .cvUrl("http://demoportal.ccvi.com.vn:8880/uploads/LECTURER/"
                                                                                + randomLecturer.getId() + "/cv.pdf")
                                                                .coverLetter(faker.lorem().paragraph(3))
                                                                .expectedSalary(BigDecimal.valueOf(
                                                                                faker.random().nextInt(15_000_000,
                                                                                                50_000_000)))
                                                                .status(status)
                                                                .build();
                                                applications.add(application);

                                                // Tạo Interview cho Application có status APPROVED
                                                if (status == ApplicationStatus.APPROVED) {
                                                        // Tạo 1-2 Interview với PASS cho Application APPROVED
                                                        int interviewCount = faker.random().nextInt(1, 3);
                                                        for (int j = 0; j < interviewCount; j++) {
                                                                Interview interview = Interview.builder()
                                                                                .application(application)
                                                                                .interviewDate(faker.date().between(
                                                                                                java.sql.Date.from(
                                                                                                                LocalDateTime.now()
                                                                                                                                .minusWeeks(2)
                                                                                                                                .atZone(ZoneId.systemDefault())
                                                                                                                                .toInstant()),
                                                                                                java.sql.Date.from(
                                                                                                                LocalDateTime.now()
                                                                                                                                .minusWeeks(1)
                                                                                                                                .atZone(ZoneId.systemDefault())
                                                                                                                                .toInstant()))
                                                                                                .toInstant()
                                                                                                .atZone(ZoneId.systemDefault())
                                                                                                .toLocalDateTime())
                                                                                .location(faker.random().nextBoolean()
                                                                                                ? "Online"
                                                                                                : faker.address()
                                                                                                                .fullAddress())
                                                                                .mode(faker.random().nextBoolean()
                                                                                                ? InterviewMode.ONLINE
                                                                                                : InterviewMode.OFFLINE)
                                                                                .status(InterviewStatus.COMPLETED)
                                                                                .feedback("Ứng viên có kiến thức vững chắc và kinh nghiệm phù hợp với yêu cầu công việc.")
                                                                                .score(faker.random().nextInt(85, 100))
                                                                                .result(InterviewResult.PASS)
                                                                                .build();
                                                                interviews.add(interview);
                                                        }

                                                        // Tạo Contract
                                                        Contract contract = Contract.builder()
                                                                        .application(application)
                                                                        .contractUrl("http://demoportal.ccvi.com.vn:8880/uploads/LECTURER/"
                                                                                        + randomLecturer.getId()
                                                                                        + "/contract.pdf")
                                                                        .contractNo("CT" + faker.random().nextInt(1000,
                                                                                        9999))
                                                                        .signedDate(faker.date().between(
                                                                                        java.sql.Date.from(LocalDateTime
                                                                                                        .now()
                                                                                                        .minusWeeks(1)
                                                                                                        .atZone(ZoneId.systemDefault())
                                                                                                        .toInstant()),
                                                                                        java.sql.Date.from(LocalDateTime
                                                                                                        .now()
                                                                                                        .atZone(ZoneId.systemDefault())
                                                                                                        .toInstant()))
                                                                                        .toInstant()
                                                                                        .atZone(ZoneId.systemDefault())
                                                                                        .toLocalDateTime())
                                                                        .status(ContractStatus.values()[faker.random()
                                                                                        .nextInt(ContractStatus
                                                                                                        .values().length)])
                                                                        .build();
                                                        contracts.add(contract);
                                                } else {
                                                        // Tạo Interview với FAIL cho Application không APPROVED
                                                        Interview interview = Interview.builder()
                                                                        .application(application)
                                                                        .interviewDate(faker.date().between(
                                                                                        java.sql.Date.from(LocalDateTime
                                                                                                        .now()
                                                                                                        .minusWeeks(2)
                                                                                                        .atZone(ZoneId.systemDefault())
                                                                                                        .toInstant()),
                                                                                        java.sql.Date.from(LocalDateTime
                                                                                                        .now()
                                                                                                        .minusWeeks(1)
                                                                                                        .atZone(ZoneId.systemDefault())
                                                                                                        .toInstant()))
                                                                                        .toInstant()
                                                                                        .atZone(ZoneId.systemDefault())
                                                                                        .toLocalDateTime())
                                                                        .location(faker.random().nextBoolean()
                                                                                        ? "Online"
                                                                                        : faker.address().fullAddress())
                                                                        .mode(faker.random().nextBoolean()
                                                                                        ? InterviewMode.ONLINE
                                                                                        : InterviewMode.OFFLINE)
                                                                        .status(InterviewStatus.COMPLETED)
                                                                        .feedback("Ứng viên chưa đáp ứng đủ yêu cầu.")
                                                                        .score(faker.random().nextInt(40, 65))
                                                                        .result(InterviewResult.FAIL)
                                                                        .build();
                                                        interviews.add(interview);
                                                }
                                        }
                                }
                        }

                        // Lưu tất cả
                        if (!applications.isEmpty()) {
                                applicationRepository.saveAll(applications);
                                applicationRepository.flush();
                        }

                        // Tạo ApplicationModule cho các CourseModule (chỉ cho COURSE projects)
                        if (!applications.isEmpty() && !courseModules.isEmpty()) {
                                // Lọc Applications APPROVED liên kết với COURSE projects
                                List<Application> approvedCourseApplications = applications.stream()
                                                .filter(app -> app.getProject().getType() == ProjectCategory.COURSE)
                                                .filter(app -> app.getStatus() == ApplicationStatus.APPROVED)
                                                .toList();

                                if (!approvedCourseApplications.isEmpty()) {
                                        // Tạo ApplicationModule cho mỗi CourseModule
                                        int applicationIndex = 0; // Index để track Application APPROVED hiện tại
                                        for (CourseModule courseModule : courseModules) {
                                                // Tạo 2-4 ApplicationModule cho mỗi CourseModule
                                                int appModuleCount = faker.random().nextInt(2, 5);

                                                for (int i = 0; i < appModuleCount; i++) {
                                                        // Gán Application APPROVED theo index (round-robin)
                                                        Application assignedApplication = approvedCourseApplications
                                                                        .get(applicationIndex);

                                                        ApplicationModule applicationModule = ApplicationModule
                                                                        .builder()
                                                                        .application(assignedApplication)
                                                                        .courseModule(courseModule)
                                                                        .build();
                                                        applicationModules.add(applicationModule);

                                                        // Tăng index và reset về 0 khi hết danh sách
                                                        applicationIndex = (applicationIndex + 1)
                                                                        % approvedCourseApplications.size();
                                                }
                                        }

                                        applicationModuleRepository.saveAll(applicationModules);
                                        applicationModuleRepository.flush();
                                }
                        }
                        if (!interviews.isEmpty()) {
                                interviewRepository.saveAll(interviews);
                                interviewRepository.flush();
                        }
                        if (!contracts.isEmpty()) {
                                contractRepository.saveAll(contracts);
                                contractRepository.flush();
                        }

                        System.out.println("✅ Đã tạo thành công dữ liệu mẫu cho Projects:");
                        System.out.println("- " + projects.size()
                                        + " Projects (10 RESEARCH - không có CourseInfo/CourseModule, 10 COURSE - có CourseInfo/CourseModule)");
                        System.out.println("- " + courseInfos.size()
                                        + " CourseInfos (chỉ cho projects COURSE, sử dụng dữ liệu có sẵn)");
                        System.out.println("- " + courseModules.size()
                                        + " CourseModules (chỉ cho projects COURSE, sử dụng dữ liệu có sẵn)");
                        System.out.println("- " + applications.size()
                                        + " Applications (tất cả projects, chỉ 1 với status APPROVED)");
                        System.out.println("- " + applicationModules.size()
                                        + " ApplicationModules (chỉ cho COURSE projects, mỗi module có đúng 1 Application APPROVED)");
                        System.out.println("- " + interviews.size() + " Interviews (chỉ cho Applications APPROVED)");
                        System.out.println("- " + contracts.size() + " Contracts (chỉ cho Applications APPROVED)");

                } catch (Exception e) {
                        System.err.println("Lỗi khi tạo dữ liệu mẫu cho Projects: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private void createSampleTrainingProgramData(
                        TrainingProgramRequestRepository trainingProgramRequestRepository,
                        TrainingProgramRepository trainingProgramRepository,
                        TrainingUnitRepository trainingUnitRepository,
                        PartnerRepository partnerOrganizationRepository,
                        LecturerRepository lecturerRepository,
                        UserRepository userRepository,
                        Faker faker) {

                try {
                        // Lấy dữ liệu đã được APPROVED
                        List<PartnerOrganization> partners = partnerOrganizationRepository.findAll().stream()
                                        .filter(partner -> partner.getStatus() == PendingStatus.APPROVED)
                                        .toList();
                        List<Lecturer> lecturers = lecturerRepository.findAll().stream()
                                        .filter(lecturer -> lecturer.getStatus() == PendingStatus.APPROVED)
                                        .toList();
                        List<User> users = userRepository.findAll();

                        if (partners.isEmpty() || lecturers.isEmpty() || users.isEmpty()) {
                                System.err.println(
                                                "Không đủ dữ liệu để tạo Training Programs (cần partners, lecturers, users)");
                                return;
                        }

                        // Dữ liệu mẫu cho Training Program Request (yêu cầu tạo chương trình đào tạo)

                        String trialVideoUrl = "https://youtu.be/ED5rM9xITNI?si=nAmzLItbb76y-TaP";

                        List<String> requestTitles = Arrays.asList(
                                        "Yêu cầu tạo khóa đào tạo Java Spring Boot cho nhân viên",
                                        "Đề xuất chương trình Data Science cho sinh viên IT",
                                        "Yêu cầu phê duyệt khóa React Native Mobile Development",
                                        "Đề xuất khóa đào tạo AI & Machine Learning nâng cao",
                                        "Yêu cầu tạo chương trình DevOps cho doanh nghiệp",
                                        "Đề xuất khóa đào tạo Cybersecurity cho CNTT",
                                        "Yêu cầu phê duyệt chương trình Full-stack Web Development",
                                        "Đề xuất khóa Digital Marketing cho nhân viên kinh doanh",
                                        "Yêu cầu tạo khóa Business Analysis chuyên nghiệp",
                                        "Đề xuất chương trình Project Management với Agile/Scrum",
                                        "Yêu cầu phê duyệt khóa Node.js Backend Development",
                                        "Đề xuất chương trình Angular Frontend cho developer",
                                        "Yêu cầu tạo khóa Docker & Kubernetes cho DevOps",
                                        "Đề xuất chương trình Database Design & Optimization",
                                        "Yêu cầu phê duyệt khóa Microservices Architecture",
                                        "Đề xuất chương trình UI/UX Design cho designer",
                                        "Yêu cầu tạo khóa Blockchain Development nâng cao",
                                        "Đề xuất chương trình AWS Cloud Practitioner",
                                        "Yêu cầu phê duyệt khóa Mobile Game Development",
                                        "Đề xuất chương trình Software Testing Automation",
                                        "Yêu cầu tạo khóa E-commerce Platform Development",
                                        "Đề xuất chương trình API Design & Development",
                                        "Yêu cầu phê duyệt khóa Data Engineering với Spark",
                                        "Đề xuất chương trình Vue.js cho Frontend Developer",
                                        "Yêu cầu tạo khóa GraphQL & Apollo cho Backend");

                        List<String> requestDescriptions = Arrays.asList(
                                        "Công ty chúng tôi cần đào tạo đội ngũ Java Developer về Spring Boot framework để nâng cao năng lực phát triển ứng dụng enterprise. Mong muốn có chương trình đào tạo từ 40-60 tiếng với thực hành dự án thực tế.",
                                        "Trường đại học đề xuất tạo chương trình Data Science với Python để trang bị kiến thức cho sinh viên ngành CNTT. Cần bao gồm machine learning, data visualization và big data analytics trong vòng 3 tháng.",
                                        "Doanh nghiệp muốn đào tạo nhóm mobile developer về React Native để phát triển ứng dụng đa nền tảng. Yêu cầu chương trình kéo dài 2 tháng với 50% lý thuyết và 50% thực hành.",
                                        "Tổ chức nghiên cứu cần khóa đào tạo AI & Machine Learning chuyên sâu cho đội ngũ data scientist. Mong muốn có nội dung về deep learning, computer vision và NLP với thời lượng 80 giờ.",
                                        "Công ty công nghệ đề xuất chương trình DevOps & Cloud Computing để nâng cao năng lực vận hành hệ thống. Cần đào tạo về Docker, Kubernetes, CI/CD và AWS trong 45 ngày.",
                                        "Tập đoàn tài chính yêu cầu đào tạo đội ngũ IT về an ninh mạng và cybersecurity. Mong muốn có chương trình toàn diện về network security, ethical hacking và incident response.",
                                        "Startup công nghệ cần đào tạo developer full-stack để xây dựng sản phẩm từ frontend đến backend. Đề xuất chương trình 60 giờ kết hợp React, Node.js và database design.",
                                        "Công ty marketing digital muốn nâng cấp kỹ năng cho nhân viên về các công cụ và phương pháp marketing hiện đại. Cần chương trình bao gồm Google Ads, Facebook Ads và content marketing.",
                                        "Tổ chức tư vấn đề xuất khóa đào tạo Business Analysis để nâng cao năng lực phân tích nghiệp vụ. Mong muốn có nội dung về requirement gathering, process modeling và stakeholder management.",
                                        "Doanh nghiệp phần mềm cần đào tạo project manager về Agile và Scrum methodology. Yêu cầu chương trình thực tế với workshop và simulation trong 30 ngày.",
                                        "Công ty outsourcing muốn đào tạo đội backend developer về Node.js và Express framework. Đề xuất chương trình 40 giờ với focus vào API development và database integration.",
                                        "Team frontend của công ty cần được đào tạo về Angular framework để modernize codebase. Mong muốn có chương trình từ cơ bản đến nâng cao với TypeScript và RxJS.",
                                        "DevOps team yêu cầu đào tạo về containerization và orchestration với Docker và Kubernetes. Cần chương trình hands-on với deployment strategies và monitoring.",
                                        "Đội database administrator muốn nâng cao kỹ năng thiết kế và tối ưu hóa cơ sở dữ liệu. Đề xuất chương trình bao gồm performance tuning, indexing và backup strategies.",
                                        "Công ty fintech cần đào tạo architect team về kiến trúc microservices và distributed systems. Mong muốn có chương trình sâu về service mesh, event-driven architecture.",
                                        "Team design muốn nâng cao kỹ năng UI/UX design và design thinking. Yêu cầu chương trình thực hành với user research, prototyping và usability testing.",
                                        "Startup blockchain đề xuất đào tạo developer về blockchain development và smart contracts. Cần chương trình bao gồm Solidity, Web3 và DeFi applications.",
                                        "IT department muốn đào tạo nhân viên về AWS cloud services và best practices. Đề xuất chương trình chuẩn bị cho AWS certification với hands-on labs.",
                                        "Game studio cần đào tạo developer về mobile game development với Unity engine. Mong muốn có chương trình từ game design đến monetization strategies.",
                                        "QA team yêu cầu đào tạo về test automation với Selenium và modern testing frameworks. Cần chương trình bao gồm CI/CD integration và performance testing.",
                                        "E-commerce company muốn đào tạo developer về xây dựng platform thương mại điện tử. Đề xuất chương trình bao gồm payment integration, inventory management và scalability.",
                                        "Backend team cần được đào tạo về API design và development best practices. Mong muốn có nội dung về RESTful API, GraphQL và API documentation.",
                                        "Data team yêu cầu đào tạo về data engineering với Apache Spark và Kafka. Cần chương trình hands-on về data pipeline, streaming và data lake architecture.",
                                        "Frontend developer muốn học Vue.js framework để diversify skill set. Đề xuất chương trình progressive từ Vue basics đến Vuex và Nuxt.js.",
                                        "Backend team cần đào tạo về GraphQL và Apollo để modernize API layer. Yêu cầu chương trình bao gồm schema design, resolver optimization và real-time subscriptions.");

                        List<String> unitTitles = Arrays.asList(
                                        "Giới thiệu và cài đặt môi trường",
                                        "Kiến thức cơ bản và nền tảng",
                                        "Thực hành và ứng dụng",
                                        "Dự án thực tế",
                                        "Kiểm tra và đánh giá",
                                        "Nâng cao và mở rộng",
                                        "Workshop và thảo luận",
                                        "Capstone Project",
                                        "Review và tổng kết",
                                        "Hướng dẫn triển khai thực tế");

                        List<TrainingProgramRequest> requests = new ArrayList<>();
                        List<TrainingProgram> programs = new ArrayList<>();
                        List<TrainingUnit> units = new ArrayList<>();

                        // Tạo nhiều TrainingProgramRequest cho mỗi PartnerOrganization
                        int titleIndex = 0;
                        for (PartnerOrganization partner : partners) {
                                // Mỗi PartnerOrganization sẽ có 2-4 TrainingProgramRequest
                                int requestCount = faker.random().nextInt(2, 4); // 2 đến 4 requests

                                for (int j = 0; j < requestCount; j++) {
                                        // Chọn title theo thứ tự, nếu hết thì lặp lại
                                        String title = requestTitles.get(titleIndex % requestTitles.size());
                                        String description = requestDescriptions
                                                        .get(titleIndex % requestDescriptions.size());
                                        titleIndex++;

                                        // Random status: 70% APPROVED, 20% PENDING, 10% REJECTED
                                        PendingStatus status;
                                        int randomStatus = faker.random().nextInt(100);
                                        if (randomStatus < 70) {
                                                status = PendingStatus.APPROVED;
                                        } else if (randomStatus < 90) {
                                                status = PendingStatus.PENDING;
                                        } else {
                                                status = PendingStatus.REJECTED;
                                        }

                                        TrainingProgramRequest request = TrainingProgramRequest.builder()
                                                        .partnerOrganization(partner)
                                                        .title(title)
                                                        .description(description)
                                                        .status(status)
                                                        .fileUrl("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                                                        .build();
                                        requests.add(request);
                                }
                        }

                        // Lưu requests trước
                        trainingProgramRequestRepository.saveAll(requests);
                        trainingProgramRequestRepository.flush();

                        // Dữ liệu cho Training Program (chương trình đào tạo thực tế)
                        List<String> programTitles = Arrays.asList(
                                        "Java Spring Boot Advanced Development",
                                        "Python Data Science Comprehensive Course",
                                        "React Native Mobile App Development",
                                        "AI & Machine Learning Professional Certificate",
                                        "DevOps & Cloud Computing Bootcamp",
                                        "Cybersecurity Specialist Program",
                                        "Full-stack Web Development Intensive",
                                        "Digital Marketing Professional Course",
                                        "Business Analysis Certification Program",
                                        "Agile Project Management with Scrum",
                                        "Node.js Backend Development Mastery",
                                        "Angular Frontend Framework Complete",
                                        "Docker & Kubernetes Container Orchestration",
                                        "Database Design & Performance Optimization",
                                        "Microservices Architecture & Design Patterns",
                                        "UI/UX Design Thinking Workshop",
                                        "Blockchain Development & Smart Contracts",
                                        "AWS Cloud Practitioner Certification",
                                        "Unity Mobile Game Development",
                                        "Software Testing Automation with Selenium",
                                        "E-commerce Platform Development",
                                        "RESTful API Design & GraphQL Implementation",
                                        "Big Data Engineering with Apache Spark",
                                        "Vue.js Progressive Web Applications",
                                        "GraphQL & Apollo Full-Stack Development");

                        List<String> programDescriptions = Arrays.asList(
                                        "Chương trình đào tạo chuyên sâu về Java Spring Boot framework, bao gồm Spring Security, Spring Data JPA, và microservices architecture với các dự án thực tế.",
                                        "Khóa học toàn diện về Data Science với Python, từ data analysis, visualization đến machine learning và deep learning với TensorFlow.",
                                        "Chương trình đào tạo phát triển ứng dụng mobile đa nền tảng với React Native, bao gồm navigation, state management và deployment.",
                                        "Khóa học chuyên nghiệp về AI và Machine Learning với ứng dụng thực tế trong computer vision, NLP và recommendation systems.",
                                        "Chương trình đào tạo DevOps và Cloud Computing với Docker, Kubernetes, CI/CD pipelines và AWS services.",
                                        "Khóa học chuyên gia an ninh mạng với network security, ethical hacking, incident response và security compliance.",
                                        "Chương trình đào tạo Full-stack từ Frontend (React/Vue) đến Backend (Node.js/Express) và database management.",
                                        "Khóa học Digital Marketing chuyên nghiệp với Google Ads, Facebook Marketing, SEO/SEM và analytics.",
                                        "Chương trình đào tạo Business Analyst với requirement gathering, process modeling và stakeholder management.",
                                        "Khóa học quản lý dự án với Agile và Scrum methodology, sprint planning và team collaboration.",
                                        "Chương trình đào tạo backend development với Node.js, Express.js, MongoDB và RESTful API design.",
                                        "Khóa học frontend framework Angular từ cơ bản đến nâng cao với TypeScript, RxJS và state management.",
                                        "Chương trình đào tạo containerization với Docker và orchestration với Kubernetes cho production deployment.",
                                        "Khóa học thiết kế và tối ưu hóa cơ sở dữ liệu với indexing, query optimization và performance tuning.",
                                        "Chương trình đào tạo kiến trúc microservices với service mesh, event-driven architecture và distributed systems.",
                                        "Khóa học thiết kế giao diện người dùng và trải nghiệm người dùng với design thinking và user research.",
                                        "Chương trình đào tạo phát triển ứng dụng blockchain với Solidity, Web3.js và smart contract development.",
                                        "Khóa học AWS cloud services và best practices với EC2, S3, Lambda và CloudFormation automation.",
                                        "Chương trình đào tạo phát triển game mobile với Unity engine, C# programming và game monetization.",
                                        "Khóa học automation testing với Selenium WebDriver, TestNG framework và CI/CD integration.",
                                        "Chương trình đào tạo xây dựng platform thương mại điện tử với payment gateway và inventory management.",
                                        "Khóa học thiết kế và phát triển RESTful API và GraphQL với authentication và rate limiting.",
                                        "Chương trình đào tạo data engineering với Apache Spark, Kafka streaming và data lake architecture.",
                                        "Khóa học Vue.js framework với Vuex state management, Vue Router và progressive web apps.",
                                        "Chương trình đào tạo GraphQL và Apollo Client/Server với real-time subscriptions và caching.");

                        // Dữ liệu chi tiết cho description field (gấp đôi shortDescription)
                        List<String> programDetailedDescriptions = Arrays.asList(
                                        "Chương trình đào tạo chuyên sâu về Java Spring Boot framework, bao gồm Spring Security, Spring Data JPA, và microservices architecture với các dự án thực tế. Khóa học được thiết kế cho các developer muốn nâng cao kỹ năng phát triển ứng dụng enterprise với Java. Bạn sẽ học cách xây dựng RESTful APIs, tích hợp với cơ sở dữ liệu, implement security layers, và deploy applications lên cloud platforms. Chương trình bao gồm hands-on projects với Spring Boot, Spring Security, Spring Data JPA, và các best practices trong việc xây dựng microservices scalable và maintainable.",
                                        "Khóa học toàn diện về Data Science với Python, từ data analysis, visualization đến machine learning và deep learning với TensorFlow. Chương trình cung cấp kiến thức từ cơ bản đến nâng cao về xử lý và phân tích dữ liệu. Bạn sẽ học cách sử dụng pandas, numpy, matplotlib, seaborn cho data manipulation và visualization. Khóa học cũng bao gồm machine learning algorithms, statistical analysis, và deep learning với TensorFlow/Keras. Các dự án thực tế sẽ giúp bạn áp dụng kiến thức vào các bài toán business intelligence và predictive analytics trong các ngành công nghiệp khác nhau.",
                                        "Chương trình đào tạo phát triển ứng dụng mobile đa nền tảng với React Native, bao gồm navigation, state management và deployment. Khóa học được thiết kế cho developers muốn xây dựng mobile apps chạy trên cả iOS và Android từ một codebase duy nhất. Bạn sẽ học cách setup development environment, tạo UI components, handle user interactions, integrate với APIs, và manage application state. Chương trình cũng bao gồm advanced topics như performance optimization, native modules integration, push notifications, và app store deployment process cho cả Apple App Store và Google Play Store.",
                                        "Khóa học chuyên nghiệp về AI và Machine Learning với ứng dụng thực tế trong computer vision, NLP và recommendation systems. Chương trình cung cấp kiến thức sâu rộng về artificial intelligence và machine learning algorithms. Bạn sẽ học các thuật toán supervised và unsupervised learning, neural networks, deep learning architectures. Khóa học bao gồm hands-on projects trong computer vision (image classification, object detection), natural language processing (sentiment analysis, chatbots), và recommendation systems. Các công cụ và frameworks được sử dụng bao gồm Python, TensorFlow, PyTorch, OpenCV, và NLTK cho các ứng dụng AI thực tế.",
                                        "Chương trình đào tạo DevOps và Cloud Computing với Docker, Kubernetes, CI/CD pipelines và AWS services. Khóa học được thiết kế cho IT professionals muốn nâng cao kỹ năng trong việc automation và cloud deployment. Bạn sẽ học cách containerize applications với Docker, orchestrate containers với Kubernetes, setup CI/CD pipelines với Jenkins/GitLab CI. Chương trình cũng bao gồm AWS services như EC2, S3, RDS, Lambda, và CloudFormation. Các chủ đề advanced như monitoring với Prometheus/Grafana, infrastructure as code, security best practices, và cost optimization cũng được đề cập trong khóa học này.",
                                        "Khóa học chuyên gia an ninh mạng với network security, ethical hacking, incident response và security compliance. Chương trình cung cấp kiến thức toàn diện về cybersecurity cho các IT professionals. Bạn sẽ học các techniques để protect networks và systems khỏi các cyber threats. Khóa học bao gồm penetration testing, vulnerability assessment, forensics, malware analysis, và incident response procedures. Các tools và frameworks như Metasploit, Wireshark, Nmap, Burp Suite sẽ được sử dụng trong hands-on labs. Chương trình cũng đề cập đến compliance frameworks như ISO 27001, NIST, và GDPR cho enterprise security management.",
                                        "Chương trình đào tạo Full-stack từ Frontend (React/Vue) đến Backend (Node.js/Express) và database management. Khóa học được thiết kế cho developers muốn trở thành full-stack engineers có thể handle cả client-side và server-side development. Bạn sẽ học cách xây dựng responsive web interfaces với React hoặc Vue.js, develop RESTful APIs với Node.js và Express. Chương trình bao gồm database design và management với SQL và NoSQL databases, authentication và authorization, testing strategies, và deployment techniques. Các modern development practices như Git workflow, code review, và agile methodologies cũng được tích hợp trong curriculum.",
                                        "Khóa học Digital Marketing chuyên nghiệp với Google Ads, Facebook Marketing, SEO/SEM và analytics. Chương trình cung cấp kiến thức comprehensive về digital marketing strategies và tactics trong thời đại số. Bạn sẽ học cách create và manage advertising campaigns trên các platforms như Google Ads, Facebook Ads Manager, LinkedIn Ads. Khóa học bao gồm search engine optimization (SEO), search engine marketing (SEM), content marketing, email marketing, và social media marketing. Các tools như Google Analytics, Google Search Console, SEMrush, và Hootsuite sẽ được sử dụng để track performance và optimize campaigns cho better ROI.",
                                        "Chương trình đào tạo Business Analyst với requirement gathering, process modeling và stakeholder management. Khóa học được thiết kế cho professionals muốn trở thành business analysts hiệu quả trong việc bridge gap giữa business và technical teams. Bạn sẽ học các techniques để gather và analyze business requirements, create process models và workflows, conduct stakeholder interviews và workshops. Chương trình bao gồm tools như Microsoft Visio, Lucidchart cho process mapping, và các methodologies như Agile, Waterfall cho project management. Advanced topics như data analysis, reporting, và change management cũng được đề cập để chuẩn bị cho real-world business analysis challenges.",
                                        "Khóa học quản lý dự án với Agile và Scrum methodology, sprint planning và team collaboration. Chương trình cung cấp kiến thức về project management best practices và agile frameworks. Bạn sẽ học cách plan, execute, và monitor projects sử dụng Agile và Scrum methodologies. Khóa học bao gồm sprint planning, daily standups, sprint reviews, retrospectives, và backlog management. Các tools như Jira, Trello, Asana sẽ được sử dụng cho project tracking và team collaboration. Chương trình cũng đề cập đến leadership skills, conflict resolution, risk management, và stakeholder communication để trở thành effective project managers trong agile environments.",
                                        "Chương trình đào tạo backend development với Node.js, Express.js, MongoDB và RESTful API design. Khóa học được thiết kế cho developers muốn specialize trong server-side development với JavaScript ecosystem. Bạn sẽ học cách build scalable backend applications với Node.js và Express framework, design và implement RESTful APIs, work với databases như MongoDB và PostgreSQL. Chương trình bao gồm authentication và authorization, error handling, logging, caching strategies, và API documentation với Swagger. Advanced topics như microservices architecture, message queues, real-time communications với WebSocket, và performance optimization cũng được covered trong khóa học này.",
                                        "Khóa học frontend framework Angular từ cơ bản đến nâng cao với TypeScript, RxJS và state management. Chương trình cung cấp kiến thức comprehensive về Angular framework cho modern web development. Bạn sẽ học cách build single-page applications (SPAs) với Angular, sử dụng TypeScript cho type-safe development, implement reactive programming với RxJS. Khóa học bao gồm component architecture, services và dependency injection, routing và navigation, forms handling, HTTP client, và state management với NgRx. Advanced concepts như lazy loading, performance optimization, testing với Jasmine/Karma, và deployment strategies cũng được đề cập để build production-ready Angular applications.",
                                        "Chương trình đào tạo containerization với Docker và orchestration với Kubernetes cho production deployment. Khóa học được thiết kế cho DevOps engineers và developers muốn master container technologies. Bạn sẽ học cách containerize applications với Docker, create efficient Docker images, manage container lifecycle. Chương trình bao gồm Kubernetes architecture, pods, services, deployments, ingress controllers, và persistent volumes. Advanced topics như Helm charts, service mesh với Istio, monitoring với Prometheus, logging với ELK stack, và security best practices cho containerized environments cũng được covered để ensure production-ready deployments.",
                                        "Khóa học thiết kế và tối ưu hóa cơ sở dữ liệu với indexing, query optimization và performance tuning. Chương trình cung cấp kiến thức sâu về database design và optimization techniques. Bạn sẽ học cách design efficient database schemas, create optimal indexes, write performant SQL queries, và analyze query execution plans. Khóa học bao gồm cả SQL và NoSQL databases, covering PostgreSQL, MySQL, MongoDB, và Redis. Advanced topics như database replication, sharding, backup và recovery strategies, transaction management, và concurrency control cũng được đề cập. Hands-on labs với real-world scenarios sẽ giúp bạn apply optimization techniques cho high-traffic applications.",
                                        "Chương trình đào tạo kiến trúc microservices với service mesh, event-driven architecture và distributed systems. Khóa học được thiết kế cho software architects và senior developers muốn build scalable distributed systems. Bạn sẽ học các principles của microservices architecture, service decomposition strategies, inter-service communication patterns. Chương trình bao gồm API gateway design, service discovery, circuit breakers, distributed tracing với Jaeger. Advanced concepts như event sourcing, CQRS, saga patterns, service mesh với Istio, và observability trong distributed systems cũng được covered. Practical workshops sẽ giúp bạn design và implement microservices architectures cho enterprise applications.",
                                        "Khóa học thiết kế giao diện người dùng và trải nghiệm người dùng với design thinking và user research. Chương trình cung cấp kiến thức comprehensive về UI/UX design process và methodologies. Bạn sẽ học cách conduct user research, create user personas, design user journeys và wireframes, prototype với tools như Figma, Adobe XD. Khóa học bao gồm design principles, color theory, typography, accessibility guidelines, và responsive design. Advanced topics như design systems, usability testing, A/B testing, và conversion optimization cũng được đề cập. Hands-on projects với real clients sẽ giúp bạn build portfolio và apply design thinking process trong practical scenarios.",
                                        "Chương trình đào tạo phát triển ứng dụng blockchain với Solidity, Web3.js và smart contract development. Khóa học được thiết kế cho developers muốn enter blockchain ecosystem và build decentralized applications (dApps). Bạn sẽ học blockchain fundamentals, cryptocurrency concepts, Ethereum ecosystem, và smart contract development với Solidity. Chương trình bao gồm Web3.js cho frontend integration, testing smart contracts với Truffle/Hardhat, deploy contracts lên testnets và mainnets. Advanced topics như DeFi protocols, NFTs, governance tokens, security best practices, và gas optimization cũng được covered. Practical projects sẽ include building DEXs, lending protocols, và NFT marketplaces.",
                                        "Khóa học AWS cloud services và best practices với EC2, S3, Lambda và CloudFormation automation. Chương trình cung cấp kiến thức comprehensive về Amazon Web Services cho cloud computing. Bạn sẽ học cách setup và manage EC2 instances, configure load balancers, use S3 cho storage solutions, develop serverless functions với Lambda. Khóa học bao gồm VPC networking, RDS database services, CloudFormation cho infrastructure as code, IAM cho security management. Advanced services như API Gateway, CloudWatch monitoring, CodePipeline cho CI/CD, và cost optimization strategies cũng được đề cập. Hands-on labs và real-world scenarios sẽ chuẩn bị bạn cho AWS certification exams.",
                                        "Chương trình đào tạo phát triển game mobile với Unity engine, C# programming và game monetization. Khóa học được thiết kế cho developers muốn enter game development industry và create mobile games. Bạn sẽ học Unity interface, C# programming for games, 2D và 3D game development workflows, physics systems, animation controllers. Chương trình bao gồm UI design cho games, audio integration, particle systems, performance optimization cho mobile platforms. Advanced topics như multiplayer networking, in-app purchases, ads integration, analytics tracking, và game monetization strategies cũng được covered. Final projects sẽ include publishing games lên Apple App Store và Google Play Store.",
                                        "Khóa học automation testing với Selenium WebDriver, TestNG framework và CI/CD integration. Chương trình cung cấp kiến thức về test automation best practices và tools. Bạn sẽ học cách write automated test scripts với Selenium WebDriver, create test suites với TestNG, implement Page Object Model design pattern. Khóa học bao gồm API testing với REST Assured, performance testing với JMeter, mobile testing với Appium. Advanced concepts như parallel test execution, test data management, reporting với Allure, và integration với CI/CD pipelines using Jenkins cũng được đề cập. Hands-on projects với real applications sẽ giúp bạn build comprehensive test automation frameworks.",
                                        "Chương trình đào tạo xây dựng platform thương mại điện tử với payment gateway và inventory management. Khóa học được thiết kế cho developers muốn build full-featured e-commerce solutions. Bạn sẽ học cách develop product catalogs, shopping carts, user authentication systems, order management workflows. Chương trình bao gồm payment gateway integrations với Stripe, PayPal, inventory tracking systems, recommendation engines, search functionality với Elasticsearch. Advanced features như multi-vendor marketplaces, subscription models, promotional campaigns, analytics dashboards, và mobile-responsive designs cũng được covered. Real-world projects sẽ include building scalable e-commerce platforms với modern technologies.",
                                        "Khóa học thiết kế và phát triển RESTful API và GraphQL với authentication và rate limiting. Chương trình cung cấp kiến thức về API design principles và implementation strategies. Bạn sẽ học cách design RESTful APIs following OpenAPI specifications, implement GraphQL schemas và resolvers, handle authentication với JWT tokens, OAuth2 flows. Khóa học bao gồm API versioning strategies, rate limiting implementations, caching mechanisms, error handling patterns. Advanced topics như API gateways, microservices communication, webhook implementations, monitoring với API analytics, và security best practices cũng được đề cập. Practical workshops sẽ include building APIs cho real-world applications với comprehensive documentation.",
                                        "Chương trình đào tạo data engineering với Apache Spark, Kafka streaming và data lake architecture. Khóa học được thiết kế cho data engineers muốn build scalable data processing systems. Bạn sẽ học cách process large datasets với Apache Spark, implement real-time data streaming với Kafka, design data lake architectures với AWS S3, Azure Data Lake. Chương trình bao gồm ETL/ELT pipelines, data quality monitoring, schema evolution, partitioning strategies. Advanced concepts như Delta Lake for ACID transactions, Apache Airflow cho workflow orchestration, data lineage tracking, và data governance frameworks cũng được covered. Hands-on projects sẽ include building end-to-end data platforms cho analytics và machine learning use cases.",
                                        "Khóa học Vue.js framework với Vuex state management, Vue Router và progressive web apps. Chương trình cung cấp kiến thức comprehensive về Vue.js ecosystem cho modern web development. Bạn sẽ học Vue.js fundamentals, component development, directives và filters, Vue CLI tooling. Khóa học bao gồm Vuex cho centralized state management, Vue Router cho single-page application routing, Nuxt.js cho server-side rendering. Advanced topics như composition API, TypeScript integration, testing với Vue Test Utils, PWA features như service workers, offline functionality, và performance optimization cũng được đề cập. Real-world projects sẽ include building responsive web applications với modern Vue.js stack.",
                                        "Chương trình đào tạo GraphQL và Apollo Client/Server với real-time subscriptions và caching. Khóa học được thiết kế cho developers muốn master GraphQL ecosystem cho efficient data fetching. Bạn sẽ học GraphQL schema design, resolvers implementation, Apollo Server setup, Apollo Client integration. Chương trình bao gồm real-time subscriptions với WebSocket connections, intelligent caching strategies, error handling patterns, authentication và authorization. Advanced concepts như schema federation, dataloader patterns, performance monitoring với Apollo Studio, security best practices, và integration với existing REST APIs cũng được covered. Practical workshops sẽ include building full-stack applications với GraphQL APIs và modern frontend frameworks.");

                        // Danh sách banner URLs thực tế từ Unsplash
                        List<String> bannerUrls = Arrays.asList(
                                        "https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=1200&h=600&fit=crop", // Java/Programming
                                        "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=1200&h=600&fit=crop", // Data
                                                                                                                           // Science
                                        "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=1200&h=600&fit=crop", // Mobile
                                                                                                                              // Development
                                        "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=1200&h=600&fit=crop", // AI/ML
                                        "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=1200&h=600&fit=crop", // DevOps/Cloud
                                        "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=1200&h=600&fit=crop", // Cybersecurity
                                        "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=1200&h=600&fit=crop", // Web
                                                                                                                              // Development
                                        "https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?w=1200&h=600&fit=crop", // Digital
                                                                                                                              // Marketing
                                        "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=1200&h=600&fit=crop", // Business
                                                                                                                              // Analysis
                                        "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=1200&h=600&fit=crop" // Project
                                                                                                                          // Management
                        );

                        // Danh sách PDF URLs thực tế
                        List<String> pdfUrls = Arrays.asList(
                                        "https://www.oracle.com/java/technologies/downloads/",
                                        "https://docs.python.org/3/tutorial/",
                                        "https://reactnative.dev/docs/getting-started",
                                        "https://www.tensorflow.org/tutorials",
                                        "https://docs.docker.com/get-started/",
                                        "https://owasp.org/www-project-top-ten/",
                                        "https://developer.mozilla.org/en-US/docs/Web",
                                        "https://support.google.com/google-ads/",
                                        "https://www.iiba.org/business-analysis-body-of-knowledge/",
                                        "https://scrumguides.org/scrum-guide.html");

                        // Danh sách địa chỉ thực tế cho OFFLINE
                        List<String> offlineAddresses = Arrays.asList(
                                        "Tầng 10, Tòa nhà FPT, 17 Duy Tân, Cầu Giấy, Hà Nội",
                                        "Lầu 5, 590 Cách Mạng Tháng 8, Phường 11, Quận 3, TP.HCM",
                                        "Tầng 8, Landmark 72, Phạm Hùng, Nam Từ Liêm, Hà Nội",
                                        "Lầu 12, Vietcombank Tower, 5 Công Trường Mê Linh, Quận 1, TP.HCM",
                                        "Tầng 6, Tòa nhà CMC, Duy Tân, Cầu Giấy, Hà Nội",
                                        "Lầu 15, Bitexco Financial Tower, 2 Hải Triều, Quận 1, TP.HCM",
                                        "Tầng 20, Lotte Center Hanoi, 54 Liễu Giai, Ba Đình, Hà Nối",
                                        "Lầu 9, Saigon Trade Center, 37 Tôn Đức Thắng, Quận 1, TP.HCM",
                                        "Tầng 14, Keangnam Landmark, Phạm Hùng, Nam Từ Liêm, Hà Nội",
                                        "Lầu 7, Diamond Plaza, 34 Lê Duẩn, Quận 1, TP.HCM");

                        // Danh sách Zoom links
                        List<String> zoomLinks = Arrays.asList(
                                        "https://us02web.zoom.us/j/85432167890?pwd=abc123",
                                        "https://us04web.zoom.us/j/91234567890?pwd=def456",
                                        "https://us06web.zoom.us/j/78901234567?pwd=ghi789",
                                        "https://us02web.zoom.us/j/12345678901?pwd=jkl012",
                                        "https://us04web.zoom.us/j/34567890123?pwd=mno345",
                                        "https://us06web.zoom.us/j/56789012345?pwd=pqr678",
                                        "https://us02web.zoom.us/j/67890123456?pwd=stu901",
                                        "https://us04web.zoom.us/j/89012345678?pwd=vwx234",
                                        "https://us06web.zoom.us/j/23456789012?pwd=yz1567",
                                        "https://us02web.zoom.us/j/45678901234?pwd=abc890");

                        // Danh sách các tags cho training programs (ưu tiên từ ngắn và viết tắt)
                        List<String> availableTags = Arrays.asList(
                                        "Java", "Python", "React", "Angular", "Vue", "Node.js", "Spring",
                                        "AI", "ML", "DL", "NLP", "CV", "BigData", "DataScience",
                                        "AWS", "Azure", "GCP", "Docker", "K8s", "DevOps", "CI/CD",
                                        "API", "REST", "GraphQL", "Microservices", "Blockchain", "DeFi",
                                        "Mobile", "iOS", "Android", "Flutter", "ReactNative", "Unity",
                                        "UI/UX", "Frontend", "Backend", "Fullstack", "Database", "SQL",
                                        "Security", "CEH", "CISSP", "Pentest", "Ethical", "Cyber",
                                        "Agile", "Scrum", "PMP", "BA", "PM", "Marketing", "SEO", "SEM",
                                        "Enterprise", "Certification", "Hands-on", "Advanced", "Beginner");

                        // Helper method để tạo random tags
                        java.util.function.Supplier<Set<String>> generateRandomTags = () -> {
                                Set<String> tags = new HashSet<>();
                                int numTags = faker.random().nextInt(2, 5); // 2-4 tags
                                while (tags.size() < numTags) {
                                        String randomTag = availableTags
                                                        .get(faker.random().nextInt(availableTags.size()));
                                        tags.add(randomTag);
                                }
                                return tags;
                        };

                        // Tạo Training Programs cho các requests đã APPROVED
                        int programCounter = 0;
                        for (int i = 0; i < requests.size(); i++) {
                                TrainingProgramRequest request = requests.get(i);
                                if (request.getStatus() == PendingStatus.APPROVED) {
                                        User user = users.get(faker.random().nextInt(users.size()));
                                        LocalDate startDate = LocalDate.now().plusDays(faker.random().nextInt(30, 90));
                                        LocalDate endDate = startDate.plusDays(faker.random().nextInt(30, 120));

                                        // Chọn mode ngẫu nhiên
                                        TrainingProgramMode mode = TrainingProgramMode.values()[faker.random()
                                                        .nextInt(TrainingProgramMode.values().length)];

                                        // Xác định classroomLink dựa trên mode
                                        String classroomLink;
                                        switch (mode) {
                                                case ONLINE:
                                                        classroomLink = zoomLinks.get(i % zoomLinks.size());
                                                        break;
                                                case OFFLINE:
                                                        classroomLink = offlineAddresses
                                                                        .get(i % offlineAddresses.size());
                                                        break;
                                                case HYBRID:
                                                        classroomLink = "Sẽ có thông báo sau";
                                                        break;
                                                default:
                                                        classroomLink = "Sẽ có thông báo sau";
                                        }

                                        // 80% programs có status PUBLISHED
                                        TrainingProgramStatus status;
                                        if (faker.random().nextInt(100) < 80) { // 80% PUBLISHED
                                                status = TrainingProgramStatus.PUBLISHED;
                                        } else {
                                                // 20% còn lại sử dụng các status khác
                                                TrainingProgramStatus[] otherStatuses = {
                                                                TrainingProgramStatus.REVIEW,
                                                                TrainingProgramStatus.UNLISTED,
                                                                TrainingProgramStatus.ARCHIVED
                                                };
                                                status = otherStatuses[faker.random().nextInt(otherStatuses.length)];
                                        }

                                        TrainingProgram program = TrainingProgram.builder()
                                                        .trainingProgramId(String.format("KH-%03d", programCounter + 1))
                                                        .trainingProgramRequest(request)

                                                        .user(user)
                                                        .partnerOrganization(request.getPartnerOrganization())
                                                        .programStatus(status)
                                                        .programMode(mode)
                                                        .programType(TrainingProgramType.values()[faker.random()
                                                                        .nextInt(TrainingProgramType.values().length)])
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .durationHours(faker.random().nextInt(40, 200))
                                                        .durationSessions(faker.random().nextInt(10, 40))
                                                        .scheduleDetail("Thứ 2, 4, 6 từ 19h00-21h30")
                                                        .programLevel(TrainingProgramLevel.values()[faker.random()
                                                                        .nextInt(TrainingProgramLevel.values().length)])
                                                        .maxStudents(faker.random().nextInt(20, 50))
                                                        .minStudents(faker.random().nextInt(10, 20))
                                                        .openingCondition("Đủ số lượng học viên tối thiểu")
                                                        .equipmentRequirement("Laptop, Internet ổn định")
                                                        .classroomLink(classroomLink)
                                                        .targetAudience("Developers, Students, IT Professionals")
                                                        .requirements("Kiến thức cơ bản về lập trình")
                                                        .scale("Enterprise")
                                                        .listedPrice(new BigDecimal(
                                                                        faker.random().nextInt(2000000, 8000000)))
                                                        .internalPrice(new BigDecimal(
                                                                        faker.random().nextInt(1500000, 6000000)))
                                                        .publicPrice(new BigDecimal(
                                                                        faker.random().nextInt(2500000, 10000000)))
                                                        .priceVisible(faker.bool().bool())
                                                        .bannerUrl(bannerUrls.get(i % bannerUrls.size()))
                                                        .contentUrl(pdfUrls.get(i % pdfUrls.size()))
                                                        .syllabusFileUrl(
                                                                        "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                                                        .tags(generateRandomTags.get())

                                                        .learningOutcomes(
                                                                        "Thành thạo framework, Xây dựng được ứng dụng thực tế")
                                                        .completionCertificateType("Certificate of Completion")
                                                        .certificateIssuer(request.getPartnerOrganization()
                                                                        .getOrganizationName())
                                                        .title(programTitles.get(programCounter % programTitles.size()))
                                                        .subTitle("Professional Training Program")
                                                        .shortDescription(programDescriptions.get(
                                                                        programCounter % programDescriptions.size()))
                                                        .description(programDetailedDescriptions.get(programCounter
                                                                        % programDetailedDescriptions.size()))
                                                        .learningObjectives("Nắm vững kiến thức và kỹ năng chuyên môn")
                                                        .trialVideoUrl(trialVideoUrl)
                                                        .rating(4.0 + faker.random().nextDouble() * 1.0) // 4.0 - 5.0
                                                        .build();
                                        programs.add(program);
                                        programCounter++;
                                }
                        }

                        // Tạo thêm một số Training Programs không có TrainingProgramRequest (tạo trực
                        // tiếp)
                        List<String> directProgramTitles = Arrays.asList(
                                        "Khóa học Blockchain Development cơ bản",
                                        "Chương trình đào tạo UI/UX Design chuyên nghiệp",
                                        "Khóa học Cloud Architecture với Azure",
                                        "Chương trình đào tạo Mobile Game Development",
                                        "Khóa học Data Engineering với Apache Spark");

                        List<String> directProgramDescriptions = Arrays.asList(
                                        "Khóa học Blockchain cơ bản từ lý thuyết đến thực hành với Solidity",
                                        "Chương trình đào tạo UI/UX Design từ cơ bản đến nâng cao",
                                        "Khóa học thiết kế kiến trúc đám mây với Microsoft Azure",
                                        "Chương trình đào tạo phát triển game mobile với Unity",
                                        "Khóa học xây dựng pipeline dữ liệu với Apache Spark");

                        List<String> directProgramDetailedDescriptions = Arrays.asList(
                                        "Khóa học Blockchain cơ bản từ lý thuyết đến thực hành với Solidity, smart contracts và DeFi applications. Chương trình được thiết kế cho developers muốn enter blockchain ecosystem và understand decentralized technologies. Bạn sẽ học blockchain fundamentals, cryptocurrency mechanics, Ethereum platform, và Solidity programming language. Khóa học bao gồm smart contract development, testing với Truffle framework, deployment strategies, và security best practices. Advanced topics như DeFi protocols, yield farming, liquidity pools, NFT development, và cross-chain technologies cũng được đề cập để chuẩn bị cho career trong blockchain industry.",
                                        "Chương trình đào tạo UI/UX Design từ cơ bản đến nâng cao với design thinking methodology và user-centered design approaches. Khóa học cung cấp kiến thức comprehensive về design process từ user research đến final product delivery. Bạn sẽ học cách conduct user interviews, create personas và user journeys, design wireframes và prototypes, perform usability testing. Chương trình bao gồm design tools như Figma, Adobe Creative Suite, Sketch, InVision cho collaborative design workflows. Advanced concepts như design systems, accessibility guidelines, conversion optimization, micro-interactions, và design leadership cũng được covered để build successful design career.",
                                        "Khóa học thiết kế kiến trúc đám mây với Microsoft Azure, covering cloud-native architectures và enterprise solutions. Chương trình được thiết kế cho solution architects và cloud engineers muốn master Azure ecosystem. Bạn sẽ học Azure services architecture, compute options như Virtual Machines, App Services, Container Instances, và Azure Kubernetes Service. Khóa học bao gồm storage solutions, networking configurations, security implementations, monitoring và governance frameworks. Advanced topics như multi-region deployments, disaster recovery planning, cost optimization strategies, Azure DevOps integration, và hybrid cloud architectures cũng được đề cập cho enterprise-grade solutions.",
                                        "Chương trình đào tạo phát triển game mobile với Unity engine, C# programming và game design principles. Khóa học được thiết kế cho aspiring game developers muốn create engaging mobile games cho iOS và Android platforms. Bạn sẽ học Unity interface, C# scripting, 2D/3D game development pipelines, physics systems, animation workflows. Chương trình bao gồm game design fundamentals, player psychology, monetization strategies, analytics integration, performance optimization techniques. Advanced features như multiplayer networking, in-app purchases, social features integration, AR/VR capabilities, và live operations cũng được covered để build successful mobile games với high user engagement.",
                                        "Khóa học xây dựng pipeline dữ liệu với Apache Spark, Kafka streaming và modern data engineering practices. Chương trình cung cấp kiến thức về big data processing và real-time analytics systems. Bạn sẽ học Spark architecture, RDD/DataFrame operations, Spark SQL, streaming applications với Structured Streaming. Khóa học bao gồm Kafka ecosystem cho event-driven architectures, Delta Lake cho ACID transactions, data quality frameworks, schema evolution strategies. Advanced topics như machine learning pipelines với MLflow, data governance implementations, performance tuning, cloud-native data platforms với AWS/Azure/GCP, và DataOps practices cũng được đề cập cho enterprise data engineering roles.");

                        // Tạo 5 Training Programs trực tiếp (không có request)
                        for (int i = 0; i < 5; i++) {
                                User user = users.get(faker.random().nextInt(users.size()));
                                // Không có PartnerOrganization cho các programs tạo trực tiếp
                                LocalDate startDate = LocalDate.now().plusDays(faker.random().nextInt(30, 90));
                                LocalDate endDate = startDate.plusDays(faker.random().nextInt(30, 120));

                                // Chọn mode ngẫu nhiên
                                TrainingProgramMode mode = TrainingProgramMode.values()[faker.random()
                                                .nextInt(TrainingProgramMode.values().length)];

                                // Xác định classroomLink dựa trên mode
                                String classroomLink;
                                switch (mode) {
                                        case ONLINE:
                                                classroomLink = zoomLinks.get(i % zoomLinks.size());
                                                break;
                                        case OFFLINE:
                                                classroomLink = offlineAddresses.get(i % offlineAddresses.size());
                                                break;
                                        case HYBRID:
                                                classroomLink = "Sẽ có thông báo sau";
                                                break;
                                        default:
                                                classroomLink = "Sẽ có thông báo sau";
                                }

                                // 80% của tổng 12 programs (7 + 5) = 9-10 programs sẽ là PUBLISHED
                                // Đã có 7 programs PUBLISHED từ phần trên, cần thêm 2-3 programs nữa
                                TrainingProgramStatus status;
                                if (programCounter < 10) { // Tổng cộng 10 out of 12 programs (83.3%) sẽ là PUBLISHED
                                        status = TrainingProgramStatus.PUBLISHED;
                                } else {
                                        // 2 programs còn lại sử dụng các status khác
                                        TrainingProgramStatus[] otherStatuses = {
                                                        TrainingProgramStatus.REVIEW,
                                                        TrainingProgramStatus.UNLISTED,
                                                        TrainingProgramStatus.ARCHIVED
                                        };
                                        status = otherStatuses[faker.random().nextInt(otherStatuses.length)];
                                }

                                TrainingProgram program = TrainingProgram.builder()
                                                .trainingProgramId(String.format("KH-%03d", programCounter + 1))
                                                .trainingProgramRequest(null) // Không có request
                                                .user(user)
                                                .partnerOrganization(null) // Không có PartnerOrganization cho programs
                                                                           // tạo trực tiếp
                                                .programStatus(status)
                                                .programMode(mode)
                                                .programType(TrainingProgramType.values()[faker.random()
                                                                .nextInt(TrainingProgramType.values().length)])
                                                .startDate(startDate)
                                                .endDate(endDate)
                                                .durationHours(faker.random().nextInt(40, 200))
                                                .durationSessions(faker.random().nextInt(10, 40))
                                                .scheduleDetail("Thứ 2, 4, 6 từ 19h00-21h30")
                                                .programLevel(TrainingProgramLevel.values()[faker.random()
                                                                .nextInt(TrainingProgramLevel.values().length)])
                                                .maxStudents(faker.random().nextInt(20, 50))
                                                .minStudents(faker.random().nextInt(10, 20))
                                                .openingCondition("Đủ số lượng học viên tối thiểu")
                                                .equipmentRequirement("Laptop, Internet ổn định")
                                                .classroomLink(classroomLink)
                                                .targetAudience("Developers, Students, IT Professionals")
                                                .requirements("Kiến thức cơ bản về lập trình")
                                                .scale("Enterprise")
                                                .listedPrice(new BigDecimal(faker.random().nextInt(2000000, 8000000)))
                                                .internalPrice(new BigDecimal(faker.random().nextInt(1500000, 6000000)))
                                                .publicPrice(new BigDecimal(faker.random().nextInt(2500000, 10000000)))
                                                .priceVisible(faker.bool().bool())
                                                .bannerUrl(bannerUrls.get((i + 5) % bannerUrls.size())) // Sử dụng
                                                                                                        // banner khác
                                                .contentUrl(pdfUrls.get((i + 5) % pdfUrls.size())) // Sử dụng PDF khác
                                                .syllabusFileUrl(
                                                                "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                                                .tags(generateRandomTags.get())

                                                .learningOutcomes("Thành thạo công nghệ mới, Áp dụng vào dự án thực tế")
                                                .completionCertificateType("Professional Certificate")
                                                .certificateIssuer("EduHubVN") // Certificate issuer cho programs tạo
                                                                               // trực tiếp
                                                .title(directProgramTitles.get(i))
                                                .subTitle("Advanced Professional Training")
                                                .shortDescription(directProgramDescriptions.get(i))
                                                .description(directProgramDetailedDescriptions.get(i))
                                                .learningObjectives("Nắm vững công nghệ tiên tiến và kỹ năng thực tế")
                                                .rating(4.0 + faker.random().nextDouble() * 1.0) // 4.0 - 5.0
                                                .trialVideoUrl(trialVideoUrl)
                                                .build();
                                programs.add(program);
                                programCounter++;
                        }

                        // Lưu programs trước
                        trainingProgramRepository.saveAll(programs);
                        trainingProgramRepository.flush();

                        // Tạo Training Units cho mỗi program
                        for (TrainingProgram program : programs) {
                                int numUnits = faker.random().nextInt(3, 8); // Mỗi program có 3-7 units
                                for (int j = 0; j < numUnits; j++) {
                                        Lecturer lecturer = lecturers.get(faker.random().nextInt(lecturers.size()));

                                        TrainingUnit unit = TrainingUnit.builder()
                                                        .trainingProgram(program)
                                                        .lecturer(lecturer)
                                                        .title(unitTitles
                                                                        .get(faker.random().nextInt(unitTitles.size())))
                                                        .description("Chi tiết nội dung cho " + unitTitles
                                                                        .get(faker.random().nextInt(unitTitles.size())))
                                                        .durationSection(faker.random().nextInt(2, 6)) // 2-5 tiếng
                                                        .orderSection(j + 1)
                                                        .lead(j == 0) // Unit đầu tiên là lead
                                                        .trialVideoUrl(trialVideoUrl)
                                                        .build();
                                        units.add(unit);
                                }
                        }

                        // Lưu units
                        trainingUnitRepository.saveAll(units);
                        trainingUnitRepository.flush();

                        System.out.println("✅ Đã tạo thành công dữ liệu mẫu cho Training Programs:");
                        long approvedRequests = requests.stream().filter(r -> r.getStatus() == PendingStatus.APPROVED)
                                        .count();
                        long pendingRequests = requests.stream().filter(r -> r.getStatus() == PendingStatus.PENDING)
                                        .count();
                        long rejectedRequests = requests.stream().filter(r -> r.getStatus() == PendingStatus.REJECTED)
                                        .count();
                        System.out.println("- " + requests.size() + " Training Program Requests (" +
                                        approvedRequests + " APPROVED, " +
                                        pendingRequests + " PENDING, " +
                                        rejectedRequests + " REJECTED)");
                        System.out.println("- Mỗi PartnerOrganization (approved) có 2-4 TrainingProgramRequest");
                        long programsFromRequests = programs.stream().filter(p -> p.getTrainingProgramRequest() != null)
                                        .count();
                        long programsWithoutRequests = programs.stream()
                                        .filter(p -> p.getTrainingProgramRequest() == null).count();
                        System.out.println("- " + programs.size() + " Training Programs (" + programsFromRequests
                                        + " từ requests, " + programsWithoutRequests + " tạo trực tiếp)");
                        System.out.println("- " + units.size() + " Training Units (3-7 units per program)");

                } catch (Exception e) {
                        System.err.println("Lỗi khi tạo dữ liệu mẫu cho Training Programs: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        /**
         * Tạo dữ liệu mẫu cho Notification
         * Mỗi user sẽ có 2-3 notifications
         */
        private void createSampleNotificationData(
                        NotificationRepository notificationRepository,
                        UserRepository userRepository,
                        Faker faker) {

                try {
                        System.out.println("\n===== TẠO DỮ LIỆU MẪU CHO NOTIFICATION =====");

                        List<User> users = userRepository.findAll();
                        if (users.isEmpty()) {
                                System.out.println("Không có user nào để tạo notification!");
                                return;
                        }

                        List<String> notificationTitles = Arrays.asList(
                                        "Chào mừng bạn đến với EduHub",
                                        "Thông báo cập nhật hệ thống",
                                        "Đơn đăng ký của bạn đã được duyệt",
                                        "Đơn đăng ký của bạn cần chỉnh sửa",
                                        "Đơn đăng ký của bạn đã bị từ chối",
                                        "Có dự án mới phù hợp với bạn",
                                        "Có khóa đào tạo mới",
                                        "Nhắc nhở: Cập nhật hồ sơ giảng viên",
                                        "Nhắc nhở: Hoàn thiện thông tin",
                                        "Bạn có một cuộc phỏng vấn sắp tới",
                                        "Kết quả phỏng vấn đã có",
                                        "Hợp đồng đang chờ xác nhận",
                                        "Hợp đồng đã được ký kết",
                                        "Thông báo về chương trình đào tạo",
                                        "Cập nhật mới từ tổ chức đối tác",
                                        "Thông báo từ trường đại học",
                                        "Có yêu cầu đào tạo mới",
                                        "Chương trình đào tạo sắp bắt đầu",
                                        "Nhắc nhở: Nộp báo cáo nghiên cứu",
                                        "Chứng chỉ của bạn đã được xác nhận",
                                        "Bằng cấp cần được cập nhật",
                                        "Thông báo về khóa học đã tham gia",
                                        "Thông báo về khóa học đang giảng dạy",
                                        "Dự án nghiên cứu đã được phê duyệt",
                                        "Thông báo bảo trì hệ thống");

                        List<String> notificationMessages = Arrays.asList(
                                        "Chào mừng bạn đến với hệ thống quản lý giáo dục EduHub. Hãy hoàn thiện hồ sơ của bạn để trải nghiệm đầy đủ các tính năng.",
                                        "Hệ thống sẽ được cập nhật vào lúc 02:00 sáng ngày mai. Trong thời gian này, một số tính năng có thể bị gián đoạn.",
                                        "Chúc mừng! Đơn đăng ký của bạn đã được phê duyệt. Bạn có thể bắt đầu sử dụng các tính năng của hệ thống.",
                                        "Đơn đăng ký của bạn cần một số chỉnh sửa. Vui lòng kiểm tra và cập nhật lại thông tin theo yêu cầu.",
                                        "Rất tiếc, đơn đăng ký của bạn đã bị từ chối do không đáp ứng đủ các yêu cầu. Vui lòng xem lý do cụ thể và nộp đơn mới.",
                                        "Có một dự án mới phù hợp với chuyên môn của bạn. Hãy xem chi tiết và nộp đơn ứng tuyển ngay!",
                                        "Một khóa đào tạo mới vừa được thêm vào hệ thống. Hãy đăng ký tham gia để nâng cao kỹ năng của bạn.",
                                        "Hồ sơ giảng viên của bạn chưa được cập nhật trong 6 tháng. Vui lòng cập nhật thông tin mới nhất.",
                                        "Hồ sơ của bạn còn thiếu một số thông tin quan trọng. Hãy hoàn thiện để tăng cơ hội được chọn cho các dự án.",
                                        "Bạn có một cuộc phỏng vấn vào lúc 10:00 ngày 15/11/2025. Vui lòng chuẩn bị và tham gia đúng giờ.",
                                        "Kết quả phỏng vấn của bạn đã có. Hãy đăng nhập vào hệ thống để xem chi tiết.",
                                        "Bạn có một hợp đồng đang chờ xác nhận. Vui lòng xem xét và ký kết trong vòng 3 ngày.",
                                        "Hợp đồng của bạn đã được ký kết thành công. Bạn có thể xem chi tiết trong mục Hợp đồng.",
                                        "Có cập nhật mới về chương trình đào tạo mà bạn đang tham gia. Hãy kiểm tra thông tin chi tiết.",
                                        "Tổ chức đối tác của bạn vừa cập nhật thông tin. Hãy xem các thay đổi trong hồ sơ tổ chức.",
                                        "Trường đại học đã gửi thông báo mới. Vui lòng kiểm tra email hoặc thông báo trong hệ thống.",
                                        "Có một yêu cầu đào tạo mới từ tổ chức đối tác. Hãy xem xét và đề xuất chương trình phù hợp.",
                                        "Chương trình đào tạo bạn đăng ký sẽ bắt đầu vào tuần tới. Hãy chuẩn bị tài liệu cần thiết.",
                                        "Nhắc nhở: Báo cáo nghiên cứu của bạn sẽ đến hạn vào cuối tháng này. Vui lòng nộp đúng thời hạn.",
                                        "Chứng chỉ bạn nộp đã được xác nhận và thêm vào hồ sơ. Cảm ơn bạn đã cập nhật thông tin.",
                                        "Bằng cấp của bạn sắp hết hạn hoặc cần được cập nhật. Vui lòng nộp bản sao mới nhất.",
                                        "Có cập nhật mới về khóa học bạn đã tham gia. Hãy xem thông tin chi tiết trong mục Khóa học.",
                                        "Khóa học bạn đang giảng dạy có cập nhật về lịch học hoặc tài liệu. Vui lòng kiểm tra.",
                                        "Dự án nghiên cứu của bạn đã được phê duyệt. Hãy bắt đầu thực hiện theo kế hoạch đã đề ra.",
                                        "Hệ thống sẽ được bảo trì vào cuối tuần này. Vui lòng lưu công việc và đăng xuất trước thời gian bảo trì.");

                        List<Notification> notifications = new ArrayList<>();
                        LocalDateTime now = LocalDateTime.now();

                        // Tạo 7 notifications cho mỗi user
                        for (User user : users) {
                                int notificationCount = 7; // Cố định 7 notifications

                                for (int i = 0; i < notificationCount; i++) {
                                        int randomIndex = faker.random().nextInt(notificationTitles.size());

                                        // Tạo thời gian ngẫu nhiên trong khoảng 30 ngày gần đây
                                        // Notification cũ hơn sẽ được tạo trước
                                        int daysAgo = faker.random().nextInt(30); // 0-30 ngày trước
                                        int hoursAgo = faker.random().nextInt(24); // 0-23 giờ
                                        int minutesAgo = faker.random().nextInt(60); // 0-59 phút

                                        LocalDateTime createdAt = now.minusDays(daysAgo)
                                                        .minusHours(hoursAgo)
                                                        .minusMinutes(minutesAgo);

                                        // updatedAt sẽ sau createdAt một chút (0-2 giờ)
                                        LocalDateTime updatedAt = createdAt.plusMinutes(faker.random().nextInt(120));

                                        // Nếu updatedAt vượt quá thời gian hiện tại, set lại bằng now
                                        if (updatedAt.isAfter(now)) {
                                                updatedAt = now;
                                        }

                                        // Notification cũ hơn có xu hướng đã được đọc nhiều hơn
                                        boolean isRead = daysAgo > 7 ? faker.random().nextBoolean()
                                                        : (daysAgo > 3 ? faker.random().nextInt(10) > 3
                                                                        : faker.random().nextInt(10) > 6);

                                        Notification notification = Notification.builder()
                                                        .title(notificationTitles.get(randomIndex))
                                                        .message(notificationMessages.get(randomIndex))
                                                        .read(isRead)
                                                        .createdAt(createdAt)
                                                        .updatedAt(updatedAt)
                                                        .user(user)
                                                        .build();

                                        notifications.add(notification);
                                }
                        }

                        // Lưu tất cả notifications
                        notificationRepository.saveAll(notifications);
                        notificationRepository.flush();

                        // In ra thống kê
                        System.out.println("✓ Tạo thành công " + notifications.size() + " notifications cho "
                                        + users.size() + " users");
                        long readNotifications = notifications.stream().filter(Notification::isRead).count();
                        long unreadNotifications = notifications.size() - readNotifications;
                        System.out.println("  - " + readNotifications + " notifications đã đọc");
                        System.out.println("  - " + unreadNotifications + " notifications chưa đọc");

                        // Thống kê theo thời gian
                        long recentNotifications = notifications.stream()
                                        .filter(n -> n.getCreatedAt().isAfter(now.minusDays(7)))
                                        .count();
                        System.out.println("  - " + recentNotifications + " notifications trong 7 ngày gần đây");

                } catch (Exception e) {
                        System.err.println("Lỗi khi tạo dữ liệu mẫu cho Notification: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
