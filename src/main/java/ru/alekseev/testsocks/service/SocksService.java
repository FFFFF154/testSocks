package ru.alekseev.testsocks.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.alekseev.testsocks.dto.SocksDto;
import ru.alekseev.testsocks.entity.Socks;
import ru.alekseev.testsocks.repository.SocksRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class SocksService {

    private final SocksRepository socksRepository;

    @Autowired
    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    public Socks findSocksById(Long id){
        return socksRepository.findById(id).orElse(null);
    }

    public boolean updateSocks(Long id, SocksDto dto){
        Socks socksDB = socksRepository.findById(id).orElse(null);

        if (socksDB != null) { // TODO возможно есть лучше реализация
            if (dto.getColor() != null) {
                socksDB.setColor(dto.getColor());
            }
            if (dto.getQuantity() != null) {
                socksDB.setQuantity(dto.getQuantity());
            }
            if (dto.getCottonPercent() != null) {
                socksDB.setCottonPercent(dto.getCottonPercent());
            }
            socksRepository.save(socksDB);
            return true;
        }
        return false;
    }

    public boolean addSocks(SocksDto dto){
        if (dto.getCottonPercent() == null && dto.getQuantity() == null && dto.getColor() == null){
            return false;
        }
        Socks socksDB = socksRepository.findByColorAndCottonPercent(
                dto.getColor(),
                dto.getCottonPercent());
        if (socksDB == null) {
            socksDB = new Socks();
            socksDB.setColor(dto.getColor());
            socksDB.setCottonPercent(dto.getCottonPercent());
            socksDB.setQuantity(dto.getQuantity());
            socksRepository.save(socksDB);
            return true;
        } else {
            socksDB.setQuantity(socksDB.getQuantity() + dto.getQuantity());
            socksRepository.save(socksDB);
            return true;
        }
    }

    public boolean removeSocks(SocksDto dto){
        Socks socksDB = socksRepository.findByColorAndCottonPercent(
                dto.getColor(),
                dto.getCottonPercent());

        if (socksDB != null){
            if (socksDB.getQuantity() - dto.getQuantity() >= 0){
                socksDB.setQuantity(socksDB.getQuantity() - dto.getQuantity());
                socksRepository.save(socksDB);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }


    public List<Socks> socksFilter(String color,
                                   Double cottonPercent,
                                   String operator,
                                   String sort,
                                   Double minPercent,
                                   Double maxPercent){
        if (color == null && cottonPercent == null && sort == null && minPercent == null && maxPercent == null) {
            return socksRepository.findAll();
        } else if (sort != null){
            switch (sort){
                case "color" -> {return socksRepository.findAllByOrderByColor();}
                case "cottonPercent" -> {return socksRepository.findAllByOrderByCottonPercent();}
            }
        } else if (minPercent != null && maxPercent != null) {
            return socksRepository.findByCottonPercentBetween(minPercent, maxPercent);
        }
        switch (operator) {
            case "equal" -> {
                if (color != null && cottonPercent != null) {
                    return socksRepository.findByCottonPercentAndColor(cottonPercent, color);
                } else if (color != null) {
                    return socksRepository.findByColor(color);
                } else {
                    return socksRepository.findByCottonPercent(cottonPercent);
                }
            }
            case "moreThan" -> {
                if (color != null && cottonPercent != null) {
                    return socksRepository.findByCottonPercentGreaterThanAndColor(cottonPercent, color);
                } else if (color != null) {
                    return socksRepository.findByColor(color);
                } else {
                    return socksRepository.findByCottonPercentGreaterThan(cottonPercent);
                }
            }
            case "lessThan" -> {
                if (color != null && cottonPercent != null) {
                    return socksRepository.findByCottonPercentLessThanAndColor(cottonPercent, color);
                } else if (color != null) {
                    return socksRepository.findByColor(color);
                } else {
                    return socksRepository.findByCottonPercentLessThan(cottonPercent);
                }
            }
        }

        return null;
    }

    public boolean addFromFile(MultipartFile file){
        try{
            XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = book.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                SocksDto socksDto = new SocksDto();
                XSSFCell cellColor = (XSSFCell) row.getCell(0);
                XSSFCell cellCottonPercent = (XSSFCell) row.getCell(1);
                XSSFCell cellQuantity = (XSSFCell) row.getCell(2);
                if (cellColor.getCellType().equals(CellType.STRING) &&
                cellCottonPercent.getCellType().equals(CellType.NUMERIC)&&
                cellQuantity.getCellType().equals(CellType.NUMERIC)) {
                    socksDto.setColor(cellColor.getStringCellValue());
                    socksDto.setCottonPercent(cellCottonPercent.getNumericCellValue());
                    socksDto.setQuantity((int)cellQuantity.getNumericCellValue());
                    addSocks(socksDto);

                }

            }
        } catch (IOException e){
            log.warn("Ошибка чтения файла");
        }
        return false;
    }

}
