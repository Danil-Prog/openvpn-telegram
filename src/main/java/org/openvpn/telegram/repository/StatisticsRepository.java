package org.openvpn.telegram.repository;

import org.openvpn.telegram.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
}
