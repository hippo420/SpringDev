package app.springdev.excel.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordVo {
    private Long id;

    private String insurerType;  // 보험자 구분

    private String treatmentYear;  // 진료년도

    private String age;  // 연령 (예: 30대, 40대)

    private String gender;  // 성별

    private String institutionType;  // 요양기관종별

    private String institutionAddress;  // 요양기관주소지

    private String treatmentType;  // 진료형태

    private String patientCount;  // 진료인원(명)

    private String hospitalDays;  // 입내원일수(일)
}
