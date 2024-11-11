package pt.pmrelvas.pmr_expense_tracker.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

    public CategoryPsql(Category category) {
        this.id = category.id();
        this.code = category.code();
        this.name = category.name();
        this.parentCategory = category.parentCategory() == null ? null : new CategoryPsql(category.parentCategory());
        this.createdAt = category.createdAt();
    }

    public Category toEntity() {
        return Category.builder()
                .id(this.id)
                .code(this.code)
                .name(this.name)
                .parentCategory(this.parentCategory == null ? null : this.parentCategory.toEntity())
                .createdAt(this.createdAt)
                .build();
    }
}
