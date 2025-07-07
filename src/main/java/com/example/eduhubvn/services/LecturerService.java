package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.*;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final DegreeRepository degreeRepository;
    private final CertificationRepository certificationRepository;
    private final AttendedTrainingCourseRepository attendedTrainingCourseRepository;
    private final DegreeUpdateRepository degreeUpdateRepository;
    private final CertificationUpdateRepository certificationUpdateRepository;

    private final LecturerMapper lecturerMapper;
    private final DegreeMapper degreeMapper;
    private final CertificationMapper certificationMapper;
    private final LecturerUpdateMapper lecturerUpdateMapper;
    private final AttendedTrainingCourseMapper attendedTrainingCourseMapper;

    private final LecturerUpdateRepository lecturerUpdateRequestRepository;


    @Transactional
    public LecturerDTO createLecturerFromUser(LecturerReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getLecturer() != null) {
            throw new IllegalStateException("Tài khoản đã đăng ký làm giảng viên.");
        }
        Lecturer lecturer = lecturerMapper.toEntity(req);
        lecturer.setUser(user);
        lecturer.setStatus(PendingStatus.PENDING);
        Lecturer saved = lecturerRepository.save(lecturer);

        lecturerRepository.flush();
        return lecturerMapper.toDTO(saved);
    }

    @Transactional
    public List<DegreeDTO> saveDegrees( List<DegreeReq> degreeReqs,User user) {
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký làm giảng viên.");
        }

        List<Degree> degrees = degreeMapper.toEntities(degreeReqs);
        degrees.forEach(degree ->{
            degree.setLecturer(lecturer);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
        } );
        List<Degree> degreeList = degreeRepository.saveAll(degrees);
        degreeRepository.flush();
        return degreeMapper.toDTOs(degreeList);
    }


    public List<CertificationDTO> saveCertification(List<CertificationReq> req, User user) {
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký làm giảng viên.");
        }
        List<Certification> certifications = certificationMapper.toEntities(req);
        certifications.forEach(certification ->{
           certification.setLecturer(lecturer);
           certification.setStatus(PendingStatus.PENDING);
           certification.setAdminNote("");
        });
        List<Certification> certificationList = certificationRepository.saveAll(certifications);
        certificationRepository.flush();
        return certificationMapper.toDTOs(certificationList);
    }

    public LecturerDTO approveLecturer(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Người dùng chưa đăng ký làm giảng viên."));
        if (lecturer.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Người dùng đã được phê duyệt.");
        }
        Lecturer saved = null;
        try {
            lecturer.getUser().setRole(Role.LECTURER);
            lecturer.setStatus(PendingStatus.APPROVED);
            saved = lecturerRepository.save(lecturer);
            lecturerRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lecturerMapper.toDTO(saved);
    }

    public DegreeDTO approveDegree(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Degree degree = degreeRepository.findById(req.getId()).orElse(null);
        if(degree == null) {
            throw new IllegalStateException("Không tìm thấy thông tin Bằng cấp.");
        }
        degree.setStatus(PendingStatus.APPROVED);
        Degree saved = degreeRepository.save(degree);
        degreeRepository.flush();
        return degreeMapper.toDTO(saved);
    }

    public CertificationDTO approveCertification(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Certification certification = certificationRepository.findById(req.getId()).orElse(null);
        if(certification == null) {
            throw new IllegalStateException("Không tìm thấy thông tin Chứng chỉ.");
        }
        certification.setStatus(PendingStatus.APPROVED);
        Certification saved = certificationRepository.save(certification);
        certificationRepository.flush();
        return certificationMapper.toDTO(saved);
    }
    @Transactional
    public LecturerUpdateReq updateLecturerFromUser(LecturerUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }

        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký làm giảng viên.");
        }

        try {
            // Tìm bản ghi LecturerUpdate theo lecturer
            Optional<LecturerUpdate> optionalUpdate = lecturerUpdateRequestRepository.findByLecturer(lecturer);

            LecturerUpdate update;
            if (optionalUpdate.isPresent()) {
                // Nếu đã tồn tại, cập nhật dữ liệu từ req
                update = optionalUpdate.get();
                lecturerUpdateMapper.updateEntityFromDto(req, update); // cập nhật từng field

            } else {
                // Nếu chưa có, tạo mới
                update = lecturerUpdateMapper.toUpdate(req);
                update.setLecturer(lecturer);
            }

            // Gán trạng thái PENDING
            update.setStatus(PendingStatus.PENDING);


            lecturerUpdateRequestRepository.save(update);
            lecturerUpdateRequestRepository.flush();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý yêu cầu cập nhật: " + e.getMessage(), e);
        }

        return req;
    }

    @Transactional
    public List<LecturerPendingDTO> getPendingLecturerUpdates() {
        List<LecturerUpdate> pendingUpdates = lecturerUpdateRequestRepository.findByStatus(PendingStatus.PENDING);

        return pendingUpdates.stream()
                .map(update -> {
                    Lecturer lecturer = update.getLecturer();
                    LecturerDTO lecturerDTO = lecturerMapper.toDTO(lecturer);
                    LecturerUpdateDTO lecturerUpdateDTO = lecturerUpdateMapper.toDTO(update);
                    return new LecturerPendingDTO(lecturerDTO, lecturerUpdateDTO);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CertificationDTO rejectCertification(RejectReq req) {
        Certification cert = certificationRepository
                .findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new IllegalStateException("Chứng chỉ không tồn tại hoặc không ở trạng thái PENDING."));
        try {
            cert.setStatus(PendingStatus.REJECTED);
            cert.setAdminNote(req.getAdminNote());
            certificationRepository.save(cert);
            certificationRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return certificationMapper.toDTO(cert);
    }
    @Transactional
    public DegreeDTO rejectDegree(RejectReq req) {
        Degree degree = degreeRepository
                .findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new IllegalStateException("Bằng cấp không tồn tại hoặc không ở trạng thái PENDING."));
        try {
            degree.setStatus(PendingStatus.REJECTED);
            degree.setAdminNote(req.getAdminNote());
            degreeRepository.save(degree);
            degreeRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Admin note in entity: " + degree.getAdminNote());
        return degreeMapper.toDTO(degree);
    }
    @Transactional
    public DegreeDTO updateDegreeFromUser(DegreeUpdateReq req, User user) {
        if(req.getId() == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Degree saved = null;
        try {
            Degree degree = degreeRepository.findById(req.getId())
                    .orElseThrow(() -> new IllegalStateException("Bằng cấp không tồn tại hoặc không ở trạng thái PENDING."));
            degreeMapper.updateEntityFromReq(req, degree);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
            saved = degreeRepository.save(degree);
            degreeRepository.flush();
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return degreeMapper.toDTO(saved);

    }
    @Transactional
    public CertificationDTO updateCertificationFromUser(CertificationUpdateReq req, User user) {
        if(req.getId() == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Bằng cấp không tồn tại hoặc không ở trạng thái PENDING."));
        Certification saved = null;
        try {
            certificationMapper.updateEntityFromReq(req, certification);
            certification.setStatus(PendingStatus.PENDING);
            certification.setAdminNote("");
            saved = certificationRepository.save(certification);
            certificationRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return certificationMapper.toDTO(saved);
    }

    @Transactional
    public AttendedTrainingCourseDTO createAttendedCourse(AttendedTrainingCourseReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }

        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký giảng viên.");
        }
        AttendedTrainingCourse course = null;
        try {
            course = attendedTrainingCourseMapper.toEntity(req);
            course.setLecturer(lecturer);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            attendedTrainingCourseRepository.save(course);
            attendedTrainingCourseRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return attendedTrainingCourseMapper.toDTO(course);
    }

    @Transactional
    public AttendedTrainingCourseDTO updateAttendedCourse(AttendedTrainingCourseUpdateReq req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu hoặc ID không được để trống.");
        }

        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký giảng viên.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Khóa học không tồn tại."));
        try {
            attendedTrainingCourseMapper.updateEntityFromReq(req, course);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            attendedTrainingCourseRepository.save(course);
            return attendedTrainingCourseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public DegreeDTO editDegreeFromUser(DegreeUpdateReq req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy bằng cấp."));
        if (degree.getStatus()!= PendingStatus.APPROVED){
            throw new IllegalArgumentException("Bẳng cấp hiện tại không thể chỉnh sửa.");
        }
        try {
            Optional<DegreeUpdate> optionalUpdate = degreeUpdateRepository.findByDegree(degree);

            DegreeUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                update.setReferenceId(req.getReferenceId());
                update.setName(req.getName());
                update.setMajor(req.getMajor());
                update.setInstitution(req.getInstitution());
                update.setStartYear(req.getStartYear());
                update.setGraduationYear(req.getGraduationYear());
                update.setLevel(req.getLevel());
                update.setUrl(req.getUrl());
                update.setDescription(req.getDescription());
            } else {
                update = DegreeUpdate.builder()
                        .referenceId(req.getReferenceId())
                        .name(req.getName())
                        .major(req.getMajor())
                        .institution(req.getInstitution())
                        .startYear(req.getStartYear())
                        .graduationYear(req.getGraduationYear())
                        .level(req.getLevel())
                        .url(req.getUrl())
                        .description(req.getDescription())
                        .degree(degree)
                        .build();
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            DegreeUpdate savedUpdate = degreeUpdateRepository.saveAndFlush(update);
            return degreeMapper.toDTO(savedUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật bằng cấp: " + e.getMessage(), e);
        }
    }
    @Transactional
    public DegreeDTO rejectEditDegree(RejectReq req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        DegreeUpdate update = degreeUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Bản cập nhật bằng cấp không tồn tại."));

        if (update.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Bản cập nhật không ở trạng thái PENDING.");
        }
        update.setStatus(PendingStatus.REJECTED);
        update.setAdminNote(req.getAdminNote());
        degreeUpdateRepository.save(update);
        degreeUpdateRepository.flush();
        return degreeMapper.toDTO(update);
    }


    @Transactional
    public CertificationDTO editCertificationFromUser(CertificationUpdateReq req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy chứng chỉ."));
        if (certification.getStatus() != PendingStatus.APPROVED) {
            throw new IllegalArgumentException("Chứng chỉ hiện tại không thể chỉnh sửa.");
        }
        try {
            Optional<CertificationUpdate> optionalUpdate = certificationUpdateRepository.findByCertification(certification);
            CertificationUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                update.setReferenceId(req.getReferenceId());
                update.setName(req.getName());
                update.setIssuedBy(req.getIssuedBy());
                update.setIssueDate(req.getIssueDate());
                update.setExpiryDate(req.getExpiryDate());
                update.setCertificateUrl(req.getCertificateUrl());
                update.setLevel(req.getLevel());
                update.setDescription(req.getDescription());
            } else {
                update = CertificationUpdate.builder()
                        .referenceId(req.getReferenceId())
                        .name(req.getName())
                        .issuedBy(req.getIssuedBy())
                        .issueDate(req.getIssueDate())
                        .expiryDate(req.getExpiryDate())
                        .certificateUrl(req.getCertificateUrl())
                        .level(req.getLevel())
                        .description(req.getDescription())
                        .certification(certification)
                        .build();
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            CertificationUpdate savedUpdate = certificationUpdateRepository.saveAndFlush(update);
            return certificationMapper.toDTO(savedUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật chứng chỉ: " + e.getMessage(), e);
        }
    }
    @Transactional
    public CertificationDTO rejectEditCertification(RejectReq req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        CertificationUpdate update = certificationUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Bản cập nhật chứng chỉ không tồn tại."));
        if (update.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Bản cập nhật không ở trạng thái PENDING.");
        }
        update.setStatus(PendingStatus.REJECTED);
        update.setAdminNote(req.getAdminNote());
        certificationUpdateRepository.saveAndFlush(update);
        return certificationMapper.toDTO(update);
    }


}
