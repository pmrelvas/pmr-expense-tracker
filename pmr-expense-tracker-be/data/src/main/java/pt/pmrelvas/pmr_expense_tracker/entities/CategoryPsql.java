package pt.pmrelvas.pmr_expense_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="categories")
@Getter
@Setter
@NoArgsConstructor
public class CategoryPsql {

    @Id
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent_category")
    private CategoryPsql parentCategory;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
