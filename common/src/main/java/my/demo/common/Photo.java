package my.demo.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Document

public class Photo {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    @Min(0L)
    Dimension dimension;

    BigInteger size;

    @Valid
    @NotNull
    @DocumentReference
    Photo thumbnail;
    @JsonSerialize(using = ObjectIdSerializer.class)
    ObjectId dataFile;
}
