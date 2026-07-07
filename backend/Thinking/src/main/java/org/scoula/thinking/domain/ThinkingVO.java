package org.scoula.thinking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThinkingVO {

    private long id;
    private String category;
    private String title;
    private String content;
    private String password;
    private int likeCount;
    private Date createdAt;
    private Date updatedAt;

}
