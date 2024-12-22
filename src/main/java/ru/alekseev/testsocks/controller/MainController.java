package ru.alekseev.testsocks.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alekseev.testsocks.dto.SocksDto;
import ru.alekseev.testsocks.entity.Socks;
import ru.alekseev.testsocks.exceptions.FileProcessingException;
import ru.alekseev.testsocks.exceptions.InvalidFormatException;
import ru.alekseev.testsocks.exceptions.NegativeValueException;
import ru.alekseev.testsocks.service.SocksService;

import java.io.File;
import java.util.List;

@RestController
@Slf4j
public class MainController {

    private final SocksService socksService;

    @Autowired
    public MainController(SocksService socksService) {
        this.socksService = socksService;
    }

    @GetMapping("/api/socks")
    public List<Socks> allSocks(@RequestParam(name = "color", required = false) String color,
                                @RequestParam(name = "cottonPercent", required = false) Double cottonPercent,
                                @RequestParam(name = "operator", required = false) String operator,
                                @RequestParam(name = "sort", required = false) String sort,
                                @RequestParam(name = "minCottonPercent", required = false) Double minPercent,
                                @RequestParam(name = "maxCottonPercent", required = false) Double maxPercent) {
        log.info("socks is present");
        return socksService.socksFilter(color, cottonPercent, operator, sort, minPercent, maxPercent);
    }


    @PostMapping("/api/socks/income")
    public void income(@RequestBody SocksDto dto) throws InvalidFormatException{
        log.info("dto income!");
        if (socksService.addSocks(dto)){
            log.info("socks added!");
        } else {
            throw new InvalidFormatException("Некорректный формат данных");
        }

    }

    @PostMapping("/api/socks/outcome")
    public void outcome(@RequestBody SocksDto dto) throws NegativeValueException{
        if (socksService.removeSocks(dto)) {
            log.info("dto outcome!");
        } else {
            throw new NegativeValueException("Нехватка носков на складе");
        }
    }

    @PutMapping("/api/socks/{id}")
    public void updateSocks(@PathVariable Long id, @RequestBody SocksDto dto) throws InvalidFormatException {
        if (socksService.updateSocks(id, dto)) {
            log.info("dto update!");
        } else {
            throw new InvalidFormatException("Некорректный формат данных");
        }
    }

    @PostMapping("/api/socks/batch")
    public void batchSocks(@RequestParam(name = "file") MultipartFile file) throws FileProcessingException {
        log.info("file income");
        if (socksService.addFromFile(file)){
            log.info("dto is update from file!");
        } else {
            throw new FileProcessingException("Ошибка при обработке файла");
        }
    }
}
