package ru.alekseev.testsocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.alekseev.testsocks.entity.Socks;

import java.util.List;

public interface SocksRepository extends JpaRepository<Socks, Long>, JpaSpecificationExecutor<Socks> {


    public Socks findByColorAndCottonPercent(String color, Double cottonPercent);

    //public List<Socks> findByColorAndCottonPercentAndOrderByColor(String color, Double cottonPercent);

    //public List<Socks> findByColorAndCottonPercentAndOrderByCottonPercent(String color, Double cottonPercent);

    public List<Socks> findByColor(String color);

    public List<Socks> findByCottonPercentBetween(Double cottonPercentMin, Double cottonPercentMax);

    public List<Socks> findByCottonPercentGreaterThan(Double cottonPercent);

    public List<Socks> findByCottonPercentGreaterThanAndColor(Double cottonPercent, String color);

    public List<Socks> findByCottonPercentLessThanAndColor(Double cottonPercent, String color);

    public List<Socks> findByCottonPercentLessThan(Double cottonPercent);

    public List<Socks> findByCottonPercent(Double cottonPercent);

    public List<Socks> findByCottonPercentAndColor(Double cottonPercent, String color);

    public List<Socks> findAllByOrderByCottonPercent();

    public List<Socks> findAllByOrderByColor();



}
