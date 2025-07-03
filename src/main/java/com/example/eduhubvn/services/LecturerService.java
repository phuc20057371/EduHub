package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerReq;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.CertificationMapper;
import com.example.eduhubvn.mapper.DegreeMapper;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.repositories.CertificationRepository;
import com.example.eduhubvn.repositories.DegreeRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final DegreeRepository degreeRepository;
    private final CertificationRepository certificationRepository;

    private final LecturerMapper lecturerMapper;
    private final DegreeMapper degreeMapper;
    private final CertificationMapper certificationMapper;


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
        });
        List<Certification> certificationList = certificationRepository.saveAll(certifications);
        certificationRepository.flush();
        return certificationMapper.toDTOs(certificationList);
    }

    public LecturerDTO approveLecturer(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(req.getId()).orElse(null);
        if(lecturer == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký làm giảng viên.");
        }
        lecturer.setStatus(PendingStatus.APPROVED);
        Lecturer saved = lecturerRepository.save(lecturer);
        lecturerRepository.flush();
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
}
