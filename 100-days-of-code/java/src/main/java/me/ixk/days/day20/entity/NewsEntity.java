package me.ixk.days.day20.entity;

import java.sql.Date;
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
@Table(name = "news")
public class NewsEntity {

    @Id
    private int id;

    private String contentTitle;
    private String contentPage;
    private Date publishDate;
}
