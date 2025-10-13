package com.example.eduhubvn;

import static io.micrometer.common.util.StringUtils.truncate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.eduhubvn.dtos.auth.RegisterRequest;
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
import com.example.eduhubvn.entities.TrainingProgramMode;
import com.example.eduhubvn.entities.TrainingProgramRequest;
import com.example.eduhubvn.entities.TrainingProgramStatus;
import com.example.eduhubvn.entities.TrainingProgramType;
import com.example.eduhubvn.entities.TrainingUnit;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.ApplicationModuleRepository;
import com.example.eduhubvn.repositories.ApplicationRepository;
import com.example.eduhubvn.repositories.AttendedTrainingCourseRepository;
import com.example.eduhubvn.repositories.AttendedTrainingCourseUpdateRepository;
import com.example.eduhubvn.repositories.CertificationRepository;
import com.example.eduhubvn.repositories.CertificationUpdateRepository;
import com.example.eduhubvn.repositories.ContractRepository;
import com.example.eduhubvn.repositories.CourseInfoRepository;
import com.example.eduhubvn.repositories.CourseModuleRepository;
import com.example.eduhubvn.repositories.DegreeRepository;
import com.example.eduhubvn.repositories.DegreeUpdateRepository;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import com.example.eduhubvn.repositories.InterviewRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.repositories.LecturerUpdateRepository;
import com.example.eduhubvn.repositories.OwnedTrainingCourseRepository;
import com.example.eduhubvn.repositories.OwnedTrainingCourseUpdateRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationUpdateRepository;
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

                        ProjectRespository projectRepository,
                        ApplicationRepository applicationRepository,
                        ApplicationModuleRepository applicationModuleRepository,
                        InterviewRepository interviewRepository,
                        ContractRepository contractRepository,
                        CourseInfoRepository courseInfoRepository,
                        CourseModuleRepository courseModuleRepository,

                        LecturerUpdateRepository lecturerUpdateRepository,
                        EducationInstitutionUpdateRepository educationInstitutionUpdateRepository,
                        PartnerOrganizationUpdateRepository partnerOrganizationUpdateRepository,
                        DegreeUpdateRepository degreeUpdateRepository,
                        CertificationUpdateRepository certificationUpdateRepository,
                        AttendedTrainingCourseUpdateRepository attendedTrainingCourseUpdateRepository,
                        OwnedTrainingCourseUpdateRepository ownedTrainingCourseUpdateRepository,
                        ResearchProjectUpdateRepository researchProjectUpdateRepository,

                        TrainingProgramRequestRepository trainingProgramRequestRepository,
                        TrainingProgramRepository trainingProgramRepository,
                        TrainingUnitRepository trainingUnitRepository,

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
                                        RegisterRequest request = RegisterRequest.builder()
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
                                                        .status(PendingStatus.values()[faker.random().nextInt(2)])
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

                                // List<Course> courses = new ArrayList<>();

                                // for (int i = 1; i <= 100; i++) {
                                // String title = courseTitles.get(faker.random().nextInt(courseTitles.size()));
                                // String topic = courseTopics.get(faker.random().nextInt(courseTopics.size()));
                                // CourseType courseType = CourseType.values()[faker.random()
                                // .nextInt(CourseType.values().length)];
                                // String description = truncate(faker.lorem().paragraph(), 255);
                                // String thumbnailUrl =
                                // "https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=700&h=1000&fit=crop";
                                // String contentUrl = "https://www.google.com/";
                                // String level = truncate(faker.options().option(
                                // "Cơ bản", "Trung cấp", "Nâng cao", "Chuyên gia"), 255);
                                // String requirements = truncate("Không yêu cầu", 255);
                                // String language = truncate(faker.options().option("English", "Vietnamese",
                                // "French", "Japanese"), 255);

                                // Boolean isOnline = faker.bool().bool();
                                // String address = isOnline ? "Zoom"
                                // : addresses.get(faker.random().nextInt(researchTopics.size()));

                                // LocalDate startDate = LocalDate.now()
                                // .plusDays(faker.number().numberBetween(0, 30));
                                // LocalDate endDate = startDate.plusDays(faker.number().numberBetween(5, 30));
                                // Double price = faker.number().randomDouble(2, 500, 5000);
                                // Boolean isPublished = faker.bool().bool();

                                // Course course = Course.builder()
                                // .title(title)
                                // .topic(topic)
                                // .courseType(courseType)
                                // .description(description)
                                // .thumbnailUrl(thumbnailUrl)
                                // .contentUrl(contentUrl)
                                // .level(level)
                                // .requirements(requirements)
                                // .language(language)
                                // .isOnline(isOnline)
                                // .address(address)
                                // .startDate(startDate)
                                // .endDate(endDate)
                                // .price(price)
                                // .isPublished(isPublished)
                                // .build();

                                // courses.add(course);
                                // }

                                // courseRepository.saveAll(courses);

                                // List<CourseLecturer> courseLecturers = new ArrayList<>();

                                // for (Course course : courses) {
                                // int numberOfLecturers = faker.number().numberBetween(1, 4); // 1-3 lecturers
                                // Set<Lecturer> selectedLecturers = new HashSet<>();

                                // while (selectedLecturers.size() < numberOfLecturers) {
                                // Lecturer randomLecturer = lecturers
                                // .get(faker.number().numberBetween(0, lecturers.size()));
                                // selectedLecturers.add(randomLecturer);
                                // }

                                // List<Lecturer> lecturerList = new ArrayList<>(selectedLecturers);

                                // int authorIndex = faker.number().numberBetween(0, lecturerList.size());
                                // Lecturer author = lecturerList.get(authorIndex);

                                // for (Lecturer lecturer : lecturerList) {
                                // CourseRole role;

                                // if (lecturer.equals(author)) {
                                // role = CourseRole.AUTHOR;
                                // } else {
                                // role = faker.options().option(CourseRole.ASSIGNED,
                                // CourseRole.ASSISTANT);
                                // }

                                // CourseLecturer courseLecturer = CourseLecturer.builder()
                                // .course(course)
                                // .lecturer(lecturer)
                                // .role(role)
                                // .build();

                                // courseLecturers.add(courseLecturer);
                                // }
                                // }

                                // courseLecturerRepository.saveAll(courseLecturers);

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

                                System.out.println("token admin: " + adminResponse.getAccessToken());
                                System.out.println("token sub_admin1: " + subAdmin1Response.getAccessToken());
                                System.out.println("token sub_admin2: " + subAdmin2Response.getAccessToken());

                        } catch (Exception e) {
                                throw new RuntimeException("không thể khởi tạo dữ liệu mẫu: " + e.getMessage(), e);
                        }
                };
        }

        private void createSampleProjectData(
                        ProjectRespository projectRepository,
                        ApplicationRepository applicationRepository,
                        ApplicationModuleRepository applicationModuleRepository,
                        InterviewRepository interviewRepository,
                        ContractRepository contractRepository,
                        CourseInfoRepository courseInfoRepository,
                        CourseModuleRepository courseModuleRepository,
                        EducationInstitutionRepository educationInstitutionRepository,
                        PartnerOrganizationRepository partnerOrganizationRepository,
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
                        PartnerOrganizationRepository partnerOrganizationRepository,
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
                                System.err.println("Không đủ dữ liệu để tạo Training Programs (cần partners, lecturers, users)");
                                return;
                        }

                        // Dữ liệu mẫu cho Training Program
                        List<String> programTitles = Arrays.asList(
                                        "Chương trình đào tạo Java Spring Boot Advanced",
                                        "Khóa học Python Data Science Comprehensive", 
                                        "Chương trình đào tạo React Native Mobile Development",
                                        "Khóa học AI & Machine Learning Professional",
                                        "Chương trình đào tạo DevOps & Cloud Computing",
                                        "Khóa học Cybersecurity Specialist",
                                        "Chương trình đào tạo Full-stack Web Development",
                                        "Khóa học Digital Marketing Professional",
                                        "Chương trình đào tạo Business Analysis",
                                        "Khóa học Project Management với Agile/Scrum"
                        );

                        List<String> programDescriptions = Arrays.asList(
                                        "Chương trình đào tạo chuyên sâu về Java Spring Boot dành cho developer có kinh nghiệm",
                                        "Khóa học toàn diện về Data Science với Python, từ cơ bản đến nâng cao",
                                        "Chương trình đào tạo phát triển ứng dụng mobile đa nền tảng với React Native",
                                        "Khóa học chuyên nghiệp về AI và Machine Learning với ứng dụng thực tế",
                                        "Chương trình đào tạo DevOps và Cloud Computing cho doanh nghiệp",
                                        "Khóa học chuyên gia an ninh mạng với các case study thực tế",
                                        "Chương trình đào tạo Full-stack từ Frontend đến Backend",
                                        "Khóa học Digital Marketing chuyên nghiệp cho doanh nghiệp",
                                        "Chương trình đào tạo Business Analyst với phương pháp hiện đại",
                                        "Khóa học quản lý dự án với Agile và Scrum methodology"
                        );

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
                                        "Hướng dẫn triển khai thực tế"
                        );

                        List<TrainingProgramRequest> requests = new ArrayList<>();
                        List<TrainingProgram> programs = new ArrayList<>();
                        List<TrainingUnit> units = new ArrayList<>();

                        // Tạo 10 Training Program Requests
                        for (int i = 0; i < 10; i++) {
                                PartnerOrganization partner = partners.get(faker.random().nextInt(partners.size()));
                                
                                TrainingProgramRequest request = TrainingProgramRequest.builder()
                                                .partnerOrganization(partner)
                                                .title(programTitles.get(i))
                                                .description(programDescriptions.get(i))
                                                .status(i < 7 ? PendingStatus.APPROVED : 
                                                       i < 9 ? PendingStatus.PENDING : PendingStatus.REJECTED)
                                                .fileUrl("https://example.com/training-program-file-" + (i + 1) + ".pdf")
                                                .build();
                                requests.add(request);
                        }

                        // Lưu requests trước
                        trainingProgramRequestRepository.saveAll(requests);
                        trainingProgramRequestRepository.flush();

                        // Tạo Training Programs cho các requests đã APPROVED
                        for (int i = 0; i < requests.size(); i++) {
                                TrainingProgramRequest request = requests.get(i);
                                if (request.getStatus() == PendingStatus.APPROVED) {
                                        User user = users.get(faker.random().nextInt(users.size()));
                                        LocalDate startDate = LocalDate.now().plusDays(faker.random().nextInt(30, 90));
                                        LocalDate endDate = startDate.plusDays(faker.random().nextInt(30, 120));

                                        TrainingProgram program = TrainingProgram.builder()
                                                        .trainingProgramRequest(request)
                                                        .user(user)
                                                        .partnerOrganization(request.getPartnerOrganization())
                                                        .programStatus(TrainingProgramStatus.values()[faker.random()
                                                                        .nextInt(TrainingProgramStatus.values().length)])
                                                        .programMode(TrainingProgramMode.values()[faker.random()
                                                                        .nextInt(TrainingProgramMode.values().length)])
                                                        .programType(TrainingProgramType.values()[faker.random()
                                                                        .nextInt(TrainingProgramType.values().length)])
                                                        .startDate(startDate)
                                                        .endDate(endDate)
                                                        .durationHours(faker.random().nextInt(40, 200))
                                                        .durationSessions(faker.random().nextInt(10, 40))
                                                        .scheduleDetail("Thứ 2, 4, 6 từ 19h00-21h30")
                                                        .maxStudents(faker.random().nextInt(20, 50))
                                                        .minStudents(faker.random().nextInt(10, 20))
                                                        .openingCondition("Đủ số lượng học viên tối thiểu")
                                                        .equipmentRequirement("Laptop, Internet ổn định")
                                                        .classroomLink(i % 2 == 0 ? "https://meet.google.com/abc-xyz-123" : null)
                                                        .targetAudience("Developers, Students, IT Professionals")
                                                        .requirements("Kiến thức cơ bản về lập trình")
                                                        .scale("Enterprise")
                                                        .listedPrice(new BigDecimal(faker.random().nextInt(2000000, 8000000)))
                                                        .internalPrice(new BigDecimal(faker.random().nextInt(1500000, 6000000)))
                                                        .publicPrice(new BigDecimal(faker.random().nextInt(2500000, 10000000)))
                                                        .isPriceVisible(faker.bool().bool())
                                                        .bannerUrl("https://example.com/banner-" + (i + 1) + ".jpg")
                                                        .contentUrl("https://example.com/content-" + (i + 1) + ".html")
                                                        .syllabusFileUrl("https://example.com/syllabus-" + (i + 1) + ".pdf")
                                                        .tags(Set.of("Programming", "Technology", "Online Learning"))
                                                        .entryRequirements("Tốt nghiệp đại học hoặc có kinh nghiệm tương đương")
                                                        .learningOutcomes("Thành thạo framework, Xây dựng được ứng dụng thực tế")
                                                        .completionCertificateType("Certificate of Completion")
                                                        .certificateIssuer(request.getPartnerOrganization().getOrganizationName())
                                                        .title(request.getTitle())
                                                        .subTitle("Professional Training Program")
                                                        .shortDescription(request.getDescription())
                                                        .learningObjectives("Nắm vững kiến thức và kỹ năng chuyên môn")
                                                        .targetLearners("IT Professionals, Developers, Students")
                                                        .rating(4.0 + faker.random().nextDouble() * 1.0) // 4.0 - 5.0
                                                        .build();
                                        programs.add(program);
                                }
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
                                                        .title(unitTitles.get(faker.random().nextInt(unitTitles.size())) + " - Phần " + (j + 1))
                                                        .description("Chi tiết nội dung cho " + unitTitles.get(faker.random().nextInt(unitTitles.size())))
                                                        .durationSection(faker.random().nextInt(2, 6)) // 2-5 tiếng
                                                        .orderSection(j + 1)
                                                        .isLead(j == 0) // Unit đầu tiên là lead
                                                        .build();
                                        units.add(unit);
                                }
                        }

                        // Lưu units
                        trainingUnitRepository.saveAll(units);
                        trainingUnitRepository.flush();

                        System.out.println("✅ Đã tạo thành công dữ liệu mẫu cho Training Programs:");
                        System.out.println("- " + requests.size() + " Training Program Requests (7 APPROVED, 2 PENDING, 1 REJECTED)");
                        System.out.println("- " + programs.size() + " Training Programs (chỉ cho requests APPROVED)");
                        System.out.println("- " + units.size() + " Training Units (3-7 units per program)");

                } catch (Exception e) {
                        System.err.println("Lỗi khi tạo dữ liệu mẫu cho Training Programs: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
