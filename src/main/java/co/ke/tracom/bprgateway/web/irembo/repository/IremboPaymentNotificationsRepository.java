package co.ke.tracom.bprgateway.web.irembo.repository;

import co.ke.tracom.bprgateway.web.irembo.entity.IremboPaymentNotifications;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface IremboPaymentNotificationsRepository extends CrudRepository<IremboPaymentNotifications, BigDecimal> {
}
