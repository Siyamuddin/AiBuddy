package com.example.SpringAI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "dataset")
@Getter
@Setter
@NoArgsConstructor
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long dataSetId;
   private String dataSetTitle;
   private String data;
    @ManyToOne
    @JoinColumn(name = "localUser")
    private LocalUser localUser;
    @CreationTimestamp
    private Date creationTime;
    @LastModifiedDate
    private Date lastUpdatedTime;

}
