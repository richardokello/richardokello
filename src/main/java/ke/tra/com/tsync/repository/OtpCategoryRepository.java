package ke.tra.com.tsync.repository;


import ke.tra.com.tsync.entities.UfsOtpCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface OtpCategoryRepository extends CrudRepository<UfsOtpCategory, BigDecimal> {
    Optional<UfsOtpCategory>findDistinctByCategoryAndIntrash(String category, String intrash);
}
