package ua.exchange.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Orderr implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore(value = false)
    private Long idd;

    private Boolean sideOfSell;
    private BigDecimal price;
    private BigDecimal size;

    @JsonIgnore
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Orderr(Boolean sideOfSell, BigDecimal price, BigDecimal size, LocalDateTime date, Participant participant, Product product) {
        this.sideOfSell = sideOfSell;
        this.price = price;
        this.size = size;
        this.date = date;
        this.participant = participant;
        this.product = product;
    }
}
