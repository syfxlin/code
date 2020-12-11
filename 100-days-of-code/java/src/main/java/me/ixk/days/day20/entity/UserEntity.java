package me.ixk.days.day20.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "user")
public class UserEntity {

    @Id
    private String username;

    private String password;
    private String realname;
    private String mobile;
    private int age;
}
