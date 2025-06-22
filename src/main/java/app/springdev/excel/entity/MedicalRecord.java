package app.springdev.excel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    //GenerationType.IDENTITY시 배치인서트하면 DB가 INSERT 시점에 ID를 생성하기 때문에 INSERT 마다 개별 쿼리가 실행됨, 결국 배치처리안됨!
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_record_seq_gen")
    @SequenceGenerator(name = "medical_record_seq_gen", sequenceName = "medical_record_seq", allocationSize = 1000)
    private Long id;

    @Column(name = "insurer_type")
    private String insurerType;  // 보험자 구분

    @Column(name = "treatment_year")
    private String treatmentYear;  // 진료년도

    @Column(name = "age")
    private String age;  // 연령 (예: 30대, 40대)

    @Column(name = "gender")
    private String gender;  // 성별

    @Column(name = "institution_type")
    private String institutionType;  // 요양기관종별

    @Column(name = "institution_address")
    private String institutionAddress;  // 요양기관주소지

    @Column(name = "treatment_type")
    private String treatmentType;  // 진료형태

    @Column(name = "patient_count")
    private String patientCount;  // 진료인원(명)

    @Column(name = "hospital_days")
    private String hospitalDays;  // 입내원일수(일)
}
