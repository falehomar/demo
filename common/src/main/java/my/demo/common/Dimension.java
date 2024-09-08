package my.demo.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Dimension {
    private Integer width;
    private Integer height;
}
