package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface AuthTokenReplicationRepository extends JpaRepository<UfsUser,Long> {

    @Procedure("REPLICATE_AUTH_TOKEN")
    void replicateToken(@Param("USERNAME") String username);
}
