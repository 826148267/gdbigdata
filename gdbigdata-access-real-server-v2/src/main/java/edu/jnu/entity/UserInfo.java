package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "table_user_info")
public class UserInfo {
    @Id
    private Long userId;
    @Column(length = 2500)
    private String userName;
    @Column(length = 2500)
    private String userOrganization;
    @Column(length = 2500)
    private String userAddress;
    @Column(length = 2500)
    private String userFileNums;
}
