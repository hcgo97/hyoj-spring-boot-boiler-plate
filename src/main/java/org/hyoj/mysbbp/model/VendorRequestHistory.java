package org.hyoj.mysbbp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vendor_request_history")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VendorRequestHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String requestId;

    @Column(length = 1024)
    private String endPoint;

    @Column(columnDefinition = "TEXT")
    private String requestHeader;

    @Column
    private String requestMethod;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseHeader;

    @Column
    private String responseStatusCode;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

}
