package ru.alekseev.testsocks.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
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
@Tag(name = "main_methods")
@RequestMapping("/api/v1/socks")
public class MainController {

    private final SocksService socksService;

    @Autowired
    public MainController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(
            summary = "Получение носков из базы",
            description = "Получение определенных носков из базы, используя параметры фильтрации"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Носки получены",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class)) }),
                    @ApiResponse(responseCode = "404", description = "Носки не найдены", content = @Content),
            }
    )
    @GetMapping("/")
    public List<Socks> allSocks(@RequestParam(name = "color", required = false) String color,
                                @RequestParam(name = "cottonPercent", required = false) Double cottonPercent,
                                @RequestParam(name = "operator", required = false) String operator,
                                @RequestParam(name = "sort", required = false) String sort,
                                @RequestParam(name = "minCottonPercent", required = false) Double minPercent,
                                @RequestParam(name = "maxCottonPercent", required = false) Double maxPercent) {
        log.info("socks is present");
        return socksService.socksFilter(color, cottonPercent, operator, sort, minPercent, maxPercent);
    }

    @Operation(
            summary = "Добавление носков в бд",
            description = "Добавляет новые носки в бд, либо к уже существующим, изменяя их количество"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Носки добавлены",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SocksDto.class)) }),
                    @ApiResponse(responseCode = "400", description = "Неверный ввод")
            }
    )
    @PostMapping("/income")
    public ResponseEntity<String> income(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Носки добавлены", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Socks.class),
                    examples = @ExampleObject(value = "{ \"color\": \"red\", \"cottonPercent\": \"50\",\"quantity\": \"100\" }")))
            @RequestBody SocksDto dto) throws InvalidFormatException{
        log.info("dto income!");
        if (socksService.addSocks(dto)){
            log.info("socks added!");
        } else {
            throw new InvalidFormatException("Некорректный формат данных");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Уменьшает носки в бд",
            description = "Уменьшает носки в бд, если их хватает на складе"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Носки уменьшены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SocksDto.class)) }),
                    @ApiResponse(responseCode = "400", description = "Нехватка носков")
            }
    )
    @PostMapping("/outcome")
    public ResponseEntity<String> outcome(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Носки уменьшены", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Socks.class),
                    examples = @ExampleObject(value = "{ \"color\": \"red\", \"cottonPercent\": \"50\",\"quantity\": \"100\" }")))
            @RequestBody SocksDto dto) throws NegativeValueException{
        if (socksService.removeSocks(dto)) {
            log.info("dto outcome!");
        } else {
            throw new NegativeValueException("Нехватка носков на складе");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Изменение носков в бд",
            description = "Изменяет носки по id в бд"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Носки обновлены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SocksDto.class)) }),
                    @ApiResponse(responseCode = "400", description = "Неверный ввод")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSocks(@PathVariable Long id,
                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                      description = "Носки добавлены", required = true,
                                                      content = @Content(mediaType = "application/json",
                                                              schema = @Schema(implementation = Socks.class),
                                                              examples = @ExampleObject(value = "{ \"color\": \"red\", \"cottonPercent\": \"50\",\"quantity\": \"100\" }")))
                                              @RequestBody SocksDto dto) throws InvalidFormatException {

        if (socksService.updateSocks(id, dto)) {
            log.info("dto update!");
        } else {
            throw new InvalidFormatException("Некорректный формат данных");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Добавляет носки в бд из xlsx файла"

    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Носки добавлены",
                            content = {@Content(mediaType = "form-data", schema = @Schema(implementation = SocksDto.class)) }),
                    @ApiResponse(responseCode = "400", description = "Проблема чтения файла")
            }
    )
    @PostMapping(path = "/batch",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> batchSocks(@RequestBody MultipartFile file) throws FileProcessingException {
        log.info("file income");
        if (socksService.addFromFile(file)){
            log.info("dto is update from file!");
        } else {
            throw new FileProcessingException("Ошибка при обработке файла");
        }
        return ResponseEntity.ok().build();
    }
}
